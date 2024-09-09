package com.telecom.data;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.telecom.utils.AutoTool;
import com.telecom.utils.SoapHandler;

public class HWSoapServices {
    public static String getStatus(String linea) {
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Customer_Info_By_Linea.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        return SoapHandler.getTagFromResponse("Envelope.Body.QueryCustomerInfoResultMsg.QueryCustomerInfoResult.Subscriber.PrimaryOffering.Status");
    }

    public static String getStatusDetail(String linea) {
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Customer_Info_By_Linea.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        return SoapHandler.getTagFromResponse("Envelope.Body.QueryCustomerInfoResultMsg.QueryCustomerInfoResult.Subscriber.SubscriberInfo.StatusDetail");
    }

    public static String getAccountNumber(String linea) {
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Customer_Info_By_Linea.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        return SoapHandler.getTagFromResponse("Envelope.Body.QueryCustomerInfoResultMsg.QueryCustomerInfoResult.Account.AcctInfo[1].RootAcctKey");
    }
}