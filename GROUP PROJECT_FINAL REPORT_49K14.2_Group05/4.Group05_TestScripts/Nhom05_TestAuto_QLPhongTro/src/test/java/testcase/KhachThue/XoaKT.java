package testcase.KhachThue;

import common.BaseTest;
import common.Constant;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.KhachThuePage;
import pageobjects.LoginPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;

public class XoaKT extends BaseTest {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    // ===== DB helpers =====

    /**
     * TC1: Lấy khách thuê có hợp đồng trạng thái "Hết hợp đồng"
     * và KHÔNG có hợp đồng nào đang "Còn hợp đồng"
     * → backend cho phép xóa
     */
    private String getTenantWithExpiredContract() throws Exception {
        String sql = "SELECT DISTINCT hd.ma_khach_thue FROM hop_dong hd " +
                "WHERE hd.trang_thai = 'Hết hợp đồng' " +
                "AND hd.ma_khach_thue NOT IN ( " +
                "    SELECT hd2.ma_khach_thue FROM hop_dong hd2 " +
                "    WHERE hd2.trang_thai = 'Còn hợp đồng' " +
                ") LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString("ma_khach_thue");
        }
        return null;
    }

    /**
     * TC2: Lấy khách thuê đang có hợp đồng "Còn hợp đồng"
     * → backend chặn xóa
     */
    private String getTenantWithActiveContract() throws Exception {
        String sql = "SELECT DISTINCT ma_khach_thue FROM hop_dong " +
                "WHERE trang_thai = 'Còn hợp đồng' LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString("ma_khach_thue");
        }
        return null;
    }

    // ===== Đăng nhập & mở trang =====

    private KhachThuePage loginAndOpenTenantPage() {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(Constant.LONG_TIMEOUT));

        LoginPage loginPage = new LoginPage();
        KhachThuePage page = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // Mở trang khách thuê
        page.open();

        // Chờ bảng dữ liệu load xong
        page.waitForTableLoaded(wait);

        return page;
    }

    // =========================================================
    // TC1: Xóa khách thuê có hợp đồng "Hết hợp đồng" → thành công
    // =========================================================
    @Test(priority = 1, description = "Xóa khách thuê thành công")
    public void XoaKT_TC1() throws Exception {
        String tenantCode = getTenantWithExpiredContract();
        Assert.assertNotNull(tenantCode,
                "Không tìm thấy khách thuê nào có hợp đồng 'Hết hợp đồng' trong DB.");

        WebDriverWait waitLong = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(20));

        KhachThuePage page = loginAndOpenTenantPage();

        page.openDeleteModal(tenantCode);
        page.clickConfirmDelete();

        // Nếu modal không đóng → xóa thất bại, in lỗi
        boolean closed = page.waitForDeleteModalClosed(waitLong);
        if (!closed) {
            page.takeScreenshot("TC1_Fail_ModalStayOpen");
            Assert.fail("Modal xóa không đóng sau khi bấm Đồng ý. Lỗi: " + page.getDeleteErrorMessage());
        }

        page.waitForStatusModalVisible(waitLong);

        Assert.assertEquals(
                page.getStatusMessage(),
                "Xóa khách thuê thành công.",
                "Thông báo xóa khách thuê không đúng.");
    }

    // =========================================================
    // TC2: Xóa khách thuê có hợp đồng "Còn hợp đồng" → thất bại
    // =========================================================
    @Test(priority = 2, description = "Xóa khách thuê không thành công")
    public void XoaKT_TC2() throws Exception {
        String tenantCode = getTenantWithActiveContract();
        Assert.assertNotNull(tenantCode,
                "Không tìm thấy khách thuê nào đang có hợp đồng 'Còn hợp đồng' trong DB.");

        WebDriverWait waitLong = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(20));

        KhachThuePage page = loginAndOpenTenantPage();

        page.openDeleteModal(tenantCode);
        page.clickConfirmDelete();

        // Backend trả lỗi → errDelete có text, modal vẫn mở
        page.waitForDeleteErrorVisible(waitLong);

        Assert.assertTrue(page.isDeleteModalDisplayed(),
                "Modal xác nhận xóa không còn hiển thị.");
        Assert.assertEquals(
                page.getDeleteErrorMessage(),
                "Không thể xóa vì khách thuê đang còn hợp đồng thuê.",
                "Thông báo lỗi không đúng.");
    }

    // =========================================================
    // TC3: Mở modal xóa rồi bấm Hủy → modal đóng, danh sách còn
    // =========================================================
    @Test(priority = 3, description = "Hủy xóa khách thuê")
    public void XoaKT_TC3() {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(Constant.LONG_TIMEOUT));

        KhachThuePage page = loginAndOpenTenantPage();

        page.openDeleteModalFirstRow();
        page.clickCancelDelete();
        page.waitForDeleteModalClosed(wait);

        Assert.assertTrue(page.isBtnThemEnabled(),
                "Không quay lại màn hình danh sách khách thuê.");
    }

    // =========================================================
    // TC4 (UI): Nút "Đồng ý" trong modal xóa có enable
    // =========================================================
    @Test(priority = 4, description = "XoaKT_UI_01: Kiểm tra button Đồng ý trong modal xóa có enable")
    public void XoaKT_UI_01() {
        KhachThuePage page = loginAndOpenTenantPage();
        page.openDeleteModalFirstRow();

        Assert.assertTrue(page.isConfirmDeleteButtonEnabled(),
                "Button 'Đồng ý' không ở trạng thái enable.");
    }

    // =========================================================
    // TC5 (UI): Nút "Hủy" trong modal xóa có enable
    // =========================================================
    @Test(priority = 5, description = "XoaKT_UI_02: Kiểm tra button Hủy trong modal xóa có enable")
    public void XoaKT_UI_02() {
        KhachThuePage page = loginAndOpenTenantPage();
        page.openDeleteModalFirstRow();

        Assert.assertTrue(page.isCancelDeleteButtonEnabled(),
                "Button 'Hủy' không ở trạng thái enable.");
    }
}
