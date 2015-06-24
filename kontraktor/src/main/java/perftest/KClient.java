package perftest;

import org.nustaq.kontraktor.Actor;
import org.nustaq.kontraktor.Actors;
import org.nustaq.kontraktor.IPromise;
import org.nustaq.kontraktor.remoting.tcp.*;
import org.nustaq.kontraktor.util.Log;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KClient {

    static int NUM_MSG = 10_000_000;

    public static class ClientActor extends Actor<ClientActor> {

        KServer server;

        public IPromise benchTellIdiomatic() {
            init();
            server.print("benchTellSumObject START");
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // pseudo wait. else actor thread gets stuck
                server.sum(i, i + 1);
            }
            server.print("benchTellSumObject DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        public IPromise benchTellSumObject() {
            init();
            server.print("benchTellSumObject START");
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // nonblocking wait. else actor thread gets stuck as messages queue up massively
                server.sumMsg(new Sum(i, i + 1));
            }
            server.print("benchTellSumObject DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        public IPromise benchAskSum() {
            init();
            server.print("benchAskSum START");
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // nonblocking wait. else actor thread gets stuck as messages queue up massively
                server.askSum(i, i + 1);
            }
            server.print("benchAskSum DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        // not async because not public. helper
        private void init() {
            server = (KServer) new TCPConnectable(KServer.class,"localhost",7001).connect( (res,err) -> {
                Log.Info(this,"disconnected, exiting");
                System.exit(0);
            }).await(); // pseudo block
        }

    }

    public static void main(String[] args) {
        ClientActor client = Actors.AsActor(ClientActor.class,256000);
        client.benchTellIdiomatic()
            .thenAnd( () -> client.benchTellSumObject())
            .then(    () -> client.benchAskSum() );
    }

}
