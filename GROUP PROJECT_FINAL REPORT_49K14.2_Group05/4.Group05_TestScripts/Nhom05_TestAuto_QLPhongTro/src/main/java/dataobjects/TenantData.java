package dataobjects;

import common.Utilities;

public class TenantData {

    public static final String VALID_NAME = "Nguyễn Văn A";
    public static final String VALID_GENDER = "Nam";
    public static final String VALID_DOB = "01/01/2000";
    public static final String VALID_ADDRESS = "Đà Nẵng";

    public static final String UPDATE_NAME = "Nguyễn Văn B";
    public static final String UPDATE_ADDRESS = "Quảng Nam";
    public static final String UPDATE_GENDER = "Nam";
    public static final String UPDATE_DOB = "01/01/2002";

    public static String getRandomPhone() {
        return Utilities.randomPhone();
    }

    public static String getRandomCCCD() {
        return Utilities.randomCCCD();
    }

    // dữ liệu lỗi
    public static final String EMPTY_NAME = "";
}