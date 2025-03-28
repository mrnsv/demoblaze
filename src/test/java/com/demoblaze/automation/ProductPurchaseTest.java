package com.demoblaze.automation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demoblaze.utils.Constants;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ProductPurchaseTest {

    private WebDriver driver;
    private String baseUrl =  Constants.BASE_URL;    
    private String userName = Constants.USERNAME ; 
    private String password = Constants.PASSWORD ;
    private JavascriptExecutor js;
    Logger log = Logger.getLogger(ProductPurchaseTest.class);
    ExtentReports report;
    ExtentTest test;
    private WebDriverWait wait;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        report = new ExtentReports("./Reports/DemoblazeTestReport.html");
        test = report.startTest("Demoblaze Product Purchase Test");
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("Browser launched and navigated to Demoblaze.");
    }

    @Test(priority = 1)
    public void testProductPurchase() {
        HomePage homePage = new HomePage(driver);
        SignUpPage signUpPage = new SignUpPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        homePage.navigateToHomePage();
        homePage.clickSignUp();
        signUpPage.registerUser(userName, password);
        wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertTrue(closeAlertAndGetItsText().contains("Sign up successful."), "Sign up failed");
        test.log(LogStatus.PASS, "User registration completed.");

        homePage.clickLogin();
        loginPage.loginUser(userName, password);
        test.log(LogStatus.PASS, "User login successful.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));

        homePage.selectProduct("Samsung galaxy s6");
        productPage.addToCart();
        wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(closeAlertAndGetItsText(), "Product added.", "Product not added to cart");
        test.log(LogStatus.PASS, "Product added to cart.");

        cartPage.navigateToCart();
        Assert.assertTrue(driver.findElement(By.xpath("//td[text()='Samsung galaxy s6']")).isDisplayed(), "Product not in cart");
        cartPage.placeOrder("User NSV 005", "0000000000000000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Thank you for your purchase!']")));
        Assert.assertTrue(driver.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).isDisplayed(), "Order not placed");
        test.log(LogStatus.PASS, "Order placed.");

        checkoutPage.confirmCheckout(); 
        checkoutPage.closePopup() ;
        test.log(LogStatus.PASS, "User confirmed the payment") ;

        homePage.logout();
        test.log(LogStatus.PASS, "User logout successful.");

    }

    @AfterMethod
    public void captureFailureScreenshot(ITestResult result) {
        if (!result.isSuccess()) {
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String screenshotPath = "./Screenshots/" + result.getName() + "_" + timestamp + ".png";
                FileUtils.copyFile(screenshot, new File(screenshotPath));
                test.log(LogStatus.FAIL, "Test failed: " + result.getName(), test.addScreenCapture(screenshotPath));
            } catch (IOException e) {
                log.error("Error while capturing screenshot: " + e.getMessage());
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        driver.quit();
        report.endTest(test);
        report.flush();
    }

    private String closeAlertAndGetItsText() {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    static class HomePage {
        private WebDriver driver;
        By signUpLink = By.id("signin2");
        By loginLink = By.id("login2");
        By productLink = By.linkText("Samsung galaxy s6");
        By cartLink = By.id("cartur");
        By logoutLink = By.id("logout2");

        public HomePage(WebDriver driver) {
            this.driver = driver;
        }

        public void navigateToHomePage() {
            driver.get("https://www.demoblaze.com/");
            driver.manage().window().maximize();
        }

        public void clickSignUp() {
            driver.findElement(signUpLink).click();
        }

        public void clickLogin() {
            driver.findElement(loginLink).click();
           
        }

        public void selectProduct(String productName) {          
            driver.findElement(By.linkText(productName)).click();
        }

        public void navigateToCart() {
            driver.findElement(cartLink).click();
        }

        public void logout() {
            driver.findElement(logoutLink).click();
        }
    }

    static class SignUpPage {
        private WebDriver driver;
        By usernameField = By.id("sign-username");
        By passwordField = By.id("sign-password");
        By signUpButton = By.xpath("//button[contains(text(),'Sign up')]");

        public SignUpPage(WebDriver driver) {
            this.driver = driver;
        }

        public void registerUser(String username, String password) {
            driver.findElement(usernameField).sendKeys(username);
            driver.findElement(passwordField).sendKeys(password);
            driver.findElement(signUpButton).click();
        }
    }

    static class LoginPage {
        private WebDriver driver;
        By usernameField = By.id("loginusername");
        By passwordField = By.id("loginpassword");
        By loginButton = By.xpath("//button[contains(text(),'Log in')]");

        public LoginPage(WebDriver driver) {
            this.driver = driver;
        }

        public void loginUser(String username, String password) {
            driver.findElement(usernameField).sendKeys(username);
            driver.findElement(passwordField).sendKeys(password);
            driver.findElement(loginButton).click();
        }
    }

    static class ProductPage {
        private WebDriver driver;
        By addToCartButton = By.linkText("Add to cart");

        public ProductPage(WebDriver driver) {
            this.driver = driver;
        }

        public void addToCart() {
            driver.findElement(addToCartButton).click();
        }
    }

    static class CartPage {
        private WebDriver driver;
        By placeOrderButton = By.xpath("//button[contains(text(),'Place Order')]");
        By nameField = By.id("name");
        By cardField = By.id("card");
        By purchaseButton = By.xpath("//button[contains(text(),'Purchase')]");

        public CartPage(WebDriver driver) {
            this.driver = driver;
        }

        public void navigateToCart() {
            driver.findElement(By.id("cartur")).click();
        }

        public void placeOrder(String name, String cardNumber) {
            driver.findElement(placeOrderButton).click();
            driver.findElement(nameField).sendKeys(name);
            driver.findElement(cardField).sendKeys(cardNumber);
            driver.findElement(purchaseButton).click();
        }
    }

    static class CheckoutPage{
        private WebDriver driver ;
        By confirmButton = By.xpath("//button[contains(@class, 'confirm btn btn-lg btn-primary')]") ;
        By closeButton = By.xpath("//button[text()='Purchase']//preceding-sibling::button") ;

        public CheckoutPage(WebDriver driver) {
            this.driver = driver ;
        } 
        public void confirmCheckout(){
            driver.findElement(confirmButton).click();
        }
        public void closePopup(){
            driver.findElement(closeButton).click();
        }

    }
}
