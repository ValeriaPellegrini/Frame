package com.telecom.pages.ejemplo;

import com.telecom.utils.AutoTool;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class FormularioPage {
    public FormularioPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//input[@id='amount']") WebElement inputCantidadDinero;
    @FindBy(xpath = "//input[@name='rate']") WebElement inputTasaInteres;
    @FindBy(xpath = "//input[@name='time']") WebElement inputTiempoInteres;

    @FindBy(xpath = "//select[@name='rate_units']") WebElement selectPeriodo;
    @FindBy(xpath = "//input[@value='year']") WebElement radioButtonAnios;
    @FindBy(xpath = "//input[@value='month']") WebElement radioButtonMeses;
    @FindBy(xpath = "//input[@type='submit']") WebElement btnCalcular;

    @FindBy(xpath = "//h2[@id='legacy-catalog-section-title-AGGC0284D1PA4ASEUQFLSEUQFLME7J']") WebElement resultado;

    public void ingresarAlSitio(){
        AutoTool.getDriver().get(AutoTool.testValues().getValue("urlAton"));
    }

    public void setInputCantidadDinero(String cantidadDinero){
        AutoTool.webHandler().waitForElement(inputCantidadDinero);
        inputCantidadDinero.sendKeys(cantidadDinero);
    }

    public void setInputTasaIntereses(String tasaIntereses){
        AutoTool.webHandler().waitForElement(inputTasaInteres);
        inputTasaInteres.sendKeys(tasaIntereses);
    }

    public void setSelectPeriodo(String valor){
        AutoTool.webHandler().selectDropdownOptions(selectPeriodo,valor);
    }

    public void setInputTiempoInteres(String tiempoInteres){
        AutoTool.webHandler().waitForElement(inputTiempoInteres);
        inputTiempoInteres.sendKeys(tiempoInteres);
    }

    public void clickRadioButtonAnios(){
        AutoTool.webHandler().waitForElement(radioButtonAnios);
        radioButtonAnios.click();
    }

    public void clickRadioButtonMeses(){
        AutoTool.webHandler().waitForElement(radioButtonMeses);
        radioButtonMeses.click();
    }


    public void clickbtnCalcular(){
        AutoTool.webHandler().waitForElement(btnCalcular);
        AutoTool.webHandler().scrollToElement(btnCalcular); //Realiza scroll al elemento
        btnCalcular.click();
        //AutoTool.webHandler().clickWebElementByJs(btnCalcular); //Realiza click mediante JS
    }
    public void cargarFormulario(String campo, String valor){
        AutoTool.webHandler().waitForElement(inputCantidadDinero);

        switch (campo.toLowerCase()){
            case "cantidad de dinero":
                inputCantidadDinero.sendKeys(valor);
                break;
            case("tasa de interes"):
                inputTasaInteres.sendKeys(valor);
                break;
            case("periodo"):
                AutoTool.webHandler().selectDropdownOptions(selectPeriodo,valor);
                break;
            case("tiempo"):
                inputTiempoInteres.sendKeys(valor);
                break;
            case("medida de tiempo"):
                if (valor.equals("Año(s)")){
                    radioButtonAnios.click();
                }else if(valor.equals("Mes(es)")){
                    radioButtonMeses.click();
                }
                break;
        }
    }

    public String getResultado(){
        AutoTool.webHandler().waitForElement(resultado);
        return resultado.getText();
    }
}
