package org.example.view.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateClientPage extends BasePage{
    public CreateClientPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "name")
    private WebElement nameInput;
    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(css = ".btn.btn-success")
    private WebElement createClientButton;
    @FindBy(tagName = "ul")
    private WebElement errorList;

    public void enterName(String name){
        nameInput.sendKeys(name);
    }

    public void enterEmail(String email){
        emailInput.sendKeys(email);
    }

    public void submitCreateClientButton() {
        createClientButton.click();
    }

    public String getErrors() {
        try {
            return errorList.getText();
        } catch (Exception e) {
            return "";
        }
    }



}
