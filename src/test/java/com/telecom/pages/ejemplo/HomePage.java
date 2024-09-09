package com.telecom.pages.ejemplo;

import com.telecom.utils.AutoTool;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    public HomePage() {
        PageFactory.initElements(AutoTool.getDriver(), this);
    }

    @FindBy(xpath = "//h2[@id='legacy-catalog-section-title-AGGC0284D1PA4ASEUQFLSEUQFLME7J']") WebElement tituloHome;

    @FindBy(xpath = "//li//span[text()='Recruitment']") WebElement seccionRecruiment;
    public boolean existeTitulo(){
        return tituloHome.isDisplayed();
    }

    public void clickSeccionRecruitment(){
        AutoTool.webHandler().waitForElement(seccionRecruiment);
        seccionRecruiment.click();
    }

}
