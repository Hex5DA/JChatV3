package main.java.com.github.jchat_v3.server;

// imports
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private int port;
    private String serverName;
    
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        if (args.length < 1) return;
        
        new Server(Integer.parseInt(args[0]), args[1]);
    }

    public Server(int portArgs, String serverNameArgs) {
        this.port = portArgs;
        this.serverName = serverNameArgs;

        String serverOnline = String.format("Server %s running on port %s.", serverName, port);
        logger.setLevel(Level.INFO);
        logger.log(Level.INFO, serverOnline);


        try (
            ServerSocket serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "New client connected.");
                new ServerThread(socket, serverName).start();
            }

        } catch (IOException exception) {
            logger.log(Level.WARNING, "IOException was thrown: ", exception);
        }
    }

    private class ServerThread extends Thread {
        private Socket socket;
        private String serverName;

        public ServerThread(Socket socketA, String serverNameA) {
            this.socket = socketA;
            this.serverName = serverNameA;
        }
        
        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                output.println("You are connected to: " + serverName);

                String txt;

                while (true) {
                    txt = input.readLine();
                    if (txt == null) continue;
                    logger.log(Level.INFO, txt);
                }
            } catch (IOException exception) {
                logger.log(Level.WARNING, "IO Error thrown: ", exception);
            }
        }
    }
}
