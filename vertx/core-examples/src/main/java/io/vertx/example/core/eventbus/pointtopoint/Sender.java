package io.vertx.example.core.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.util.Runner;
import org.nustaq.kontraktor.util.RateMeasure;
import org.nustaq.serialization.FSTConfiguration;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Sender extends AbstractVerticle {

    private static final int NUM_MSG = 5_000_000;

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runClusteredExample(Sender.class);
    }

    static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    RateMeasure msg = new RateMeasure("rate");
    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();

        AtomicInteger count = new AtomicInteger(0);
        // Send a message every second
        for (int i = 0; i < NUM_MSG; i++) {
            eb.send("ping-address", conf.asByteArray(new Sum(i, i + 1)), reply -> {
                if (reply.succeeded()) {
                    msg.count();
                    count.incrementAndGet();

                    if ( count.get() == NUM_MSG ) {
                        System.out.println("all received");
                    }
                } else {
                    System.out.println("No reply");
                }
            });
        }

    }


}
