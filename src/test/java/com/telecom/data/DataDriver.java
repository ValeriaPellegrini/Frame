package com.telecom.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.telecom.utils.AutoTool;
import com.telecom.utils.QueryHandler;
import com.telecom.utils.SoapHandler;

public class DataDriver {

    public static HashMap<String, String> getLinea(String status, String market) {
        return getLinea(status, market, "");
    }

    public static HashMap<String, String> getLinea(String status, String market, String plan) {
        return getLinea(status, market, plan, false);
    }

    public static HashMap<String, String> getLinea(String status, String market, String plan, Boolean prefa) {
        Map<String, String> datosLinea = new HashMap<>();
        String queryPlan = setPlanQuery(market, plan);
        String fechaDesde = setCurrentDateMinus(12);

        Map<String, String> map = new HashMap<String, String>();
        map.put("v_status", status);
        map.put("v_plan", queryPlan);
        map.put("v_date", fechaDesde);

        QueryHandler.loadSqlFile("QueryLineas.sql");
        QueryHandler.changeQueryValues(map);
        String query = QueryHandler.getSql();

        List<String> randomLineas = new ArrayList<>(SalesforceSoapQueries.query_lines_salesforce(query));
        Collections.shuffle(randomLineas);

        for (String linea: randomLineas) {
            if(HWSoapValidator.validarLinea(linea)){
                try{
                    datosLinea = setDatosLinea(linea, prefa);
                    boolean validPrefa = mdwValidatePrefa(datosLinea.get("PROVINCIA"),
                            datosLinea.get("CIUDAD"),
                            datosLinea.get("LOCALIDAD"),
                            datosLinea.get("CALLE"),
                            datosLinea.get("ALTURA"));
                    if((prefa&&validPrefa)||(!prefa&&!validPrefa))
                        break;
                } catch (Exception ignored) {

                }
            }
        }
        AutoTool.addLogToCucumberExecutionReport("Linea 1: "+ datosLinea);
        return (HashMap<String, String>) datosLinea;
    }

    public static void updateLineAfterUse(String linea){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Change_Account_Name.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_idtest", time+String.valueOf((1000000 + Math.random() * 9000000)));
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", linea);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
    }

    private static Map<String, String> setDatosLinea(String linea, Boolean prefa){
        Map<String, String> datosLinea = new HashMap<String, String>();
        datosLinea.put("LINEA", linea);
        datosLinea.put("PREFA", String.valueOf(prefa));
        datosLinea = hwQueryLiteBySubscriber(datosLinea);
        datosLinea = hwQueryCustomerInfoByLinea(datosLinea);
        return setAddressInstalationInfo(datosLinea);
    }

    private static Map<String,String> hwQueryLiteBySubscriber(Map<String,String> datosLinea){
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Lite_By_Subscriber.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", datosLinea.get("LINEA"));
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        datosLinea = setStatus(SoapHandler.getTagFromResponse("Envelope.Body.QueryLiteBySubscriberResultMsg.QueryLiteBySubscriberResult.CurrentStatusIndex"),datosLinea);
        datosLinea.put("TIPO_CLIENTE", "Existente");
        if(datosLinea.get("ESTADO").equals("Preactivo")){
            datosLinea.put("TIPO_CLIENTE", "Cliente Nuevo");
        }
        datosLinea.put("PLAN", SoapHandler.getTagFromResponse("Envelope.Body.QueryLiteBySubscriberResultMsg.QueryLiteBySubscriberResult.PrimaryOffering.OfferingName"));
        return setMarket(datosLinea);
    }

