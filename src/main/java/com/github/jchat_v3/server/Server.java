package main.java.com.github.jchat_v3.server;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private int port;
    private int connectedClients;
    private String serverName;

    private static final int MAX_CLIENTS = 10;
    private static final String POISON = Double.toString(Double.MAX_VALUE);
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private ArrayList<ServerThread> threads = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 2)
            return;

        new Server(Integer.parseInt(args[0]), args[1], args[2].equals("log"));
    }

    // add loggin support, look at v1 for setup. gl <3
    public Server(int portArgs, String serverNameArgs, boolean logEnabled) {
        this.port = portArgs;
        this.serverName = serverNameArgs;

        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, String.format("Server %s running on port %s.", serverName, port));

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (true) {
                if (connectedClients >= MAX_CLIENTS) break;
                Socket socket = serverSocket.accept();
                connectedClients++;
                LOGGER.log(Level.INFO, String.format("New client connected. Total of: %s", connectedClients));
                ServerThread thread = new ServerThread(socket, serverName);
                threads.add(thread);
                thread.start();
            }

        } catch (IOException exception) {
            throwError(exception);
        }
    }

    public void forwardMessages(String msg, String name) {
        for (ServerThread thread : threads) {
            LOGGER.info(msg);
            thread.getOutput().println(name + ": " + msg);
        }
    }

    public void throwError(Exception exception) {
        LOGGER.severe("Error of type: " + exception.toString() + " thrown.");
    }

    private class ServerThread extends Thread {
        private PrintWriter output;
        private Socket socket;
        private BufferedReader input;
        private String serverName;
        private String name;

        public ServerThread(Socket socketA, String serverNameA) {
            this.socket = socketA;
            this.serverName = serverNameA;
        }

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException exception) {
                throwError(exception);
            }

            output.println(String.format("You are connected to: %s. There %s currently connected.", serverName,
                    ((connectedClients == 1) ? "is " : "are ") + connectedClients
                            + ((connectedClients == 1) ? " client" : " clients")));

            String incMsg;

            while (true) {
                if (Thread.interrupted())
                    break;

                try {
                    incMsg = input.readLine();
                    if (incMsg == null)
                        continue;

                    if (incMsg.equals(POISON)) {
                        close();
                        break;
                    }

                    if (incMsg.startsWith("##NAME##")) {
                        name = incMsg.split("##NAME##")[1];
                        continue;
                    }

                    forwardMessages(incMsg, name);
                } catch (IOException exception) {
                    close();
                }
            }
        }

        public PrintWriter getOutput() {
            return output;
        }

        public void close() {
            connectedClients--;
            LOGGER.info(String.format("Closing thread %s. There %s currently connected.",
                    Thread.currentThread().getName(), ((connectedClients == 1) ? "is " : "are ") + connectedClients
                            + ((connectedClients == 1) ? " client" : " clients")));
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException exception) {
                throwError(exception);
            }
            Thread.currentThread().interrupt();
        }
    }
}