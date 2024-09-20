package com.telecom.pages.ejemplo;
import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;

public class CanalesPage {

    public CanalesPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//button[@class='item-profile__button btn btn-primary marg-top-0']/span[ contains(text(), ' Pedir ahora ')]") WebElement btnPedirAhora;
    @FindBy(xpath = "//span[@id='pageTitle_b4eaedab-4f95-f274-a0bf-98f807b692b1']") WebElement tituloCanalesForm;
    @FindBy(xpath = "//button[@id='__9d1ab12d-4942-35a0-7c56-ddd334f28bcd']") WebElement btnSeleccionar;
    @FindBy(xpath = "//button[@id='__9d1ab12d-4942-35a0-7c56-ddd334f28bcd_popup-0']") WebElement btnSalesforce;
    @FindBy(xpath = "//button[@id='__9d1ab12d-4942-35a0-7c56-ddd334f28bcd_popup-1']") WebElement btnOpen;
    @FindBy(xpath = "//button[@id='__9d1ab12d-4942-35a0-7c56-ddd334f28bcd_popup-2']") WebElement btnSiebel;
    @FindBy(xpath = "//button[@id='__9d1ab12d-4942-35a0-7c56-ddd334f28bcd_popup-none']") WebElement btnNinguno;
    @FindBy(xpath = "//button[text()=' Siguiente ']") WebElement btnSiguiente;
    @FindBy(xpath = "//span[@id='pageTitle_5cd20818-aef0-c983-28dd-e5ad9e918194']") WebElement tituloFormSalesforce;
    @FindBy(xpath = "//span[@id='pageTitle_a9fc386a-5243-43bb-9b5b-42de9bf5ca62']") WebElement tituloFormOpen;
    @FindBy(xpath = "//span[@id='pageTitle_202d5dc2-f00d-234a-7c28-aac3989a1fad']") WebElement tituloFormSiebel;
    @FindBy(xpath = "//div[@role='alert']") WebElement alerta;


    public void clickBtnPedirAhora() {
        btnPedirAhora.click();
    }

    public boolean existeTitulo(){
        return tituloCanalesForm.isDisplayed();
    }

    public void clickBtnSeleccionar() {
        btnSeleccionar.click();
    }

    public void clickEnOpcion(String seleccion) {
        if ("Salesforce".equals(seleccion)){
            btnSalesforce.click();
        } else if ("Open".equals(seleccion)){
            btnOpen.click();
        } else if ("Siebel".equals(seleccion)){
            btnSiebel.click();
        } else if ("Ninguno".equals(seleccion)){
            btnNinguno.click();
        }
    }

    public void clickBtnSiguiente() {
        btnSiguiente.click();
    }

    public boolean estoyEnFormularioSeleccionado(String seleccion) {
        if ("Salesforce".equals(seleccion)){
            return tituloFormSalesforce.isDisplayed();
        } else if ("Open".equals(seleccion)){
            return tituloFormOpen.isDisplayed();
        } else if ("Siebel".equals(seleccion)){
            return tituloFormSiebel.isDisplayed();
        } else{
            return false;
        }
    }

    public String obtenerMensajeDeError() {
        return alerta.getText();
    }


    }


    

