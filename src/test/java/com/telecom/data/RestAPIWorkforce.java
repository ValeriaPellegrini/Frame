package com.telecom.data;

import com.telecom.utils.AutoTool;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class RestAPIWorkforce
{
    public static String postAssignTechnician ()
    {
        RestAssured.baseURI=AutoTool.testValues().getValue("url_service_asignar_OT");
        String requestBody = Constants.getBodyAssignTech(AutoTool.testValues().getValue("v_tecnico_workforce"));
        AutoTool.addLogToCucumberExecutionReport(requestBody);
        return RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .header("authorization", AutoTool.testValues().getValue("BearerAsignarOT"))
                .header("cache-control", "no-cache")
                .header("omssrc", "FAN")
                .header("Source-ID", "FAN")
                .header("wexis_mso", "cablevision")
                .header("Cookie", "BIGipServerwfx-unified-teco-pre.app~wfx-unified-teco-pre-api-base-all-pool=!WCVtIt3M2Al++gXSessgZzLmZ+pCms3sM5i9b1JoKzKEc7MbB5uOzYANYIUxhHApGybHO6SFWEkiIQ==")
                .body(requestBody)
                .when()
                .post("/"+AutoTool.testValues().getValue("OT"))
                .then().assertThat().log().all().statusCode(200).body("status", equalTo("OK"))
                .extract().response().body().prettyPrint();

    }

    public static void getActivitiesOT ()
    {
        RestAssured.baseURI=AutoTool.testValues().getValue("url_service_consultar_activities");
        String response = RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .header("authorization", AutoTool.testValues().getValue("BearerAsignarOT"))
                .header("cache-control", "no-cache")
                .header("omssrc", "FAN")
                .header("Source-ID", "FAN")
                .header("wexis_mso", "cablevision")
                .header("Cookie", "BIGipServerwfx-unified-teco-pre.app~wfx-unified-teco-pre-api-base-all-pool=!WCVtIt3M2Al++gXSessgZzLmZ+pCms3sM5i9b1JoKzKEc7MbB5uOzYANYIUxhHApGybHO6SFWEkiIQ==")
                .when()
                .get("/"+AutoTool.testValues().getValue("OT"))
                .then().assertThat().log().all().statusCode(200).body("id", equalTo(AutoTool.testValues().getValue("OT")))
                .extract().response().body().prettyPrint();
        JsonPath jsonPathEvaluator = JsonPath.from(response.toString());
        int i=0;
        List<Activity> listaActividades = new ArrayList<>();
        AutoTool.addLogToCucumberExecutionReport("Actividades de la OT");
        while (jsonPathEvaluator.get("workOrderItem.id["+i+"]") != null)
        {  listaActividades.add(new Activity(jsonPathEvaluator.get("workOrderItem.id["+i+"]").toString(), jsonPathEvaluator.get("workOrderItem.sequence["+i+"]").toString(),jsonPathEvaluator.get("workOrderItem.description["+i+"]").toString()));
            AutoTool.addLogToCucumberExecutionReport(listaActividades.get(i).getActivityData());
            i++;
        }
        AutoTool.testValues().setValue("listaActividades", listaActividades);
    }

    public static void setCompleteActivities ()
    {
        List <Activity> lista = (List<Activity>) AutoTool.testValues().getTestValueAsObject("listaActividades");
        RestAssured.baseURI=AutoTool.testValues().getValue("url_complete_activities_OT");
        String requestBody=Constants.getBodyCompleteActivities(lista);
        AutoTool.addLogToCucumberExecutionReport(requestBody);
        String response = RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .header("accept", "application/json")
                .header("authorization", AutoTool.testValues().getValue("BearerAsignarOT"))
                .header("cache-control", "no-cache")
                .header("omssrc", "FAN")
                .header("Source-ID", "FAN")
                .header("wexis_mso", "msokairos")
                .header("Cookie", "BIGipServerwfx-unified-teco-pre.app~wfx-unified-teco-pre-api-base-all-pool=!WCVtIt3M2Al++gXSessgZzLmZ+pCms3sM5i9b1JoKzKEc7MbB5uOzYANYIUxhHApGybHO6SFWEkiIQ==")
                .body(requestBody)
                .when()
                .post("/"+AutoTool.testValues().getValue("OT"))
                .then().assertThat().log().all().statusCode(200).body("status", equalTo("OK"))
                .extract().response().body().prettyPrint();
        AutoTool.addLogToCucumberExecutionReport(response);
    }

    public static void postCloseOT ()
    {   RestAssured.baseURI=AutoTool.testValues().getValue("url_close_OT");
        String requestBody = Constants.getBodyCloseOT();
        AutoTool.addLogToCucumberExecutionReport(requestBody);
        String response = RestAssured.given().log().all()
                    .header("Content-type", "application/json")
                    .header("accept", "application/json")
                    .header("authorization", AutoTool.testValues().getValue("BearerAsignarOT"))
                    .header("cache-control", "no-cache")
                    .header("omssrc", "FAN")
                    .header("Source-ID", "FAN")
                    .header("wexis_mso", "msokairos")
                    .header("Cookie", "BIGipServerwfx-unified-teco-pre.app~wfx-unified-teco-pre-api-base-all-pool=!WCVtIt3M2Al++gXSessgZzLmZ+pCms3sM5i9b1JoKzKEc7MbB5uOzYANYIUxhHApGybHO6SFWEkiIQ==")
                    .body(requestBody)
                    .when()
                    .post("/"+AutoTool.testValues().getValue("OT"))
                    .then().assertThat().log().all().statusCode(200).body("status", equalTo("OK"))
                    .extract().response().body().prettyPrint();
        AutoTool.addLogToCucumberExecutionReport(response);
     }

    public static void getStatusOT ()
    {   RestAssured.baseURI=AutoTool.testValues().getValue("url_service_consultar_cita");
        String response = RestAssured.given().log().all()
                .header("Content-type", "application/json")
                .header("authorization", AutoTool.testValues().getValue("BearerAsignarOT"))
                .header("cache-control", "no-cache")
                .header("omssrc", "FAN")
                .header("Source-ID", "FAN")
                .header("wexis_mso", "cablevision")
                .header("Cookie", "BIGipServerwfx-unified-teco-pre.app~wfx-unified-teco-pre-api-base-all-pool=!WCVtIt3M2Al++gXSessgZzLmZ+pCms3sM5i9b1JoKzKEc7MbB5uOzYANYIUxhHApGybHO6SFWEkiIQ==")
                .when()
                .get("/"+AutoTool.testValues().getValue("OT"))
                .then().assertThat().log().all().statusCode(200).body("id", equalTo(AutoTool.testValues().getValue("OT")))
                .assertThat().log().all().body("status",equalTo("Cumplida"))
                .extract().response().body().prettyPrint();
            AutoTool.addLogToCucumberExecutionReport(response);
    }
}
