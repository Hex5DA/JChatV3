package main.java.com.github.jchat_v3;

// imports
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private HashMap<Tab, String> tabs;
    private JTabbedPane tabbedPane;
    public JMenu deleteTab;
    public JMenuItem newTab;

    public Main() {
        super("Java Chatroom V3");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logger.setLevel(Level.INFO);
        tabs = new HashMap<>();
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        JMenuBar top = new JMenuBar();
        newTab = new JMenuItem("New tab");
        newTab.addActionListener(new ClickHandler());
        deleteTab = new JMenu("Delete tab");

        top.add(newTab);
        top.add(deleteTab);
        deleteTab.addMenuListener(new MenuHandler());

        add(top, BorderLayout.NORTH);
        addTab(new Tab(getConnectionDetails()), "Tab " + (tabs.size() + 1));    
        //updateMenuContents();
        setVisible(true);
    }

    public void updateMenuContents() {
        deleteTab.removeAll();
        for (String index : tabs.values()) {
            JMenuItem item = new JMenuItem(index);
            item.addActionListener(new ClickHandler());
            deleteTab.add(item);
        }
    }

    public void addTab(Tab toAdd, String title) {
        tabbedPane.addTab(title, toAdd);
        tabs.put(toAdd, title);
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

    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == newTab) {
                logger.log(Level.INFO, "new tab created here");
            }

            if (tabs.containsValue(event.getSource().toString().split("text=")[1].split("]")[0])) {
                logger.log(Level.INFO, "was delete button");
            }
        }
    }

    private class MenuHandler implements MenuListener {
        @Override
        public void menuSelected(MenuEvent event) {
            if(event.getSource() == deleteTab) {
                updateMenuContents();
                logger.log(Level.INFO, "focus gained");
            }
        }
        @Override
        public void menuDeselected(MenuEvent event) {
            // Isnt needed
        }
        @Override
        public void menuCanceled(MenuEvent event) {
            // Isnt needed
        }
     }
}
