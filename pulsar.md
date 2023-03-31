### Pulsar Notes
- supports both Pub/Sub and streaming (queues)
- implements/complies/ maybe certified for JMS
- tiered storage means off loading of old entries to s3 buckets or so, so not expiration necessary
- replicated cursors between clusters
- lots of OS libs for clients across languages 
  - for scala - Neutron (some nice builder patterns), pulsar4s
- can create persistent and non-persistent topics on fly
- supports sync and async replication
- supports lots of tuning pa5ams for publishing and subscriptions - batching by time and size, subscriptions can configure fetch size