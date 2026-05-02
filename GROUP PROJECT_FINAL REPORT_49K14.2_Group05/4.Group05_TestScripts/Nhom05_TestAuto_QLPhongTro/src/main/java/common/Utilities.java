package common;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class Utilities {


    public static String randomPhone() {
        Random rand = new Random();
        return "0" + (100000000 + rand.nextInt(900000000));
    }


    public static String randomCCCD() {
        Random rand = new Random();
        StringBuilder cccd = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            cccd.append(rand.nextInt(10));
        }
        return cccd.toString();
    }
    public static String randomTienCoc() {
        int value = 1000000 + new java.util.Random().nextInt(9000000);
        return String.valueOf(value);
    }


    public static String randomDieuKhoan() {
        return "https://document/test_" + System.currentTimeMillis();
    }


    public static String today() {
        return java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    public static String endDate() {
        return java.time.LocalDate.now()
                .plusMonths(6)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}



