package testcase.KhachThue;

import common.BaseTest;
import common.Constant;
import dataobjects.TenantData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.KhachThuePage;
import pageobjects.LoginPage;
import org.openqa.selenium.Keys;

import java.time.Duration;

public class SuaKT extends BaseTest {
    private KhachThuePage openEditTenantForm() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();
        khachThuePage.clickIconSuaDauTien();

        Assert.assertTrue(khachThuePage.isEditModalDisplayed());

        return khachThuePage;
    }

    private String convertDateToHtmlFormat(String date) {
        String[] parts = date.split("/");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
    @Test
    public void SuaKT_TC_01() {
        KhachThuePage khachThuePage = openEditTenantForm();

        String updatePhone = TenantData.getRandomPhone();
        String updateCCCD = TenantData.getRandomCCCD();

        // 5. Chỉnh sửa thông tin hợp lệ
        khachThuePage.enterHoTen(TenantData.UPDATE_NAME);
        khachThuePage.selectGioiTinh(TenantData.UPDATE_GENDER);
        khachThuePage.enterNgaySinh(TenantData.UPDATE_DOB);
        khachThuePage.enterSdt(updatePhone);
        khachThuePage.enterCccd(updateCCCD);
        khachThuePage.enterQueQuan(TenantData.UPDATE_ADDRESS);

        khachThuePage.clickSave();

        // 7. Kiểm tra thông báo cập nhật thành công
        Assert.assertEquals(
                khachThuePage.getStatusMessage(),
                "Cập nhật thông tin khách thuê thành công"
        );
    }

    @Test
    public void SuaKT_TC_02() {
        KhachThuePage khachThuePage =  openEditTenantForm();

        // 5. Để trống Tên khách thuê
        khachThuePage.enterHoTen(TenantData.EMPTY_NAME);

        // 6. Click nút Lưu
        khachThuePage.clickSave();

        // 7. Kiểm tra thông báo lỗi
        Assert.assertEquals(
            khachThuePage.getHoTenErrorMessage(),
                "Tên khách thuê trống. Vui lòng nhập tên khách thuê."
        );
    }

    @Test
    public void SuaKT_TC_03() {

        KhachThuePage khachThuePage = openEditTenantForm();

        // 1. Để trống SĐT
        khachThuePage.enterSdt("");
        // 2. Click nút Lưu
        khachThuePage.clickSave();

        // 3. Kiểm tra thông báo lỗi

        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại trống. Vui lòng nhập số điện thoại."
        );
    }
    @Test
    public void SuaKT_TC_04() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Khai báo dữ liệu test trực tiếp trong test case
        String invalidPhone = "012345678";

        // Click nút Sửa
        // Cập nhật thông tin mới: Nhập SĐT ít hơn 10 số
        khachThuePage.enterSdt(invalidPhone);

        // Click nút Lưu
        khachThuePage.clickSave();

        // Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số."
        );
    }
    @Test
    public void SuaKT_TC_05() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Khai báo dữ liệu test trực tiếp trong test case
        String invalidPhone = "012345678901";

        // Cập nhật thông tin mới: Nhập SĐT nhiều hơn 10 số
        khachThuePage.enterSdt(invalidPhone);

        // Click nút Lưu
        khachThuePage.clickSave();

        // Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số."
        );
    }
    @Test
    public void SuaKT_TC_06() {
         
        KhachThuePage khachThuePage = openEditTenantForm();
        // Khai báo dữ liệu test trực tiếp trong test case
        String invalidPhone = "1987654321";

        // Nhập SĐT sai định dạng
        khachThuePage.enterSdt(invalidPhone);
        khachThuePage.clickSave();

        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số."
        );
    }
    @Test
    public void SuaKT_TC_07() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Khai báo dữ liệu test trực tiếp trong test case
        String[] invalidPhones = {
                "01234abcde",
                "01234@6789"};
        for (String invalidPhone : invalidPhones) {
            // 2. Cập nhật thông tin mới: Nhập SĐT chứa ký tự không phải số
            khachThuePage.enterSdt(invalidPhone);
            // 3. Click nút Lưu
            khachThuePage.clickSave();

            // Kiểm tra thông báo lỗi
            Assert.assertEquals(
                    khachThuePage.getSdtErrorMessage(),
                    "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số."
            );
        }
    }
    @Test(description = " Sửa khách thuê không thành công. Để trống CCCD")
    public void SuaKT_TC_08() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Để trống CCCD
        khachThuePage.enterCccd("");
        khachThuePage.clickSave();

        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD trống. Vui lòng nhập số CCCD."
        );
    }
    @Test
    public void SuaKT_TC_09() {
        KhachThuePage khachThuePage = openEditTenantForm();

        String invalidCCCD = "12345678901";
        khachThuePage.enterCccd(invalidCCCD);
        khachThuePage.clickSave();

        // Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD không hợp lệ."
        );
    }
    @Test
    public void SuaKT_TC_10() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Khai báo dữ liệu test trực tiếp trong test case
        String invalidCCCD = "1234567890123";

        khachThuePage.enterCccd(invalidCCCD);
        khachThuePage.clickSave();

        // Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD không hợp lệ."
        );
    }
    @Test
    public void SuaKT_TC_11() {
        KhachThuePage khachThuePage = openEditTenantForm();

        // Khai báo dữ liệu test trực tiếp trong test case
        String[] invalidCCCDs = {
                "12345abc9012",
                "12345@789012"
        };

        for (String invalidCCCD : invalidCCCDs) {
            khachThuePage.enterCccd(invalidCCCD);
            khachThuePage.clickSave();

            // Kiểm tra thông báo lỗi
            Assert.assertEquals(
                    khachThuePage.getCccdErrorMessage(),
                    "Số CCCD không hợp lệ."
            );
        }
    }

    @Test
    public void SuaKT_TC_12() {
        KhachThuePage khachThuePage = openEditTenantForm();
        // Khai báo dữ liệu test trực tiếp trong test case
        String existedCCCD = "098765432112";

        khachThuePage.enterCccd(existedCCCD);
        khachThuePage.clickSave();

        // 7. Kiểm tra thông báo lỗi
        Assert.assertEquals(
            khachThuePage.getCccdErrorMessage(),
                "Số CCCD đã tồn tại. Vui lòng kiểm tra lại."
        );
    }
    @Test(description = " Sửa khách thuê không thành công. Để trống quê quán")
    public void SuaKT_TC_13() {
        KhachThuePage khachThuePage = openEditTenantForm();
        // 5. Để trống quê quán
        khachThuePage.enterQueQuan("");
        khachThuePage.clickSave();

        // 7. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getQueQuanErrorMessage(),
                "Quê quán trống. Vui lòng nhập quê quán."
        );
    }
    @Test
    public void SuaKT_TC_14() {
        KhachThuePage khachThuePage = openEditTenantForm();
        // Khai báo dữ liệu sửa thử
        khachThuePage.enterHoTen("Dữ liệu sửa thử");
        khachThuePage.clickCancel();

        Assert.assertTrue(
                khachThuePage.isCancelConfirmModalDisplayed(),
                "Popup xác nhận hủy không hiển thị."
        );

        khachThuePage.clickConfirmCancel();

        Assert.assertTrue(
                khachThuePage.isEditModalClosed(),
                "Form sửa vẫn hiển thị sau khi xác nhận hủy."
        );
    }
    @Test
    public void SuaKT_TC_15() {
        KhachThuePage khachThuePage = openEditTenantForm();

        String testName = "Dữ liệu sửa thử";

        khachThuePage.enterHoTen(testName);
        khachThuePage.clickCancel();

        Assert.assertTrue(
                khachThuePage.isCancelConfirmModalDisplayed(),
                "Popup xác nhận hủy không hiển thị."
        );

        khachThuePage.clickCloseCancelPopup();

        Assert.assertTrue(
                khachThuePage.isEditModalDisplayed(),
                "Form sửa không hiển thị lại sau khi hủy thao tác."
        );

        Assert.assertEquals(
                khachThuePage.getHoTenValue(),
                testName,
                "Dữ liệu đã nhập không được giữ lại."
        );
    }

    @Test
    public void SuaKT_TC_16() {
        KhachThuePage khachThuePage = openEditTenantForm();

        khachThuePage.enterNgaySinh("");
        khachThuePage.clickSave();

        Assert.assertEquals(
                khachThuePage.getNgaySinhErrorMessage(),
                "Ngày sinh trống. Vui lòng nhập ngày sinh."
        );
    }
    @Test(description = "Sửa khách thuê không thành công. Ngày sinh lớn hơn ngày hiện tại")
    public void SuaKT_TC_17() {
        KhachThuePage khachThuePage = openEditTenantForm();

        khachThuePage.enterNgaySinh("01/01/2027");
        khachThuePage.clickSave();

        Assert.assertEquals(
                khachThuePage.getNgaySinhErrorMessage(),
                "Ngày sinh không hợp lệ. Vui lòng nhập lại.",
                "Thông báo lỗi không đúng khi ngày sinh lớn hơn ngày hiện tại."
        );
    }
    @Test(description = " Sửa khách thuê không thành công. Để trống giới tính")
    public void SuaKT_TC_18() {
        KhachThuePage khachThuePage = openEditTenantForm();

        khachThuePage.selectGioiTinh("-- Chọn --");
        khachThuePage.clickSave();

        Assert.assertEquals(
                khachThuePage.getGioiTinhErrorMessage(),
                "Giới tính trống. Vui lòng chọn giới tính.",
                "Thông báo lỗi không đúng khi để trống giới tính."
        );
    }
    @Test(description = "SuaKT_UI_01: [X] icon")
    public void SuaKT_UI_01() {
        KhachThuePage khachThuePage = openEditTenantForm();

        Assert.assertTrue(khachThuePage.isCloseIconEnabled());
    }
    @Test(description = "SuaKT_UI_02: [Mã khách thuê, Tên khách thuê, Giới tính, Số điện thoại, Ngày sinh, Số CCCD, Quê quán] Label")
    public void SuaKT_UI_02() {
        KhachThuePage khachThuePage = openEditTenantForm();

        Assert.assertTrue(khachThuePage.isAllLabelsDisplayed());
    }
    @Test(description = "SuaKT_UI_03: [Mã khách thuê] Textbox")
    public void SuaKT_UI_03() {
        KhachThuePage khachThuePage = openEditTenantForm();

        Assert.assertFalse(khachThuePage.isMaKhachThueEnabled());
    }
    @Test(description = "SuaKT_UI_04: [Tên khách thuê, Số điện thoại, Số CCCD, Quê quán] Textbox")
    public void SuaKT_UI_04() {
        KhachThuePage khachThuePage = openEditTenantForm();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        khachThuePage.enterHoTen(TenantData.UPDATE_NAME);
        khachThuePage.enterSdt(phone);
        khachThuePage.enterCccd(cccd);
        khachThuePage.enterQueQuan(TenantData.UPDATE_ADDRESS);

        Assert.assertEquals(khachThuePage.getHoTenValue(), TenantData.UPDATE_NAME);
        Assert.assertEquals(khachThuePage.getSdtValue(), phone);
        Assert.assertEquals(khachThuePage.getCccdValue(), cccd);
        Assert.assertEquals(khachThuePage.getQueQuanValue(), TenantData.UPDATE_ADDRESS);
    }
    @Test(description = "SuaKT_UI_05: [Giới tính] Dropdown")
    public void SuaKT_UI_05() {
        KhachThuePage khachThuePage = openEditTenantForm();

        khachThuePage.selectGioiTinh(TenantData.UPDATE_GENDER);

        Assert.assertEquals(khachThuePage.getGioiTinhValue(), TenantData.UPDATE_GENDER);
    }
    @Test(description = "SuaKT_UI_06: [Ngày sinh] Date Picker")
    public void SuaKT_UI_06() {
        KhachThuePage khachThuePage = openEditTenantForm();

        khachThuePage.enterNgaySinh(TenantData.UPDATE_DOB);

        Assert.assertEquals(
                khachThuePage.getNgaySinhValue(),
                convertDateToHtmlFormat(TenantData.UPDATE_DOB)
        );
    }
    @Test(description = "SuaKT_UI_07: [Hủy] Button")
    public void SuaKT_UI_07() {
        KhachThuePage khachThuePage = openEditTenantForm();

        Assert.assertTrue(khachThuePage.isCancelButtonEnabled());
    }
    @Test(description = "SuaKT_UI_08: [Lưu] Button")
    public void SuaKT_UI_08() {
        KhachThuePage khachThuePage = openEditTenantForm();

        Assert.assertTrue(khachThuePage.isSaveButtonEnabled());
    }
}