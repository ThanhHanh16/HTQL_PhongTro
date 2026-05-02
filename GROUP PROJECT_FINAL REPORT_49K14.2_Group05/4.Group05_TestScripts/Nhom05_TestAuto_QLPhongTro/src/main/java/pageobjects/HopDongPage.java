package pageobjects;


import common.Constant;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import common.Constant;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;


public class HopDongPage {


    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait; // 5 giây — dùng cho inline validation
    private final Random random = new Random();


    public HopDongPage() {
        this.driver    = Constant.WEBDRIVER;
        this.wait      = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }


    // ===== URL =====
    public void open() {
        driver.get(Constant.HOP_DONG_URL);
    }


    // ===== LOCATORS =====

    // Search
    private final By inputTimKiem    = By.xpath("//input[@placeholder='Tìm kiếm theo mã hợp đồng']");
    private final By rowsHopDong     = By.xpath("//tbody[@id='contractContainer']/tr");
    private final By cellMaHD        = By.xpath("//tbody[@id='contractContainer']/tr/td[1]");
    private final By lblKhongTimThay = By.xpath("//*[contains(text(),'Không tìm thấy hợp đồng nào phù hợp')]");


    // Add Contract
    private final By btnThemHopDong    = By.cssSelector("button.btn-add");
    private final By ddlMaPhong        = By.id("selMaPhong");
    private final By btnThemKT         = By.xpath("//button[contains(.,'Thêm KT')]");
    private final By modalKT           = By.id("ktModal");
    private final By checkboxKhachThue = By.xpath("//div[@id='ktModal']//input[@type='checkbox']");
    private final By btnOk             = By.xpath("//div[@id='ktModal']//button[contains(.,'Ok')]");
    private final By ddlNguoiDaiDien   = By.id("selDaiDien");
    private final By ngayBatDau        = By.id("inpNgayBD");
    private final By ngayKetThuc       = By.id("inpNgayKT");
    private final By inputTienCoc      = By.id("inpTienCoc");
    private final By inputDieuKhoan    = By.id("inpDieuKhoan");
    private final By btnLuu            = By.id("btnSaveAction");

    // Nút "Hủy" trên form Thêm hợp đồng
    private final By btnHuyForm        = By.xpath("//button[normalize-space()='Hủy' and not(ancestor::*[@id='confirmModal']) and not(ancestor::*[@id='popupXacNhanHuy'])]");

    // Popup xác nhận hủy ("Bạn có chắc chắn muốn hủy?")
    private final By popupXacNhanHuy   = By.xpath("//*[contains(text(),'Bạn có chắc chắn muốn hủy')]/ancestor::div[contains(@class,'modal') or contains(@class,'popup') or contains(@class,'swal') or contains(@role,'dialog')][1]");
    private final By lblTextXacNhanHuy = By.xpath("//*[contains(text(),'Bạn có chắc chắn muốn hủy')]");
    private final By btnXacNhanHuyPopup = By.xpath("//*[contains(text(),'Bạn có chắc chắn muốn hủy')]/following::button[normalize-space()='Xác nhận'][1]");
    // Nút "Hủy" trong popup xác nhận hủy — TC_12
    private final By btnHuyTrenPopup    = By.xpath("//*[contains(text(),'Bạn có chắc chắn muốn hủy')]/following::button[normalize-space()='Hủy'][1]");

    // Form Thêm hợp đồng (dùng để kiểm tra form đã đóng)
    private final By formThemHopDong   = By.xpath("//*[contains(@class,'modal') or contains(@id,'addContractModal') or contains(@id,'themHopDongModal')][.//*[contains(text(),'Thêm hợp đồng') or contains(text(),'Thông tin hợp đồng')]]");


    // Status modal (xuất hiện sau khi click Lưu)
    private final By modalStatus  = By.id("modal-status");
    private final By lblStatusMsg = By.id("statusMsg");
    private final By btnCloseStatus = By.xpath("//div[@id='modal-status']//button[contains(text(), 'Đóng')]");


    // Text đỏ inline "Vui lòng chọn phòng trước."
    // hiện ngay dưới nút + Thêm KT khi chưa chọn Mã phòng — TC_02
    private final By lblChonPhongTruoc = By.xpath(
            "//*[contains(text(),'Vui lòng chọn phòng trước')]"
    );


