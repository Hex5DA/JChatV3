package main.java.com.github.jchat_v3;

// imports
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private ArrayList<Tab> tabs;
    private JTabbedPane tabbedPane;

    public Main() {
        super("Java Chatroom V3");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);

        tabs = new ArrayList<>();

        logger.setLevel(Level.INFO);

        tabbedPane = new JTabbedPane();

        // temporary
        add(tabbedPane);
        setVisible(true);

        Connection connection = getConnectionDetails();
        addTab(new Tab(connection), "Tab");
    }

    public void addTab(Tab toAdd, String title) {
        tabbedPane.addTab(title, toAdd);
        tabs.add(toAdd);
    }

    public Connection getConnectionDetails() {
        Login login = new Login(this);

        try {
            while (!login.hasInput()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException exception) {
            logger.log(Level.WARNING, "Thread interrupted.", exception);
            Thread.currentThread().interrupt();
        }

        login.dispose();
        return new Connection(login.getPort(), login.getHost());
    }
}
