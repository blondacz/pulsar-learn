import cats.effect._
import dev.profunktor.pulsar._
import dev.profunktor.pulsar.schema.PulsarSchema
import fs2.Stream

import scala.concurrent.duration._

object NeutronProducer extends IOApp {

  def run(subName: String, t: String): IO[Unit] = {
    val config = Config.Builder.default


    val topic =
      Topic.Builder
        .withName(t)
        .withConfig(config)
        .withType(Topic.Type.Persistent)
        .build

    val subs =
      Subscription.Builder
        .withName(subName)
        .withType(Subscription.Type.Shared)
        .build
    val settings =
      Producer.Settings[IO, String]()
        .withUnsafeConf(_.autoUpdatePartitions(false))

    val csettings =
      Consumer.Settings[IO, String]()
        .withUnsafeConf(_.autoUpdatePartitions(false))

    val schema = PulsarSchema.utf8

    val resources: Resource[IO, (Consumer[IO, String], Producer[IO, String])] =
      for {
        pulsar <- Pulsar.make[IO](config.url)
        consumer <- Consumer.make[IO, String](pulsar, topic, subs, schema, csettings)
        producer <- Producer.make[IO, String](pulsar, topic, schema, settings)
      } yield consumer -> producer
    Stream
      .resource(resources)
      .flatMap {
        case (consumer, producer) =>
          val consume =
            consumer
              .autoSubscribe
              .evalMap(c => IO.println("P1" + c))


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

  override def run(args: List[String]): IO[ExitCode] = {
      run(args.head, args.tail.head).as(ExitCode.Success)
  }
}
