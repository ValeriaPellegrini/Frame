package com.telecom.steps;

import com.telecom.utils.AutoTool;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;


public class VDSSteps {

    @Given("Me encuentro en la pagina de login VDS con el navegador {string}")
    public void meEncuentroEnLaPaginaDeLoginOrangeConElNavegador(String navegador) {
        AutoTool.setupDriver(navegador);
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlVDS"));
        AutoTool.webHandler().waitWebPageLoad();
    }


    @When("^ingreso mi nombre de usuario \"(.*)\" y mi contraseña \"(.*)\"$")
    public void ingresarCredenciales(String usuario, String contrasenia) {
        AutoTool.pageObject().loginPage().ingresarUsuario(usuario);
        AutoTool.pageObject().loginPage().ingresoContrasenia(contrasenia);
    }

    @When("ingreso credenciales validas")
    public void ingresoCredencialesValidas() {
        AutoTool.pageObject().loginPage().ingresarUsuario(AutoTool.testValues().getValue("userVDS"));
        AutoTool.pageObject().loginPage().ingresoContrasenia(AutoTool.testValues().getValue("passVDS"));
    }

    @When("ingreso credenciales con delay")
    public void ingresoCredencialesConDelay() {
        AutoTool.pageObject().loginPage().ingresarUsuarioDelay(AutoTool.testValues().getValue("userVDS"));
        AutoTool.pageObject().loginPage().ingresoContraseniaDelay(AutoTool.testValues().getValue("passVDS"));
    }

    @And("hago clic en el botón de inicio de sesión")
    public void hagoClicEnElBotónDeInicioDeSesión() {
        AutoTool.pageObject().loginPage().clickIngreso();
    }


    @Then("debería ser redirigido a la página de inicio")
    public void deberíaSerRedirigidoALaPáginaDeInicio() {

        Assertions.assertTrue(AutoTool.pageObject().homePage().existeTitulo(), "No logro acceder a la home");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }


    @Then("debería visualizarse alerta {string}")
    public void deberíaVisualizarseAlerta(String mensajeEsperado) {
        Assertions.assertEquals(mensajeEsperado,AutoTool.pageObject().loginPage().obtenerMensajeDeError(), "No se recibio el mensaje esperado");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }



}
