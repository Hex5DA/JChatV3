package main.java.com.github.jchat_v3.client;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.logging.Logger;

public class ChangeName extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(ChangeName.class.getName());

    private JButton confirmName;
    private String name;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private static final int BORDER_SIZE = 10;

    public ChangeName(JFrame frame) {
        super(frame, "Input Name");
        setResizable(false);
        JPanel root = new JPanel();

        JLabel nameLabel = new JLabel("Input username:", SwingConstants.CENTER);
        JTextField nameField = new JTextField();
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        confirmName = new JButton("Confirm");

        confirmName.addActionListener(event -> {
            name = nameField.getText();
            nameField.setText("");
        });

        root.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        root.add(nameLabel);
        root.add(nameField);
        root.add(confirmName);

        root.setLayout(new GridLayout(3, 0, 0, 25));
        add(root);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public String getNameVar() {
        LOGGER.info(name);
        return name;
    }

    // True -> name != null, name is not blank
    public boolean hasInput() {
        return name != null && !name.isBlank();
    }
}
