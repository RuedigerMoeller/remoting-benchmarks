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

Numbers on Haswell XEON 2.5 ghz Centos 7:

Kontraktor:	a) 1.900.000	b) 1.050.000
AKKA	a) 76.000	b) 65000
RestExpress	a) 15.000	 b) 15.000  Note: http 1.1 does not allow for fire and forget
Rest 100 connections:	48.000	48.000  Note: client uses 100 (!) connections to fire requests concurrently. 
This is cheating as message order is messed up this way. Was just curious.