    // Text đỏ inline lỗi khách thuê — TC_03
    // "Tên khách thuê trống. Vui lòng nhập tên khách thuê."
    private final By lblLoiKhachThue = By.xpath(
            "//*[contains(text(),'Tên khách thuê trống') or contains(text(),'Vui lòng nhập tên khách thuê')]"
    );


    // Text đỏ inline lỗi người đại diện — TC_03, TC_05
    // "Vui lòng chọn người đại diện." hoặc "Tên người đại diện trống..."
    private final By lblLoiNguoiDaiDien = By.xpath(
            "//*[contains(text(),'Vui lòng chon người đại diện') or contains(text(),'Vui lòng chọn người đại diện') or contains(text(),'Tên người đại diện trống')]"
    );

    // Delete Contract
    private final By btnDeleteFirstRow = By.xpath("(//tbody[@id='contractContainer']//i[contains(@class,'fa-trash')])[1] | (//tbody[@id='contractContainer']//button[contains(@class, 'btn-delete') or contains(@class, 'delete')])[1]");
    private final By modalDelete       = By.id("modal-delete");
    private final By btnConfirmDelete  = By.id("btnConfirmDelete");
    private final By btnCancelDelete   = By.xpath("//div[@id='modal-delete']//button[contains(text(),'Hủy')]");
    private final By contractList      = By.id("contractContainer");


    // ===== SEARCH =====

