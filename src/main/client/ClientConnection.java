package main.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientConnection {
    private Socket socket;
    private BufferedReader reader;
    private BufferedReader in;
    private PrintWriter out;
    private String userName;

    private String address;
    private int port;

    public ClientConnection (String address, int port) {
        this.address = address;
        this.port = port;

        try {
            this.socket = new Socket(address, port);
        } catch (IOException e) {
            System.err.println("Connection failed");
        }

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            this.getUserName();
            new ReadMessage().start();
            new WriteMessage().start();
        } catch (IOException e) {
            ClientConnection.this.closeAll();
        }
    }

    private void getUserName() throws IOException {
        System.out.print("Enter your nickname: ");
        userName = reader.readLine();
        out.println(userName);
    }

    private void closeAll() {
        try {
            if (!socket.isClosed()){
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.out.println("Close error");
        }
    }

    private class ReadMessage extends Thread {
        @Override
        public void run() {
            String serverMsg;

            try {
                while (true) {
                    serverMsg = in.readLine();
                    if (serverMsg.equals("exit")) {
                        ClientConnection.this.closeAll();
                        break;
                    }
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                ClientConnection.this.closeAll();
            }
        }
    }

    private class WriteMessage extends Thread {
        @Override
        public void run() {
            String message;

            try {
                while (true) {
                    message = reader.readLine();
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");

                    if (!message.equals("exit")) {
                        out.println(userName + "(" + dateFormat.format(date) + ")" + ":" + " " + message);
                    } else {
                        out.println("exit");
                        ClientConnection.this.closeAll();
                        break;
                    }
                }
            } catch (IOException e) {
                ClientConnection.this.closeAll();
            }
        }
    }
}
