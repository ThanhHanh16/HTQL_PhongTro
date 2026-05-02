package testcase.HopDong;


import common.BaseTest;
import common.Constant;
import common.Utilities;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.HopDongPage;
import pageobjects.LoginPage;
import java.lang.Thread;


public class ThemHD extends BaseTest {




    // ------------------------------------------------------------------ //
    //  HELPER: đăng nhập + mở trang hợp đồng
    // ------------------------------------------------------------------ //
    private HopDongPage loginAndOpenHopDong() {
        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();
        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();
        return hopDongPage;
    }




    // ================================================================== //
    //  TC_01 – Thêm hợp đồng thành công                                 //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thành công")
    public void ThemHD_TC_01() {
        LoginPage loginPage     = new LoginPage();
        HopDongPage hopDongPage = new HopDongPage();




        loginPage.open();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);
        hopDongPage.open();




        String tienCoc    = Utilities.randomTienCoc();
        String dieuKhoan  = Utilities.randomDieuKhoan();
        String ngayBatDau = Utilities.today();
        String ngayKetThuc = Utilities.endDate();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(ngayBatDau);
        hopDongPage.setNgayKetThuc(ngayKetThuc);
        hopDongPage.setTienCoc(tienCoc);
        hopDongPage.setDieuKhoan(dieuKhoan);
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isStatusDisplayed(),
                "Không hiển thị popup trạng thái"
        );
        String message = hopDongPage.getStatusMessage();
        Assert.assertTrue(
                message.contains("Thêm hợp đồng thành công!"),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_02 – Để trống Mã phòng rồi click "Thêm KT"                   //
    //  Expected: "Vui lòng chọn phòng trước."                           //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: để trống Mã phòng")
    public void ThemHD_TC_02() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        // Không chọn Mã phòng – click thẳng vào Thêm KT
        hopDongPage.clickThemKT();




        // Text đỏ inline xuất hiện NGAY dưới nút + Thêm KT — không qua modal-status




        Assert.assertTrue(
                hopDongPage.isThongBaoChonPhongHienThi(),
                "Không hiển thị thông báo lỗi 'Vui lòng chọn phòng trước.'"
        );
        String message = hopDongPage.getThongBaoChonPhong();
        Assert.assertTrue(
                message.contains("Vui lòng chọn phòng trước."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_03 – Để trống khách thuê VÀ người đại diện, nhập đủ còn lại  //
    //  Expected: text đỏ inline ở cả 2 ô sau khi click Lưu             //
    //    - "Tên khách thuê trống. Vui lòng nhập tên khách thuê."        //
    //    - "Vui lòng chọn người đại diện."                              //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: để trống khách thuê và người đại diện")
    public void ThemHD_TC_03() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        // Không chọn khách thuê
        // Không chọn người đại diện
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        // Verify lỗi khách thuê — text đỏ inline dưới ô Khách Thuê
        Assert.assertTrue(
                hopDongPage.isLoiKhachThueHienThi(),
                "Không hiển thị lỗi 'Tên khách thuê trống'"
        );
        String msgKT = hopDongPage.getLoiKhachThue();
        Assert.assertTrue(
                msgKT.contains("Tên khách thuê trống. Vui lòng nhập tên khách thuê."),
                "Thông báo lỗi khách thuê không đúng: " + msgKT
        );




        // Verify lỗi người đại diện — text đỏ inline bên ô Tên Người Đại Diện
        Assert.assertTrue(
                hopDongPage.isLoiNguoiDaiDienHienThi(),
                "Không hiển thị lỗi 'Vui lòng chọn người đại diện'"
        );
        String msgNDD = hopDongPage.getLoiNguoiDaiDien();
        Assert.assertTrue(
                msgNDD.contains("Tên người đại diện trống. Vui lòng nhập tên người đại diện."),
                "Thông báo lỗi người đại diện không đúng: " + msgNDD
        );
    }




    // ================================================================== //
    //  TC_04 – Chọn phòng tối đa 2 người, chọn 3 khách thuê rồi Lưu   //
    //  Expected: "Số lượng khách thuê không được vượt quá..."           //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: vượt quá số khách tối đa")
    public void ThemHD_TC_04() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectPhongToiDa2Nguoi();
        hopDongPage.selectKhachThueVoiSoLuong(3);
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiVuotSoKhachHienThi(),
                "Không hiển thị lỗi 'Số lượng khách thuê không được vượt quá'"
        );




        String message = hopDongPage.getLoiVuotSoKhach();




        Assert.assertTrue(
                message.contains("Số lượng khách thuê không được vượt quá số lượng khách tối đa của phòng."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_05 – Có khách thuê nhưng để trống người đại diện rồi Lưu     //
    //  Expected: "Tên người đại diện trống. Vui lòng nhập tên người đại diện." //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: để trống người đại diện")
    public void ThemHD_TC_05() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        // Không chọn người đại diện
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiNguoiDaiDienHienThi(),
                "Không hiển thị lỗi 'Tên người đại diện trống'"
        );




        String message = hopDongPage.getLoiNguoiDaiDien();




        Assert.assertTrue(
                message.contains("Tên người đại diện trống. Vui lòng nhập tên người đại diện."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_06 – Ngày bắt đầu trước ngày hiện tại                        //
    //  Expected: "Ngày bắt đầu không được nhỏ hơn ngày hiện tại."      //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: ngày bắt đầu trước ngày hiện tại")
    public void ThemHD_TC_06() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        String ngayBatDau = java.time.LocalDate.now()
                .minusDays(1)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));




        String ngayKetThuc = java.time.LocalDate.now()
                .plusDays(30)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();




        // 👇 QUAN TRỌNG
        hopDongPage.setNgayBatDauBangJS(ngayBatDau);




        hopDongPage.setNgayKetThuc(ngayKetThuc);
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiNgayBatDauHienThi(),
                "Không hiển thị lỗi 'Ngày bắt đầu không được nhỏ hơn ngày hiện tại.'"
        );




        String message = hopDongPage.getLoiNgayBatDau();








        Assert.assertTrue(
                message.contains("Ngày bắt đầu không được nhỏ hơn ngày hiện tại."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_07 – Ngày kết thúc trước ngày bắt đầu                        //
    //  Expected: "Ngày kết thúc phải lớn hơn ngày bắt đầu."            //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: ngày kết thúc trước ngày bắt đầu")
    public void ThemHD_TC_07() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        String ngayKetThucSai = java.time.LocalDate.now()
                .minusDays(1)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(ngayKetThucSai);
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiNgayKetThucHienThi(),
                "Không hiển thị lỗi 'Ngày kết thúc phải lớn hơn ngày bắt đầu'"
        );




        String message = hopDongPage.getLoiNgayKetThuc();
        Assert.assertTrue(
                message.contains("Ngày kết thúc phải lớn hơn ngày bắt đầu."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_08 – Tiền cọc không cho nhập chữ / ký tự đặc biệt             //
    //  Expected: Ô tiền cọc vẫn rỗng                                    //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: tiền cọc không cho nhập chữ/ký tự đặc biệt")
    public void ThemHD_TC_08() {
        HopDongPage hopDongPage = loginAndOpenHopDong();

        hopDongPage.clickThemHopDong();
        hopDongPage.setTienCoc("abc!@#");

        String currentValue = hopDongPage.getTienCoc();
        Assert.assertTrue(
                currentValue.isEmpty(),
                "Ô tiền cọc vẫn cho phép nhập chữ/ký tự đặc biệt. Giá trị hiện tại: " + currentValue
        );
    }




    // ================================================================== //
    //  TC_09 – Tiền cọc = 0 hoặc âm                                     //
    //  Expected: "Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại." //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: tiền cọc = 0 hoặc âm")
    public void ThemHD_TC_09() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc("-500000");
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiTienCocHienThi(),
                "Không hiển thị lỗi 'Tiền cọc phải là số'"
        );




        String message = hopDongPage.getLoiTienCoc();




        Assert.assertTrue(
                message.contains("Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_10 – Để trống ô Điều khoản                                    //
    //  Expected: "Điều khoản trống. Vui lòng nhập điều khoản."          //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: để trống điều khoản")
    public void ThemHD_TC_10() {
        HopDongPage hopDongPage = loginAndOpenHopDong();




        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan("");
        hopDongPage.clickLuu();




        Assert.assertTrue(
                hopDongPage.isLoiDieuKhoanHienThi(),
                "Không hiển thị lỗi 'Điều khoản trống'"
        );




        String message = hopDongPage.getLoiDieuKhoan();




        Assert.assertTrue(
                message.contains("Điều khoản trống. Vui lòng nhập điều khoản."),
                "Thông báo không đúng: " + message
        );
    }




    // ================================================================== //
    //  TC_11 – Click "Hủy" trên form → popup → Click "Xác nhận"         //
    //  Expected: Đóng form, không lưu dữ liệu đã nhập                   //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: hủy form bằng cách click Hủy rồi xác nhận")
    public void ThemHD_TC_11() {
        HopDongPage hopDongPage = loginAndOpenHopDong();

        // Bước 1: Nhập liệu vào form Thêm hợp đồng
        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());

        // Bước 2: Click nút "Hủy" → hiện popup "Bạn có chắc chắn muốn hủy?"
        hopDongPage.clickHuyForm();

        Assert.assertTrue(
                hopDongPage.isPopupXacNhanHuyHienThi(),
                "Không hiển thị popup xác nhận hủy 'Bạn có chắc chắn muốn hủy?'"
        );

        // Bước 3: Click "Xác nhận" trên popup → form phải đóng
        hopDongPage.clickXacNhanHuyPopup();

        Assert.assertTrue(
                hopDongPage.isFormThemHopDongDaDong(),
                "Form Thêm hợp đồng vẫn còn hiển thị sau khi xác nhận hủy"
        );
    }




    // ================================================================== //
    //  TC_12 – Click "Hủy" trên form → popup → Click "Hủy" trên popup   //
    //  Expected: Đóng popup, giữ nguyên form và dữ liệu đang nhập        //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng: hủy popup xác nhận → form vẫn mở, dữ liệu không mất")
    public void ThemHD_TC_12() {
        HopDongPage hopDongPage = loginAndOpenHopDong();

        // Bước 1: Nhập liệu vào form Thêm hợp đồng
        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        String tienCocDaNhap = Utilities.randomTienCoc();
        hopDongPage.setTienCoc(tienCocDaNhap);
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());

        // Bước 2: Click nút "Hủy" trên form → hiện popup "Bạn có chắc chắn muốn hủy?"
        hopDongPage.clickHuyForm();

        Assert.assertTrue(
                hopDongPage.isPopupXacNhanHuyHienThi(),
                "Không hiển thị popup xác nhận hủy 'Bạn có chắc chắn muốn hủy?'"
        );

        // Bước 3: Click "Hủy" trên popup → popup đóng, form vẫn còn hiển thị
        hopDongPage.clickHuyTrenPopup();

        Assert.assertTrue(
                hopDongPage.isFormThemHopDongVanHienThi(),
                "Form Thêm hợp đồng đã bị đóng sau khi click Hủy trên popup — dữ liệu bị mất"
        );

        // Verify dữ liệu vẫn còn trong form (kiểm tra trường tiền cọc)
        String tienCocHienTai = hopDongPage.getTienCoc();
        Assert.assertEquals(
                tienCocHienTai, tienCocDaNhap,
                "Dữ liệu tiền cọc bị mất sau khi click Hủy trên popup. Kỳ vọng: "
                        + tienCocDaNhap + " — Thực tế: " + tienCocHienTai
        );
    }




    // ================================================================== //
    //  TC_13 – Để trống ngày bắt đầu                                     //
    //  Expected: "Vui lòng chọn ngày bắt đầu."                           //
    // ================================================================== //
    @Test(description = " Thêm hợp đồng thất bại: để trống ngày bắt đầu")
    public void ThemHD_TC_13() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();




        // hopDongPage.setNgayBatDau(Utilities.today());


        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();


        // Verify hiển thị lỗi
        Assert.assertTrue(
                hopDongPage.isLoiNgayBatDauHienThi(),
                "Không hiển thị lỗi 'Ngày bắt đầu trống'"
        );


        // Lấy message
        String message = hopDongPage.getLoiNgayBatDau();


        Assert.assertTrue(
                message.contains("Vui lòng chọn ngày bắt đầu."),
                "Thông báo không đúng: " + message
        );
    }
    // ================================================================== //
    //  TC_14 – Để trống ngày kết thúc                                    //
    //  Expected: "Vui lòng chọn ngày kết thúc."                          //
    // ================================================================== //
    @Test(description = "Thêm hợp đồng thất bại: để trống ngày kết thúc")
    public void ThemHD_TC_14() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();


        hopDongPage.setNgayBatDau(Utilities.today());




        // hopDongPage.setNgayKetThuc(Utilities.endDate());


        hopDongPage.setTienCoc(Utilities.randomTienCoc());
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();


        // Verify hiển thị lỗi
        Assert.assertTrue(
                hopDongPage.isLoiNgayKetThucHienThi(),
                "Không hiển thị lỗi 'Ngày kết thúc trống'"
        );


        // Lấy message
        String message = hopDongPage.getLoiNgayKetThuc();


        Assert.assertTrue(
                message.contains("Vui lòng chọn ngày kết thúc."),
                "Thông báo không đúng: " + message
        );
    }





    // ================================================================== //
    //  TC_15 – Nếu tiền cọc trống → Thông báo “Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại.” //
    // ================================================================== //
    @Test(description = " Thêm hợp đồng thất bại: để trống tiền cọc")
    public void ThemHD_TC_15() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();
        hopDongPage.selectRandomMaPhong();
        hopDongPage.selectRandomKhachThue();
        hopDongPage.selectRandomNguoiDaiDien();
        hopDongPage.setNgayBatDau(Utilities.today());
        hopDongPage.setNgayKetThuc(Utilities.endDate());
        hopDongPage.setTienCoc("");
        hopDongPage.setDieuKhoan(Utilities.randomDieuKhoan());
        hopDongPage.clickLuu();


        Assert.assertTrue(
                hopDongPage.isLoiTienCocHienThi(),
                "Không hiển thị lỗi 'Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại.'"
        );


        String message = hopDongPage.getLoiTienCoc();


        Assert.assertTrue(
                message.contains("Tiền cọc phải là số và lớn hơn 0. Vui lòng nhập lại."),
                "Thông báo không đúng: " + message
        );
    }


    // =========================================================================
    // ======================== NHÓM TEST CASE GUI =============================
    // =========================================================================


    // ThemHD_UI_01: Kiểm tra icon X
    @Test(description = "ThemHD_UI_01 - Kiểm tra icon X")
    public void ThemHD_UI_01() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isIconXEnabled(),
                "LỖI: Icon X đang bị disable."
        );
    }


    // ThemHD_UI_02: Kiểm tra các Label trên form Thêm hợp đồng
    @Test(description = "ThemHD_UI_02 - Kiểm tra các label trên form Thêm hợp đồng")
    public void ThemHD_UI_02() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isLabelFormThemHopDongDisplayed(),
                "LỖI: Các label trên form Thêm hợp đồng không hiển thị đầy đủ."
        );
    }


    // ThemHD_UI_03: Kiểm tra textbox Mã hợp đồng
    @Test(description = "ThemHD_UI_03 - Kiểm tra textbox Mã hợp đồng")
    public void ThemHD_UI_03() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isMaHopDongDisabled(),
                "LỖI: Textbox Mã hợp đồng không bị disable."
        );
    }


    // ThemHD_UI_04: Kiểm tra dropdown Mã phòng
    @Test(description = "ThemHD_UI_04 - Kiểm tra dropdown Mã phòng")
    public void ThemHD_UI_04() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isDropdownMaPhongEnabled(),
                "LỖI: Dropdown Mã phòng đang bị disable."
        );
    }


    // ThemHD_UI_05: Kiểm tra nút Thêm KT
    @Test(description = "ThemHD_UI_05 - Kiểm tra nút Thêm KT")
    public void ThemHD_UI_05() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isBtnThemKTEnabled(),
                "LỖI: Nút Thêm KT đang bị disable."
        );
    }


    // ThemHD_UI_06: Kiểm tra textbox Tên khách thuê
    @Test(description = "ThemHD_UI_06 - Kiểm tra textbox Tên khách thuê")
    public void ThemHD_UI_06() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isTenKhachThueKhongNhapTrucTiepDuoc("Nguyen Van A"),
                "LỖI: Textbox Tên khách thuê vẫn cho nhập dữ liệu trực tiếp."
        );
    }


    // ThemHD_UI_07: Kiểm tra DatePicker Ngày bắt đầu, Ngày kết thúc
    @Test(description = "ThemHD_UI_07 - Kiểm tra DatePicker Ngày bắt đầu và Ngày kết thúc")
    public void ThemHD_UI_07() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isNgayBatDauEnabled() && hopDongPage.isNgayKetThucEnabled(),
                "LỖI: DatePicker Ngày bắt đầu hoặc Ngày kết thúc đang bị disable."
        );
    }


    // ThemHD_UI_08: Kiểm tra textbox Tiền cọc
    @Test(description = "ThemHD_UI_08 - Kiểm tra textbox Tiền cọc")
    public void ThemHD_UI_08() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isTienCocEnabled(),
                "LỖI: Textbox Tiền cọc đang bị disable."
        );
    }


    // ThemHD_UI_09: Kiểm tra textbox Điều khoản
    @Test(description = "ThemHD_UI_09 - Kiểm tra textbox Điều khoản")
    public void ThemHD_UI_09() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isDieuKhoanEnabled(),
                "LỖI: Textbox Điều khoản đang bị disable."
        );
    }


    // ThemHD_UI_10: Kiểm tra nút Lưu
    @Test(description = "ThemHD_UI_10 - Kiểm tra nút Lưu")
    public void ThemHD_UI_10() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isBtnLuuEnabled(),
                "LỖI: Nút Lưu đang bị disable."
        );
    }


    // ThemHD_UI_11: Kiểm tra nút Hủy
    @Test(description = "ThemHD_UI_11 - Kiểm tra nút Hủy")
    public void ThemHD_UI_11() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isBtnHuyEnabled(),
                "LỖI: Nút Hủy đang bị disable."
        );
    }


    // ThemHD_UI_12: Kiểm tra nút Thêm hợp đồng
    @Test(description = "ThemHD_UI_12 - Kiểm tra nút Thêm hợp đồng")
    public void ThemHD_UI_12() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        Assert.assertTrue(
                hopDongPage.isBtnThemHopDongEnabled(),
                "LỖI: Nút Thêm hợp đồng đang bị disable."
        );
    }


    // ThemHD_UI_13: Kiểm tra dropdown Tên người đại diện
    @Test(description = "ThemHD_UI_13 - Kiểm tra dropdown Tên người đại diện")
    public void ThemHD_UI_13() {
        HopDongPage hopDongPage = loginAndOpenHopDong();


        hopDongPage.clickThemHopDong();


        Assert.assertTrue(
                hopDongPage.isDropdownNguoiDaiDienEnabled(),
                "LỖI: Dropdown Tên người đại diện đang bị disable."
        );
    }


}
