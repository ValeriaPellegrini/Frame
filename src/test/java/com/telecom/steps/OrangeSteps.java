package com.telecom.steps;

import com.telecom.utils.AutoTool;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;


public class OrangeSteps {

    @Given("Me encuentro en la pagina de login Orange con el navegador {string}")
    public void meEncuentroEnLaPaginaDeLoginOrangeConElNavegador(String navegador) {
        AutoTool.setupDriver(navegador);
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlOrange"));
        AutoTool.webHandler().waitWebPageLoad();
    }


    @When("^ingreso mi nombre de usuario \"(.*)\" y mi contraseña \"(.*)\"$")
    public void ingresarCredenciales(String usuario, String contrasenia) {
        AutoTool.pageObject().loginPage().ingresarUsuario(usuario);
        AutoTool.pageObject().loginPage().ingresoContrasenia(contrasenia);
    }

    @When("ingreso credenciales validas")
    public void ingresoCredencialesValidas() {
        AutoTool.pageObject().loginPage().ingresarUsuario(AutoTool.testValues().getValue("userOrange"));
        AutoTool.pageObject().loginPage().ingresoContrasenia(AutoTool.testValues().getValue("passOrange"));
    }

    @When("ingreso credenciales con delay")
    public void ingresoCredencialesConDelay() {
        AutoTool.pageObject().loginPage().ingresarUsuarioDelay(AutoTool.testValues().getValue("userOrange"));
        AutoTool.pageObject().loginPage().ingresoContraseniaDelay(AutoTool.testValues().getValue("passOrange"));
    }

    @And("hago clic en el botón de inicio de sesión")
    public void hagoClicEnElBotónDeInicioDeSesión() {
        AutoTool.pageObject().loginPage().clickIngreso();
    }


    @Then("debería ser redirigido a la página del tablero de mi cuenta")
    public void deberíaSerRedirigidoALaPáginaDelTableroDeMiCuenta() {

        Assertions.assertTrue(AutoTool.pageObject().homePage().existeTitulo(), "No logro acceder a la home");
        AutoTool.addLogToCucumberExecutionReport("Se visualiza el elemento");
        AutoTool.addScreenshotToCucumberExecutionReport("Imagen resultante");
    }


    @Then("debería visualizarse alerta {string}")
    public void deberíaVisualizarseAlerta(String mensajeEsperado) {
        Assertions.assertEquals(mensajeEsperado,AutoTool.pageObject().loginPage().obtenerMensajeDeError(), "No se recibio el mensaje esperado");
    }



}
