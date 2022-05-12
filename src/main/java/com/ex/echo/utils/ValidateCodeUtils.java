package com.ex.echo.utils;

import java.util.Random;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 随机生成验证码工具类
 */
public class ValidateCodeUtils {

    /**
     * 随机生成验证码
     *
     * @param length .
     * @return .
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            // 生成随机数，最大为9999
            code = new Random().nextInt(9999);
            if(code < 1000){
                // 保证随机数为4位数字
                code = code + 1000;
            }
        }else if(length == 6){
            // 生成随机数，最大为999999
            code = new Random().nextInt(999999);
            if(code < 100000){
                // 保证随机数为6位数字
                code = code + 100000;
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     *
     * @param length .
     * @return .
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash = Integer.toHexString(rdm.nextInt());
        return hash.substring(0, length);
    }
}
