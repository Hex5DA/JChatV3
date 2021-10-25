package main.java.com.github.jchat_v3;

import javax.swing.JButton;
// imports
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;

public class Tab extends JPanel {
    private final Connection connection;

    public Tab(Connection connection) {
        this.connection = connection;
        
        setLayout(new BorderLayout());
        JPanel bottom = new JPanel();
        JTextArea sendBar = new JTextArea();
        JButton sendButton = new JButton("Send");
        JTextArea history = new JTextArea();
        
        bottom.add(sendBar);
        bottom.add(sendButton);
        
        add(history, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
