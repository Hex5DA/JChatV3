package main.java.com.github.jchat_v3.server;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.ArrayList;

import main.java.com.github.jchat_v3.server.Logging.LogTypes;

public class ServerThread extends Thread {
    private static final String POISON = Double.toString(Double.MAX_VALUE);
    private static ArrayList<ServerThread> threads = new ArrayList<>();
    private static int connectedClients = 0;

    private BufferedReader input;
    private PrintWriter output;
    private String serverName;
    private Socket socket;
    private String name;

    public static int getConnectedClients() {
        return connectedClients;
    }

    public static void setConnectedClients(int connectedClients) {
        ServerThread.connectedClients = connectedClients;
    }

    public void forwardMessages(String msg, String name) {
        for (ServerThread thread : threads) {
            thread.getOutput().println(name + ": " + msg);
        }

        Logging.log(LogTypes.CLIENT, String.format("[%s] %s", name, msg));
    }

    public ServerThread(Socket socket, String serverName) {
        setConnectedClients(getConnectedClients() + 1);
        threads.add(this);

        this.socket = socket;
        this.serverName = serverName;

        try {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException exception) {
            Logging.throwError(exception);
        }
    }

    @Override
    public void run() {
        output.println(String.format("You are connected to: %s. There %s currently connected.", serverName,
                ((connectedClients == 1) ? "is " : "are ") + connectedClients
                        + ((connectedClients == 1) ? " client" : " clients")));

        String incMsg;
        while (!Thread.interrupted()) {
            try {
                incMsg = input.readLine();
                if (incMsg == null)
                    continue;

                if (incMsg.equals(POISON)) {
                    close();
                    break;
                }

                if (incMsg.startsWith("##NAME##")) {
                    String old = name;
                    name = incMsg.split("##NAME##")[1];

                    if (old != null) {
                        forwardMessages(String.format("\"%s\" has changed their name to \"%s\"", old, name),
                                LogTypes.SERVER.toString());
                    } else {
                        forwardMessages(String.format("\"%s\" has joined the server!", name),
                                LogTypes.SERVER.toString());
                    }

                    continue;
                }

                forwardMessages(incMsg, name);
            } catch (IOException exception) {
                close();
            }
        }
    }

    public PrintWriter getOutput() {
        return this.output;
    }

    public void close() {
        setConnectedClients(getConnectedClients() - 1); // Java moment

        String remaining = String.format("There %s currently connected.",
                ((connectedClients == 1) ? "is " : "are ") + connectedClients
                        + ((connectedClients == 1) ? " client" : " clients"));

        forwardMessages(String.format("\"%s\" has left. %s", name, remaining), LogTypes.SERVER.toString());
        Logging.log(LogTypes.SERVER,
                String.format("Closing thread %s. %s", Thread.currentThread().getName(), remaining));

        try {
            this.output.close();
            this.input.close();
            this.socket.close();
        } catch (IOException exception) {
            Logging.throwError(exception);
        }
        Thread.currentThread().interrupt();
    }
}
