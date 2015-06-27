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