package io.vertx.example.core.eventbus.pointtopoint;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.impl.codecs.ByteArrayMessageCodec;
import io.vertx.example.util.Runner;
import org.nustaq.kontraktor.util.RateMeasure;
import org.nustaq.serialization.FSTConfiguration;

import java.util.Set;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

    static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runClusteredExample(Receiver.class);
    }

    @Override
    public void start() throws Exception {
        Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();

        EventBus eb = vertx.eventBus();
        RateMeasure measure = new RateMeasure("rate");

        eb.consumer("ask", new Handler<Message<byte[]>>() {
                @Override
                public void handle(Message<byte[]> event) {
                    Sum msg = (Sum) FSTConfiguration.getDefaultConfiguration().asObject((byte[]) event.body());
                    measure.count();
                    // Now send back reply
                    event.reply(msg.getFirst() + msg.getSecond());
                }
            });

        eb.consumer("tell", new Handler<Message<byte[]>>() {
                @Override
                public void handle(Message<byte[]> event) {
                    Sum msg = (Sum) FSTConfiguration.getDefaultConfiguration().asObject((byte[]) event.body());
                    measure.count();
                }
            });

        System.out.println("Receiver ready!");
    }
}
