package main.java.com.github.jchat_v3.client;


// imports
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tab extends JPanel {    
    public RecieveThread thread;
    private final Connection connection;
    private static final int BORDER_SIZE = 20;

    private static final Logger logger = Logger.getLogger(Tab.class.getName());

    private JButton sendButton;
    private JTextArea sendBar;
    private JTextArea history;

    public Tab(Connection connection) {
        this.connection = connection;
        
        setLayout(new BorderLayout());
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        sendBar = new JTextArea();
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ClickHandler());
        history = new JTextArea();
        
        sendBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        bottom.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        bottom.add(sendBar);
        bottom.add(sendButton, BorderLayout.EAST);
        
        add(history, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        thread = new RecieveThread();
        // !! This breaks things !! delete later !!
        thread.active = true;
        thread.start();
    }

    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == sendButton) {
                logger.info("Sending " + sendBar.getText());
                connection.sendMessage(sendBar.getText());
                history.append(sendBar.getText() + System.lineSeparator());
                sendBar.setText("");
            }
        }
    }

    public class RecieveThread extends Thread {
        public boolean active = false;

        @Override
        public void run() {
            while (active) {
                history.append(connection.recieveMessage() + System.lineSeparator());
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException exception) {
                    logger.log(Level.WARNING, "Thread interrupted.", exception);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
