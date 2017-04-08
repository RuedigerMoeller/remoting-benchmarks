# remoting-benchmarks

test RPC/messaging point to point performance of various products.
currently only
* kontraktor
* akka
* RestExpress (because of latency-dependence of http 1.1 throughput, practically any rest implementation 
will have similar performance except for extremely crappy ones which manage significantly outstrip 
http 1.1 keep alive design fail)

* finagle - wants me to fork and compile their version of thrift just to get something started. No thanks.
* JBoss Remoting 3 - no documentation. Samples on the internet manage to send byte arrays with >100 lines of code.
* JBoss Remoting 2 - will be added later, seems more comparable
* Paralell Universe - current version had build failures and galaxy did not work. Also no buildable examples. 
  Will revisit later.
  
Pull requests are welcome.

Testcase:

a) fire and forget throughput
server.tellSum( a, b ); // server computes sum but does not send a reply

b) send a sumrequest and receive result async.  
server.askSum( a, b ).then( result -> ... )

results see http://java-is-the-new-c.blogspot.de/2015/06/dont-rest-revisiting-rpc-performance.html

**Note**: test has been updated to kontraktor 3.33 (from 3.01) this leads to a performance loss of 30-70% in results (still beating the other frameworks by a huge margin)

Reasons are practical (all of those changes can be reverted by overloading/configuring kontraktor to obtain raw throughput of 3.01-3.14):

* remotecalls now contain their call arguments serialized (downside: slower encoding, upside: can use "routing remote actors" which route remotecalls without having knowledge of the classes passed through [no downtime when updating a distributed system])
* switched from fixed-sized/block-if-full queues to dynamically growing unbounded queues. Bounded queues of a performance advantage however with larger distributed systems a queue overflow easily messes up the whole system by blocking a random actor thread, so the unbounded queues are more foolproof and require less tuning and testing.
