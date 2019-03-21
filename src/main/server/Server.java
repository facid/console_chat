package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.server.Constants.PORT;

public class Server {
    static final List<Connection> connections = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server running: " + serverSocket);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.print("Connected: " + socket + " ");

                try {
                    connections.add(new Connection(socket));
                } catch (IOException e) {
                    System.out.println("Client closed");
                    socket.close();
                }
            }
        }
    }

    static boolean checkNickname(String nickname) {
        String nickWithoutSpace = nickname.replaceAll("\\s", "");
        return (nickWithoutSpace == null || nickWithoutSpace.length() == 0);
    }
}