package testcase.KhachThue;

import common.BaseTest;
import common.Constant;
import dataobjects.TenantData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.KhachThuePage;
import pageobjects.LoginPage;

public class ThemKT extends BaseTest {

    @Test(description = " Thêm khách thuê thành công")
    public void ThemKT_TC_01() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                phone,
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra popup thông báo
        Assert.assertTrue(
                khachThuePage.isStatusModalDisplayed(),
                "Popup thông báo không hiển thị."
        );

        Assert.assertEquals(
                khachThuePage.getStatusMessage(),
                "Thêm khách thuê thành công",
                "Nội dung thông báo không đúng."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. Để trống tên khách thuê")
    public void ThemKT_TC_02() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống tên
        khachThuePage.addTenant(
                "", // bỏ trống tên
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                phone,
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getHoTenErrorMessage(),
                "Tên khách thuê trống. Vui lòng nhập tên khách thuê.",
                "Thông báo lỗi không đúng khi để trống tên khách thuê."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. Để trống giới tính")
    public void ThemKT_TC_03() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống giới tính
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                "", // bỏ trống giới tính
                TenantData.VALID_DOB,
                phone,
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getGioiTinhErrorMessage(),
                "Giới tính trống. Vui lòng chọn giới tính.",
                "Thông báo lỗi không đúng khi để trống giới tính."
        );
    }
    @Test(description = " Thêm khách thuê không thành công. Để trống số điện thoại")
    public void ThemKT_TC_04() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống số điện thoại
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                "", // bỏ trống SĐT
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại trống. Vui lòng nhập số điện thoại.",
                "Thông báo lỗi không đúng khi để trống số điện thoại."
        );
    }
//    sdt it hon 10 so
    @Test(description = " Thêm khách thuê không thành công. Số điện thoại ít hơn 10 số")
    public void ThemKT_TC_05() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với SĐT ít hơn 10 số
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                "012345678", // SĐT ít hơn 10 số
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số.",
                "Thông báo lỗi không đúng khi SĐT ít hơn 10 số."
        );
    }
//    nhieu hon 10 so
    @Test(description = " Thêm khách thuê không thành công. Số điện thoại nhiều hơn 10 số")
    public void ThemKT_TC_06() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với SĐT nhiều hơn 10 số
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                "012345678789", // SĐT nhiều hơn 10 số
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số.",
                "Thông báo lỗi không đúng khi SĐT nhiều hơn 10 số."
        );
    }
