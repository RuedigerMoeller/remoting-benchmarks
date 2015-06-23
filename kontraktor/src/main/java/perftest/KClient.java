package perftest;

import org.nustaq.kontraktor.remoting.tcp.*;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KClient {

    public static void main(String[] args) {
        KServer server = (KServer) new TCPConnectable(KServer.class,"localhost",7001).connect( (res,err) -> {
            System.out.println("disconnected, exiting");
            System.exit(0);
        }).await();
        int NUM_MSG = 5_000_000;
//        for ( int i = 0; i < NUM_MSG; i++ ) {
//            server.sum(i,i+1);
//        }
//        for ( int i = 0; i < NUM_MSG; i++ ) {
//            server.sumMsg(new Sum(i, i + 1));
//        }


        for ( int i = 0; i < NUM_MSG; i++ ) {
            server.askSum(new Sum(i, i + 1));
        }

    }
}
