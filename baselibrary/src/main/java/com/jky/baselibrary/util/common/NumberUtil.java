package com.jky.baselibrary.util.common;

/**
 * Created by Android_01 on 2017/3/27.
 */

public class NumberUtil {

    /**
     * @param number  要截取小数位数的数字
     * @param decimal 截取到的小数位数
     */
    public static String fixDecimal(double number, int decimal) {
        return String.format("%." + decimal + "f", number);
    }
}
