package main.java.com.github.jchat_v3.client;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());


    private static final int WIDTH = 200;
    private static final int HEIGHT = 350;
    private static final int BORDER_SIZE = 5;

    private int port = 0;
    private String prePort = null;
    private String host = null;

    private JButton confirm;
    private JTextField hostField;
    private JTextField portField;

    public Login(Main main) {
        super(main, "Input Details.", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                setModalityType(ModalityType.MODELESS);
                LOGGER.info("Closing.");
                dispose();
            }
        });

        JPanel root = new JPanel();

        JLabel hostLabel = new JLabel("Host:", SwingConstants.CENTER);
        hostField = new JTextField();
        hostField.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel portLabel = new JLabel("Port:", SwingConstants.CENTER);
        portField = new JTextField();
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        confirm = new JButton("Confirm");

        confirm.addActionListener(event -> {
            setModal(false);
            LOGGER.log(Level.INFO, "confirm button pressed.");
            prePort = portField.getText();
            host = hostField.getText();

            if (!checkInput()) {
                JOptionPane.showMessageDialog(main, "Invalid input entered.", "Invalid input.",
                        JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.WARNING, "Invalid input.");
            }

            dispose();
        });

        root.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        root.add(hostLabel);
        root.add(hostField);
        root.add(portLabel);
        root.add(portField);
        root.add(confirm);

        root.setLayout(new GridLayout(5, 0, 0, 45));
        add(root);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(main);

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
}
