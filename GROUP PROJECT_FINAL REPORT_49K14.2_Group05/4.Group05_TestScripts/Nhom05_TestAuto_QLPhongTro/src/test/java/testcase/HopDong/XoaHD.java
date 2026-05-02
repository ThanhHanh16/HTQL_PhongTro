package testcase.HopDong;

import common.BaseTest;
import common.Constant;
import common.Utilities;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.HopDongPage;
import pageobjects.LoginPage;


public class XoaHD extends BaseTest {

    @Test(description = "Xoa hop dong thanh cong")
    public void XoaHD_TC_01() {
        LoginPage loginPage = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Dam bao co hop dong trong danh sach
        prepareData(hopDongPage);

        // 1. Click nut xoa dong dau tien
        hopDongPage.clickDeleteFirstRow();

        // 2. Click "Dong y" tren popup xac nhan
        hopDongPage.clickAgreeDelete();

        // Kiem tra: Thong bao xoa thanh cong
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        if (hopDongPage.waitForStatusModalWithTimeout(5)) {
            Assert.assertEquals(
                    hopDongPage.getStatusMessage(),
                    "Xóa hợp đồng thành công.",
                    "Thong bao khong dung!"
            );
        } else {
            Assert.fail("Status modal không xuất hiện sau khi xóa");
        }
    }


    @Test(description = "Huy thao tac xoa hop dong")
    public void XoaHD_TC_02() {
        LoginPage loginPage = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Dam bao co hop dong trong danh sach
        prepareData(hopDongPage);
        int countBefore = hopDongPage.getSoDongHienThi();

        // 1. Click nut xoa dong dau tien
        hopDongPage.clickDeleteFirstRow();

        // 2. Click "Huy" tren popup xac nhan
        hopDongPage.clickCancelDelete();

        // Kiem tra: Quay lai danh sach va so luong hop dong khong doi
        Assert.assertTrue(hopDongPage.isContractListDisplayed(),
                "Khong quay lai man hinh danh sach hop dong.");
        Assert.assertEquals(hopDongPage.getSoDongHienThi(), countBefore,
                "Hop dong da bi xoa du da chon Huy!");
    }


    // ==========================================
    // ===== GUI TEST =====
    // ==========================================

    @Test(description = "XoaHD_UI_01: Kiem tra button Dong y trong modal xoa co enable")
    public void XoaHD_UI_01() {
        LoginPage loginPage = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Chờ danh sách hợp đồng hiển thị
        hopDongPage.waitForContractListDisplayed();

        // Mở modal xóa
        hopDongPage.openDeleteModal();

        // Kiểm tra nút Đồng ý có enable không
        Assert.assertTrue(hopDongPage.isConfirmDeleteButtonEnabled(),
                "Button 'Đồng ý' không ở trạng thái enable.");
    }


    @Test(description = "XoaHD_UI_02: Kiem tra button Huy trong modal xoa co enable")
    public void XoaHD_UI_02() {
        LoginPage loginPage = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();

        // Chờ danh sách hợp đồng hiển thị
        hopDongPage.waitForContractListDisplayed();

        // Mở modal xóa
        hopDongPage.openDeleteModal();

        // Kiểm tra nút Hủy có enable không
        Assert.assertTrue(hopDongPage.isCancelDeleteButtonEnabled(),
                "Button 'Hủy' không ở trạng thái enable.");
    }


    // ===== Helper Methods =====

    /**
     * Chuẩn bị dữ liệu: Tạo một hợp đồng mới để phục vụ cho test xóa.
     */
    private void prepareData(HopDongPage hopDongPage) {
        try {
            hopDongPage.clickThemHopDong();
            hopDongPage.selectRandomMaPhong();
            hopDongPage.selectRandomKhachThue();
            hopDongPage.selectRandomNguoiDaiDien();
            hopDongPage.setNgayBatDau(Utilities.today());
            hopDongPage.setNgayKetThuc(Utilities.endDate());
            hopDongPage.setTienCoc("5000000");
            hopDongPage.setDieuKhoan("Khong co dieu khoan dac biet.");
            hopDongPage.clickLuu();

            // Thêm delay để form xử lý
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

            hopDongPage.closeStatusModal();
        } catch (Exception e) {
            System.out.println("❌ prepareData failed: " + e.getMessage());
            throw e;
        }
    }
}
