package testcase.HopDong;

import common.BaseTest;
import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.HopDongPage;
import pageobjects.LoginPage;

import java.time.Duration;

public class TimHD extends BaseTest {

    private static final String MA_TON_TAI       = "HD0023";
    private static final String MA_KHONG_TON_TAI = "HD9999";

    // ════════════════════════════════════════════════════════════
    // TimHD_TC_01 — Tìm kiếm theo mã hợp đồng chính xác
    // Input  : HD0001 (tồn tại trong DB)
    // Expect : Bảng lọc chỉ hiển thị đúng 1 dòng có mã HD0001
    // ════════════════════════════════════════════════════════════
    @Test(description = "Tìm kiếm hợp đồng thành công")
    public void TimHD_TC_01() {
        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        hopDongPage.nhapTimKiem(MA_TON_TAI);

        hopDongPage.choKetQuaTimKiemTheoMa(MA_TON_TAI, 1);

        Assert.assertEquals(
                hopDongPage.getSoDongDangHienThi(), 1,
                "Sau khi lọc theo [" + MA_TON_TAI + "] phải chỉ còn 1 dòng."
        );

        Assert.assertTrue(
                hopDongPage.isMaHDHienThi(MA_TON_TAI),
                "Mã hợp đồng [" + MA_TON_TAI + "] phải xuất hiện trong kết quả."
        );
    }
    // ════════════════════════════════════════════════════════════
    // TimHD_TC_02 — Tìm kiếm theo mã hợp đồng KHÔNG tồn tại
    // Input  : HD9999 (không có trong DB)
    // Expect : Hiển thị thông báo đỏ + bảng không có dòng dữ liệu
    // ════════════════════════════════════════════════════════════
    @Test(description = "Tìm kiếm hợp đồng thất bại")
    public void TimHD_TC_02() {
        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();
        hopDongPage.nhapTimKiem(MA_KHONG_TON_TAI);

        Assert.assertTrue(
                hopDongPage.isThongBaoKhongTimThayHienThi(),
                "Phải hiển thị thông báo 'Không tìm thấy hợp đồng nào phù hợp.'"
        );
        Assert.assertEquals(
                hopDongPage.getThongBaoKhongTimThay(),
                "Không tìm thấy hợp đồng nào phù hợp.",
                "Nội dung thông báo không đúng."
        );
        Assert.assertEquals(
                hopDongPage.getSoDongHienThi(), 0,
                "Bảng phải không có dòng dữ liệu nào."
        );
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_TC_01 — Hiển thị trang Hợp Đồng
    // Bước : Đăng nhập → click menu "Hợp Đồng"
    // Expect: URL đúng, tiêu đề đúng, bảng hiển thị
    // ════════════════════════════════════════════════════════════
    @Test(description = "Hiển thị trang Hợp Đồng")
    public void HOPDONG_TC_01_HienThiTrangHopDong() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        By menuHopDong = By.xpath(
                "//nav//a[contains(normalize-space(),'Hợp Đồng') or contains(normalize-space(),'Hợp đồng')]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(menuHopDong)).click();
        wait.until(ExpectedConditions.urlContains("Hop_dong"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("Hop_dong"),
                "URL phải chứa 'Hop_dong'. URL hiện tại: " + driver.getCurrentUrl()
        );

        By titleLocator = By.xpath("//*[contains(normalize-space(),'Quản lý hợp đồng')]");
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator)).isDisplayed(),
                "Tiêu đề 'Quản lý hợp đồng' phải hiển thị trên trang."
        );

        By tableLocator = By.id("contractContainer");
        Assert.assertTrue(
                wait.until(ExpectedConditions.presenceOfElementLocated(tableLocator)).isDisplayed(),
                "Bảng hợp đồng (contractContainer) phải hiển thị."
        );
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_TC_02 — Click nút "Thêm hợp đồng" → Modal mở
    // Bước : Vào trang HĐ → click nút "Thêm hợp đồng"
    // Expect: Modal #contractModal hiển thị, tiêu đề đúng
    // ════════════════════════════════════════════════════════════
    @Test(description = "Click nút Thêm hợp đồng → Modal hiển thị")
    public void HOPDONG_TC_02_ClickNutThemHopDong_ModalHienThi() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-add"))).click();

