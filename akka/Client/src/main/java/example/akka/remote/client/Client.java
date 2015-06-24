package example.akka.remote.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        // Creating environment
        ActorSystem system = ActorSystem.create("AkkaRemoteClient", ConfigFactory.load());

        // Client actor
        ActorRef client = system.actorOf(Props.create(ClientActor.class));
        client.tell(system, ActorRef.noSender());

        // start benchmark
        client.tell("tell", ActorRef.noSender());

        // start benchmark
//        client.tell("ask", ActorRef.noSender());

        while (true) {
            Thread.sleep(1000);
            client.tell("printFutures", ActorRef.noSender());
        }
    }
}
