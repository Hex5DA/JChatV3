package main.java.com.github.jchat_v3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JDialog {
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    private static final int WIDTH = 200;
    private static final int HEIGHT = 350;

    private int port = 0;
    private String host = null;

    private JButton confirm;
    private JTextField hostField;
    private JTextField portField;

    public Login(JFrame frame) {
        super(frame, "Input Connection Details.");

        logger.setLevel(Level.INFO);

        JPanel root = new JPanel();

        JLabel hostLabel = new JLabel("Host:", SwingConstants.CENTER);
        hostField = new JTextField();
        hostField.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel portLabel = new JLabel("Port:", SwingConstants.CENTER);
        portField = new JTextField();
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        confirm = new JButton("Confirm?");

        confirm.addActionListener(new ClickListener());

        root.add(hostLabel);
        root.add(hostField);
        root.add(portLabel);
        root.add(portField);
        root.add(confirm);

        root.setLayout(new GridLayout(5, 0, 0, 45));
        add(root);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(frame);

        setVisible(true);
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public boolean hasInput() {
        return !(host == null && port == 0);
    }

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
                if(event.getSource() == confirm) {
                    logger.log(Level.INFO, "confirm button pressed.");
                    port = Integer.parseInt(portField.getText());
                    host = hostField.getText();
                }
        }
    }

}
