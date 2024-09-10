package com.telecom.steps;

import com.telecom.utils.AutoTool;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class EjemplosSteps {


    @Given("Abro el navegador {string}")
    public void abroElNavegador(String navegador) {
        AutoTool.setupDriver(navegador);

    }
    @Given("Me encuentro en el sitio de pruebas del formulario")
    public void meEncuentroEnElSitioDePruebasDelFormulario() {
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlNeoris")); //esto es para el ingreso al sitio.
        AutoTool.webHandler().waitWebPageLoad();
    }

    @When("completo el formulario")
    public void completoElFormulario(DataTable dataTable) {
        Map<String, String> map = dataTable.asMap(String.class, String.class);
        map.forEach((clave,valor) -> AutoTool.pageObject().formularioPage().cargarFormulario(clave,valor));
        AutoTool.addScreenshotToCucumberExecutionReport("Formulario cargado");
    }

    @And("calculo el interes")
    public void calculoElInteres() {
        AutoTool.pageObject().formularioPage().clickbtnCalcular();
    }

    @Then("el resultado es {string}")
    public void elResultadoEs(String intereses) {
        Assertions.assertEquals(intereses,AutoTool.pageObject().formularioPage().getResultado() , "Los intereses calculados son erroneos");
        AutoTool.addLogToCucumberExecutionReport("salio ok");
        AutoTool.addScreenshotToCucumberExecutionReport("Resultado Final");
    }

    @Given("Me encuentro en la pagina de html con el navegador {string}")
    public void meEncuentroEnLaPaginaDeHTMLDemoQaConElNavegador(String navegador) {
        AutoTool.setupDriver(navegador);
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlHtml"));
        AutoTool.webHandler().waitWebPageLoad();
    }

    @When("Cargo imagen en el ultimo ejemplo de formulario")
    public void cargoImagenEnElUltimoEjemploDeFormulario() {
        AutoTool.pageObject().htmlPage().ingresarAlFormularioFinal();
        AutoTool.pageObject().htmlPage().setInputImagen();
    }

    @And("Presiono cargar")
    public void presionoCargar() {
        AutoTool.pageObject().htmlPage().clickBtnCargar();
    }

    @And("Visualizo la informacion de la imagen {string}")
    public void visualizoLaInformacionDeLaImagen(String datosImagen) {
        Assertions.assertEquals(datosImagen, AutoTool.pageObject().htmlPage().getResultadoCarga(), "Los datos de la imagen son erroneos");
        AutoTool.addScreenshotToCucumberExecutionReport("Datos de la imagem");
    }

    @Then("Recibo mensaje {string}")
    public void reciboMensaje(String mensaje) {
        Assertions.assertEquals(mensaje, AutoTool.pageObject().htmlPage().getResultadoCarga(), "Los datos de la imagen son erroneos");
        AutoTool.addScreenshotToCucumberExecutionReport("La imagen se cargo con exito");
    }
}
