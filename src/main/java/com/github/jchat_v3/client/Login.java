package main.java.com.github.jchat_v3.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());

    private JFrame frame;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 350;
    private static final int BORDER_SIZE = 5;

    private int port = 0;
    private String prePort = null;
    private String host = null;

    private JButton confirm;
    private JTextField hostField;
    private JTextField portField;

    public Login(JFrame frame) {
        super(frame, "Input Details.");
        setResizable(false);

        this.frame = frame;
        JPanel root = new JPanel();

        JLabel hostLabel = new JLabel("Host:", SwingConstants.CENTER);
        hostField = new JTextField();
        hostField.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel portLabel = new JLabel("Port:", SwingConstants.CENTER);
        portField = new JTextField();
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        confirm = new JButton("Confirm?");
        confirm.addActionListener(new ClickHandler());

        root.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        root.add(hostLabel);
        root.add(hostField);
        root.add(portLabel);
        root.add(portField);
        root.add(confirm);

        root.setLayout(new GridLayout(5, 0, 0, 45));
        add(root);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(frame);

        // Delete this later, just for testing connections
        hostField.setText("localhost");
        portField.setText("42");

        setVisible(true);
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    // True -> host != null, port != 0, checkinput == true
    public boolean hasInput() {
        return !(host == null && port == 0) && checkInput();
    }

    public boolean checkInput() {
        if (host == null && port == 0)
            return false;
        try {
            port = Integer.parseInt(prePort);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == confirm) {
                LOGGER.log(Level.INFO, "confirm button pressed.");
                prePort = portField.getText();
                host = hostField.getText();

                if (!checkInput()) {
                    JOptionPane.showMessageDialog(frame, "Invalid input entered.", "Invalid input.", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.WARNING, "Invalid input.");
                }
            }
        }
    }
}
