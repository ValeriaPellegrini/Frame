package com.telecom.utils;

import java.util.Properties;

/** Extension of Properties object defined to implement multithread safe system properties. It helps defining Selenium's drivers attributes for each thread.
 */
public class ThreadLocalProperties extends Properties {

    private static final long serialVersionUID = 1L;
    private final ThreadLocal<Properties> localProperties = new ThreadLocal<Properties>() {
        @Override
        protected Properties initialValue() {
            return new Properties();
        }
    };

    public ThreadLocalProperties(Properties properties) {
        super(properties);
    }

    @Override
    public String getProperty(String key) {
        String localValue = localProperties.get().getProperty(key);
        return localValue == null ? super.getProperty(key) : localValue;
    }

    @Override
    public Object setProperty(String key, String value) {
        return localProperties.get().setProperty(key, value);
    }
}