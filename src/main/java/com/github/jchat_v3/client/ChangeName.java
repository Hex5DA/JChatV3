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

import java.util.logging.Logger;

public class ChangeName extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(ChangeName.class.getName());

    private JButton confirmName;
    private String name;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private static final int BORDER_SIZE = 10;

    public ChangeName(Main main) {
        super(main, "Input Name", ModalityType.APPLICATION_MODAL);
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

        JLabel nameLabel = new JLabel("Input username:", SwingConstants.CENTER);
        JTextField nameField = new JTextField();
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        confirmName = new JButton("Confirm");

        confirmName.addActionListener(event -> {
            setModal(false);
            name = nameField.getText();
            nameField.setText("");

            if (!hasInput()) {
                JOptionPane.showMessageDialog(main, "Invalid input entered.", "Invalid input.",
                        JOptionPane.ERROR_MESSAGE);
                LOGGER.warning("Invalid input.");
            }

            dispose();
        });

        root.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        root.add(nameLabel);
        root.add(nameField);
        root.add(confirmName);

        root.setLayout(new GridLayout(3, 0, 0, 25));
        add(root);

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(main);
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