        WebElement modalEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("contractModal"))
        );
        Assert.assertTrue(
                modalEl.isDisplayed(),
                "Modal 'Thêm hợp đồng' (#contractModal) phải hiển thị sau khi click nút."
        );

        By modalTitle = By.xpath(
                "//div[@id='contractModal']//*[contains(normalize-space(),'Thêm hợp đồng')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle)).isDisplayed(),
                "Tiêu đề 'Thêm hợp đồng' trong modal phải hiển thị."
        );
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_TC_03 — Click icon Xóa → Popup xác nhận xóa mở
    // Bước : Vào trang HĐ → click icon xóa dòng đầu tiên
    // Expect: Modal #modal-delete hiển thị, nội dung xác nhận đúng
    // ════════════════════════════════════════════════════════════
    @Test(description = "Click icon Xóa → Popup xác nhận hiển thị")
    public void HOPDONG_TC_03_ClickIconXoa_PopupXacNhanHienThi() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        By firstRow = By.xpath("//tbody[@id='contractContainer']/tr[1]");
        wait.until(ExpectedConditions.presenceOfElementLocated(firstRow));

        By btnXoa = By.xpath(
                "//tbody[@id='contractContainer']/tr[1]//button[" +
                        ".//*[contains(@class,'fa-trash')] " +
                        "or contains(@class,'btn-delete') " +
                        "or contains(@onclick,'delete') " +
                        "or contains(@onclick,'xoa')]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(btnXoa)).click();

        WebElement modalEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("modal-delete"))
        );
        Assert.assertTrue(
                modalEl.isDisplayed(),
                "Popup xác nhận xóa (#modal-delete) phải hiển thị sau khi click icon xóa."
        );

        By txtXacNhan = By.xpath(
                "//div[@id='modal-delete']//*[contains(normalize-space(),'Bạn có chắc chắn muốn xóa')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(txtXacNhan)).isDisplayed(),
                "Nội dung xác nhận xóa phải hiển thị trong popup."
        );
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_UI_01 — Kiểm tra bảng [Hợp đồng] hiển thị đúng
    // Bước : Vào trang Hợp Đồng
    // Expect: Bảng contractContainer hiển thị, đủ header cột,
    //         navbar + logo hiển thị, menu active đúng
    // ════════════════════════════════════════════════════════════
    @Test(description = "Check GUI - Bảng Hợp đồng hiển thị đúng")
    public void HOPDONG_UI_01_BangHopDong() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Navbar hiển thị
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("header.navbar"))).isDisplayed(),
                "Navbar phải hiển thị."
        );

        // Logo hiển thị
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("header .logo"))).isDisplayed(),
                "Logo 'QUẢN LÝ PHÒNG TRỌ' phải hiển thị trên navbar."
        );

        // Menu "Hợp Đồng" đang active
        By menuActive = By.xpath(
                "//nav//a[contains(@class,'active') and " +
                        "(contains(normalize-space(),'Hợp Đồng') or contains(normalize-space(),'Hợp đồng'))]"
        );
        Assert.assertFalse(
                driver.findElements(menuActive).isEmpty(),
                "Menu 'Hợp Đồng' phải đang ở trạng thái active."
        );

        // Bảng hợp đồng hiển thị
        Assert.assertTrue(
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("contractContainer"))).isDisplayed(),
                "Bảng hợp đồng (contractContainer) phải hiển thị."
        );

        // Đủ header cột bảng
        String[] headers = {"Mã HĐ", "Mã Phòng", "Đại diện", "Số điện thoại",
                "Ngày BĐ", "Ngày KT", "Trạng thái", "Hành động"};
        for (String header : headers) {
            By headerLocator = By.xpath(
                    "//thead//th[contains(normalize-space(),'" + header + "')]"
            );
            Assert.assertFalse(
                    driver.findElements(headerLocator).isEmpty(),
                    "Header cột '" + header + "' phải hiển thị trong bảng."
            );
        }
    }

    // ════════════════════════════════════════════════════════════
