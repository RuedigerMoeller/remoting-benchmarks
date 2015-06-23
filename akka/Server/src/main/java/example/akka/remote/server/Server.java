package example.akka.remote.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Server {

    public static void main(String... args) {
        // Creating environment
        ActorSystem system = ActorSystem.create("AkkaRemoteServer", ConfigFactory.load());

        // Create an actor
        system.actorOf(Props.create(CalculatorActor.class), "CalculatorActor");
    }
}
