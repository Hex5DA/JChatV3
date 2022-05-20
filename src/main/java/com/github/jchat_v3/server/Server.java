package main.java.com.github.jchat_v3.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.github.jchat_v3.server.Logging.LogTypes;

public class Server {
    private String serverName;
    private int port;

    private static int maxClients = 0;

    public static void main(String[] args) {
        if (args.length < 3)
            return;

        maxClients = Integer.parseInt(args[2]);
        new Server(Integer.parseInt(args[0]), args[1]);
    }

    // add loggin support, look at v1 for setup. gl <3
    public Server(int portArgs, String serverNameArgs) {
        this.port = portArgs;
        this.serverName = serverNameArgs;

        Logging.log(LogTypes.SERVER, String.format("Server %s running on port %s.", serverName, port));

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (true) {
                if (ServerThread.getConnectedClients() >= maxClients)
                    break;

                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket, serverName);
                thread.start();

                Logging.log(LogTypes.SERVER, String.format("New client connected. Total of: %s", ServerThread.getConnectedClients()));
            }
        } catch (IOException exception) {
            Logging.throwError(exception);
        }
    }
}