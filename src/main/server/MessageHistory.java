package main.server;

import java.io.PrintWriter;
import java.util.LinkedList;

class MessageHistory {
    private static final LinkedList<String> history = new LinkedList<>();

    void save(String message) {
        if (history.size() < 10) {
            history.add(message);
        } else {
            history.removeFirst();
            history.add(message);
        }
    }

    void send(PrintWriter out) {
        for (String message : history) {
            out.println(message);
        }
    }
}
