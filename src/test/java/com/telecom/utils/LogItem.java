package com.telecom.utils;

import java.util.logging.Level;

public class LogItem {
	private Level logLvl;
	private String callingClass;
	private String callingMethod;
	private String logText;
	
	public Level getLogLvl() {
		return logLvl;
	}
	
	public void setLogLvl(Level logLvl) {
		this.logLvl = logLvl;
	}
	
	public String getLogText() {
		return logText;
	}
	
	public void setLogText(String logText) {
		this.logText = logText;
	}

	public String getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(String callingClass) {
		this.callingClass = callingClass;
	}

	public String getCallingMethod() {
		return callingMethod;
	}

	public void setCallingMethod(String callingMethod) {
		this.callingMethod = callingMethod;
	}
}