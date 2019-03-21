package main.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Connection (Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    @Override
    public void run() {
        try {
            String userName = in.readLine();
            if (Server.checkNickname(userName)) {
                out.println("Nickname can not be empty! Enter exit and try again.");
            } else {
                System.out.println("- " + userName + " joined");

                out.println("Welcome, " + userName + "! " + "Enter your message or exit to leave:");
            }
        } catch (IOException e){
            System.out.println(e);
        }

        try {
            while (true) {
                String message = in.readLine();
                if (message.equals("exit")) {
                    out.println("exit");
                    this.closeAll();
                    break;
                }
                System.out.println("Echoing: " + message);

                MessageHistory history = new MessageHistory();
                history.save(message);
                history.send(out);
            }
        } catch (IOException e) {
            this.closeAll();
        }
    }

    private void closeAll() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

                for (Connection connection : Server.connections) {
                    if (connection.equals(this)) {
                        connection.interrupt();
                    }
                    Server.connections.remove(this);
                }
            }
        } catch (IOException e) {
            System.out.println("Close error");
        }
    }
}
