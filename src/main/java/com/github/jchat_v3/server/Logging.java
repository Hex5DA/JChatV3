package main.java.com.github.jchat_v3.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    private static final String LOG_FILE = "log.txt";
    private static final BufferedWriter FILE_WRITER = init();

    private static BufferedWriter init() {
        File logFile = new File(LOG_FILE);

        try {
            if (!logFile.createNewFile())
               System.out.println("Log file already exists!");

            return new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void throwError(Exception exception) {
        log(LogTypes.ERROR, "Error of type: \"" + exception.toString() + "\" thrown.");
    }

    public static void log(LogTypes logStart, String message) {
        try {
            String msg = String.format("%s[%s] [%s] : %s", System.lineSeparator(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), logStart.toString(),
                    message);

            FILE_WRITER.write(msg);
            FILE_WRITER.flush();
            System.out.println(msg);
        } catch (IOException exception) {
            throwError(exception);
        }
    }

    enum LogTypes {
        SERVER, CLIENT, ERROR, DEBUG
    }
}
