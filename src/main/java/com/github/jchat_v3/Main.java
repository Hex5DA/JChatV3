package main.java.com.github.jchat_v3;

// imports
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super("Java Chatroom V3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        Tab test2 = new Tab();
        tabs.add("hiiii", test2);

        setVisible(true);
    }
}
