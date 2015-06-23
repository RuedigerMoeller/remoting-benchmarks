package example.akka.remote.server;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static example.akka.remote.shared.Messages.*;

public class CalculatorActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    RateMeasure measure = new RateMeasure("received tell");
    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Sum) {
            measure.count();
            Sum sum = (Sum) message;

            int result = sum.getFirst() + sum.getSecond();
//            getSender().tell(new Result(result), getSelf());

//            loggingActor.tell(sum.getFirst() + " + " + sum.getSecond() + " = " + result, getSelf());
        } else {
            unhandled(message);
        }
    }
}
