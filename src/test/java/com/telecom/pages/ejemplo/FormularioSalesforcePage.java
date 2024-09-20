package com.telecom.pages.ejemplo;

import com.telecom.utils.AutoTool;

import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


public class FormularioSalesforcePage {
    public FormularioSalesforcePage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//button[@id='__91679619-c088-5b00-6f4d-5a4576cf3749']") WebElement btnSeleccionarSalesforce;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Mi Personal')]") WebElement btnMiPersonal;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Mi Tienda Personal')]") WebElement btnMiTiendaPersonal;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Flow App')]") WebElement btnFlowApp;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Disney +')]") WebElement btnDisney;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Star +')]") WebElement btnStar;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'Paramount +')]") WebElement btnParamount;
    @FindBy(xpath = "//adapt-highlight[contains(text(), 'HBO Max')]") WebElement btnHboMax;

    @FindBy(xpath = "//span[contains(text(), 'Mi Personal App')]") WebElement radioButtonDondeApp;
    @FindBy(xpath = "//span[contains(text(), 'Mi Personal Web')]") WebElement radioButtonDondeWeb;
    @FindBy(xpath = "//span[contains(text(), 'De Acceso')]") WebElement radioButtonTipoAcceso;
    @FindBy(xpath = "//span[contains(text(), 'De Funcionamiento')]") WebElement radioButtonTipoFuncionamiento;
    @FindBy(xpath = "//input[@id='__2de54b63-c68e-afc7-9607-cf15ba7f12eb']") WebElement inputDni;
    @FindBy(xpath = "//input[@id='__cbcb3078-6713-96f7-9c99-0267cb00a17a']") WebElement inputCuenta;
    @FindBy(xpath = "//input[@id='__e13597bd-4be2-45a8-586c-f11137721072']") WebElement inputCorreo;
    @FindBy(xpath = "//input[@id='__cd0a8c3c-3a45-9045-b8fe-2a75536fcfae']") WebElement inputCodigo;
    @FindBy(xpath = "//textarea[@id='__fcdf57b8-b8f6-d51f-7106-073ca78a02a8']") WebElement inputComentario;
    @FindBy(xpath = "//input[@type='file']") WebElement inputImagen;
    @FindBy(xpath = "//span[contains(text(), 'Cargado')]") WebElement mensajeCarga;
    @FindBy(xpath = "//button[@class='btn-block btn btn-primary']") WebElement btnEnviarPeticion;
    @FindBy(xpath = "//a[@class='event-card__media--centered flex-grow-1 ng-star-inserted']//h2[contains(text(), ' Canales Digitales ')]") WebElement btnDetalles;
    @FindBy(xpath = "//span[contains(text(), 'Comentarios')]") WebElement comentariosPeticion;


    public void clickBtnSeleccionarSalesforce() {
        btnSeleccionarSalesforce.click();
    }

    public void clickEnOpcionCanal(String seleccion) {
        AutoTool.webHandler().waitForElement(btnMiPersonal);
        if ("Mi Personal".equals(seleccion)){
            btnMiPersonal.click();
        } else if ("Mi Tienda Personal".equals(seleccion)){
            btnMiTiendaPersonal.click();
        } else if ("Flow App".equals(seleccion)){
            btnFlowApp.click();
        } else if ("Disney +".equals(seleccion)){
            btnDisney.click();
        } else if ("Star +".equals(seleccion)){
            btnParamount.click();
        } else if ("Paramount +".equals(seleccion)){
            btnParamount.click();
        } else if ("HBO Max".equals(seleccion)){
            btnHboMax.click();
        }
    }

    public void clickRadioButtonDonde(String valor){
        AutoTool.webHandler().waitForElement(radioButtonDondeApp);
        if ("Mi Personal App".equals(valor)){
            radioButtonDondeApp.click();
        } else if ("Mi Personal Web".equals(valor)){
            radioButtonDondeWeb.click();
        }
        
    }

    public void clickRadioButtonTipo(String valor){
        AutoTool.webHandler().waitForElement(radioButtonTipoAcceso);
        if ("De Acceso".equals(valor)){
            radioButtonTipoAcceso.click();
        } else if ("De Funcionamiento".equals(valor)) {
            radioButtonTipoFuncionamiento.click();
        }
        
    }

    public void cargarFormulario(String clave, String valor){       
        switch (clave){
            case("¿Dónde se presenta el error?"):
                clickRadioButtonDonde(valor);
                break;
            case("¿Qué tipo de error necesitás reportar?"):
                clickRadioButtonTipo(valor);
                break;
            case ("Indicanos el Número de Identificación(DNI/CUIL/CUIT)"):
                inputDni.sendKeys(valor);
                break;
            case("Indicanos el número de contrato / Cuenta"):
                inputCuenta.sendKeys(valor);
                break;
            case("Indicanos el Correo Electrónico del Cliente"):
                inputCorreo.sendKeys(valor);
                break;
            case("Detallá el código del error"):
                inputCodigo.sendKeys(valor);
                break;
            case("Contanos algo más que nos pueda servir con tu pedido"):
                inputComentario.sendKeys(valor);
                break;
            case("Acordate de adjuntar la imagen del Error"):
                setInputImagen("Cargado");
                break;
        }
    }

    public String obtenerMensajeDeCarga() {
        return mensajeCarga.getText();
    }

    public void setInputImagen(String mensajeEsperado){
        AutoTool.fileUpload("Files/DNI-pruebas.jpg", inputImagen);
        AutoTool.webHandler().waitForElement(mensajeCarga);
        Assertions.assertEquals(mensajeEsperado,obtenerMensajeDeCarga(), "No se recibio el mensaje esperado");
    }

    public void clickEnEnviarPeticion() {
        AutoTool.webHandler().waitAndClickWebElement(btnEnviarPeticion);        
    }

    public void verificarTicket() {
        AutoTool.addScreenshotToCucumberExecutionReport("Mi Actividad");
        btnDetalles.click();
        AutoTool.webHandler().waitWebPageLoad();
        AutoTool.addScreenshotToCucumberExecutionReport("Detalle Ticket Parte 1");
        AutoTool.webHandler().scrollToElement(comentariosPeticion);
        AutoTool.addScreenshotToCucumberExecutionReport("Detalle Ticket Parte 2");
        
    }

   


}
