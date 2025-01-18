package com.library.management.util;

public class ThreadManager {

    public static void startDataBackupTask(Runnable task) {
        Thread backupThread = new Thread(task);
        backupThread.setDaemon(true);
        backupThread.start();
    }
}