    public void nhapTimKiem(String maHD) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTimKiem));

        input.click();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(maHD);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('keyup', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                input
        );
    }

    public int getSoDongHienThi() {
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        return driver.findElements(rowsHopDong).size();
    }


    public boolean isMaHDHienThi(String maHD) {
        try {
            List<WebElement> cells = driver.findElements(cellMaHD);

            for (WebElement cell : cells) {
                try {
                    if (cell.isDisplayed()
                            && cell.getText().trim().equalsIgnoreCase(maHD)) {
                        return true;
                    }
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }


    public boolean isThongBaoKhongTimThayHienThi() {
        try {
            WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(15));

            WebElement thongBao = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(lblKhongTimThay)
            );

            return thongBao.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getThongBaoKhongTimThay() {
        try {
            WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(15));

            WebElement thongBao = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(lblKhongTimThay)
            );

            return thongBao.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    // ===== ADD HỢP ĐỒNG =====

    public void clickThemHopDong() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemHopDong)).click();
    }


    public void selectRandomMaPhong() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(ddlMaPhong));
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();
        if (options.size() <= 1) throw new RuntimeException("Không có phòng để chọn");
        select.selectByIndex(1 + random.nextInt(options.size() - 1));
    }


    /**
     * Chọn phòng có sức chứa tối đa 2 người (TC_04).
     * Điều chỉnh điều kiện lọc text theo dữ liệu thực tế trong dropdown.
     */
    public void selectPhongToiDa2Nguoi() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(ddlMaPhong));
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();
        for (WebElement opt : options) {
            String text = opt.getText();
            if (text.contains("(2)") || text.toLowerCase().contains("max: 2")
                    || text.toLowerCase().contains("sức chứa: 2")) {
                select.selectByVisibleText(text);
                return;
            }
        }
        throw new RuntimeException("Không tìm thấy phòng có sức chứa tối đa 2 người");
    }


    /**
     * Chỉ click nút "+ Thêm KT" mà KHÔNG chọn Mã phòng trước (TC_02).
     * Hệ thống sẽ hiển thị text đỏ "Vui lòng chọn phòng trước." ngay dưới nút.
     * Test case kết thúc ở đây — KHÔNG click Lưu.
     */
    public void clickThemKT() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemKT)).click();
    }


    private void forceCloseModal() {
        try {
            // Đợi cho popup khách thuê biến mất
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalKT));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Xóa lớp nền mờ
            js.executeScript(
                    "var backdrops = document.getElementsByClassName('modal-backdrop');" +
                            "while (backdrops.length > 0) { backdrops[0].parentNode.removeChild(backdrops[0]); }"
            );
            // Xóa class 'modal-open' khỏi thẻ body
            js.executeScript("document.body.classList.remove('modal-open');");

            // Chờ một chút để đảm bảo DOM đã được cập nhật
            Thread.sleep(500);
        } catch (Exception e) {
            // Bỏ qua nếu có lỗi
        }
    }

    public void selectRandomKhachThue() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemKT)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalKT));
        List<WebElement> list = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(checkboxKhachThue));
        if (list.isEmpty()) throw new RuntimeException("Không có khách thuê để chọn");
        WebElement checkbox = list.get(random.nextInt(list.size()));
        if (!checkbox.isSelected()) checkbox.click();

        // Click OK và dọn dẹp
        wait.until(ExpectedConditions.elementToBeClickable(btnOk)).click();
        forceCloseModal();
    }


    /**
     * Chọn đúng {@code soLuong} khách thuê trong popup (TC_04).
     */
    public void selectKhachThueVoiSoLuong(int soLuong) {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemKT)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalKT));
        List<WebElement> list = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(checkboxKhachThue));
        if (list.size() < soLuong)
            throw new RuntimeException("Không đủ khách thuê để chọn " + soLuong + " người");
        for (int i = 0; i < soLuong; i++) {
            if (!list.get(i).isSelected()) list.get(i).click();
        }

        // Click OK và dọn dẹp
        wait.until(ExpectedConditions.elementToBeClickable(btnOk)).click();
        forceCloseModal();
    }


    public void selectRandomNguoiDaiDien() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(ddlNguoiDaiDien));
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();
        if (options.size() <= 1) throw new RuntimeException("Không có người đại diện để chọn");
        select.selectByIndex(1 + random.nextInt(options.size() - 1));
    }


    public void setNgayBatDau(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(ngayBatDau));
        input.clear();
        input.sendKeys(value);
    }


    public void setNgayKetThuc(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(ngayKetThuc));
        input.clear();
        input.sendKeys(value);
    }


    public void setTienCoc(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTienCoc));
        input.clear();
        input.sendKeys(value);
    }

    public String getTienCoc() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inputTienCoc)).getAttribute("value");
    }


    public void setDieuKhoan(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputDieuKhoan));
        input.clear();
        input.sendKeys(value);
    }


    public void clickLuu() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLuu)).click();
    }


    // ===== HỦY FORM THÊM HỢP ĐỒNG — TC_11 =====

    /** Click nút "Hủy" trên form Thêm hợp đồng để mở popup xác nhận hủy. */
    public void clickHuyForm() {
        wait.until(ExpectedConditions.elementToBeClickable(btnHuyForm)).click();
    }

    /** Kiểm tra popup "Bạn có chắc chắn muốn hủy?" có xuất hiện hay không. */
    public boolean isPopupXacNhanHuyHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblTextXacNhanHuy));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Click nút "Xác nhận" trên popup xác nhận hủy. */
    public void clickXacNhanHuyPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(btnXacNhanHuyPopup)).click();
    }

    /**
     * Kiểm tra form Thêm hợp đồng đã được đóng (không còn hiển thị).
     * Trả về true khi form biến mất sau khi xác nhận hủy.
     */
    public boolean isFormThemHopDongDaDong() {
        try {
            // Sau khi hủy, ô nhập tiền cọc phải biến mất — dùng làm proxy cho form đã đóng
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(inputTienCoc));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Click nút "Hủy" trên popup xác nhận h��y (giữ nguyên form, đóng popup). */
    public void clickHuyTrenPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(btnHuyTrenPopup)).click();
    }

    /**
     * Kiểm tra form Thêm hợp đồng vẫn còn hiển thị.
     * Trả về true khi ô tiền cọc vẫn visible — proxy cho form chưa đóng.
     */
    public boolean isFormThemHopDongVanHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(inputTienCoc));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    // ===== VALIDATION INLINE — TC_02 =====

    /**
     * Chờ text đỏ "Vui lòng chọn phòng trước." xuất hiện ngay dưới nút + Thêm KT.
     * Dùng shortWait (5s) vì text này render tức thì sau khi click.
     */
    public boolean isThongBaoChonPhongHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblChonPhongTruoc));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getThongBaoChonPhong() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblChonPhongTruoc))
                .getText().trim();
    }


    // ===== VALIDATION INLINE — TC_03, TC_05 =====

    /** Text đỏ lỗi khách thuê trống hiện ngay dưới ô Khách Thuê sau khi click Lưu. */
    public boolean isLoiKhachThueHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiKhachThue));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiKhachThue() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiKhachThue))
                .getText().trim();
    }


    /** Text đỏ lỗi người đại diện trống hiện ngay bên ô Tên Người Đại Diện sau khi click Lưu. */
    public boolean isLoiNguoiDaiDienHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNguoiDaiDien));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiNguoiDaiDien() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNguoiDaiDien))
                .getText().trim();
    }

    // Text đỏ inline lỗi vượt quá số khách tối đa — TC_04
    private final By lblLoiVuotSoKhach = By.xpath(
            "//*[contains(text(),'Số lượng khách thuê không được vượt quá')]"
    );


    public boolean isLoiVuotSoKhachHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiVuotSoKhach));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiVuotSoKhach() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiVuotSoKhach))
                .getText().trim();
    }

    // Text đỏ inline lỗi tiền cọc — TC_08, TC_09
    private final By lblLoiTienCoc = By.xpath(
            "//*[contains(text(),'Tiền cọc phải là số') or contains(text(),'Vui lòng nhập lại')]"
    );


    public boolean isLoiTienCocHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiTienCoc));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiTienCoc() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiTienCoc))
                .getText().trim();
    }


    // Text đỏ inline lỗi điều khoản — TC_10
    private final By lblLoiDieuKhoan = By.xpath(
            "//*[contains(text(),'Điều khoản trống') or contains(text(),'Vui lòng nhập điều khoản')]"
    );


    public boolean isLoiDieuKhoanHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiDieuKhoan));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiDieuKhoan() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiDieuKhoan))
                .getText().trim();
    }

    // ===== STATUS (modal sau khi click Lưu) =====

    public boolean isStatusDisplayed() {
        return !driver.findElements(modalStatus).isEmpty()
                && driver.findElement(modalStatus).isDisplayed();
    }


    public String getStatusMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(lblStatusMsg))
                .getText().trim();
    }

    /**
     * Đóng modal status với xử lý lỗi robust.
     * Không throw exception nếu modal không xuất hiện trong 10 giây.
     */
    public void closeStatusModal() {
        try {
            // Đợi modal status xuất hiện
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalStatus));

            // Đợi nút "Đóng" có thể click
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(btnCloseStatus));
            closeBtn.click();

            // Đợi modal biến mất
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalStatus));
        } catch (TimeoutException e) {
            // Nếu modal không xuất hiện, có thể dữ liệu đã tồn tại hoặc save thất bại
            System.out.println("⚠️ Modal status không xuất hiện - có thể form save thất bại hoặc không có validation");
            // Không throw exception, tiếp tục test
        }
    }

    /**
     * Kiểm tra xem status modal có xuất hiện không (không throw exception).
     * Dùng để check trước khi assert message.
     */
    public boolean waitForStatusModalWithTimeout(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(modalStatus));
            return true;
        } catch (TimeoutException e) {
            System.out.println("⚠️ Status modal không xuất hiện trong " + seconds + "s");
            return false;
        }
    }

    public void setNgayBatDauBangJS(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(ngayBatDau));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input'));" +
                        "arguments[0].dispatchEvent(new Event('change'));",
                input,
                value
        );
    }

    private final By lblLoiNgayBatDau = By.xpath(
            "//*[contains(normalize-space(),'Vui lòng chọn ngày bắt đầu.')" +
                    " or contains(normalize-space(),'Ngày bắt đầu không được nhỏ hơn ngày hiện tại')]"
    );


    public boolean isLoiNgayBatDauHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNgayBatDau));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiNgayBatDau() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNgayBatDau))
                .getText().trim();
    }

    private final By lblLoiNgayKetThuc = By.xpath(
            "//*[contains(normalize-space(),'Vui lòng chọn ngày kết thúc.')" +
                    " or contains(normalize-space(),'Ngày kết thúc phải lớn hơn ngày bắt đầu')]"
    );


    public boolean isLoiNgayKetThucHienThi() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNgayKetThuc));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getLoiNgayKetThuc() {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(lblLoiNgayKetThuc))
                .getText().trim();
    }

    // ===== DELETE HỢP ĐỒNG — REFACTORED HELPERS =====

    /**
     * Chờ danh sách hợp đồng hiển thị trước khi thực hiện thao tác xóa.
     */
    public void waitForContractListDisplayed() {
        wait.until(ExpectedConditions.presenceOfElementLocated(rowsHopDong));
    }

    /**
     * Mở modal xóa bằng cách click nút xóa hàng đầu tiên và đợi modal hiển thị.
     */
    public void openDeleteModal() {
        clickDeleteFirstRow();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDelete));
    }

    /**
     * Click nút xóa trên hàng đầu tiên (dùng JavaScript để tránh bị che bởi z-index).
     * Hỗ trợ cả icon i.fa-trash và button.btn-delete
     */
    public void clickDeleteFirstRow() {
        // Chờ danh sách hiển thị trước
        wait.until(ExpectedConditions.presenceOfElementLocated(rowsHopDong));

        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnDeleteFirstRow));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", btn);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /**
     * Click nút "Đồng ý" trên modal xác nhận xóa.
     * Dùng JavaScript để tránh bị che bởi z-index.
     */
    public void clickAgreeDelete() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDelete));
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnConfirmDelete));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", btn);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /**
     * Click nút "Hủy" trên modal xác nhận xóa.
     * Dùng JavaScript để tránh bị che bởi z-index.
     */
    public void clickCancelDelete() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalDelete));
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnCancelDelete));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", btn);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /**
     * Kiểm tra xem danh sách hợp đồng có hiển thị hay không.
     */
    public boolean isContractListDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(contractList));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Kiểm tra nút "Đồng ý" xóa có ở trạng thái enable hay không.
     */
    public boolean isConfirmDeleteButtonEnabled() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnConfirmDelete));
        return btn.isEnabled();
    }

    /**
     * Kiểm tra nút "Hủy" xóa có ở trạng thái enable hay không.
     */
    public boolean isCancelDeleteButtonEnabled() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnCancelDelete));
        return btn.isEnabled();
    }

    // ===== UI HELPER METHODS =====

    public boolean isIconXHienThi() {
        try {
            WebDriverWait waitIcon = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Chờ modal Thêm hợp đồng hiển thị
            waitIcon.until(ExpectedConditions.visibilityOfElementLocated(By.id("contractModal")));

            // Icon X trong modal Thêm hợp đồng là span.close-btn
            By iconX = By.xpath("//div[@id='contractModal']//span[contains(@class,'close-btn') and normalize-space()='×']");

            WebElement x = waitIcon.until(
                    ExpectedConditions.visibilityOfElementLocated(iconX)
            );

            String onclick = x.getAttribute("onclick");

            return x.isDisplayed()
                    && onclick != null
                    && onclick.contains("closeModal");

        } catch (Exception e) {
            return false;
        }
    }


    public boolean isLabelsDayDu() {
        // ← kiểm tra tất cả label bắt buộc hiện diện trên form
        String[] labels = {
                "Mã hợp đồng", "Mã Phòng", "Trạng thái",
                "Khách thuê", "Tên người đại diện",
                "Ngày Bắt Đầu", "Ngày Kết Thúc",
                "Tiền cọc", "Điều khoản"
        };
        for (String label : labels) {
            By loc = By.xpath("//*[contains(normalize-space(text()),'" + label + "')]");
            if (driver.findElements(loc).isEmpty()) return false;
        }
        return true;
    }


    public boolean isMaHopDongEnabled() {
        // ← field mã hợp đồng — thay id nếu khác
        By inputMaHD = By.id("inpMaHD");
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(inputMaHD));
        return el.isEnabled() && el.getAttribute("readonly") == null
                && el.getAttribute("disabled") == null;
    }


    public boolean isDdlMaPhongEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(ddlMaPhong));
        return el.isEnabled();
    }


    public boolean isDdlMaPhongCoOptions() {
        Select select = new Select(driver.findElement(ddlMaPhong));
        return select.getOptions().size() > 1;
    }


    public boolean isBtnThemKTEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(btnThemKT));
        return el.isEnabled();
    }


    public boolean isTenKhachThueReadOnly() {
        By inputTenKT = By.xpath(
                "//input[contains(@id,'TenKhach') " +
                        "or contains(@name,'TenKhach') " +
                        "or contains(@placeholder,'khách thuê') " +
                        "or contains(@placeholder,'Khách thuê')]"
        );

        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(inputTenKT));

        String readonly = el.getAttribute("readonly");
        String disabled = el.getAttribute("disabled");

        return readonly != null || disabled != null || !el.isEnabled();
    }


    public boolean isNgayBatDauEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(ngayBatDau));
        return el.isEnabled();
    }


    public boolean isNgayKetThucEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(ngayKetThuc));
        return el.isEnabled();
    }


    public boolean isTienCocEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTienCoc));
        return el.isEnabled();
    }


    public boolean isDieuKhoanEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(inputDieuKhoan));
        return el.isEnabled();
    }


    public boolean isBtnLuuEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(btnLuu));
        return el.isEnabled();
    }


    public boolean isBtnHuyEnabled() {
        // ← dùng locator nút Hủy trên form chính (không phải trong confirmModal)
        By btnHuyFormCheck = By.xpath(
                "//button[contains(@class,'btn-cancel') or contains(.,'Hủy')]"
                        + "[not(ancestor::div[@id='confirmModal'])]"
        );
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(btnHuyFormCheck));
        return el.isEnabled();
    }


    public boolean isBtnThemHopDongEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(btnThemHopDong));
        return el.isEnabled();
    }


    public boolean isDdlNguoiDaiDienEnabled() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(ddlNguoiDaiDien));
        return el.isEnabled();
    }


    public boolean isDdlNguoiDaiDienCoOptions() {
        Select select = new Select(driver.findElement(ddlNguoiDaiDien));
        return select.getOptions().size() > 1;
    }

    // ================= FIX MAPPING CHO TEST =================

    // UI_01
    public boolean isIconXEnabled() {
        return isIconXHienThi();
    }


    // UI_02
    public boolean isLabelFormThemHopDongDisplayed() {
        String[] labels = {
                "Mã hợp đồng",
                "Mã phòng",
                "Trạng thái",
                "Khách thuê",
                "Tên người đại diện",
                "Ngày bắt đầu",
                "Ngày kết thúc",
                "Tiền cọc",
                "Điều khoản"
        };

        for (String label : labels) {
            By loc = By.xpath("//*[contains(translate(normalize-space(.), " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZĐÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ', " +
                    "'abcdefghijklmnopqrstuvwxyzđáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ'), " +
                    "'" + label.toLowerCase() + "')]");
            if (driver.findElements(loc).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // UI_03
    public boolean isMaHopDongDisabled() {
        return !isMaHopDongEnabled();
    }


    // UI_04
    public boolean isDropdownMaPhongEnabled() {
        return isDdlMaPhongEnabled();
    }


    // UI_13
    public boolean isDropdownNguoiDaiDienEnabled() {
        return isDdlNguoiDaiDienEnabled();
    }

    // UI_06
    public boolean isTenKhachThueKhongNhapTrucTiepDuoc(String value) {

        By inputTenKT = By.id("khachThueInput");

        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTenKT));

        String before = el.getAttribute("value");

        try {
            el.click();
            el.clear();
            el.sendKeys(value);
        } catch (Exception e) {
            return true;
        }

        String after = el.getAttribute("value");
        // nếu không đổi → đúng read-only
        return after == null || after.equals(before) || !after.contains(value);
    }

    public int getSoDongDangHienThi() {
        try {
            List<WebElement> rows = driver.findElements(rowsHopDong);
            int count = 0;

            for (WebElement row : rows) {
                try {
                    String text = row.getText().trim();

                    if (row.isDisplayed()
                            && !text.isEmpty()
                            && !text.contains("Không tìm thấy hợp đồng nào phù hợp")) {
                        count++;
                    }
                } catch (StaleElementReferenceException e) {
                    return -1; // bảng đang render lại, cho wait chạy lại
                }
            }

            return count;
        } catch (StaleElementReferenceException e) {
            return -1;
        }
    }

    public void choKetQuaTimKiemTheoMa(String maHD, int soDongMongDoi) {
        WebDriverWait waitSearch = new WebDriverWait(driver, Duration.ofSeconds(15));

        waitSearch.until(d -> {
            int soDong = getSoDongDangHienThi();

            if (soDong == -1) {
                return false; // bảng đang cập nhật, chờ tiếp
            }

            if (soDong != soDongMongDoi) {
                return false;
            }

            if (soDongMongDoi == 0) {
                return true;
            }

            return isMaHDHienThi(maHD);
        });
    }
}
