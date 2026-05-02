package testcase.KhachThue;

import common.BaseTest;
import common.Constant;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.KhachThuePage;
import pageobjects.LoginPage;

public class TimKT extends BaseTest {

    // Dữ liệu test dựa theo hình ảnh giao diện "Quản lý khách thuê"
    private static final String TEN_TON_TAI       = "Nguyễn Văn A";
    private static final String TEN_KHONG_TON_TAI = "Nguyễn Văn Khách Lạ";

    // ════════════════════════════════════════════════════════════
    // TimKhach_TC_01
    // Tìm kiếm tên khách thuê chính xác
    // Input  : Tên khách thuê tồn tại (VD: Nguyễn Văn A)
    // Expect : Danh sách khách thuê tự động lọc và chỉ hiển thị đúng khách thuê có tên tương ứng
    // ════════════════════════════════════════════════════════════
    @Test(description = " Tìm kiếm tên khách thuê chính xác")
    public void TimKT_TC_01() {
        LoginPage loginPage         = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập (Pre-condition: Chủ trọ đã đăng nhập)
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê (Trigger: Chủ trọ chọn chức năng "Tìm khách thuê")
        khachThuePage.open();

        // 4. Lấy số lượng dòng dữ liệu trước khi tìm kiếm để so sánh (tuỳ chọn)
        int initialRowCount = khachThuePage.getSoDongHienThi();

        // 5. Nhập tên khách thuê vào ô "Tìm kiếm theo tên khách thuê"
        khachThuePage.nhapTimKiem(TEN_TON_TAI);

        // 6. Kiểm tra xem sau khi tìm kiếm, số dòng có giảm xuống/được lọc hay không
        int filteredRowCount = khachThuePage.getSoDongHienThi();
        Assert.assertTrue(
                filteredRowCount > 0 && filteredRowCount <= initialRowCount,
                "Danh sách phải hiển thị ít nhất 1 kết quả và nhỏ hơn hoặc bằng tổng số dòng ban đầu."
        );

        // 7. Kiểm tra tất cả các dòng hiển thị đều chứa tên khách thuê vừa tìm
        Assert.assertTrue(
                khachThuePage.isTenKTHienThiTrongTatCaKetQua(TEN_TON_TAI),
                "Tất cả các kết quả hiển thị phải chứa tên khách thuê: [" + TEN_TON_TAI + "]."
        );
    }

    // ════════════════════════════════════════════════════════════
    // TimKhach_TC_02
    // Tìm kiếm tên khách thuê KHÔNG tồn tại
    // Input  : Tên khách thuê không tồn tại
    // Expect : Danh sách khách thuê trống hoặc hiển thị thông báo "Khách thuê không tồn tại"
    // ════════════════════════════════════════════════════════════
    @Test(description = " Tìm kiếm tên khách thuê KHÔNG tồn tại")
    public void TimKT_TC_02() {
        LoginPage loginPage         = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Nhập tên khách thuê không tồn tại vào ô "Tìm kiếm theo tên khách thuê"
        khachThuePage.nhapTimKiem(TEN_KHONG_TON_TAI);

        // 5. Kiểm tra bảng danh sách khách thuê trống
        Assert.assertEquals(
                khachThuePage.getSoDongHienThi(),
                0,
                "Bảng dữ liệu khách thuê phải trống khi tìm tên không tồn tại."
        );

        // 6. Kiểm tra hiển thị thông báo "Khách thuê không tồn tại" (Dựa theo Exception flow 2a)
        Assert.assertTrue(
                khachThuePage.isThongBaoKhongTimThayHienThi(),
                "Phải hiển thị thông báo lỗi khi không tìm thấy dữ liệu."
        );

        // Kiểm tra chính xác nội dung câu thông báo theo Use Case
        Assert.assertEquals(
                khachThuePage.getThongBaoKhongTimThay(),
                "Khách thuê không tồn tại",
                "Nội dung thông báo lỗi không khớp với Use Case."
        );
    }

    @Test(description = "KHACH_TC_01 - Hiển thị khởi tạo trang Khách thuê")
    public void KHACH_TC_01() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        khachThuePage.openByMenu();

