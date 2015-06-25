package io.vertx.example.core.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.impl.codecs.ByteArrayMessageCodec;
import io.vertx.example.util.Runner;
import org.nustaq.kontraktor.util.RateMeasure;
import org.nustaq.serialization.FSTConfiguration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

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

    static boolean ASKTEST = false;

    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();
//        new Thread( () -> {
//            while( true ) {
//                if ( ASKTEST )
//                    sendMsg(eb,500);
//                else
//                    sendMsg(eb,200);
//            }
//        }).start();
        vertx.setPeriodic(1, num -> {
            if ( ASKTEST )
                sendMsg(eb,200);
            else
                sendMsg(eb,200);
        });

    }

    RateMeasure counter = new RateMeasure("send");
    private void sendMsg(EventBus eb, int numMsg) {
        for (int i = 0; i < numMsg; i++) {
            if ( ASKTEST ) {
                counter.count();
                eb.send("ask", conf.asByteArray(new Sum(i, i + 1)), reply -> {
                    if (reply.succeeded()) {
                        // vert.x is not single threaded, callbacks run in arbitrary workers
                        // => contention on big machines
//                            count.incrementAndGet();
//                            if (count.get() == NUM_MSG) {
//                                System.out.println("all received");
//                            }
                    } else {
                        System.out.println("No reply");
                        reply.cause().printStackTrace();
                    }
                });
            } else {
                eb.send("tell", conf.asByteArray(new Sum(i, i + 1)));
            }
        }
    }


}
