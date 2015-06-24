package io.vertx.example.core.eventbus.pointtopoint;

import java.io.Serializable;

/**
 * Created by ruedi on 24/06/15.
 */
public class Sum implements Serializable {
    private int first;
    private int second;

    public Sum(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}

