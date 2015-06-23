package perftest;

import org.nustaq.kontraktor.*;
import org.nustaq.kontraktor.remoting.tcp.*;
import org.nustaq.kontraktor.util.*;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KServer extends Actor<KServer> {

    RateMeasure measure = new RateMeasure("msg/sec");

    // that's how a no-return sum method would look like with Kontraktor
    public void sum( int x, int y ) {
        measure.count();
        int result = x + y;
    }

    // that's a graceful slower impl to match Akka sample 1:1
    public void sumMsg( Sum sum ) {
        measure.count();
        int result = sum.getFirst() + sum.getSecond();
    }

    // that's how the method look as idiomatic kontraktor method (params instead message class)
    public IPromise<Integer> askSum( int x, int y ) {
        measure.count();
        return new Promise<>(x + y);
    }

    // unnecessary message object, but obviate accusation of testing unfair ..
    public IPromise<Integer> askSumMsg( Sum sum ) {
        measure.count();
        return new Promise<>(sum.getFirst()+sum.getSecond());
    }

    public static void main(String[] args) {
        KServer server = Actors.AsActor(KServer.class);
        new TCPPublisher(server,7001).publish();
    }

}
