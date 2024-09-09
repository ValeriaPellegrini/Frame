package com.telecom.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public final class QueryHandler {
    static String sql;

    public static void loadSqlFile(String sqlPath) {
        try {
            InputStream sqlfile = new FileInputStream(".\\Queries\\"+sqlPath);
            sql = new String(sqlfile.readAllBytes());
            sqlfile.close();
        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    public static void changeQueryValues(Map<String, String> data) {
        data.forEach((key,value)->{
            final String actualValue = sql;
            sql = actualValue.replaceFirst(key, value);
        });
    }

    public static String getSql(){
        return sql;
    }
}
