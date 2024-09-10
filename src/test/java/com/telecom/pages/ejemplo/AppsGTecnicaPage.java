package com.telecom.pages.ejemplo;
import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;

public class AppsGTecnicaPage {
    public AppsGTecnicaPage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//h2[@id='legacy-catalog-section-title-AGGC0284D1PA4ASEUQFLSEUQFLME7J']") WebElement tituloAppsGTecnica;
    @FindBy(xpath = "//div[@class='line-clamp-3 title break-word' and contains(text(), 'Canales Digitales')]") WebElement btnCanalesDigitales;
    

    public boolean existeTitulo(){
        return tituloAppsGTecnica.isDisplayed();
    }

    public void clickCanalesDigitales() {
        btnCanalesDigitales.click();
    }

    
}
