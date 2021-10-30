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
    private String serverName;

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private ArrayList<ServerThread> threads = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 2)
            return;

        new Server(Integer.parseInt(args[0]), args[1]);
    }

    public Server(int portArgs, String serverNameArgs) {
        this.port = portArgs;
        this.serverName = serverNameArgs;

        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, String.format("Server %s running on port %s.", serverName, port));

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.log(Level.INFO, "New client connected.");
                ServerThread thread = new ServerThread(socket, serverName);
                threads.add(thread);
                thread.start();
            }

        } catch (IOException exception) {
            throwError(exception);
        }
    }

    public void forwardMessages(String msg) {
        for (ServerThread thread : threads) {
            if (!thread.getSocket().isConnected()) {
                thread.close();
                threads.remove(thread);
                thread.interrupt();
            }

            LOGGER.info(msg);
            thread.getOutput().println(msg);
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

            output.println("You are connected to: " + serverName);

            String incMsg;

            while (true) {
                try {
                    incMsg = input.readLine();
                    if (incMsg == null)
                        continue;

                    forwardMessages(incMsg);
                } catch (IOException exception) {
                    close();
                }
            }
        }

        public Socket getSocket() {
            return socket;
        }

        public PrintWriter getOutput() {
            return output;
        }

        public void close() {
            output.close();
            try {
                input.close();
                socket.close();
            } catch (IOException exception) {
                throwError(exception);
            }
        }
    }
}