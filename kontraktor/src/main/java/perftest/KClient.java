package perftest;

import org.nustaq.kontraktor.remoting.tcp.*;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KClient {

    public static void main(String[] args) {
        KServer server = (KServer) new TCPConnectable(KServer.class,"localhost",7001).connect( (res,err) -> {
            System.out.println("sidconnected, exiting");
            System.exit(0);
        }).await();
//        for ( int i = 0; i < 5_000_000; i++ ) {
//            server.sum(i,i+1);
//        }
        for ( int i = 0; i < 5_000_000; i++ ) {
            server.sumMsg(new Sum(i,i+1));
        }
    }
}
