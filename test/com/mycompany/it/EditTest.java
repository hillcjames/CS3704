package com.mycompany.it;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditTest {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\test\\testdriver\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testEdit() throws Exception {
        driver.get(baseUrl + "CS3704/faces/index.xhtml");
        
        // First add a test job application
        driver.findElement(By.id("JobLink")).click();
        driver.findElement(By.id("JobAppListForm:datalist:createButton")).click();
        driver.findElement(By.id("JobAppCreateForm:company")).clear();
        driver.findElement(By.id("JobAppCreateForm:company")).sendKeys("VT");
        driver.findElement(By.id("JobAppCreateForm:j_idt65")).click();
        driver.findElement(By.id("JobAppCreateForm:position")).clear();
        driver.findElement(By.id("JobAppCreateForm:position")).sendKeys("Internship");
        driver.findElement(By.id("JobAppCreateForm:location")).clear();
        driver.findElement(By.id("JobAppCreateForm:location")).sendKeys("Blacksburg");
        driver.findElement(By.id("JobAppCreateForm:typeOfWork")).clear();
        driver.findElement(By.id("JobAppCreateForm:typeOfWork")).sendKeys("SDE");
        driver.findElement(By.id("JobAppCreateForm:create")).click();
        
        // Need to click twice to sort id in descending order to get the most
        // recently added job application  
        WebElement sortID = driver.findElement(By.id("JobAppListForm:datalist:j_idt31"));
        sortID.click();
        sortID.click();
        // Required to wait due to how DOM elements may be deleted and re-added
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[3]")));
        
        // Now we can edit the job application
        driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[3]")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("JobAppListForm:datalist:editButton")));
        driver.findElement(By.id("JobAppListForm:datalist:editButton")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("JobAppEditForm:company")));
        driver.findElement(By.id("JobAppEditForm:company")).clear();
        driver.findElement(By.id("JobAppEditForm:company")).sendKeys("My Company");
        driver.findElement(By.id("JobAppEditForm:position")).clear();
        driver.findElement(By.id("JobAppEditForm:position")).sendKeys("Full-Time");
        driver.findElement(By.id("JobAppEditForm:location")).clear();
        driver.findElement(By.id("JobAppEditForm:location")).sendKeys("New York");
        driver.findElement(By.id("JobAppEditForm:typeOfWork")).clear();
        driver.findElement(By.id("JobAppEditForm:typeOfWork")).sendKeys("SDE2");
        driver.findElement(By.id("JobAppEditForm:edit")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[3]")));

        assertEquals("My Company", driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[3]")).getText());
        assertEquals("Full-Time", driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[4]")).getText());
        assertEquals("New York", driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[5]")).getText());
        assertEquals("SDE2", driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[6]")).getText());
        
        // Deleting the test job application
        driver.findElement(By.cssSelector("[data-ri='" + 0 + "']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("JobAppListForm:datalist:deleteButton")));
        driver.findElement(By.id("JobAppListForm:datalist:deleteButton")).click();
        driver.findElement(By.id("JobAppListForm:datalist:delete")).click();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
