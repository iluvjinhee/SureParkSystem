package com.lge.sureparksystem.parkserver.manager.databasemanager;

public class LogHelper {

    private static boolean LOG_ON = true;

    public static void setLogOn(boolean enable) {
        LOG_ON = enable;
    }

    public static void log() {
        if (LOG_ON) {
            StackTraceElement[] ste = (new Throwable()).getStackTrace();
            String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
                    + ste[1].getMethodName() + "()"
                    + "] oooooo ";
//            System.out.println("[" + text + "]");
            System.out.println(text);
        }
    }

    
    public static void log(String message) {
        if (LOG_ON) {
            StackTraceElement[] ste = (new Throwable()).getStackTrace();
            String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
                    + ste[1].getMethodName() + "()"
                    + "] oooooo ";
//            System.out.println("[" + text + "]" + "[" + message + "]");
            System.out.println(text + message);
        }
    }

    public static void log(String TAG, String message) {
        if (LOG_ON) {
            StackTraceElement[] ste = (new Throwable()).getStackTrace();
            String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
                    + ste[1].getMethodName() + "()"
                    + "] oooooo ";
//            System.out.println("[" + TAG + "]" + "[" + text + "]" + "[" + message + "]");
            System.out.println("[" + TAG + "]" + text + message);
        }
    }
}
