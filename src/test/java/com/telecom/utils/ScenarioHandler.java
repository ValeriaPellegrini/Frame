package com.telecom.utils;
/**Class that implements singleton design pattern (creational pattern) and the eager initialization concept. It's main use is the handling Cucumber's scenario object in a multithread safe manner. The class is declared public and final, and should have a private constructor to prevent instantiation if needed. The final keyword prevents sub-classing and can improve efficiency at runtime.
 * @author Marcelo Luna.
 * @version 2.0
 * @since 2.0
 */

import io.cucumber.java.Scenario;

public final class ScenarioHandler {
	private static ScenarioHandler instance = new ScenarioHandler();
	private ThreadLocal<Scenario> threadScenarioContainer = new ThreadLocal <Scenario>();
	
	private ScenarioHandler(){
	}
	
    public static ScenarioHandler getInstance() {
        return instance;
    }
    
	/**Sets a Cucumber's Scenario object in a ThreadLocal object. This allows the user to store the scenario in a multithread safe manner for later use.
	 * 
	 * @param incScenario Cucumber's Scenario object to be stored.
	 */
	public void setThreadScenario(Scenario incScenario) {
		threadScenarioContainer.set(incScenario);
	}
	
	/**Returns a Cucumber's Scenario object previously stored in a ThreadLocal object.
	 * @return Stored Cucumber's Scenario object.
	 */
	public Scenario getScenario() {
		return threadScenarioContainer.get();
	}
	
	/**Clears a Cucumber's Scenario object from a ThreadLocal object.
	 * 
	 */
	public void clearScenario() {
		threadScenarioContainer.remove();
	}

}
