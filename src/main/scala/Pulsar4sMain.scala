import cats.Id
import com.sksamuel.pulsar4s.{AsyncHandler, ConsumerConfig, MessageId, ProducerConfig, PulsarClient, Subscription, Topic}
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.impl.MessageIdImpl

object Pulsar4sMain extends App {

  val client = PulsarClient("pulsar://localhost:6650")
  implicit val schema: Schema[String] = Schema.STRING


  val topic = Topic("persistent://public/default/my-topic2")
  val producerConfig = ProducerConfig(topic)
  val producer = client.producer[String](producerConfig)

  val consumerConfig = ConsumerConfig(Subscription("my-sub"),Seq(topic))
  val consumerFn = client.consumer[String](consumerConfig)

  consumerFn.seek(MessageId.fromJava(new MessageIdImpl(22,10,-1)))
  println("Subscribing")

  producer.send("Hi2")


  Iterator.iterate(1){ i =>
    val m = consumerFn.receive
    producer.send(s"Hi$i")
    val messageId = m.get.messageId
//    println()
println(m.get.value)
//    println(messageId)
//    println(s"Topic: ${messageId.topic}")
//    println(s"PartitionIndex:${messageId.partitionIndex}")
//    println(s"EntryId:${messageId.entryId}")
//    println(s"LedgerId:${messageId.ledgerId}")
//    println(s"topicPartition:${messageId.topicPartition}")
//    Thread.sleep(1000)
    i+1
  }.foreach(_=>())

}
