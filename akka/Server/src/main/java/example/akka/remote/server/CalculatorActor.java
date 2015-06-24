package example.akka.remote.server;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static example.akka.remote.shared.Messages.*;

public class CalculatorActor extends UntypedActor {

    RateMeasure measure = new RateMeasure("received tell");
    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof SumAsk) {
            measure.count();
            SumAsk sum = (SumAsk) message;

            int result = sum.getFirst() + sum.getSecond();
            getSender().tell(new Result(result), getSelf());
        } else if (message instanceof Sum) {
            measure.count();
            Sum sum = (Sum) message;

            int result = sum.getFirst() + sum.getSecond();
            // no reply for simple one way message
//            getSender().tell(new Result(result), getSelf());
        } else {
            unhandled(message);
        }
    }
}
