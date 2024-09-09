package com.telecom.steps;
import com.telecom.pages.ejemplo.HtmlPage;
import com.telecom.utils.AutoTool;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.sql.Time;
import java.util.Map;

public class PracticaSteps {

    @Given("Me encuentro en la pagina de login DemoQa con el navegador {string}")
    public void meEncuentroEnLaPaginaDeLoginDemoQaConElNavegador(String navegador) {
        AutoTool.setupDriver(navegador);
        AutoTool.getDriver().get(AutoTool.testValues().getValue("ulrDemoQa"));
        AutoTool.webHandler().waitWebPageLoad();
    }


}
