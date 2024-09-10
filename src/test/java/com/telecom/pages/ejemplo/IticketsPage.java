package com.telecom.pages.ejemplo;
import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;

public class IticketsPage {
    public IticketsPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//adapt-menu[@id=\'adapt-menu-4\']/div/div/div/button/span") WebElement btnApps;
    @FindBy(xpath = "//ul[@id=\'adapt-menu-4-submenu-0_menu\']/li[4]/div/a/span") WebElement opcGTecnica;


    public void clickBtnApps() {
        btnApps.click();
    }

    public void clickOpcGTecnica() {
        opcGTecnica.click();
    }

    @FindBy(id = "frame_examples") WebElement iframeEjemploFinal2;

    @FindBy(xpath = "//input[@id='image_uploads']") WebElement inputImagen;

    @FindBy(xpath = "//div[@class='preview']//p") WebElement resultadoCarga;
    
    
    public void setInputImagen(){
        AutoTool.fileUpload("Files/DNI-pruebas.jpg", inputImagen);
    }

    public String getResultadoCarga(){
        return resultadoCarga.getText();
    }

    
}
