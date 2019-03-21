package main.client;

import static main.server.Constants.LOCAL_HOST;
import static main.server.Constants.PORT;

public class Client {
    public static void main(String[] args) {
        new ClientConnection(LOCAL_HOST, PORT);
    }
}


