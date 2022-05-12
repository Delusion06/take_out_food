package com.ex.echo.utils;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

import java.util.regex.Pattern;

/**
 * @Author: Exception
 * @Date: 2022/5/11
 * @Description: 各类正则校验
 */
public class ValidateUtils extends ObjectUtils {

    /**
     * 校验手机是否合规 2020年最全的国内手机号格式
     */
    private static final String REGEX_MOBILE =
            "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    /**
     * 校验身份证号码
     */
    private static final String REGEX_ID =
            "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    public static boolean isIdCard(String idCard) {
        if (idCard == null || "".equals(idCard)) {
            return false;
        }
        boolean matches = idCard.matches(REGEX_ID);
        //判断第18位校验值
        if (matches) {
            if (idCard.length() == 18) {
                try {
                    char[] charArray = idCard.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return matches;
    }

    /**
     * 判断对象为空
     *
     * @param object .
     * @return .
     */
    public static boolean isNull(Object object) {
        if (isEmpty(object)) {
            return true;
        } else {
            try {
                for (Field f : object.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    if (!StringUtils.isEmpty(f.get(object))) {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}