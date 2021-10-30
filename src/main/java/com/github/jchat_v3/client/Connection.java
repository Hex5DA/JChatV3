package main.java.com.github.jchat_v3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Connection {
    private int port;
    private String host;

    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    private Socket socket;
    private PrintWriter output;
    private BufferedReader reader;

    private JFrame frame;

    public Connection(int portA, String hostA, JFrame frameA) {
        this.frame = frameA;
        this.port = portA;
        this.host = hostA;

        try {
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException exception) {
            throwError(exception);
        }
    }

    public void throwError(Exception exception) {
        if (exception instanceof SocketException && socket != null) {
            try {
                output.close();
                reader.close();
                reader = null;
                socket.close();
            } catch (IOException internalException) {
                throwError(internalException);
            }
        }

        LOGGER.log(Level.WARNING, exception.toString());
        JOptionPane.showMessageDialog(frame, "Error occured of type: " + System.lineSeparator() + exception.toString()
                + System.lineSeparator() + "Please close this tab.", "Error.", JOptionPane.ERROR_MESSAGE);
    }

    public void sendMessage(String toSend) {
        output.println(toSend);
    }

    public String recieveMessage() {
        if (reader == null)
            return null;

        try {
            return reader.readLine();
        } catch (IOException exception) {
            throwError(exception);
        }
        return "Could not send message.";
    }
}