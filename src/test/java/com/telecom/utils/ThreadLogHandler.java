package com.telecom.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** It's main use is creating and handling a text only log file in a multithread safe manner.
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */
public class ThreadLogHandler {

    private int thisThreadTestCase = 0;

    private String threadUserLogFileName = new String();
    private String threadDriverLogFileName = new String();

    private Path userLogFilePath = null;
    private Path driverLogFilePath = null;

    private Logger loggerInstance = null;

    private TreeMap<Integer, LogItem> threadLogs = new TreeMap<Integer, LogItem>();

    public void setupTestCaseLogFile(int testCaseNumber) {
        thisThreadTestCase = testCaseNumber;
        loggerInstance = Logger.getLogger("ThreadLogHandler"+testCaseNumber);
        setThreadFileNames();
        setThreadFilePaths();
        defineLogFeatureInnerTitle();
    }

    private void setThreadFileNames() {
        threadUserLogFileName="UserLogFromTestCase"+thisThreadTestCase+".log";
        threadDriverLogFileName="DriverLogFromTestCase"+thisThreadTestCase+".log";
    }

    private void setThreadFilePaths() {
        userLogFilePath=Paths.get(AutoTool.getFACRunningPath()+"//Logs//"+threadUserLogFileName);
        driverLogFilePath=Paths.get(AutoTool.getFACRunningPath()+"//Logs//"+threadDriverLogFileName);
    }

    private String getUserLogAbsolutePath() {
        return userLogFilePath.toAbsolutePath().toString();
    }

    public String getDriverLogAbsolutePath() {
        return driverLogFilePath.toAbsolutePath().toString();
    }

	public void defineLogFeatureInnerTitle() {
		String titleVisualDetail=LogHandler.createLineOfAChar('*');
		addLogP(Level.INFO,"","",titleVisualDetail);
		addLogP(Level.INFO,"","","Feature File: "+AutoTool.scenarioHandler().getScenario().getUri().toString());
		addLogP(Level.INFO,"","","Scenario: "+AutoTool.scenarioHandler().getScenario().getName());
		addLogP(Level.INFO,"","",titleVisualDetail);
	}

    /**Appends text to log in a lazy manner, it adds the calling class and method detail by its own. Can be used with the methods exposed by "AutoTool.get Scenario().", to add meaningful data to the logs.
     * Example: .getId(), .getName(), .getLine(), .getStatus(), .isFailed(), etc.
     * @param kindOfLogLvl log line's kind.
     * @param textToAppend the text that will should be in the log.
     */
    public void addLog(Level kindOfLogLvl,String textToAppend) {
        addLogP(kindOfLogLvl, "Class: "+Thread.currentThread().getStackTrace()[2].getClassName(),
                "Method: "+Thread.currentThread().getStackTrace()[2].getMethodName(), textToAppend);
    }

    /**Appends a detailed text line to the log.
     *
     * @param kindOfLogLvl Log level.
     * @param Class Calling class name.
     * @param Method Calling Method name.
     * @param textToAppend Text to append to the log.
     */
    public void addLogP(Level kindOfLogLvl,String Class, String Method, String textToAppend) {
        int position= threadLogs.size();
        LogItem logLine = new LogItem();
        logLine.setLogLvl(kindOfLogLvl);
        logLine.setCallingClass(Class);
        logLine.setCallingMethod(Method);
        logLine.setLogText(textToAppend);
        threadLogs.put(position, logLine);
    }

    /**Saves log to file.
     *
     */
    public void writeThisThreadTestCaseLog() {
        Handler fileHandler=null;
        try {
            fileHandler = new FileHandler(getUserLogAbsolutePath(), false);
        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        fileHandler.setLevel(Level.ALL);

        loggerInstance.addHandler(fileHandler);
        loggerInstance.setLevel(Level.ALL);

        String detailLineOfNumeralSign = LogHandler.createLineOfAChar('#');

        loggerInstance.logp(Level.INFO, "", "", detailLineOfNumeralSign);
        //Logs all the lines that the user sent to the log.
        threadLogs
                .forEach((a,b)->{
                            loggerInstance.logp(b.getLogLvl(), b.getCallingClass(), b.getCallingMethod(), b.getLogText());
                        }
                );
        loggerInstance.logp(Level.INFO, "", "", detailLineOfNumeralSign);
        fileHandler.close();
        //If it is a front end test case, this logs the driver interactions.
        if (Files.exists(driverLogFilePath)) {
            try {
                BufferedReader seleniumExecutionLogReader = Files.newBufferedReader(driverLogFilePath);
                FileOutputStream logLineComposer = new FileOutputStream(getUserLogAbsolutePath(), true);

                logLineComposer.write(("#############################################################################\n").getBytes());
                logLineComposer.write(("\n").getBytes());
                logLineComposer.write(("============================ Selenium Driver Log ============================\n").getBytes());
                logLineComposer.write(("\n").getBytes());

                seleniumExecutionLogReader.lines().forEach((a)->{
                    try {
                        byte[] byteArr = (a+"\n").getBytes();
                        logLineComposer.write(byteArr);
                    }catch (Exception exp) {
                        System.out.println(exp.getCause());
                        System.out.println(exp.getMessage());
                        exp.printStackTrace();
                    }
                });
                logLineComposer.write((detailLineOfNumeralSign+"#############\n").getBytes());
                seleniumExecutionLogReader.close();
                logLineComposer.close();
                Files.delete(driverLogFilePath);
            } catch (Exception exp) {
                System.out.println(exp.getCause());
                System.out.println(exp.getMessage());
                exp.printStackTrace();
            }
        }
    }
}