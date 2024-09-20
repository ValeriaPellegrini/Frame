package com.telecom.steps;

import com.telecom.utils.AutoTool;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class AtonSteps {


    @Given("Me encuentro en el sitio de iTickets con el navegador {string}")
    public void abroElNavegadorEnItickets(String navegador) {
        AutoTool.setupDriver(navegador); //esto es para que abra el navegador seleccionado
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlAton")); //esto es para el ingreso al sitio.
        AutoTool.webHandler().waitWebPageLoad();

    }  

    @When("hago click en Apps")
    public void presionoApps() {
        AutoTool.pageObject().IticketsPage().clickBtnApps();
    }

    @And("selecciono la opción Negocio - G. Técnica")
    public void seleccionoGTecnica() {
        AutoTool.pageObject().IticketsPage().clickOpcGTecnica();
    }

    @Then("ingreso a Apps Negocio Gestión Técnica")
    public void deberíaSerRedirigidoALaPáginaDeAppsNegocioGestionTecninca() {
        Assertions.assertTrue(AutoTool.pageObject().AppsGTecnicaPage().existeTitulo(), "No se visualiza el servicio");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }

    @Given("Me encuentro en la sección de Apps Negocio Gestión Técnica")
    public void abroServicioAppsGestionTecnica() {
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlAtonGestionTecnica")); //esto es para el ingreso al sitio.
        AutoTool.webHandler().waitWebPageLoad();
    }  

    @When("hago click en Canales Digitales")
    public void presionoCanalesDigitales() {
        AutoTool.pageObject().AppsGTecnicaPage().clickCanalesDigitales();
        AutoTool.webHandler().waitWebPageLoad();
    }

    @And("hago click en el botón Pedir Ahora")
    public void clickBtnPedirAhora() {
        AutoTool.pageObject().CanalesPage().clickBtnPedirAhora();
        AutoTool.webHandler().waitWebPageLoad();
    }

    @Then("ingreso al formulario de Canales Digitales")
    public void deberíaSerRedirigidoALFormularioCanalesDigitales() {
        Assertions.assertTrue(AutoTool.pageObject().CanalesPage().existeTitulo(), "No se visualiza el servicio");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }
    
    @Given("Me encuentro en la sección de Peticiones de Canales Digitales")
    public void abroFormularioCanalesDigitales() {
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlAtonFormCanalesDigitales")); //esto es para el ingreso al sitio.
        AutoTool.webHandler().waitWebPageLoad();
        clickBtnPedirAhora();
    } 

    @When("hago click en desplegable Seleccionar")
    public void presionoSeleccionar() {
        AutoTool.pageObject().CanalesPage().clickBtnSeleccionar();
    }

    @And("selecciono {string}")
    public void clickEnOpcionCanales(String seleccion) {
        AutoTool.pageObject().CanalesPage().clickEnOpcion(seleccion);
        AutoTool.webHandler().waitWebPageLoad();
    }

    @And("presiono Siguiente")
    public void clickEnSiguiente() {
        AutoTool.pageObject().CanalesPage().clickBtnSiguiente();
    }

    @Then("ingreso al formulario de peticiones {string} de Canales Digitales")
    public void ingesoOpcionFormularioCanalesDigitales(String seleccion) {
        Assertions.assertTrue(AutoTool.pageObject().CanalesPage().estoyEnFormularioSeleccionado(seleccion), "No se visualiza el servicio");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }

    @Then("visualizo mensaje de {string}")
    public void deberíaVisualizarseAlerta(String mensajeEsperado) {
        Assertions.assertEquals(mensajeEsperado,AutoTool.pageObject().CanalesPage().obtenerMensajeDeError(), "No se recibio el mensaje esperado");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }

    @Given("Me encuentro en la sección de Peticiones {string} de Canales Digitales")
    public void abroFormularioSeleccionadoDeCanalesDigitales(String servicio) {
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlAtonFormCanalesDigitales")); //esto es para el ingreso al sitio.
        AutoTool.webHandler().waitWebPageLoad();
        clickBtnPedirAhora();
        presionoSeleccionar();
        clickEnOpcionCanales(servicio);
        clickEnSiguiente();        
    }

    @When("se despliegan opciones del botón Seleccionar")
    public void presionoSeleccionarSalesforce() {
        AutoTool.pageObject().FormularioSalesforcePage().clickBtnSeleccionarSalesforce();
    }

    @And("selecciono el canal {string}")
    public void clickEnCanal(String seleccion) {
        AutoTool.pageObject().FormularioSalesforcePage().clickEnOpcionCanal(seleccion);
        AutoTool.webHandler().waitWebPageLoad();
    }

    @When("completo el formulario del canal seleccionado")
    public void completoElFormulario(DataTable dataTable) {
        Map<String, String> map = dataTable.asMap(String.class, String.class);
        map.forEach((clave,valor) -> AutoTool.pageObject().FormularioSalesforcePage().cargarFormulario(clave,valor));
        AutoTool.addScreenshotToCucumberExecutionReport("Formulario cargado");
    }

    @And("selecciono Enviar petición")
    public void clickEnviarPeticion() {
        AutoTool.pageObject().FormularioSalesforcePage().clickEnEnviarPeticion();
    }

    @Then("se verifica la creación del ticket")
    public void verficarTicketCreado() {
        AutoTool.webHandler().waitWebPageLoad();
        AutoTool.pageObject().FormularioSalesforcePage().verificarTicket();
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
    }

    



}
