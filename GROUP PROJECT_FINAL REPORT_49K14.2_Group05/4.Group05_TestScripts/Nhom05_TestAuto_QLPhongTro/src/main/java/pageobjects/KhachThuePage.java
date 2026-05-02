package pageobjects;

import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class KhachThuePage {

    private final WebDriver driver;

    public KhachThuePage() {
        this.driver = Constant.WEBDRIVER;
    }

    // ===== URL =====
    public void open() {
        driver.get(Constant.KHACH_THUE_URL);
    }

    // ===== Locator Tìm kiếm khách thuê =====
    private final By txtTimKiem = By.xpath("//input[@placeholder='Tìm kiếm theo tên khách thuê']");
    private final By danhSachDong = By.xpath("//table/tbody/tr");
    private final By msgKhongTimThay = By.xpath("//*[contains(text(), 'Khách thuê không tồn tại')]");

    // ===== Locator danh sách / mở form =====
    private final By btnOpenAdd = By.id("btnOpenAdd");
    private final By modalForm = By.id("modal-form");
    private final By tableKhachThue = By.tagName("table");

    // ===== Locator form thêm/sửa khách thuê =====
    private final By txtMaKhach = By.id("inpMaKhach");
    private final By txtHoTen = By.id("inpHoten");
    private final By ddlGioiTinh = By.id("inpGioiTinh");
    private final By txtNgaySinh = By.id("inpNgaySinh");
    private final By txtSdt = By.id("inpSdt");
    private final By txtCccd = By.id("inpCccd");
    private final By txtQueQuan = By.id("inpQueQuan");
    private final By btnSave = By.id("btnSaveAction");
    private final By btnCancel = By.xpath("//div[@id='modal-form']//button[text()='Hủy']");

    // ===== Popup xác nhận hủy =====
    private final By modalCancelConfirm = By.id("modal-cancel-confirm");
    private final By btnCloseCancelPopup = By.xpath("//div[@id='modal-cancel-confirm']//button[contains(@class,'btn-huy')]");
    private final By btnConfirmCancel = By.xpath("//div[@id='modal-cancel-confirm']//button[contains(@class, 'btn-luu-green')]");

    // ===== Popup status =====
    private final By modalStatus = By.id("modal-status");
    private final By lblStatusMsg = By.id("statusMsg");

    // ===== Error message =====
    private final By lblErrHoten = By.id("errHoten");
    private final By lblErrGioiTinh = By.id("errGioiTinh");
    private final By lblErrNgaySinh = By.id("errNgaySinh");
    private final By lblErrSdt = By.id("errSdt");
    private final By lblErrCccd = By.id("errCccd");
    private final By lblErrQueQuan = By.id("errQueQuan");
    private final By lblErrFormGeneral = By.id("errFormGeneral");

    // ===== Locator Sửa & Xóa =====
    private final By iconSuaDong1 = By.xpath("(//table/tbody/tr[1]//td[last()]//i)[1]");
    private final By iconXoaDong1 = By.xpath("(//table/tbody/tr[1]//td[last()]//i)[2]");
    private final By modalDelete = By.id("modal-delete");

    // ===== Locator Modal Xóa =====
    private final By btnConfirmDelete = By.id("btnConfirmDelete");
    private final By btnHuyDelete     = By.xpath("//div[@id='modal-delete']//button[normalize-space()='Hủy']");
    private final By lblErrDelete     = By.id("errDelete");

    // ===== Screenshot =====
    private static final String SCREENSHOT_DIR = "test-screenshots/";

    // ==========================================
    // ===== CÁC HÀM XỬ LÝ (ACTIONS) =====
    // ==========================================

    // ===== Action Tìm kiếm =====
    public void nhapTimKiem(String keyword) {
        WebElement input = driver.findElement(txtTimKiem);
        input.clear();
        input.sendKeys(keyword);
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public int getSoDongHienThi() {
        if (isThongBaoKhongTimThayHienThi()) return 0;
        return driver.findElements(By.xpath("//table/tbody/tr")).size();
    }

    public boolean isTenKTHienThiTrongTatCaKetQua(String tenKT) {
        for (WebElement row : driver.findElements(danhSachDong)) {
            if (!row.findElement(By.xpath("./td[2]")).getText().contains(tenKT)) return false;
        }
        return true;
    }

    public boolean isThongBaoKhongTimThayHienThi() {
        try { return driver.findElement(msgKhongTimThay).isDisplayed(); }
        catch (org.openqa.selenium.NoSuchElementException e) { return false; }
    }

    public String getThongBaoKhongTimThay() {
        return driver.findElement(msgKhongTimThay).getText().trim();
    }

    // ===== Action mở form =====
    public void clickOpenAddButton() { driver.findElement(btnOpenAdd).click(); }

    // ===== Nhập dữ liệu form =====
    public void enterHoTen(String hoTen) {
        driver.findElement(txtHoTen).clear();
        driver.findElement(txtHoTen).sendKeys(hoTen);
    }

    public void selectGioiTinh(String gioiTinh) {
        if (gioiTinh == null || gioiTinh.trim().isEmpty()) return;
        new Select(driver.findElement(ddlGioiTinh)).selectByVisibleText(gioiTinh);
    }

    public void enterNgaySinh(String ngaySinh) {
        driver.findElement(txtNgaySinh).clear();
        driver.findElement(txtNgaySinh).sendKeys(ngaySinh);
    }

    public void enterSdt(String sdt) {
        driver.findElement(txtSdt).clear();
        driver.findElement(txtSdt).sendKeys(sdt);
    }

    public void enterCccd(String cccd) {
        driver.findElement(txtCccd).clear();
        driver.findElement(txtCccd).sendKeys(cccd);
    }

    public void enterQueQuan(String queQuan) {
        driver.findElement(txtQueQuan).clear();
        driver.findElement(txtQueQuan).sendKeys(queQuan);
    }

    // ===== Nút hành động form =====
    public void clickSave() { driver.findElement(btnSave).click(); }

    public void clickCancel() {
        driver.findElement(By.xpath("//div[@id='modal-form']//button[normalize-space(text())='Hủy']")).click();
    }

    public void clickConfirmCancel() { driver.findElement(btnConfirmCancel).click(); }

    public void clickCloseCancelPopup() { driver.findElement(btnCloseCancelPopup).click(); }

    public void addTenant(String hoTen, String gioiTinh, String ngaySinh, String sdt, String cccd, String queQuan) {
        clickOpenAddButton();
        enterHoTen(hoTen); selectGioiTinh(gioiTinh); enterNgaySinh(ngaySinh);
        enterSdt(sdt); enterCccd(cccd); enterQueQuan(queQuan);
        clickSave();
    }

    // ===== Lấy thông báo lỗi form =====
    public String getHoTenErrorMessage()      { return driver.findElement(lblErrHoten).getText().trim(); }
    public String getGioiTinhErrorMessage()   { return driver.findElement(lblErrGioiTinh).getText().trim(); }
    public String getNgaySinhErrorMessage()   { return driver.findElement(lblErrNgaySinh).getText().trim(); }
    public String getSdtErrorMessage()        { return driver.findElement(lblErrSdt).getText().trim(); }
    public String getCccdErrorMessage()       { return driver.findElement(lblErrCccd).getText().trim(); }
    public String getQueQuanErrorMessage()    { return driver.findElement(lblErrQueQuan).getText().trim(); }
    public String getFormGeneralErrorMessage(){ return driver.findElement(lblErrFormGeneral).getText().trim(); }
    public String getStatusMessage()          { return driver.findElement(lblStatusMsg).getText().trim(); }

    // ===== Kiểm tra hiển thị Modal =====
    public boolean isAddModalDisplayed()          { return driver.findElement(modalForm).isDisplayed(); }
    public boolean isCancelConfirmModalDisplayed(){ return driver.findElement(modalCancelConfirm).isDisplayed(); }
    public boolean isStatusModalDisplayed()       { return driver.findElement(modalStatus).isDisplayed(); }
    public boolean isEditModalDisplayed()         { return driver.findElement(modalForm).isDisplayed(); }
    public boolean isEditModalClosed()            { return !driver.findElement(modalForm).isDisplayed(); }
    public boolean isDeleteModalDisplayed()       { return driver.findElement(modalDelete).isDisplayed(); }

    // ===== Getter giá trị form =====
    public String getGeneratedTenantCode() { return driver.findElement(txtMaKhach).getAttribute("value"); }
    public String getHoTenValue()          { return driver.findElement(txtHoTen).getAttribute("value"); }
    public String getSdtValue()            { return driver.findElement(txtSdt).getAttribute("value"); }
    public String getCccdValue()           { return driver.findElement(txtCccd).getAttribute("value"); }
    public String getQueQuanValue()        { return driver.findElement(txtQueQuan).getAttribute("value"); }
    public String getGioiTinhValue()       { return new Select(driver.findElement(ddlGioiTinh)).getFirstSelectedOption().getText(); }
    public String getNgaySinhValue()       { return driver.findElement(txtNgaySinh).getAttribute("value"); }

    // ===== Action Sửa & Xóa =====
    public void clickIconSuaDauTien() { driver.findElement(iconSuaDong1).click(); }
    public void clickIconXoaDauTien() { driver.findElement(iconXoaDong1).click(); }

    // ===== GUI checks =====
    public boolean isTableEnabled()        { return driver.findElement(tableKhachThue).isEnabled(); }
    public boolean isTxtTimKiemEnabled()   { return driver.findElement(txtTimKiem).isEnabled(); }
    public boolean isBtnThemEnabled()      { return driver.findElement(btnOpenAdd).isEnabled(); }
    public boolean isIconSuaEnabled()      { return driver.findElement(iconSuaDong1).isEnabled(); }
    public boolean isIconXoaEnabled()      { return driver.findElement(iconXoaDong1).isEnabled(); }
    public boolean isCancelButtonEnabled() { return driver.findElement(btnCancel).isEnabled(); }
    public boolean isSaveButtonEnabled()   { return driver.findElement(btnSave).isEnabled(); }

    public boolean isMaKhachThueEnabled() {
        WebElement txtMa = driver.findElement(txtMaKhach);
        return txtMa.isEnabled() && txtMa.getAttribute("readonly") == null;
    }

    public void enterTenantInfo(String hoTen, String gioiTinh, String ngaySinh, String sdt, String cccd, String queQuan) {
        enterHoTen(hoTen); selectGioiTinh(gioiTinh); enterNgaySinh(ngaySinh);
        enterSdt(sdt); enterCccd(cccd); enterQueQuan(queQuan);
    }

    public void clearTenantForm() {
        enterHoTen(""); enterNgaySinh(""); enterSdt(""); enterCccd(""); enterQueQuan("");
    }

    public void clickSaveButton() { clickSave(); }

    public void waitForStatusMessage() {
        new WebDriverWait(driver, java.time.Duration.ofSeconds(Constant.LONG_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(lblStatusMsg));
    }

    // ===== Locator icon đóng modal & label =====
    private final By lblAllLabels    = By.xpath("//div[@id='modal-form']//label");
    private final By iconCloseModal  = By.xpath("//div[@id='modal-form']//span[contains(@onclick, 'closeModal')]");

    public void clickCloseIcon()        { driver.findElement(iconCloseModal).click(); }
    public boolean isCloseIconEnabled() { return driver.findElement(iconCloseModal).isEnabled(); }

    public boolean isAllLabelsDisplayed() {
        List<WebElement> labels = driver.findElements(lblAllLabels);
        if (labels.isEmpty()) return false;
        for (WebElement lbl : labels) { if (!lbl.isDisplayed()) return false; }
        return true;
    }

    // ==========================================
    // ===== HÀM XÓA KHÁCH THUÊ  =====
    // ==========================================

    /**
     * Click bằng JS để tránh bị navbar che.
     * Thử element.click() trước, nếu bị chặn thì fallback sang JS click.
     */
    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /** Chụp màn hình lưu vào test-screenshots/ */
    public void takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            Files.copy(src.toPath(),
                    Paths.get(SCREENSHOT_DIR + name + "_" + System.currentTimeMillis() + ".png"));
        } catch (Exception ignored) {}
    }

    /**
     * Chờ bảng khách thuê load xong (ít nhất 1 hàng dữ liệu).
     */
    public void waitForTableLoaded(WebDriverWait wait) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tbody[@id='tenantTableBody']/tr")));
    }

    /**
     * Click nút xóa (thùng rác) của hàng có mã tenantCode,
     * rồi chờ modal-delete hiện ra.
     * Hỗ trợ cả WebDriverWait từ tham số hoặc tạo mới.
     */
    public void openDeleteModal(String tenantCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.LONG_TIMEOUT));
        By btnDelete = By.xpath(
                "//tbody[@id='tenantTableBody']/tr[td[1][normalize-space()='" + tenantCode + "']]" +
                        "//i[contains(@class,'fa-trash-can')]"
        );
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnDelete));
        jsClick(btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDelete));
    }

    /**
     * Click nút xóa (thùng rác) dòng đầu tiên,
     * rồi chờ modal-delete hiện ra.
     */
    public void openDeleteModalFirstRow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.LONG_TIMEOUT));
        By btnDeleteFirstRow = By.xpath(
                "(//tbody[@id='tenantTableBody']//i[contains(@class,'fa-trash-can')])[1]"
        );
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnDeleteFirstRow));
        jsClick(btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDelete));
    }

    /**
     * Click nút "Đồng ý" trong modal xóa.
     */
    public void clickConfirmDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.LONG_TIMEOUT));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnConfirmDelete));
        jsClick(btn);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /**
     * Click nút "Hủy" trong modal xóa.
     */
    public void clickCancelDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.LONG_TIMEOUT));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnHuyDelete));
        jsClick(btn);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /**
     * Chờ modal-delete đóng (display:none).
     * Trả về false nếu timeout (modal vẫn còn mở).
     */
    public boolean waitForDeleteModalClosed(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalDelete));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Chờ modal-status hiện (display:block) sau khi xóa thành công.
     */
    public void waitForStatusModalVisible(WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalStatus));
    }

    /**
     * Lấy text lỗi trong modal xóa (khi xóa bị chặn do còn hợp đồng).
     */
    public String getDeleteErrorMessage() {
        return driver.findElement(lblErrDelete).getText().trim();
    }

    /**
     * Chờ thông báo lỗi xóa xuất hiện (text không rỗng).
     */
    public void waitForDeleteErrorVisible(WebDriverWait wait) {
        wait.until(d -> !d.findElement(lblErrDelete).getText().trim().isEmpty());
    }

    /**
     * Kiểm tra nút "Đồng ý" trong modal xóa có enable không.
     */
    public boolean isConfirmDeleteButtonEnabled() {
        return driver.findElement(btnConfirmDelete).isEnabled();
    }

    /**
     * Kiểm tra nút "Hủy" trong modal xóa có enable không.
     */
    public boolean isCancelDeleteButtonEnabled() {
        return driver.findElement(btnHuyDelete).isEnabled();
    }
    public void openByMenu() {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));

        By menuKhachThue = By.xpath(
                "//nav//a[contains(normalize-space(),'Khách Thuê') " +
                        "or contains(normalize-space(),'Khách thuê')]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(menuKhachThue)).click();
    }

    public boolean isTrangKhachThueHienThi() {
        try {
            WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));

            By titleKhachThue = By.xpath(
                    "//*[contains(normalize-space(),'Quản lý khách thuê') " +
                            "or contains(normalize-space(),'Khách Thuê') " +
                            "or contains(normalize-space(),'Khách thuê')]"
            );

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(titleKhachThue)
            ).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }
}
