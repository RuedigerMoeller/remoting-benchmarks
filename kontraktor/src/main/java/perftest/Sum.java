package perftest;

import java.io.*;

/**
 * Created by moelrue on 23.06.2015.
 *
 * Unneeded in principle as for kontraktor parameters make up the "message",
 * but tested for fairness (class name is transmitted with each msg, allocation)
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
