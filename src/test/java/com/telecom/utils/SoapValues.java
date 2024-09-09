package com.telecom.utils;

import java.util.List;

import io.restassured.path.xml.XmlPath;

public class SoapValues {
    private String webServiceAdress;
    private String xmlContentAsString;
    private String soapResponse;
    private int responseCode;
    private String responseMSG;
    private String responseAsString;
    private XmlPath xmlPath;

    public String getSoapWebServiceAdress() {
        return webServiceAdress;
    }

    public void setSoapWebServiceAdress(String webServiceAdress) {
        this.webServiceAdress = webServiceAdress;
    }

    public String getXmlContentAsString() {
        return xmlContentAsString;
    }

    public void setXmlContentAsString(String xmlContentAsString) {
        this.xmlContentAsString = xmlContentAsString;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(String soapResponse) {
        this.soapResponse = soapResponse;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMSG() {
        return responseMSG;
    }

    public void setResponseMSG(String responseMSG) {
        this.responseMSG = responseMSG;
    }

    public String getResponseAsString() {
        return responseAsString;
    }

    public void setResponseAsString(String responseAsString) {
        this.responseAsString = responseAsString;
    }

    public String getTagFromResponse(String tag) {
        return xmlPath.getString(tag);
    }

    public List<String> getListFromResponse(String tag){
        return xmlPath.getList(tag);
    }

    public void setXmlPath() {
        this.xmlPath = new XmlPath(getResponseAsString());
    }
}