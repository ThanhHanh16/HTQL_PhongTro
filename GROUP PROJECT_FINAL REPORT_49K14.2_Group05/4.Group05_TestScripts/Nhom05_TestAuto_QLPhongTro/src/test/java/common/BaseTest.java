package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        Constant.WEBDRIVER = new ChromeDriver();
        Constant.WEBDRIVER.manage().window().maximize();
        Constant.WEBDRIVER.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constant.LONG_TIMEOUT));
    }

    @AfterMethod
    public void tearDown() {
        if (Constant.WEBDRIVER != null) {
            Constant.WEBDRIVER.quit();
        }
    }
}