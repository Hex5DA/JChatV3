package main.java.com.github.jchat_v3.client;

// imports
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private int port;
    private String host;

    private static final Logger logger = Logger.getLogger(Connection.class.getName());

    private Socket socket;
    private PrintWriter output;
    private BufferedReader reader;

    static boolean active = true;

    public Connection(int portA, String hostA) {
        this.port = portA;
        this.host = hostA;

        try {
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException exception) {
            logger.log(Level.WARNING, "UnknownHostException thrown: ", exception);
        } catch (IOException exception) {
            logger.log(Level.WARNING, "IOException thrown: ", exception);
        }        
    }

    public void sendMessage(String toSend) {
        output.println(toSend);
    }

    public String recieveMessage() {
        try {
            return reader.readLine();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "IOException thrown: ", exception);
        }
        return "Could not send message.";
    }
}