        Assert.assertTrue(
                khachThuePage.isTrangKhachThueHienThi(),
                "Trang Khách thuê phải được hiển thị sau khi click menu Khách Thuê."
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_TC_02
    // Thêm mới khách thuê (Check điều hướng mở form)
    // Hành động: Click vào nút "Thêm khách thuê"
    // Expect : Hệ thống chuyển hướng người dùng đến màn hình popup "Thêm khách thuê"
    // ════════════════════════════════════════════════════════════
    @Test(description = " Check điều hướng mở form Thêm khách thuê")
    public void KHACH_TC_02() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        // 1. Mở trang và đăng nhập
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 2. Điều hướng đến trang quản lý Khách Thuê
        khachThuePage.open();

        // 3. Click vào nút "Thêm khách thuê"
        khachThuePage.clickOpenAddButton();

        // 4. Chỉ kiểm tra xem popup "Thêm khách thuê" có hiển thị hay không đúng như file Excel
        Assert.assertTrue(
                khachThuePage.isAddModalDisplayed(),
                "LỖI: Popup 'Thêm khách thuê' không hiển thị sau khi click nút Thêm!"
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_TC_03
    // ════════════════════════════════════════════════════════════
    @Test(description = " Check điều hướng mở form Chỉnh sửa")
    public void KHACH_TC_03() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        // 1. Click vào icon "Chỉnh sửa"
        khachThuePage.clickIconSuaDauTien();

        // 2. Kiểm tra hệ thống có chuyển hướng mở popup "Chỉnh sửa" hay không
        Assert.assertTrue(
                khachThuePage.isEditModalDisplayed(),
                "LỖI: Popup 'Chỉnh sửa khách thuê' (modal-form) không hiển thị!"
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_TC_04
    // ════════════════════════════════════════════════════════════
    @Test(description = " Check điều hướng mở form Xóa")
    public void KHACH_TC_04() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        // 1. Click vào icon "Xóa"
        khachThuePage.clickIconXoaDauTien();

        // 2. Kiểm tra hệ thống có hiển thị popup xác nhận xóa (modal-delete) hay không
        Assert.assertTrue(
                khachThuePage.isDeleteModalDisplayed(),
                "LỖI: Popup xác nhận xóa (modal-delete) không hiển thị!"
        );
    }


    // =========================================================================
    // ======================== NHÓM TEST CASE GUI =============================
    // =========================================================================

    // ════════════════════════════════════════════════════════════
    // KHACH_UI_01: Kiểm tra bảng Khách thuê
    // ════════════════════════════════════════════════════════════
    @Test(description = " Kiểm tra bảng Khách thuê")
    public void KHACH_UI_01() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        Assert.assertTrue(
                khachThuePage.isTableEnabled(),
                "LỖI: Bảng Khách thuê đang bị disable (không thể tương tác)."
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_UI_02: Kiểm tra ô nhập liệu tìm kiếm
    // ════════════════════════════════════════════════════════════
    @Test(description = " Kiểm tra ô nhập liệu tìm kiếm")
    public void KHACH_UI_02() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        Assert.assertTrue(
                khachThuePage.isTxtTimKiemEnabled(),
                "LỖI: Ô nhập liệu tìm kiếm đang bị disable (không thể nhập liệu)."
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_UI_03: Kiểm tra nút Thêm
    // ════════════════════════════════════════════════════════════
    @Test(description = " Kiểm tra nút Thêm")
    public void KHACH_UI_03() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        Assert.assertTrue(
                khachThuePage.isBtnThemEnabled(),
                "LỖI: Nút Thêm khách thuê đang bị disable."
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_UI_04: Kiểm tra icon Chỉnh sửa
    // ════════════════════════════════════════════════════════════
    @Test(description = " Kiểm tra icon Chỉnh sửa")
    public void KHACH_UI_04() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        Assert.assertTrue(
                khachThuePage.isIconSuaEnabled(),
                "LỖI: Icon Chỉnh sửa đang bị disable."
        );
    }

    // ════════════════════════════════════════════════════════════
    // KHACH_UI_05: Kiểm tra icon Xóa
    // ════════════════════════════════════════════════════════════
    @Test(description = " Kiểm tra icon Xóa")
    public void KHACH_UI_05() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        Assert.assertTrue(
                khachThuePage.isIconXoaEnabled(),
                "LỖI: Icon Xóa đang bị disable."
        );
    }
}