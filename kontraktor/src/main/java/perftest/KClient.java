package perftest;

import org.nustaq.kontraktor.Actor;
import org.nustaq.kontraktor.Actors;
import org.nustaq.kontraktor.IPromise;
import org.nustaq.kontraktor.remoting.tcp.*;
import org.nustaq.kontraktor.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by moelrue on 23.06.2015.
 */
public class KClient {

    static int NUM_MSG = 20_000_000;

    public static class ClientActor extends Actor<ClientActor> {

        KServer server;

        public IPromise benchTellIdiomatic() {
            server.print("benchTellIdiomatic START");
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // pseudo wait. else actor thread gets stuck (queue overflow)
                server.sum(i, i + 1);
            }
            server.print("benchTellIdiomatic DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        public IPromise benchTellSumObject() {
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
            server.print("benchAskSum START");
            int resCount[] = {0}; // valid callback in same thread
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // nonblocking wait. else actor thread gets stuck as messages queue up massively
                server.askSum(i, i + 1).then(res -> {
                    resCount[0]++;
                });
            }
            while( resCount[0] < NUM_MSG ) {
                yield(1000);
                System.out.println("waiting "+resCount);
            }
            server.print("benchAskSum DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        public IPromise benchAskSumMsg() {
            server.print("benchAskSumMsg START");
            int resCount[] = {0}; // valid, callback in same thread
            for ( int i = 0; i < NUM_MSG; i++ ) {
                while ( server.isMailboxPressured() )
                    yield(); // nonblocking wait. else actor thread gets stuck as messages queue up massively
                server.askSumMsg(new Sum(i,i+1)).then( res -> resCount[0]++ );
            }
            while( resCount[0] < NUM_MSG ) {
                yield(1000);
                System.out.println("waiting "+resCount[0]);
            }
            server.print("benchAskSumMsg DONE");
            Log.Info(this,"Done");
            return resolve();
        }

        public IPromise init() {
            server = (KServer) new TCPConnectable(KServer.class,"localhost",7001).connect( (res,err) -> {
                Log.Info(this,"disconnected, exiting");
                System.exit(0);
            }).await(); // pseudo block as inside actor
            return resolve();
        }

    }

    public static void main(String[] args) {
        ClientActor client = Actors.AsActor(ClientActor.class,512000);
        client.init().await(); // we are outside actors => blocking is ok here
        client.benchTellIdiomatic()
            .thenAnd( () -> client.benchTellSumObject())
            .thenAnd(() -> client.benchAskSum())
            .then(() -> client.benchAskSumMsg() );
    }

}
