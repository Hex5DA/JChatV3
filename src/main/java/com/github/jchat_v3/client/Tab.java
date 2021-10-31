package main.java.com.github.jchat_v3.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Tab extends JPanel {
    private RecieveThread thread;
    private final Connection CONNECTION;
    private static final int BORDER_SIZE = 20;

    private static final Logger LOGGER = Logger.getLogger(Tab.class.getName());

    private JButton sendButton;
    private JTextField sendBar;
    private JTextArea history;

    public Tab(Connection connection) {
        this.CONNECTION = connection;

        setLayout(new BorderLayout());
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        sendBar = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ClickHandler());
        history = new JTextArea();
        history.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(history, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        sendBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        bottom.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        bottom.add(sendBar);
        bottom.add(sendButton, BorderLayout.EAST);

        sendBar.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER)
                    send();
            }
            // #region
            @Override
            public void keyTyped(KeyEvent event) {
                /**/}

            @Override
            public void keyReleased(KeyEvent event) {
                /**/}
            // #endregion
        });

        add(historyScrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        thread = new RecieveThread();
        thread.active = true;
        thread.start();
    }

    public RecieveThread getThread() {
        return this.thread;
    }

    public void send() {
        LOGGER.info("Sending: " + sendBar.getText());
        CONNECTION.sendMessage(sendBar.getText());
        sendBar.setText("");
    }

    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == sendButton)
                send();
        }
    }

    public class RecieveThread extends Thread {
        private boolean active = false;

        @Override
        public void run() {
            String incMsg;
            while (active) {
                incMsg = CONNECTION.recieveMessage();
                if (incMsg != null) {
                    history.append(incMsg + System.lineSeparator());

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException exception) {
                        LOGGER.log(Level.WARNING, "Thread interrupted.", exception);
                        Thread.currentThread().interrupt();
                    }
                } else Thread.currentThread().interrupt();
            }
        }

        public void setActive(boolean targetActive) {
            this.active = targetActive;
        }
    }
}
