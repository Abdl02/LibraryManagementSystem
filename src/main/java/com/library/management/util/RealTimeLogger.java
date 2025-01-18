package com.library.management.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RealTimeLogger {
    private static final BlockingQueue<LogEvent> logQueue = new LinkedBlockingQueue<>();
    private static final String LOG_FILE = "library_system.log";
    private static boolean running = true;

    // Consumer
    static {
        Thread logConsumerThread = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                while (running || !logQueue.isEmpty()) {
                    LogEvent event = logQueue.take(); // Take event from the queue
                    writer.write(event.toString());
                    writer.newLine();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        logConsumerThread.setDaemon(true);
        logConsumerThread.start();
    }

    // (Producer)
    public static void log(String actionType, String message) {
        try {
            logQueue.put(new LogEvent(actionType, message)); // Add event to the queue
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        running = false;
    }
}