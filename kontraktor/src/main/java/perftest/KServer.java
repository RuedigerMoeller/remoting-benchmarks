package perftest;

import org.nustaq.kontraktor.*;
import org.nustaq.kontraktor.remoting.tcp.*;
import org.nustaq.kontraktor.util.*;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KServer extends Actor<KServer> {

    RateMeasure measure = new RateMeasure("msg/sec");

    // that's how a no-return sum method would look like with Kontraktor 2 million/sec
    public void sum( int x, int y ) {
        measure.count();
        int result = x + y;
    }

    // unnecessary message object, but obviate accusation of testing unfair .. 1.5 million/sec
    public void sumMsg( Sum sum ) {
        measure.count();
        int result = sum.getFirst() + sum.getSecond();
    }

    // that's how the method look as idiomatic kontraktor method (params instead message class) 1.05 million/sec
    public IPromise<Integer> askSum( int x, int y ) {
        measure.count();
        return new Promise<>(x + y);
    }

    // unnecessary message object, but obviate accusation of testing unfair ..
    public IPromise<Integer> askSumMsg( Sum sum ) {
        measure.count();
        return new Promise<>(sum.getFirst()+sum.getSecond());
    }

    // just enable client to put separating text
    public void print( String s ) {
        System.out.println("Client:"+s);
    }

    public static void main(String[] args) {
        KServer server = Actors.AsActor(KServer.class, 512000);
        new TCPPublisher(server,7001).publish();
    }

}
