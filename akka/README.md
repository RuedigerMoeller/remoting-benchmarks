Akka Remote Bench in Java
=========================

I added kryo serialization as performance was extremely bad, however it did not make that much a difference. Which implies
performance is lost outside of the serialization.

Preregistering classes and turning off shared references/object graph is not done it also has not been done
for the kontraktor benchmark (has also option for preregistering and no-shared-refs).
