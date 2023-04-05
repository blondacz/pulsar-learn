import scala.concurrent.duration._

import dev.profunktor.pulsar._
import dev.profunktor.pulsar.schema.PulsarSchema

import cats.effect._
import fs2.Stream


object NeutronConsumer extends IOApp {


  def run(st: Subscription.Type, subName: String, topicName: String): IO[Unit] = {
    val config = Config.Builder.default

    val topic =
      Topic.Builder
        .withName(topicName)
        .withConfig(config)
        .withType(Topic.Type.Persistent)
        .build

    val subs =
      Subscription.Builder
        .withName(subName)
        .withType(st)
        .build

    val csettings =
      Consumer.Settings[IO, String]()
        .withUnsafeConf(_.autoUpdatePartitions(false))

    val schema = PulsarSchema.utf8

    val resources: Resource[IO, Consumer[IO, String]] =
      for {
        pulsar <- Pulsar.make[IO](config.url)
        consumer <- Consumer.make[IO, String](pulsar, topic, subs, schema, csettings)
      } yield consumer
    Stream
      .resource(resources)
      .flatMap {
        case (consumer) =>
          val consume =
            consumer
              .autoSubscribe
              .evalMap(c => IO.println("C1" + c))

          consume
      }
      .compile
      .drain
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val (st,sn, tn) = args match {
      case "failover" :: tn ::  Nil => (Subscription.Type.Failover, "sub-failover", tn)
      case "exclusive" :: tn::  Nil => (Subscription.Type.Exclusive,"sub-exclusive",tn)
      case "shared" :: tn::Nil => (Subscription.Type.Shared, "sub-shared", tn)
    }
    run(st,sn,tn).as(ExitCode.Success)
  }
}
