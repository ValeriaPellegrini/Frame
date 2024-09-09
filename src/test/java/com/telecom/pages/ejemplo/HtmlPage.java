package com.telecom.pages.ejemplo;
import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;

public class HtmlPage {
    public HtmlPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//iframe[@id='frame_examples']") WebElement iframeEjemploFinal;
    @FindBy(id = "frame_examples") WebElement iframeEjemploFinal2;

    @FindBy(xpath = "//input[@id='image_uploads']") WebElement inputImagen;

    @FindBy(xpath = "//div[@class='preview']//p") WebElement resultadoCarga;
    @FindBy(xpath = "//button[text()='Submit']") WebElement btnCargar;

    public void ingresarAlFormularioFinal(){
        AutoTool.webHandler().scrollToElement(iframeEjemploFinal);
        AutoTool.webHandler().switchToFrameByWebElement(iframeEjemploFinal);
    }

    public void setInputImagen(){
        AutoTool.fileUpload("Files/DNI-pruebas.jpg", inputImagen);
    }

    public String getResultadoCarga(){
        return resultadoCarga.getText();
    }

    public void clickBtnCargar() {
        btnCargar.click();
    }
}