    private static Map<String, String> hwQueryCustomerInfoByLinea(Map<String, String> datosLinea){
        String customerResult = "Envelope.Body.QueryCustomerInfoResultMsg.QueryCustomerInfoResult.Customer.";
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointHW"));
        SoapHandler.loadXml("HW_Query_Customer_Info_By_Linea.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userHW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordHW"));
        map.put("v_msisdn", datosLinea.get("LINEA"));
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        Assertions.assertEquals(200, SoapHandler.getResponseCode());
        datosLinea = setDocType(SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.IDType"), datosLinea);
        datosLinea.put("NUM_DOC",SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.IDNumber"));
        datosLinea.put("NOMBRE", SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.FirstName"));
        datosLinea.put("APELLIDO", SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.LastName"));
        datosLinea = setBirthdayDate(SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.Birthday"), datosLinea);
        datosLinea = setGender(SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.Gender"), datosLinea);
        datosLinea.put("EMAIL", SoapHandler.getTagFromResponse(customerResult+"IndividualInfo.Email"));
        datosLinea.put("NUMERO_DE_CUENTA", SoapHandler.getTagFromResponse(customerResult+"CustKey")+"10001");
        return datosLinea;
    }

    private static Map<String, String> setAddressInstalationInfo(Map<String, String> datosLinea){
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_linea", datosLinea.get("LINEA"));
        QueryHandler.loadSqlFile("QueryOrderScopeAccount.sql");
        QueryHandler.changeQueryValues(map);
        String query = QueryHandler.getSql();
        String installationAddressId = SalesforceSoapQueries.query_order_scope_account_salesforce(query);
        Map<String, String> mapInstallation = new HashMap<String, String>();
        mapInstallation.put("v_address_id", installationAddressId);
        QueryHandler.loadSqlFile("QueryInstallationAddress.sql");
        QueryHandler.changeQueryValues(mapInstallation);
        datosLinea.putAll(SalesforceSoapQueries.query_installation_address_salesforce(QueryHandler.getSql()));
        return datosLinea;

    }

    private static boolean mdwValidatePrefa(String provincia, String ciudad, String localidad, String calle, String altura){
        SoapHandler.setSoapWebServiceAdress(AutoTool.testValues().getValue("endpointMDW")+"/verificarPrefactibilidad");
        SoapHandler.loadXml("MDW_Validate_Prefa.xml");
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_username", AutoTool.testValues().getValue("userMDW"));
        map.put("v_password", AutoTool.testValues().getValue("passwordMDW"));
        map.put("v_provincia", provincia);
        map.put("v_ciudad", ciudad);
        map.put("v_localidad", localidad);
        map.put("v_calle", calle);
        map.put("v_altura", altura);
        SoapHandler.changeXmlValues(map);
        SoapHandler.doPostRequest();
        return SoapHandler.getResponseCode()==200;
    }

    private static Map<String,String> setStatus(String status, Map<String,String> datosLinea){
        switch (status) {
            case "1": datosLinea.put("ESTADO", "Preactivo"); break;
            case "2": ;
            case "3": datosLinea.put("ESTADO", "Activo"); break;
            case "4": datosLinea.put("ESTADO", "Suspendido"); break;
            default: datosLinea.put("ESTADO", "");
        }
        return datosLinea;

    }

    private static String setPlanQuery(String market, String plan){
        String queryPlan="";
        if(plan.equals("")){
            switch (market.toLowerCase()){
                case("pre"):
                    queryPlan += "\n AND Name like 'Plan con %' ";
                    break;
                case("mix"):
                    queryPlan += "\n AND (Name like 'Plan 1GB%' OR";
                    queryPlan += "\n      Name like 'Plan 3GB%' OR";
                    queryPlan += "\n      Name like 'Plan 5GB%' OR";
                    queryPlan += "\n      Name like 'Plan 8GB%')";
                    break;
                case("pos"):
                    queryPlan += "\n AND (Name like 'Plan 12%' OR";
                    queryPlan += "\n      Name like 'Plan 15%' OR";
                    queryPlan += "\n      Name like 'Plan 20%' OR";
                    queryPlan += "\n      Name like 'Plan 25%')";
                    break;
            }
        }else {
            queryPlan += "\n AND Name like '"+plan+"%' ";
        }

        return queryPlan;
    }

    private static String setCurrentDateMinus(long months){
        String sql = "";
        LocalDate fecha = LocalDate.now().minusMonths(months);
        sql += "\n"+  " AND CreatedDate > " + fecha + "T00:00:00.000Z";
        return sql;
    }

    private static Map<String, String> setMarket(Map<String,String> datosLinea){
        if (datosLinea.get("PLAN").equalsIgnoreCase("Plan con tarjeta")
            || datosLinea.get("PLAN").equalsIgnoreCase("Plan con tarjeta repro")
            || datosLinea.get("PLAN").equalsIgnoreCase("Plan Tarjeta Personal 2")) {

            datosLinea.put("MERCADO", "PRE");
        }
        if (datosLinea.get("PLAN").contains("Black")
            || datosLinea.get("PLAN").contains("Standard")
            || datosLinea.get("PLAN").contains("Rural")
            || datosLinea.get("PLAN").contains("Internet")) {

            datosLinea.put("MERCADO", "POS");
        }
        if (datosLinea.get("PLAN").contains("Blue")
            || datosLinea.get("PLAN").contains("Start")
            || datosLinea.get("PLAN").contains("Plus")
            || datosLinea.get("PLAN").contains("Lite")
            || datosLinea.get("PLAN").contains("Classic")
            || datosLinea.get("PLAN").contains("IOT")
            || datosLinea.get("PLAN").contains("Radio")
            || datosLinea.get("PLAN").contains("Reducido por mora")
            || datosLinea.get("PLAN").contains("Hipoac")) {

            datosLinea.put("MERCADO", "MIX");
        }
        return datosLinea;
    }

    private static Map<String,String> setDocType(String docType, Map<String,String> datosLinea){
        switch (docType) {
            case "96": datosLinea.put("TIPO_DOC", "DNI"); break;
            case "94": datosLinea.put("TIPO_DOC", "PASAPORTE"); break;
            default: datosLinea.put("TIPO_DOC", "");
        }
        return datosLinea;
    }

    private static Map<String,String> setBirthdayDate(String date, Map<String,String> datosLinea){
        datosLinea.put("FECHA_NAC",
                date.substring(6, 8) + "/"
                + date.substring(4, 6) + "/"
                + Integer.toString((Integer.parseInt(date.substring(0, 4)) + 1))
        );
        return datosLinea;
    }

    private static Map<String,String> setGender(String gender, Map<String,String> datosLinea){
        switch (gender) {
            case "1": datosLinea.put("GENERO", "Masculino"); break;
            case "2": datosLinea.put("GENERO", "Femenino"); break;
            default: datosLinea.put("GENERO", "");
        }
        return datosLinea;
    }

    public static Map<String, String> getLineasAcuerdoDePago(String plan) {
        Map<String,String> datosLinea = new HashMap<>();
        String tipoPlan = "";
        String marketPlan = "";
        String datePlan = setCurrentDateMinus(14);

        switch(plan.toLowerCase()){
            case "pospago":
                tipoPlan = "%Pospago";
                marketPlan = setPlanQuery("pos", "");
                break;
            case "mix":
                tipoPlan = "%Hibrido";
                marketPlan = setPlanQuery("mix", "");
                break;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("v_plan", tipoPlan);
        map.put("v_market", marketPlan);
        map.put("v_date", datePlan);

        QueryHandler.loadSqlFile("QueryAcuerdoDePago.sql");
        QueryHandler.changeQueryValues(map);
        String query = QueryHandler.getSql();

        List<String> randomLineas = new ArrayList<>(SalesforceSoapQueries.query_lines_salesforce(query));
        Collections.shuffle(randomLineas);
        Collections.shuffle(randomLineas);

        for (String linea: randomLineas) {
            String currentStatus = HWSoapServices.getStatus(linea);
            
            if(currentStatus.equals("2")){                
                String statusDetail = HWSoapServices.getStatusDetail(linea);

                if(statusDetail.equals("000000000000000000010000")){
                    String accountNumber = HWSoapServices.getAccountNumber(linea);

                    if(!accountNumber.contains("_") && !accountNumber.contains("-") && !accountNumber.isEmpty() && !accountNumber.equals("")) { 
                        datosLinea.put("LINEA", linea);
                        datosLinea.put("NUMERO_DE_CUENTA", accountNumber);

                        break;
                    }
                }
            }
        }

        return datosLinea;
    }

    public static boolean existsContactInSalesforce(String dni) {
        boolean exists = false;
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_dni", String.valueOf(dni));
        QueryHandler.loadSqlFile("QueryContact.sql");
        QueryHandler.changeQueryValues(map);
        String query = QueryHandler.getSql();

        List<String> documentNumber = SalesforceSoapQueries.query_contact_salesforce(query);
        if(!documentNumber.isEmpty())
            exists = true;

        return exists;
    }
}
