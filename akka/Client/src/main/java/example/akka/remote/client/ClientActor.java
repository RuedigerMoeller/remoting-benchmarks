package example.akka.remote.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.JavaPartialFunction;
import akka.pattern.Patterns;
import akka.actor.UntypedActor;
import example.akka.remote.shared.Messages;
import example.akka.remote.shared.RateMeasure;
import scala.concurrent.Future;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientActor extends UntypedActor {

    public static final int NUM_MSG = 1_000_000;
    // Getting the other actor
    private ActorRef server = getContext().actorFor("akka.tcp://AkkaRemoteServer@127.0.0.1:2552/user/CalculatorActor");
    ActorSystem system; // just didn't know where to get this

    RateMeasure rm = new RateMeasure("futures");
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorSystem ) {
            this.system = (ActorSystem) message;
        } else
        if (message.equals("tell")) {
            // note: this one seems to queue up a lot of stuff, so throughput
            // is low in the beginning. After slow start throughput peaks then.
            // peak only is measured
            for ( int i = 0; i < NUM_MSG; i++) {
                server.tell(new Messages.Sum(i, i+1), getSelf());
            }
        } else if (message.equals("ask")) {

            Runnable r = () -> {
                AtomicInteger openFut = new AtomicInteger(0);
                for (int i = 0; i < NUM_MSG; i++) {

                    openFut.incrementAndGet();
                    Future<Object> ask = Patterns.ask(server, new Messages.SumAsk(i, i + 1), 10000);

                    // don't know how to handle this for akka, but this
                    // definitely speeds things up. Else queues get huge and
                    // everything starves
                    while( openFut.get() > 100_000 ) {
                        Thread.yield();
                    }
                    ask.andThen(
                        new JavaPartialFunction() {
                            @Override
                            public Object apply(Object x, boolean isCheck) throws Exception {
                                rm.count();
                                openFut.decrementAndGet();
                                return x;
                            }
                        },
                        context().dispatcher()
                    );
                }
            };
            new Thread(r).start();

        }
    }
}
