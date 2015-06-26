package example.akka.remote.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Client {
    public static void main(String[] args) throws InterruptedException {

        boolean ask = true;

        // Creating environment
        ActorSystem system = ActorSystem.create("AkkaRemoteClient", ConfigFactory.load());

        // Client actor
//        ActorRef client = system.actorOf(Props.create(ClientActor.class).withDispatcher("pinned-dispatcher"));
        ActorRef client = system.actorOf(Props.create(ClientActor.class));
        client.tell(system, ActorRef.noSender());

        if ( !ask ) {
            while (true) {
                client.tell("tell", ActorRef.noSender());
                Thread.sleep(30000);
                System.out.println("next run");
            }
        } else {
            while (true) {
                client.tell("ask", ActorRef.noSender());
                Thread.sleep(30000);
                System.out.println("next run");
            }
        }

        // start benchmark


    }
}
