package com.mycompany.it;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * @author Jason You
 * Created: April 19, 2017
 * @author Justin Park
 * Updated: April 20, 2017
 * @author Chris Hill
 * Updated: April 21, 2017
 */
public class DeleteTest {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        
        String driverLocation = System.getProperty("user.dir");
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("win")) {
            driverLocation += "\\test\\testdriver\\chromedriver.exe";
        }
        else if (osName.contains("nix") || osName.contains("nux")) {
            driverLocation += "/test/testdriver/chromedriver";
        }
        
        System.setProperty("webdriver.chrome.driver", driverLocation);
        
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testDelete() throws Exception {
        driver.get(baseUrl + "CS3704/faces/index.xhtml");
        
        // First add a test job application
        driver.findElement(By.id("JobLink")).click();
        driver.findElement(By.id("JobAppListForm:datalist:createButton")).click();
        driver.findElement(By.id("JobAppCreateForm:company")).clear();
        driver.findElement(By.id("JobAppCreateForm:company")).sendKeys("VT");
        driver.findElement(By.id("JobAppCreateForm:position")).clear();
        driver.findElement(By.id("JobAppCreateForm:position")).sendKeys("Internship");
        driver.findElement(By.id("JobAppCreateForm:location")).clear();
        driver.findElement(By.id("JobAppCreateForm:location")).sendKeys("Blacksburg");
        driver.findElement(By.id("JobAppCreateForm:typeOfWork")).clear();
        driver.findElement(By.id("JobAppCreateForm:typeOfWork")).sendKeys("SDE");
        driver.findElement(By.id("JobAppCreateForm:create")).click();
        
        // Need to click twice to sort id in descending order to get the most
        // recently added job application  
        WebElement sortID = driver.findElement(By.id("JobAppListForm:datalist:sortByIDButton"));
        sortID.click();
        sortID.click();
        // Required to wait due to how DOM elements may be deleted and re-added
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[3]")));
        String testID = driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[1]")).getText();
        
        // Deleting the test job application
        driver.findElement(By.cssSelector("[data-ri='" + 0 + "']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("JobAppListForm:datalist:deleteButton")));
        driver.findElement(By.id("JobAppListForm:datalist:deleteButton")).click();
        driver.findElement(By.id("JobAppListForm:datalist:delete")).click();
        
        
        //driver.findElements(By.cssSelector("tr.ui-widget-content.ui-datatable-empty-message > td")).size() > 0
        if (isElementPresent(driver, By.cssSelector("tr.ui-widget-content.ui-datatable-empty-message > td"))) {
            assertEquals("No records found.", driver.findElement(By.cssSelector("tr.ui-widget-content.ui-datatable-empty-message > td")).getText());
        }
        else {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("JobAppListForm:datalist:delete")));
            String otherID = driver.findElement(By.xpath("//tbody[@id='JobAppListForm:datalist_data']/tr/td[1]")).getText();
            System.out.println("TEST ID: " + testID);
            System.out.println("OTHER ID: " + otherID);
            assertNotEquals(testID, otherID);
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(WebDriver tempDriver, By by) {
        tempDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); 
        try {
            tempDriver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println(e);
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
