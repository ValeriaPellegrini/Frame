package com.telecom.utils;
/**Class that implements singleton design pattern (creational pattern) and the eager initialization concept. It's main use is the handling test values in a multithread safe manner. The class is declared public and final, and should have a private constructor to prevent instantiation if needed. The final keyword prevents sub-classing and can improve efficiency at runtime.
 * @author Marcelo Luna.
 * @version 2.0
 * @since 2.0
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class TestValuesHandler {
	private static TestValuesHandler instance = new TestValuesHandler();
	private ThreadLocal <Map<String, Object>> threadTestValuesContainer = new ThreadLocal <Map<String, Object>>();
	private Map<String, String> mainTestValues = new HashMap<>();
	private final String mainTestValuesTxtPath = "src/test/resources/TestValues/TestValues.txt";

	private TestValuesHandler() {
	}

	public static TestValuesHandler getInstance() {
		return instance;
	}

	/** Loads all test values from a .txt file.
	 * 
	 */
	public void loadMainTestValues() {
		if(Files.notExists(Paths.get(mainTestValuesTxtPath))){
			LogHandler.logErrorToConsole("MISSING FILE: 'TestValues.txt'.");
			System.exit(0);
		}
		try (Stream<String> lines = Files.lines(Paths.get(mainTestValuesTxtPath))) {
			lines.filter(line -> line.contains("="))
			.forEach(line -> {
				String[] keyValuePair = line.split("=", 2);
				String key = keyValuePair[0];
				String value = keyValuePair[1];
				mainTestValues.put(key, value);
			});
			lines.close();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	/**Creates a map to store the test values that the scenarios will use in a multithread safe manner.
	 * 
	 */
	public void startThreadTestValues() {
		threadTestValuesContainer.set(new HashMap<String, Object>());
		threadTestValuesContainer.get().putAll(mainTestValues);
	}

	/**Clears the calling thread map of test values.
	 * 
	 */
	public void removeThreadTestValues() {
		threadTestValuesContainer.remove();
	}

	/**Stores test value for later use.
	 * 
	 * @param testValueKey Reference key for the stored value.
	 * @param testValue Value to be stored.
	 */
	public void setValue(String testValueKey, Object testValue) {
		threadTestValuesContainer.get().put(testValueKey, testValue);
	}

	/**Gets a stored test value from TestValues.txt or a previous setTestValue call, casted to String.
	 * 
	 * @param testValueKey Reference key for the stored value.
	 * @return Stored test value Object as String.
	 */
	public String getValue(String testValueKey) {
		return (String)threadTestValuesContainer.get().get(testValueKey);
	}
	
	/**Gets a stored test value from TestValues.txt or a previous setTestValue call, as Object. This allows access and work with elements of different types.
	 * 
	 * @param testValueKey Reference key for the stored value.
	 * @return Stored test value Object.
	 */
	public Object getTestValueAsObject(String testValueKey) {
		return threadTestValuesContainer.get().get(testValueKey);
	}

	/**Gets all the previously stored test values.
	 * 
	 * @return Map with all the previously stored test values.
	 */
	public Map<String, Object> getAllTestValues(){
		return threadTestValuesContainer.get();
	}
	
	/** Checks if a test value is already stored.
	 * 
	 * @param testValueKey
	 * @return Boolean related to a test value presence.
	 */
	public boolean checkValuePresence(String testValueKey) {
		return threadTestValuesContainer.get().containsKey(testValueKey);
	}
}