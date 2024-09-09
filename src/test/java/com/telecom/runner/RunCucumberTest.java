package com.telecom.runner;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import com.telecom.utils.AutoTool;
import com.telecom.utils.LogHandler;
import com.telecom.utils.SemaphoreControl;
import com.telecom.utils.SoapHandler;
import com.telecom.utils.ThreadLocalProperties;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,html:target/cucumber-reports.html,json:target/cucumber.json")
//Could use Junit5 tags like @IncludeTags("Firefox|Chrome") etc
public class RunCucumberTest {

	@BeforeAll
	public static void frameworkSetup() {
		System.setProperties(new ThreadLocalProperties(System.getProperties()));
		AutoTool.loadSetupValues();
		AutoTool.testValues().loadMainTestValues();
		SemaphoreControl.setupSemaphore();
		LogHandler.startSuiteLogFile();
	}

	@Before(order=0)
	public void scenarioSetup(Scenario scenario) {
		SemaphoreControl.getThreadToWork();
		AutoTool.scenarioHandler().setThreadScenario(scenario);
		LogHandler.startTestCaseLogFile();
		AutoTool.testValues().startThreadTestValues();
	}

	@After(order=100)
	public void scenarioTearDown(Scenario scenario) {
		boolean takeScreenshotOnError = AutoTool.getSetupValue("takeBrowserScreenshotOnError").equals("true");
		boolean driverStillOn=AutoTool.getDriver()!=null;
		if (driverStillOn&&scenario.isFailed()&&takeScreenshotOnError) {
			AutoTool.addScreenshotToCucumberExecutionReport("Before Error Screenshot.");
		}
		AutoTool.clearDriver();
		LogHandler.writeTestCaseLog();
		AutoTool.scenarioHandler().clearScenario();
		SoapHandler.clearSoapValues();
		AutoTool.testValues().removeThreadTestValues();
		SemaphoreControl.releaseThreadToWork();
		System.gc();
	}

	@AfterAll
	public static void frameworkFinalTasks() {
		LogHandler.writeSuiteLog();
	}
}