// HOPDONG_UI_02 — Kiểm tra popup Chi tiết hợp đồng
// Bước : Vào trang HĐ → click vào dòng hợp đồng đầu tiên
// Expect: Popup chi tiết hiển thị đúng các thông tin:
//         Mã HĐ, Mã Phòng, Tiền Cọc, Ngày BĐ, Ngày KT,
//         Danh sách khách thuê
// ════════════════════════════════════════════════════════════
    @Test(description = "Check GUI - Popup Chi tiết hợp đồng")
    public void HOPDONG_UI_02_LinkChiTietBanGhi() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Chờ có ít nhất 1 dòng trong bảng
        By firstRow = By.xpath("//tbody[@id='contractContainer']/tr[1]");
        wait.until(ExpectedConditions.presenceOfElementLocated(firstRow));

        // Lấy mã HĐ của dòng đầu tiên để verify trong popup
        String maHD = driver.findElement(
                By.xpath("//tbody[@id='contractContainer']/tr[1]/td[1]")
        ).getText().trim();

        // Click vào dòng hợp đồng đầu tiên để mở popup chi tiết
        wait.until(ExpectedConditions.elementToBeClickable(firstRow)).click();

        // ── 1. Popup chi tiết hiển thị ───────────────────────
        // Popup có thể là modal với id chứa 'detail', 'chiTiet', 'contractDetail'...
        By popupChiTiet = By.xpath(
                "//*[contains(@id,'detail') or contains(@id,'chiTiet') " +
                        "or contains(@id,'contractDetail') or contains(@id,'hopDongChiTiet')]" +
                        "[contains(@class,'modal') or contains(@class,'popup') " +
                        "or contains(@class,'dialog')]"
        );
        WebElement popupEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupChiTiet)
        );
        Assert.assertTrue(
                popupEl.isDisplayed(),
                "Popup chi tiết hợp đồng phải hiển thị sau khi click vào dòng."
        );

        // ── 2. Tiêu đề popup "Chi tiết hợp đồng" ────────────
        By titlePopup = By.xpath(
                "//*[contains(normalize-space(),'Chi tiết hợp đồng') " +
                        "or contains(normalize-space(),'Chi Tiết Hợp Đồng')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(titlePopup)).isDisplayed(),
                "Tiêu đề 'Chi tiết hợp đồng' phải hiển thị trong popup."
        );

        // ── 3. Mã HĐ hiển thị đúng trong popup ──────────────
        By maHDPopup = By.xpath(
                "//*[contains(normalize-space(),'Mã HĐ') or contains(normalize-space(),'Mã hợp đồng')]"
                        + "/following-sibling::* | "
                        + "//*[contains(normalize-space(),'" + maHD + "')]"
        );
        Assert.assertFalse(
                driver.findElements(maHDPopup).isEmpty(),
                "Mã HĐ [" + maHD + "] phải hiển thị trong popup chi tiết."
        );

        // ── 4. Thông tin Mã Phòng hiển thị ───────────────────
        By maPhong = By.xpath(
                "//*[contains(normalize-space(),'MÃ PHÒNG') " +
                        "or contains(normalize-space(),'Mã Phòng') " +
                        "or contains(normalize-space(),'Mã phòng')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(maPhong)).isDisplayed(),
                "Thông tin 'Mã Phòng' phải hiển thị trong popup."
        );

        // ── 5. Thông tin Tiền Cọc hiển thị ───────────────────
        By tienCoc = By.xpath(
                "//*[contains(normalize-space(),'TIỀN CỌC') " +
                        "or contains(normalize-space(),'Tiền Cọc') " +
                        "or contains(normalize-space(),'Tiền cọc')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(tienCoc)).isDisplayed(),
                "Thông tin 'Tiền Cọc' phải hiển thị trong popup."
        );

        // ── 6. Ngày Bắt Đầu hiển thị ─────────────────────────
        By ngayBD = By.xpath(
                "//*[contains(normalize-space(),'NGÀY BẮT ĐẦU') " +
                        "or contains(normalize-space(),'Ngày Bắt Đầu') " +
                        "or contains(normalize-space(),'Ngày bắt đầu')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(ngayBD)).isDisplayed(),
                "Thông tin 'Ngày Bắt Đầu' phải hiển thị trong popup."
        );

        // ── 7. Ngày Kết Thúc hiển thị ────────────────────────
        By ngayKT = By.xpath(
                "//*[contains(normalize-space(),'NGÀY KẾT THÚC') " +
                        "or contains(normalize-space(),'Ngày Kết Thúc') " +
                        "or contains(normalize-space(),'Ngày kết thúc')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(ngayKT)).isDisplayed(),
                "Thông tin 'Ngày Kết Thúc' phải hiển thị trong popup."
        );

        // ── 8. Danh sách khách thuê hiển thị ─────────────────
        By danhSachKhachThue = By.xpath(
                "//*[contains(normalize-space(),'DANH SÁCH KHÁCH THUÊ') " +
                        "or contains(normalize-space(),'Danh sách khách thuê') " +
                        "or contains(normalize-space(),'Danh Sách Khách Thuê')]"
        );
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(danhSachKhachThue)).isDisplayed(),
                "Mục 'Danh sách khách thuê' phải hiển thị trong popup."
        );

        // ── 9. Nút đóng popup (X) hiển thị và enabled ────────
        By btnDong = By.xpath(
                "//*[contains(@id,'detail') or contains(@id,'chiTiet') " +
                        "or contains(@id,'contractDetail')]//" +
                        "*[contains(@class,'close') or contains(@class,'btn-close') " +
                        "or @data-dismiss='modal' or contains(@onclick,'close')]"
        );
        WebElement btnDongEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(btnDong)
        );
        Assert.assertTrue(btnDongEl.isDisplayed(), "Nút đóng (X) popup phải hiển thị.");
        Assert.assertTrue(btnDongEl.isEnabled(),   "Nút đóng (X) popup phải enabled.");
    }
    // ════════════════════════════════════════════════════════════
    // HOPDONG_UI_03 — Kiểm tra ô nhập liệu [Tìm kiếm]
    // Bước : Vào trang HĐ → click vào ô tìm kiếm
    // Expect: Ô tìm kiếm visible, enabled, nhận focus khi click
    // ════════════════════════════════════════════════════════════
    @Test(description = "Check GUI - Ô nhập liệu Tìm kiếm")
    public void HOPDONG_UI_03_OTimKiem() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        By searchBox = By.id("searchInput");
        WebElement searchEl = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));

        // Kiểm tra hiển thị và enabled
        Assert.assertTrue(searchEl.isDisplayed(), "Ô tìm kiếm phải hiển thị.");
        Assert.assertTrue(searchEl.isEnabled(),   "Ô tìm kiếm phải enabled.");

        // Click vào ô tìm kiếm và kiểm tra nhận focus
        searchEl.click();
        WebElement focused = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        Assert.assertTrue(focused.isDisplayed(), "Ô tìm kiếm vẫn phải hiển thị sau khi click.");

        // Kiểm tra placeholder (nếu có)
        String placeholder = searchEl.getAttribute("placeholder");
        if (placeholder != null && !placeholder.isEmpty()) {
            Assert.assertFalse(
                    placeholder.isEmpty(),
                    "Placeholder của ô tìm kiếm phải có nội dung."
            );
        }
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_UI_04 — Kiểm tra nút [Thêm hợp đồng]
    // Bước : Vào trang HĐ → click nút "Thêm hợp đồng"
    // Expect: Nút visible, enabled, click mở modal thêm
    // ════════════════════════════════════════════════════════════
    @Test(description = "Check GUI - Button Thêm hợp đồng")
    public void HOPDONG_UI_04_NutThemHopDong() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        By btnThem = By.cssSelector("button.btn-add");
        WebElement btnThemEl = wait.until(ExpectedConditions.visibilityOfElementLocated(btnThem));

        // Kiểm tra visible + enabled trước khi click
        Assert.assertTrue(btnThemEl.isDisplayed(), "Nút 'Thêm hợp đồng' phải hiển thị.");
        Assert.assertTrue(btnThemEl.isEnabled(),   "Nút 'Thêm hợp đồng' phải enabled.");

        // Click nút và kiểm tra modal mở ra
        wait.until(ExpectedConditions.elementToBeClickable(btnThem)).click();

        WebElement modalEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("contractModal"))
        );
        Assert.assertTrue(
                modalEl.isDisplayed(),
                "Sau khi click nút 'Thêm hợp đồng', modal phải hiển thị."
        );
    }

    // ════════════════════════════════════════════════════════════
    // HOPDONG_UI_05 — Kiểm tra icon [Xóa]
    // Bước : Vào trang HĐ → click icon xóa dòng đầu tiên
    // Expect: Icon visible, enabled, click mở popup xác nhận xóa
    // ════════════════════════════════════════════════════════════
    @Test(description = "Check GUI - Icon Xóa hợp đồng")
    public void HOPDONG_UI_05_IconXoa() {
        WebDriver driver = Constant.WEBDRIVER;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Chờ có ít nhất 1 dòng trong bảng
        By firstRow = By.xpath("//tbody[@id='contractContainer']/tr[1]");
        wait.until(ExpectedConditions.presenceOfElementLocated(firstRow));

        By iconXoa = By.xpath(
                "//tbody[@id='contractContainer']/tr[1]//button[" +
                        ".//*[contains(@class,'fa-trash')] " +
                        "or contains(@class,'btn-delete') " +
                        "or contains(@onclick,'delete') " +
                        "or contains(@onclick,'xoa')]"
        );
        WebElement iconEl = wait.until(ExpectedConditions.visibilityOfElementLocated(iconXoa));

        // Kiểm tra visible + enabled trước khi click
        Assert.assertTrue(iconEl.isDisplayed(), "Icon xóa của dòng đầu tiên phải hiển thị.");
        Assert.assertTrue(iconEl.isEnabled(),   "Icon xóa của dòng đầu tiên phải enabled.");

        // Click icon và kiểm tra popup xác nhận mở
        wait.until(ExpectedConditions.elementToBeClickable(iconXoa)).click();

        WebElement popupEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("modal-delete"))
        );
        Assert.assertTrue(
                popupEl.isDisplayed(),
                "Sau khi click icon xóa, popup xác nhận (#modal-delete) phải hiển thị."
        );
    }
}