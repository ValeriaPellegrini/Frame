package com.telecom.data;

import com.telecom.utils.AutoTool;
import com.telecom.utils.SoapHandler;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class HWSoapValidator {

    public static boolean validarLinea(String lineaToValidate){
        return validateHwQueryLiteBySubscriber(lineaToValidate)&&validateHwQueryCustomerInfoByLinea(lineaToValidate);
    }

    private static boolean validateHwQueryLiteBySubscriber(String linea){
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Lite_By_Subscriber.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        return SoapHandler.getResponseCode() == 200 && SoapHandler.getTagFromResponse("Envelope.Body.QueryLiteBySubscriberResultMsg.ResultHeader.ResultCode").equals("0");
    }

    private static boolean validateHwQueryCustomerInfoByLinea(String linea){
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Customer_Info_By_Linea.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        boolean validName= true;
        if(SoapHandler.getTagFromResponse("Envelope.Body.QueryCustomerInfoResultMsg.QueryCustomerInfoResult.Customer.IndividualInfo.FirstName").equalsIgnoreCase("no usar")){
            validName = false;
        }
        return SoapHandler.getResponseCode() == 200 && SoapHandler.getTagFromResponse("Envelope.Body.QueryCustomerInfoResultMsg.ResultHeader.ResultCode").equals("0")&&validName;
    }

}
