### Apache Pulsar Notes
- supports both Pub/Sub and streaming (queues)
- implements/complies/ maybe certified for JMS
- tiered storage means off loading of old entries to s3 buckets or so, so not expiration necessary (this is done by offloaders: S3, HDFS, file system, Azure, GCS)
  (transparent to clients, I think as a scheduled job)
- replicated cursors between clusters
- lots of OS libs for clients across languages 
  - for scala - Neutron (some nice builder patterns), pulsar4s
- can create persistent and non-persistent topics on fly
- supports sync and async replication
- supports lots of tuning params for publishing and subscriptions - batching by time and size, subscriptions can configure fetch size

- supports scheduled delivery (somehow delaying the messages)
- supports dead letter queue out of the box (on client?)
- supports some custom functions deployed directly to Pulsar Broker
- Brokers scala independently from storage
- - supports message de-duplication
- topic compaction something like a sow, triggered async, consumer can wait or just check the compaction status. Example is stock ticker with deleted and updated prices for a stock. 
- supports 4 different types of subs, 3 of them are ordered. Failover one seems interesting, could it replace leadership?
- supports multi-tenancy - Instance (multiple servers suprisingly) => cluster => tenants => namespace => topics
- authentication can use **jwttoken** or cert file
- partitioned topics/producers allow faster publishing with different modes for partitioning, subscriptions still use those 4 available modes


### Apache Nifi Notes
- **Camunda** is a bpm(Business Process Management) tool. 
It proceeds with workflow and managing decisions. 
- **Nifi** is a data flow manager. It consumes the data from the "x" platform and proceed and then publish or send another platform or tool.
 - **Camel** is a library
Apache Camel: A versatile open source integration framework. 
An open source Java framework that focuses on making integration easier and more accessible to developers; Apache NiFi: A reliable system to process and distribute data. An easy to use, powerful, and reliable system to process and distribute data.
