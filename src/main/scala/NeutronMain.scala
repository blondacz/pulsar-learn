import scala.concurrent.duration._

import dev.profunktor.pulsar._
import dev.profunktor.pulsar.schema.PulsarSchema

import cats.effect._
import fs2.Stream

object NeutronMain extends IOApp.Simple {

  val config = Config.Builder.default

  val batching =
    Producer.Batching.Enabled(maxDelay = 5.seconds, maxMessages = 500)

  val topic  =
    Topic.Builder
      .withName("my-topic3")
      .withConfig(config)
      .withType(Topic.Type.Persistent)
      .build

  val subs =
    Subscription.Builder
      .withName("my-sub")
      .withType(Subscription.Type.Exclusive)
      .build
  val settings =
    Producer.Settings[IO, String]()
      .withBatching(batching)
      .withUnsafeConf(_.autoUpdatePartitions(false))

  val csettings =
    Consumer.Settings[IO, String]()
      .withReadCompacted
      .withUnsafeConf(_.autoUpdatePartitions(false))

  val schema = PulsarSchema.utf8

  val resources: Resource[IO, (Consumer[IO, String], Producer[IO, String])] =
    for {
      pulsar   <- Pulsar.make[IO](config.url)
      consumer <- Consumer.make[IO, String](pulsar, topic, subs, schema,csettings)
      producer <- Producer.make[IO, String](pulsar, topic, schema,settings)
    } yield consumer -> producer

  val run: IO[Unit] =
    Stream
      .resource(resources)
      .flatMap {
        case (consumer, producer) =>
          val consume =
            consumer
              .autoSubscribe
              .parEvalMap(8)(IO.println)

          val produce: Stream[IO, Unit] =
            Stream
              .repeatEval(IO.pure("test data"))
              .zipWithIndex
              .map{case (s,l) => s + l}
              .evalMap(producer.send_)

          consume.concurrently(produce)
      }
      .compile
      .drain

}
