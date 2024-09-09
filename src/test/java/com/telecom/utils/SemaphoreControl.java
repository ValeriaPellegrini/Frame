package com.telecom.utils;
/**There is a bug in Junit 5 that affects the parallel executions of test cases. This class is implemented to prevent the framework to open as much web drivers as front end scenarios are present because of that bug. Related info: https://github.com/junit-team/junit5/issues/1858 . This should be fixed in Junit 5.9 .
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public final class SemaphoreControl {
	private static Semaphore semaphore;
		
	/**Reads Junit's config file to find the amount of threads that should be used in this execution and sets a semaphore object to control them. The file's path in this case is: "src/test/resources/junit-platform.properties" and its contents are arranged in a "Key-Value" manner.
	 */
	public static void setupSemaphore() {
		try {
			InputStream jUnitConfigFileInput = new FileInputStream("src/test/resources/junit-platform.properties");
		    Properties prop = new Properties();
            prop.load(jUnitConfigFileInput);
            int limitThreadAmount = Integer.valueOf(prop.getProperty("cucumber.execution.parallel.config.fixed.parallelism"));
            semaphore = new Semaphore((limitThreadAmount > 0 ? limitThreadAmount : 1), true);
            
            jUnitConfigFileInput.close();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}
	
	/**Uses a Semaphore java object to limit the amount of threads that can start simultaneously.
	 */
	public static void getThreadToWork() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**Releases a thread taken by the getThreadToWork() method, from the Semaphore java object.
	 * 
	 */
	public static void releaseThreadToWork() {
		semaphore.release();
	}

}