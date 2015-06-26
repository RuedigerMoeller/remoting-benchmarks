package client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.nustaq.kontraktor.util.RateMeasure;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ruedi on 23/06/15.
 */
public class HttpFlood {

    public static int MAX_CONN_PER_ROUTE = 15; // > 1 is cheating as events not delivered in order !
    public static int MAX_CONN_TOTAL = 1000;

    protected CloseableHttpAsyncClient asyncHttpClient;

    public CloseableHttpAsyncClient getClient() {
        synchronized (this) {
            if (asyncHttpClient == null ) {
                asyncHttpClient = HttpAsyncClients.custom()
                    .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                    .setMaxConnTotal(MAX_CONN_TOTAL)
                    .setDefaultIOReactorConfig(
                        IOReactorConfig.custom()
                            .setIoThreadCount(1)
                            .setSoKeepAlive(true)
                            .setSoReuseAddress(true)
                            .build()
                    ).build();
                asyncHttpClient.start();
            }
            return asyncHttpClient;
        }
    }

    public void run() throws InterruptedException {
        int numMsg = 5_000_000;
        final CountDownLatch counter = new CountDownLatch(numMsg);
        final RateMeasure rm = new RateMeasure("rate");
        final AtomicInteger requestsUnderway = new AtomicInteger(0);
        for ( int i = 0; i < numMsg; i++ ) {
            HttpGet get = new HttpGet("http://localhost:8081/sum/"+i+"/"+(i+1));
            requestsUnderway.incrementAndGet();
            // avoid memory crash by queing up too many requests
            while( requestsUnderway.get() > 200000 ) {
                Thread.yield();
            }
            getClient().execute(get, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {
                    rm.count();
                    requestsUnderway.decrementAndGet();
                    counter.countDown();
                }

                @Override
                public void failed(Exception ex) {
                    System.out.println("error");
                    requestsUnderway.decrementAndGet();
                    rm.count();
                    counter.countDown();
                }

                @Override
                public void cancelled() {

                }
            });
        }
        counter.await();
        System.out.println("DONE");
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpFlood().run();
    }
}