//    sdt ko bat dau bằng số 0
    @Test (description = " Thêm khách thuê không thành công. Số điện thoại không bắt đâ từ số 0")
    public void ThemKT_TC_07() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với SĐT không bắt đầu bằng 0
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                "1234567898", // không bắt đầu bằng 0
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getSdtErrorMessage(),
                "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số.",
                "Thông báo lỗi không đúng khi SĐT không bắt đầu bằng 0."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. Số điện thoại chứ chữ hoạc kí tự đặc biệt")
    public void ThemKT_TC_08() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String[] invalidPhones = {
                "01234abcde", // SĐT chứa chữ
                "01234@6789"  // SĐT chứa ký tự đặc biệt
        };

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        // Chỉ mở form 1 lần
        khachThuePage.clickOpenAddButton();

        for (String phone : invalidPhones) {
            String cccd = TenantData.getRandomCCCD();

            // Chỉ nhập dữ liệu, không mở form lại
            khachThuePage.enterTenantInfo(
                    TenantData.VALID_NAME,
                    TenantData.VALID_GENDER,
                    TenantData.VALID_DOB,
                    phone,
                    cccd,
                    TenantData.VALID_ADDRESS
            );

            khachThuePage.clickSave();

            Assert.assertEquals(
                    khachThuePage.getSdtErrorMessage(),
                    "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng số 0 và chỉ chứa số.",
                    "Thông báo lỗi không đúng khi SĐT chứa ký tự không phải số: " + phone
            );

            khachThuePage.clearTenantForm();
        }
    }

    @Test(description = " Thêm khách thuê không thành công. Bỏ trống CCCD")
    public void ThemKT_TC_09() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống CCCD
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                phone,
                "", // bỏ trống CCCD
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD trống. Vui lòng nhập số CCCD.",
                "Thông báo lỗi không đúng khi để trống CCCD."
        );
    }
    // CCCD it hon 12 so
    @Test(description = " Thêm khách thuê không thành công. CCCD ít hơn 12 số")
    public void ThemKT_TC_10() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với CCCD ít hơn 12 số
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                TenantData.getRandomPhone(),
                "12345678901", // CCCD ít hơn 12 số
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD không hợp lệ.",
                "Thông báo lỗi không đúng khi CCCD ít hơn 12 số."
        );
    }

    //CCCD NHIEU HON 12 SO
    @Test(description = " Thêm khách thuê không thành công. CCCD nhiều hơn 12 số")
    public void ThemKT_TC_11() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với CCCD nhiều hơn 12 số
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                TenantData.getRandomPhone(),
                "1234567890123", // CCCD nhiều hơn 12 số
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD không hợp lệ.",
                "Thông báo lỗi không đúng khi CCCD nhiều hơn 12 số."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. CCCD chứa chữ hoạc ký tự đặc biệt")
    public void ThemKT_TC_12() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String[] invalidCccds = {
                "12345abc9012", // CCCD chứa chữ cái
                "12345@789012"  // CCCD chứa ký tự đặc biệt
        };

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Chỉ mở form thêm khách thuê 1 lần
        khachThuePage.clickOpenAddButton();

        for (String cccd : invalidCccds) {
            // 5. Nhập dữ liệu khách thuê với CCCD không hợp lệ
            khachThuePage.enterTenantInfo(
                    TenantData.VALID_NAME,
                    TenantData.VALID_GENDER,
                    TenantData.VALID_DOB,
                    TenantData.getRandomPhone(),
                    cccd,
                    TenantData.VALID_ADDRESS
            );

            // 6. Nhấn Lưu
            khachThuePage.clickSave();

            // 7. Kiểm tra thông báo lỗi
            Assert.assertEquals(
                    khachThuePage.getCccdErrorMessage(),
                    "Số CCCD không hợp lệ.",
                    "Thông báo lỗi không đúng khi CCCD chứa chữ cái hoặc ký tự đặc biệt: " + cccd
            );

            // 8. Xóa form để nhập dữ liệu CCCD sai tiếp theo
            khachThuePage.clearTenantForm();
        }
    }


    @Test(description = " Thêm khách thuê không thành công. CCCD bị trùng lặp")
    public void ThemKT_TC_13() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone1 = TenantData.getRandomPhone();
        String phone2 = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        khachThuePage.open();

        // B1 Thêm lần 1
        khachThuePage.addTenant(
                "Nguyễn Văn A",
                "Nam",
                "01/01/2000",
                phone1,
                cccd,
                "Đà Nẵng"
        );

        Thread.sleep(1000);

        // Refresh để đóng popup thành công lần 1
        Constant.WEBDRIVER.navigate().refresh();

        Thread.sleep(1000);

        // B2 Thêm lần 2 với CCCD trùng
        khachThuePage.addTenant(
                "Nguyễn Văn B",
                "Nam",
                "01/01/2000",
                phone2,
                cccd,
                "Đà Nẵng"
        );

        Assert.assertEquals(
                khachThuePage.getCccdErrorMessage(),
                "Số CCCD đã tồn tại. Vui lòng kiểm tra lại.",
                "Thông báo lỗi không đúng khi CCCD trùng."
        );
    }
    @Test(description = " Thêm khách thuê không thành công. Để trống quê quán")
    public void ThemKT_TC_14() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống quê quán
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                phone,
                cccd,
                "" // Quê quán rỗng
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getQueQuanErrorMessage(),
                "Quê quán trống. Vui lòng nhập quê quán.",
                "Thông báo lỗi không đúng khi để trống quê quán."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. Để trống ngày sinh")
    public void ThemKT_TC_15() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê nhưng để trống ngày sinh
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                "", // bỏ trống ngày sinh
                phone,
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getNgaySinhErrorMessage(),
                "Ngày sinh trống. Vui lòng nhập ngày sinh.",
                "Thông báo lỗi không đúng khi để trống ngày sinh."
        );
    }

    @Test(description = " Thêm khách thuê không thành công. Ngày sinh lớn hơn hiện tại")
    public void ThemKT_TC_16() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Thêm khách thuê với ngày sinh lớn hơn hiện tại
        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                "01/01/2050", // ngày sinh tương lai
                phone,
                cccd,
                TenantData.VALID_ADDRESS
        );

        // 5. Kiểm tra thông báo lỗi
        Assert.assertEquals(
                khachThuePage.getNgaySinhErrorMessage(),
                "Ngày sinh không hợp lệ. Vui lòng nhập lại.",
                "Thông báo lỗi không đúng khi ngày sinh lớn hơn hiện tại."
        );
    }
    @Test
    public void ThemKT_TC_17() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        // 1. Mở trang đăng nhập
        loginPage.open();

        // 2. Đăng nhập
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // 3. Mở trang khách thuê
        khachThuePage.open();

        // 4. Mở form và nhập dữ liệu
        khachThuePage.clickOpenAddButton();
        khachThuePage.enterHoTen(TenantData.VALID_NAME);
        khachThuePage.selectGioiTinh(TenantData.VALID_GENDER);
        khachThuePage.enterNgaySinh(TenantData.VALID_DOB);
        khachThuePage.enterSdt(phone);
        khachThuePage.enterCccd(cccd);
        khachThuePage.enterQueQuan(TenantData.VALID_ADDRESS);

        // 5. Nhấn Hủy
        khachThuePage.clickCancel();

        // 6. Kiểm tra popup xác nhận hiển thị
        Assert.assertTrue(
                khachThuePage.isCancelConfirmModalDisplayed(),
                "Popup chưa hiển thị"
        );

        // 7. Nhấn Xác nhận
        khachThuePage.clickConfirmCancel();

        Thread.sleep(1000);

        // 8. Kiểm tra form đã đóng
        Assert.assertFalse(
                khachThuePage.isAddModalDisplayed(),
                "Form vẫn hiển thị sau khi hủy."
        );
    }

    @Test
    public void ThemKT_TC_18() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        khachThuePage.open();

        // 1. Mở form và nhập dữ liệu
        khachThuePage.clickOpenAddButton();
        khachThuePage.enterHoTen(TenantData.VALID_NAME);
        khachThuePage.selectGioiTinh(TenantData.VALID_GENDER);
        khachThuePage.enterNgaySinh(TenantData.VALID_DOB);
        khachThuePage.enterSdt(phone);
        khachThuePage.enterCccd(cccd);
        khachThuePage.enterQueQuan(TenantData.VALID_ADDRESS);

        // 2. Nhấn Hủy ở form
        khachThuePage.clickCancel();

        Assert.assertTrue(
                khachThuePage.isCancelConfirmModalDisplayed(),
                "Popup xác nhận hủy không hiển thị."
        );

        // 3. Nhấn Hủy trên popup xác nhận
        khachThuePage.clickCloseCancelPopup();

        Thread.sleep(1000);

        // 4. Kiểm tra quay lại form thêm khách thuê
        Assert.assertTrue(
                khachThuePage.isAddModalDisplayed(),
                "Form thêm khách thuê không hiển thị lại."
        );

        // 5. Kiểm tra dữ liệu vẫn giữ nguyên
        Assert.assertEquals(khachThuePage.getHoTenValue(), TenantData.VALID_NAME);
        Assert.assertEquals(khachThuePage.getSdtValue(), phone);
        Assert.assertEquals(khachThuePage.getCccdValue(), cccd);
        Assert.assertEquals(khachThuePage.getQueQuanValue(), TenantData.VALID_ADDRESS);
    }


    //=======GUI======/
    @Test(description = "ThemKT_UI_01: [Thêm khách thuê] Button")
    public void ThemKT_UI_01() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        Assert.assertTrue(khachThuePage.isAddModalDisplayed(), "Form 'Thêm khách thuê' không hiển thị.");
    }

    @Test(description = "ThemKT_UI_02: [X] Icon")
    public void ThemKT_UI_02() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        khachThuePage.clickCloseIcon();
        Thread.sleep(1000);
        Assert.assertFalse(khachThuePage.isAddModalDisplayed(), "Form 'Thêm khách thuê' chưa được đóng lại.");
    }

    @Test(description = "ThemKT_UI_03: [Mã khách thuê...] Label")
    public void ThemKT_UI_03() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        Assert.assertTrue(khachThuePage.isAllLabelsDisplayed(), "Các nhãn (Label) không hiển thị đầy đủ.");
    }

    @Test(description = "ThemKT_UI_04: [Mã khách thuê] Textbox")
    public void ThemKT_UI_04() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        Assert.assertFalse(khachThuePage.isMaKhachThueEnabled(), "Ô Mã Khách Thuê đang cho phép nhập.");
    }

    @Test(description = "ThemKT_UI_05: [Tên khách thuê...] Textbox")
    public void ThemKT_UI_05() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();

        String phone = TenantData.getRandomPhone();
        String cccd = TenantData.getRandomCCCD();

        khachThuePage.enterHoTen(TenantData.VALID_NAME);
        khachThuePage.enterSdt(phone);
        khachThuePage.enterCccd(cccd);
        khachThuePage.enterQueQuan(TenantData.VALID_ADDRESS);

        Assert.assertEquals(khachThuePage.getHoTenValue(), TenantData.VALID_NAME, "Không thể nhập Họ Tên.");
        Assert.assertEquals(khachThuePage.getSdtValue(), phone, "Không thể nhập Số Điện Thoại.");
        Assert.assertEquals(khachThuePage.getCccdValue(), cccd, "Không thể nhập CCCD.");
        Assert.assertEquals(khachThuePage.getQueQuanValue(), TenantData.VALID_ADDRESS, "Không thể nhập Quê Quán.");
    }


    @Test(description = "ThemKT_UI_06: [Giới tính] Dropdown")
    public void ThemKT_UI_06() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        khachThuePage.selectGioiTinh(TenantData.VALID_GENDER);
        Assert.assertEquals(khachThuePage.getGioiTinhValue(), TenantData.VALID_GENDER, "Không thể chọn Giới Tính.");
    }

    @Test(description = "ThemKT_UI_07: [Ngày sinh] Date Picker")
    public void ThemKT_UI_07() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        khachThuePage.enterNgaySinh(TenantData.VALID_DOB);

        // HTML5 input type="date" luôn trả về định dạng YYYY-MM-DD (Năm-Tháng-Ngày)
        // Nên ta cần convert từ "DD/MM/YYYY" sang "YYYY-MM-DD" để so sánh
        String[] parts = TenantData.VALID_DOB.split("/");
        String expectedDate = parts.length == 3 ? (parts[2] + "-" + parts[1] + "-" + parts[0]) : TenantData.VALID_DOB;

        Assert.assertEquals(khachThuePage.getNgaySinhValue(), expectedDate, "Không thể chọn/nhập Ngày Sinh.");
    }


    @Test(description = "ThemKT_UI_08: [Hủy] Button")
    public void ThemKT_UI_08() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        Assert.assertTrue(khachThuePage.isCancelButtonEnabled(), "Nút Hủy đang bị disable.");
    }

    @Test(description = "ThemKT_UI_09: [Lưu] Button")
    public void ThemKT_UI_09() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        Assert.assertTrue(khachThuePage.isSaveButtonEnabled(), "Nút Lưu/Thêm đang bị disable.");
    }

    @Test(description = "ThemKT_UI_10: [Báo lỗi] Validation Text")
    public void ThemKT_UI_10() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        khachThuePage.clickSaveButton();

        Assert.assertFalse(khachThuePage.getHoTenErrorMessage().isEmpty(), "Không hiển thị text cảnh báo Họ Tên.");
        Assert.assertFalse(khachThuePage.getGioiTinhErrorMessage().isEmpty(), "Không hiển thị text cảnh báo Giới Tính.");
        Assert.assertFalse(khachThuePage.getNgaySinhErrorMessage().isEmpty(), "Không hiển thị text cảnh báo Ngày Sinh.");
        Assert.assertFalse(khachThuePage.getSdtErrorMessage().isEmpty(), "Không hiển thị text cảnh báo Số điện thoại.");
        Assert.assertFalse(khachThuePage.getCccdErrorMessage().isEmpty(), "Không hiển thị text cảnh báo CCCD.");
        Assert.assertFalse(khachThuePage.getQueQuanErrorMessage().isEmpty(), "Không hiển thị text cảnh báo Quê Quán.");
    }

    @Test(description = "ThemKT_UI_11: [Xác nhận Hủy] Modal")
    public void ThemKT_UI_11() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.clickOpenAddButton();
        khachThuePage.enterHoTen("Nguyễn Văn A");
        khachThuePage.clickCancel();
        Assert.assertTrue(khachThuePage.isCancelConfirmModalDisplayed(), "Không hiển thị popup xác nhận hủy.");
    }

    @Test(description = "ThemKT_UI_12: [Thành công] Toast/Modal")
    public void ThemKT_UI_12() {
        LoginPage loginPage = new LoginPage();
        KhachThuePage khachThuePage = new KhachThuePage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        khachThuePage.open();

        khachThuePage.addTenant(
                TenantData.VALID_NAME,
                TenantData.VALID_GENDER,
                TenantData.VALID_DOB,
                TenantData.getRandomPhone(),
                TenantData.getRandomCCCD(),
                TenantData.VALID_ADDRESS
        );
        Assert.assertTrue(khachThuePage.isStatusModalDisplayed(), "Popup thông báo trạng thái không hiển thị.");
        Assert.assertEquals(khachThuePage.getStatusMessage(), "Thêm khách thuê thành công", "Nội dung toast thành công không đúng.");
    }






}