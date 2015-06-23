package example.akka.remote.client;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import example.akka.remote.shared.Messages;

public class ClientActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    // Getting the other actor
    private ActorSelection selection = getContext().actorSelection("akka.tcp://AkkaRemoteServer@127.0.0.1:2552/user/CalculatorActor");

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals("DoCalcs")) {
            selection.tell(new Messages.Sum(1, 2), getSelf());
        } else if (message instanceof Messages.Result) {
            Messages.Result result = (Messages.Result) message;
            log.info("Got result back from calculator: {}", result.getResult());
        }


    }
}
