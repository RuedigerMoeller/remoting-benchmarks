package example.akka.remote.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import example.akka.remote.shared.Messages;

public class Server {

    public static void main(String... args) {
        System.out.println(Messages.Sum.class.getName());
        // Creating environment
        ActorSystem system = ActorSystem.create("AkkaRemoteServer", ConfigFactory.load());

        // Create an actor
        system.actorOf(Props.create(CalculatorActor.class), "CalculatorActor");
    }
}
