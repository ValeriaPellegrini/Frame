package com.telecom.data;

import com.telecom.utils.AutoTool;
import java.util.*;

public final class Constants {

    /*
     * Generate an email with name, surname and a random domain
     */
    public static String generateEmail() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder name = new StringBuilder();
        StringBuilder surname = new StringBuilder();
        Random rnd = new Random();
        String[] dominio = {"@gmail.com", "@yopmail.com", "@teco.com.ar", "@yahoo.com.ar", "@hotmail.com"};
        Random rnd2 = new Random();
        while (name.length() < 10) { // length of the random string.
            int index_name = (int) (rnd.nextFloat() * SALTCHARS.length());
            int index_surname = (int) (rnd.nextFloat() * SALTCHARS.length());
            name.append(SALTCHARS.charAt(index_name));
            surname.append(SALTCHARS.charAt(index_surname));
        }
        String saltStr = name + "_" + surname;
        return saltStr + dominio[rnd2.nextInt(dominio.length)];

    }

    /*
     * receive a parameter for the environment and return a url_wsdl required
     */
    public static String getWSDLSOM(String ambiente) {
        switch (ambiente) {
            case "testing2":
                return AutoTool.testValues().getValue("wsdlSOM_testing2");
            case "UAT1":
                return AutoTool.testValues().getValue("wsdlSOM_UAT1");
            case "UAT2":
                return AutoTool.testValues().getValue("wsdlSOM_UAT2");
            default:
                return AutoTool.testValues().getValue("wsdlSOM_testing");

        }
    }

    public static String getWebServiceWSDLSOM(String ambiente) {
        switch (ambiente) {
            case "testing2":
                return AutoTool.testValues().getValue("serviceWSDLSOM_URL_testing2");
            case "UAT1":
                return AutoTool.testValues().getValue("serviceWSDLSOM_URL_UAT1");
            case "UAT2":
                return AutoTool.testValues().getValue("serviceWSDLSOM_URL_UAT2");
            default:
                return AutoTool.testValues().getValue("serviceWSDLSOM_URL_testing");
        }
    }

    /*
     * receive a parameter for the environment and return a url_SOM required
     */
    public static String getURLSOM(String ambiente) {
        switch (ambiente) {
            case "testing2":
                return AutoTool.testValues().getValue("url_page_SOM_testing2");
            case "UAT1":
                return AutoTool.testValues().getValue("url_page_SOM_UAT1");
            case "UAT2":
                return AutoTool.testValues().getValue("url_page_SOM_UAT2");
            default:
                return AutoTool.testValues().getValue("url_page_SOM_testing");
        }
    }

    /*
     * receive a parameter for the environment and return a URL_PA required
     */
    public static String getURLPA(String ambiente) {
        switch (ambiente) {
            case "testing2":
                return AutoTool.testValues().getValue("url_page_PA_testing2");
            case "UAT1":
                return AutoTool.testValues().getValue("url_page_PA_UAT1");
            case "UAT2":
                return AutoTool.testValues().getValue("url_page_PA_UAT2");
            default:
                return AutoTool.testValues().getValue("url_page_PA_testing");
        }
    }

    public static Map getUserSOMPASS(String ambiente) {
        Map<String, String> map = new HashMap<String, String>();
        switch (ambiente) {
            case "testing2":
                map.put("User", AutoTool.testValues().getValue("user_som_testing2"));
                map.put("Pass", AutoTool.testValues().getValue("pass_som_testing2"));
                return map;
            case "UAT1":
                map.put("User", AutoTool.testValues().getValue("user_som_UAT1"));
                map.put("Pass", AutoTool.testValues().getValue("pass_som_UAT1"));
                return map;
            case "UAT2":
                map.put("User", AutoTool.testValues().getValue("user_som_UAT2"));
                map.put("Pass", AutoTool.testValues().getValue("pass_som_UAT2"));
                return map;
            default:
                map.put("User", AutoTool.testValues().getValue("user_som_testing"));
                map.put("Pass", AutoTool.testValues().getValue("pass_som_testing"));
                return map;
        }
    }

    public static Map getUserPAPASS(String ambiente) {
        Map<String, String> map = new HashMap<String, String>();
        switch (ambiente) {
            case "testing2":
                map.put("User", AutoTool.testValues().getValue("user_pa_testing2"));
                map.put("Pass", AutoTool.testValues().getValue("pass_pa_testing2"));
                return map;
            case "UAT1":
                map.put("User", AutoTool.testValues().getValue("user_pa_UAT1"));
                map.put("Pass", AutoTool.testValues().getValue("pass_pa_UAT1"));
                return map;
            case "UAT2":
                map.put("User", AutoTool.testValues().getValue("user_pa_UAT2"));
                map.put("Pass", AutoTool.testValues().getValue("pass_pa_UAT2"));
                return map;
            default:
                map.put("User", AutoTool.testValues().getValue("user_pa_testing"));
                map.put("Pass", AutoTool.testValues().getValue("pass_pa_testing"));
                return map;
        }
    }

    public static String generateCuil(String gender) {
        String genero;
        if (gender.equalsIgnoreCase("Female")) {
            genero = "27";
        } else {
            genero = "20";
        }
        int dniRandom = (int) (10000000 + Math.random() * 90000000);
        String dniRandomSt = String.valueOf(dniRandom);
        int digitoVer = Integer.parseInt(String.valueOf(genero.charAt(0))) * 5 + Integer.parseInt(String.valueOf(genero.charAt(1))) * 4;
        int[] multipliers = {3, 2, 7, 6, 5, 4, 3, 2};
        char[] digits1 = dniRandomSt.toCharArray();

        for (int i = 0; i < multipliers.length; i++) {
            digitoVer += Integer.parseInt(String.valueOf(digits1[i])) * multipliers[i];
        }

        int lastDigit = 11 - (digitoVer % 11);

        if (digitoVer % 11 == 0) {
            lastDigit = 0;
        } else if (digitoVer % 11 == 1 && genero.equalsIgnoreCase("27")) {
            lastDigit = 4;
            genero = "23";
        } else if (digitoVer % 11 == 1 && genero.equalsIgnoreCase("20")) {
            lastDigit = 9;
            genero = "23";
        }

        return genero + dniRandomSt + lastDigit;
    }

    public static Map getUserSAMPASS() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("User", AutoTool.testValues().getValue("user_SAM"));
        map.put("Pass", AutoTool.testValues().getValue("pass_SAM"));
        return map;
    }

    public static String getURLSAM() {
        return AutoTool.testValues().getValue("url_SAM");
    }

    public static String getBodyAssignTech(String tecnico) {
        String requestBody = "{\n" +
                "   \"WFXDispatchLogin\": \"WFX\",\n" +
                "   \"Job\": {\n" +
                "                   \"BusinessUnit\": \"1\",\n" +
                "                   \"FulfilmentCenter\": \"1\", \n" +
                "                   \"TechnicianNum\": \"v_tecnico\", \n" +
                "                   \"DispatcherStatusCD\": \"AS\" } \n" +
                "}";
        return requestBody.replace("v_tecnico", tecnico);
    }

    public static String getBodyCloseOT() {
        String requestBody =    "{\n" +
                                "   \"CodeSrc\": \"WFX\",\n" +
                                "   \"WFXDispatchLogin\": \"cmedina\", \n" +
                                "   \"Job\": { \n" +
                                "               \"FulfillmentCenter\": 1, \n" +
                                "               \"DispatcherStatusCd\": \"CE\", \n" +
                                "               \"JobResolutionCdList\": [ \n" +
                                "                  {   \n" +
                                "                       \"JobResolutionCd\": \"2093\"   \n" +
                                "                   } \n" +
                                "                ] \n" +
                                "           } \n" +
                                "}";
        return requestBody;
    }

    public static String getBodyCompleteActivities(List<Activity> lista) {
        String requestBody="";
        AutoTool.addLogToCucumberExecutionReport("tamaño de la lista: "+lista.size());
        AutoTool.addLogToCucumberExecutionReport(lista.get(0).toString());
        switch (lista.size())
        {
            case 1: {
                requestBody = "{\n" +
                        "   \"CodeSrc\": \"WFX\",\n" +
                        "   \"WFXDispatchLogin\": \"cmedina\",\n" +
                        "   \"Job\": {\n" +
                        "                   \"LastChangeOper\": \"SFWorkarround\",\n" +
                        "                   \"FulfillmentCenter\": 1  \n" +
                        "            }, \n" +
                        "   \"JobActivityList\": [ \n" +
                        "    {  \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_1, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_1\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_1\", \n" +
                        "       \"ActivityId\": \"v_act_id_1\", \n" +
                        "       \"StatusCd\": \"CP\", \n" +
                        "       \"AddSerialNum\": \"v_dispositivoCM_1\" \n" +
                        "   } \n" +
                        " ] \n " +
                        "} ";
                break;
            }
            case 2: {
                requestBody = "{\n" +
                        "   \"CodeSrc\": \"WFX\",\n" +
                        "   \"WFXDispatchLogin\": \"cmedina\",\n" +
                        "   \"Job\": {\n" +
                        "                   \"LastChangeOper\": \"SFWorkarround\",\n" +
                        "                   \"FulfillmentCenter\": 1  \n" +
                        "            }, \n" +
                        "   \"JobActivityList\": [ \n" +
                        "    {  \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_1, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_1\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_1\", \n" +
                        "       \"ActivityId\": \"v_act_id_1\", \n" +
                        "       \"StatusCd\": \"CP\", \n" +
                        "       \"AddSerialNum\": \"v_dispositivoCM_1\" \n" +
                        "   }, \n" +
                        "   { \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_2, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_2\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_2\", \n" +
                        "       \"ActivityId\": \"v_act_id_2\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_2 \n" +
                        "   } \n" +
                        "   ] \n " +
                        "} ";
                break;
            }
            case 3: {
                requestBody = "{\n" +
                        "   \"CodeSrc\": \"WFX\",\n" +
                        "   \"WFXDispatchLogin\": \"cmedina\",\n" +
                        "   \"Job\": {\n" +
                        "                   \"LastChangeOper\": \"SFWorkarround\",\n" +
                        "                   \"FulfillmentCenter\": 1 \n" +
                        "            }, \n" +
                        "   \"JobActivityList\": [ \n" +
                        "    {  \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_1, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_1\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_1\", \n" +
                        "       \"ActivityId\": \"v_act_id_1\", \n" +
                        "       \"StatusCd\": \"CP\", \n" +
                        "       \"AddSerialNum\": \"v_dispositivoCM_1\" \n" +
                        "   }, \n" +
                        "   { \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_2, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_2\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_2\", \n" +
                        "       \"ActivityId\": \"v_act_id_2\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_2 \n" +
                        "   }, \n" +
                        "   {  \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_3, \n" +
                        "       \"ActivitySkill\": null, \n " +
                        "       \"ActivityCd\": \"v_act_cd_3\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_3\", \n" +
                        "       \"ActivityId\": \"v_act_id_3\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_3 \n " +
                        "   } " +
                        "   ] \n " +
                        "} ";
                break;
            }
            case 4: {
                requestBody = "{\n" +
                        "   \"CodeSrc\": \"WFX\",\n" +
                        "   \"WFXDispatchLogin\": \"cmedina\",\n" +
                        "   \"Job\": {\n" +
                        "                   \"LastChangeOper\": \"SFWorkarround\",\n" +
                        "                   \"FulfillmentCenter\": 1 \n" +
                        "            }, \n" +
                        "   \"JobActivityList\": [ \n" +
                        "    {  \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_1, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_1\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_1\", \n" +
                        "       \"ActivityId\": \"v_act_id_1\", \n" +
                        "       \"StatusCd\": \"CP\", \n" +
                        "       \"AddSerialNum\": \"v_dispositivoCM_1\" \n" +
                        "   }, \n" +
                        "   { \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_2, \n" +
                        "       \"ActivitySkill\": null, \n" +
                        "       \"ActivityCd\": \"v_act_cd_2\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_2\", \n" +
                        "       \"ActivityId\": \"v_act_id_2\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_2 \n" +
                        "   }, \n" +
                        "   {  \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_3, \n" +
                        "       \"ActivitySkill\": null, \n " +
                        "       \"ActivityCd\": \"v_act_cd_3\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_3\", \n" +
                        "       \"ActivityId\": \"v_act_id_3\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_3 \n " +
                        "   }, " +
                        "   {  \n" +
                        "       \"RemoveSerialNum\": null, \n" +
                        "       \"Outlet\": null, \n" +
                        "       \"WorkSeqNum\": v_secuencia_4, \n" +
                        "       \"ActivitySkill\": null, \n " +
                        "       \"ActivityCd\": \"v_act_cd_4\", \n" +
                        "       \"JobActivitySubList\": [], \n" +
                        "       \"ActivityName\": \"v_descripcion_4\", \n" +
                        "       \"ActivityId\": \"v_act_id_3\", \n" +
                        "       \"StatusCd\": \"CP\",   \n " +
                        "       \"AddSerialNum\": v_dispositivoCM_4 \n " +
                        "   }, " +
                        "   ] \n " +
                        "} ";
                break;
            }
        }

        for (int i=0;i<lista.size();i++) {
            requestBody=requestBody.replace("v_act_cd_" + (i+1), lista.get(i).getNro_id());
            requestBody=requestBody.replace("v_act_id_" + (i + 1), lista.get(i).getNro_id());
            requestBody=requestBody.replace("v_secuencia_" + (i + 1), lista.get(i).getSecuencia());
            requestBody=requestBody.replace("v_descripcion_" + (i + 1), lista.get(i).getSecuencia());

            if (lista.get(i).getDescripcion().contains("Cablemodem"))
                requestBody= requestBody.replace("v_dispositivoCM_" + (i + 1), AutoTool.testValues().getValue("v_cable_mode_wifi"));
            else if (lista.get(i).getDescripcion().contains("CM"))
                requestBody=requestBody.replace("v_dispositivoCM_" + (i + 1), AutoTool.testValues().getValue("v_cable_mode"));
            else if (lista.get(i).getDescripcion().contains("DECO FULL"))
                requestBody=requestBody.replace("v_dispositivoCM_" + (i + 1), AutoTool.testValues().getValue("v_deco_full"));
            else if (lista.get(i).getDescripcion().contains("DECO Flow"))
                requestBody=requestBody.replace("v_dispositivoCM_" + (i + 1), AutoTool.testValues().getValue("v_deco_hibrido"));
            else
                requestBody=requestBody.replace("v_dispositivoCM_" + (i + 1), "null");
        }
        return requestBody;
    }
}
