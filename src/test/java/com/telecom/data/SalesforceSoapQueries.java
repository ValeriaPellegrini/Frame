package com.telecom.data;

import com.telecom.utils.AutoTool;
import com.telecom.utils.SoapHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SalesforceSoapQueries {
    private static String serverUrl;
    private static String sessionId;

    private static void login_salesforce() {
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointSALESFORCE"));
        SoapHandler.loadXml("Login_Soap_Salesforce.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userSALESFORCE"));
        map.put("v_password", AutoTool.testValues().getValue("passwordSALESFORCE"));
        map.put("v_organization_id", AutoTool.testValues().getValue("organizationIdSALESFORCE"));
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        serverUrl = SoapHandler.getTagFromResponse("Envelope.Body.loginResponse.result.serverUrl");
        sessionId = SoapHandler.getTagFromResponse("Envelope.Body.loginResponse.result.sessionId");
    }

    public static List<String> query_lines_salesforce(String query){
        login_salesforce();
        SoapHandler.setSoapWebServiceAdress(serverUrl);
        SoapHandler.loadXml("Query_Soap_Salesforce.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_session_id", sessionId);
        map.put("v_query", query);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        return SoapHandler.getListFromResponse("Envelope.Body.queryResponse.result.records.MSISDN__c");
    }

    public static String query_order_scope_account_salesforce(String query){
        login_salesforce();
        SoapHandler.setSoapWebServiceAdress(serverUrl);
        SoapHandler.loadXml("Query_Soap_Salesforce.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_session_id", sessionId);
        map.put("v_query", query);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        String response = SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.OrderScopeValues__c");
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("InstalationAddress__c");
    }

    public static Map<String, String> query_installation_address_salesforce(String query){
        login_salesforce();
        SoapHandler.setSoapWebServiceAdress(serverUrl);
        SoapHandler.loadXml("Query_Soap_Salesforce.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_session_id", sessionId);
        map.put("v_query", query);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        Map<String, String> mapResponse = new HashMap<String, String>();
        mapResponse.put("PROVINCIA", SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.Location_ID__r.StateName__c"));
        mapResponse.put("CIUDAD", SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.Location_ID__r.DEPARTAMENTOMU__c"));
        mapResponse.put("LOCALIDAD", SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.Location_Name__c"));
        mapResponse.put("CALLE", SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.Street_Name__c"));
        String altura = SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.Address_Number__c");
        mapResponse.put("ALTURA", altura.substring(0, altura.indexOf(".")));
        mapResponse.put("CODIGO_POSTAL",SoapHandler.getTagFromResponse("Envelope.Body.queryResponse.result.records.ZIP_Code__c"));
        return mapResponse;
    }

    public static List<String> query_contact_salesforce(String query) {
        login_salesforce();
        SoapHandler.setSoapWebServiceAdress(serverUrl);
        SoapHandler.loadXml("Query_Soap_Salesforce.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_session_id", sessionId);
        map.put("v_query", query);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        return SoapHandler.getListFromResponse("Envelope.Body.queryResponse.result.records.DocumentNumber__c");
    }

}
