package common;

import org.openqa.selenium.WebDriver;

public class Constant {

    // WebDriver dùng chung toàn project
    public static WebDriver WEBDRIVER;

    // Base URL
    public static final String BASE_URL = "http://localhost:3000";

    // URL thật trong project
    public static final String LOGIN_URL = BASE_URL + "/Đăng_nhập/Dang_nhap.html";
    public static final String KHACH_THUE_URL = BASE_URL + "/Khách_thuê/Khach_thue.html";
    public static final String HOP_DONG_URL = BASE_URL + "/Hợp_Đồng/Hop_dong.html";

    // Tài khoản test
    public static final String USERNAME = "0123456789";
    public static final String PASSWORD = "Admin@123";

    // Timeout
    public static final int SHORT_TIMEOUT = 5;
    public static final int LONG_TIMEOUT = 10;

    // Nếu bạn dùng chromedriver local thì giữ 2 dòng này
    public static final String CHROME_DRIVER = "webdriver.chrome.driver";
    public static final String CHROME_DRIVER_PATH = "drivers/chromedriver.exe";
}
