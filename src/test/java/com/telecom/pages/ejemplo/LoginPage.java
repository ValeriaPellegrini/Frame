package com.telecom.pages.ejemplo;

import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
public class LoginPage {
    public LoginPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }
    @FindBy(name = "username") WebElement inputUsuario;

    @FindBy(xpath = "//input[@name='password']") WebElement inputContrasenia;

    @FindBy(xpath = "//form//button[@type='submit']") WebElement btnIngreso;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']") WebElement alerta;


    public void ingresarUsuario(String usuario){
        AutoTool.webHandler().waitForElement(inputUsuario);
        AutoTool.webHandler().waitForElement(inputUsuario);
        inputUsuario.sendKeys(usuario);
    }

    public void ingresoContrasenia(String contraseia){

        AutoTool.webHandler().waitForElement(inputContrasenia);
        inputContrasenia.sendKeys(contraseia);
    }

    public void clickIngreso(){
        btnIngreso.click();
    }

    public void login(String usuario, String contrasenia){
        AutoTool.webHandler().waitForElement(inputUsuario);
        inputUsuario.sendKeys(usuario);
        inputContrasenia.sendKeys(contrasenia);
        btnIngreso.click();
    }


    public String obtenerMensajeDeError() {
        AutoTool.webHandler().waitForElement(alerta);
        return alerta.getText();
    }

    public void ingresarUsuarioDelay(String usuario) {
        AutoTool.webHandler().waitForElement(inputUsuario);
        AutoTool.webHandler().sendKeysWithDelay(usuario,inputUsuario,3);
    }


    public void ingresoContraseniaDelay(String contrasenia) {
        AutoTool.webHandler().waitForElement(inputContrasenia);
        AutoTool.webHandler().sendKeysWithDelay(contrasenia,inputContrasenia,3);
    }
}
