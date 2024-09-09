package com.telecom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/** It's main use is creating and handling an text only log file in a multithread safe manner.
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */
public final class LogHandler {
	private static String masterLogfileName;
	private static ThreadLocal <ThreadLogHandler> threadTestCaseLogger = new ThreadLocal <ThreadLogHandler>();
	private static int testCaseNumber = 0;
	private static String writeLogs;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

    /**Defines the log filename and location in the project. In the example the name is composed by the date and time of the execution.
     *
     */
    private static void setLogFileName() {
        masterLogfileName = "./Logs/"+getDateAndTimeAsString()+".log";
    }

    public static String getDateAndTimeAsString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        return formatter.format(date);
    }

    public static synchronized void startTestCaseLogFile() {
        if (writeLogs.equals("true")) {
            threadTestCaseLogger.set(new ThreadLogHandler());
            testCaseNumber++;
            threadTestCaseLogger.get().setupTestCaseLogFile(testCaseNumber);
        }
    }

    /**Crate the log file and sets the log level for reporting and writing.
     *
     */
    public static void startSuiteLogFile() {
        writeLogs = AutoTool.getSetupValue("writeTxtLogs");
        if (writeLogs.equals("true")) {
            safetyMeasureClearThreadLogFiles();
            LogHandler.setLogFileName();
            try {
                new File(masterLogfileName).createNewFile();
            } catch (Exception exp) {
                System.out.println(exp.getCause());
                System.out.println(exp.getMessage());
                exp.printStackTrace();
            }
        }
    }

    public static String getDriverLogAbsolutePath() {
        return threadTestCaseLogger.get().getDriverLogAbsolutePath().toString();
    }

    /**Creates a line of one type of character, that will be used as item separator in the log.
     *
     * @param charValue The char that will compose the line.
     * @return String of all the same char and 96 units length.
     */
    public static String createLineOfAChar(char charValue) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 96; i++) {
            stringBuilder.append(charValue);
        }
        return stringBuilder.toString();
    }

	public static void writeTestCaseLog() {
		if (writeLogs.equals("true")) {
			threadTestCaseLogger.get().writeThisThreadTestCaseLog();
			threadTestCaseLogger.remove();
		}
	}

    /**Saves log to file.
     *
     */
    public static void writeSuiteLog() {
        if (writeLogs.equals("true")) {
            List<String> testCaseLogsList= new ArrayList<String>();
            String userLogFile;
            for (int i = 1; i < (testCaseNumber+1); i++) {
                userLogFile=AutoTool.getFACRunningPath()+"//Logs//UserLogFromTestCase"+i+".log";
                if(Files.exists(Paths.get(userLogFile))) {
                    testCaseLogsList.add(userLogFile);
                }
            }
            try {
                OutputStream out = new FileOutputStream(masterLogfileName);
                byte[] buf = new byte[1 << 20];
                for (String file : testCaseLogsList) {
                    InputStream in = new FileInputStream(file);
                    int b = 0;
                    while ( (b = in.read(buf)) >= 0)
                        out.write(buf, 0, b);
                    in.close();
                }
                out.close();
            } catch (Exception exp) {
                System.out.println(exp.getCause());
                System.out.println(exp.getMessage());
                exp.printStackTrace();
            }
            safetyMeasureClearThreadLogFiles();
        }
    }

    private static void safetyMeasureClearThreadLogFiles() {
        File yourDir = new File("./Logs/");
        Arrays.stream(yourDir.listFiles((f, p) -> p.contains("UserLogFromTestCase"))).forEach(File::delete);
    }

    /**Appends text to log in a lazy manner, it adds the calling class and method detail by its own. Can be used with the methods exposed by "AutoTool.get Scenario().", to add meaningful data to the logs.
     * Example: .getId(), .getName(), .getLine(), .getStatus(), .isFailed(), etc.
     * @param kindOfLogLvl log line's kind.
     * @param textToAppend the text that will should be in the log.
     */
    public static void addLog(Level kindOfLogLvl,String textToAppend) {
        if (writeLogs.equals("true")) {
            threadTestCaseLogger.get().addLogP(kindOfLogLvl, "Class: "+Thread.currentThread().getStackTrace()[2].getClassName(),
                    "Method: "+Thread.currentThread().getStackTrace()[2].getMethodName(), textToAppend);
        }
    }

    /**Appends a detailed text line to the log.
     *
     * @param kindOfLogLvl Log level.
     * @param Class Calling class name.
     * @param Method Calling Method name.
     * @param textToAppend Text to append to the log.
     */
    public static void addLogP(Level kindOfLogLvl,String Class, String Method, String textToAppend) {
        threadTestCaseLogger.get().addLogP(kindOfLogLvl, Class, Method, textToAppend);
    }
    
	public static void logErrorToConsole(String errorMSG) {
		System.out.println(ANSI_RED+LogHandler.createLineOfAChar('#')+ANSI_RED);
		System.out.println(ANSI_RED+errorMSG+ANSI_RED);
		System.out.println(ANSI_RED+LogHandler.createLineOfAChar('#')+ANSI_RED);
	}
}