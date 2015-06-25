Akka Remote Bench in Java
=========================

I added kryo serialization as performance was bad, however it did not make that much a difference. Which implies
performance is lost outside of the serialization.

Preregistering classes and turning off shared references/object graph is not done it also has not been done
for the kontraktor benchmark (has also option for preregistering and no-shared-refs).

Run:

Import pom to IDE or build manually with maven.

Start Server, Start Client. Server dumps event rate to sysout.

Ask/Tell variant must be switched by commenting/decommenting in Client.java