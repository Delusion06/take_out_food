package com.ex.echo.config;

import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Data
public class AlipayConfig {

    /**
     * 应用ID,收款账号即是APPID对应支付宝账号
     */
    private String appId = "2021000116670110";

    /**
     * 商户私钥,PKCS8格式RSA2私钥
     */
    private String merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCDXuKQFPl1w6xR/IhKAG1eRVUNOgkn255Rp+HWSSpaP0VLAGoHAb/pSAWF9qAWUh45b5uj0ScxwBqsre9C6M/vEikRpJ5nABOYrnz6k250WIqUVZm6FiY5GYTjsjWcBqtAo63kusKqHtHDjOhI40381s8pfTLTr9Hv0NBKUsPMYXu2v/Oozr8e6PpLHDDmMj3xYUMQWra34hFk9jHWRm0nTdC6zju7avIctikMgOVE6lRIR7TrJdla7OI9duip3r/F7gZ+9ULYnIThW1bB9tpMCzV33TX890OLrOA67beiJkAIa312aBuIs1T7r1GHG4gc2TmAfK2H1I/0aFw8YtalAgMBAAECggEBAIAeB0KQ0gBhhiqm55uT8xVe9GVj1g4DBtGHsp2y2RUOY1uT2dNfh3PQg2J0dQePql5FVDpc/aZtPRZuwso0FqWGxnqoWrsEvhfHA6e0JKJevzeOi8+JTlmdY5ZT9Chn8JeniS1qXfJfAB9/ygFbfHE6ZOqHaQw9ibYhpSD2unEUsy52e4rj30x2tAfv+mLQgD0d3oykvDfj/sZKxWb6vMZOK/hqlRITxKWWnd8Q522UGdCVA7xUxbd3v8OSAu+tenIk/xHv/ApH0GDF8K6C1VRxQt8DgK+bSRkPlZ8g9fL/c6N+SbkPHajWvNCufE1utV6Js4/fKVCPUjRDRYrKWTkCgYEAu+w9pH1Vl3av1fNSolhZcfY7U4R2lWnm1UOqNubBOJWjTuB+Yy3311WzKzN+oiUxebl/PSksF7RpSXFcoIHL0PeyEm7JcEG/eJ9oFz/4BPM3z/RgOrQrBP2xRfvqLEBVYAzkjrv1LLXAe9mjKe5n1Fj6FpyEeWFmLzxhWilhzFMCgYEAsvYOj8uQDpL2bzFkRoSjka4j6Qwz0VayPPZH41AY+Q+VXaAxC+xFCjCOdAH7FSPjutMKG1L59uvLFHpq9Mw6l/ht2waaI5GuW8czMTLiET8RnGLiBcYmf7JffGtRrVkQA2Ll9Zidu0Zh1KhFSJO/TG/yUlkVgqYSd52Di1KJsicCgYBZwZ43oH1vGIsnf05qqBQgsGDppQ0WlDxihC0ah+Cl7CLSG7uCF4SW3yHycmf7fhRBdW3mgnHC6LKZF4IMT7JPnt74mt2vtleDf2+yGyWVB64Srz//Ceq6OYmgSZAxjpC4ja/k1LVno2/KN8sMzgfdjpn0jtFfQDwUN5yv+a/ybQKBgB7+SitRH9PGU/OR5EHqOsHCHOoBeP/qOdgg9vZBKvIHZuDeG2BrPNG08xbYOPnCS3BRmFB/IFdJItSbFubqb8Nd/PiPviYm1sGCUDVXEJvYBl7FKUvCs2wuIidUFsMkx4cum5DbK4YBfHPt6IHXRKwiPx9P2k/UHyKkHuIEG2DFAoGBAKZT0RKkZSNJwauTiYSBnlLXxvoNy/GiHY4F0uBK0ZWD2P3wROcEBvZ7bpnU3kYIy7Fm/5Ef2NiR1YMLAR9tM5zYg2b896wjg4HPDBQhK2J8D9QRvlspPIBptZKzXTrP7CsX/XZzeZzw6wZfdhBh48xSH7SgtDgXs9tTy2oQAwvi";

    /**
     * 支付宝公钥,查看地址:https://openhome.alipay.com/platform/keyManage.html对应APPID下的支付宝公钥
     */
    private String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAls9J375dQzn9jD4qSHYsvyHjOwBugzCMa/clJdkI1/2QDDvKxdHgWJDCUWkNDUsq+G0AbSoK5mU8IwudNVRBmHp0ncw19Rhq/spZWuEjmalfeZVN8a4qbpy0YdjRfltI3JzO98tvW5ZvlfI2chHQyOwIVE+4NvaQb51AO01F6dbZ1Gi5Cye3S6Go3nNWRuV7+NameDL922aeqP/nrWmiMCzMExzaNDysxLIT9goqLiYuVZ/fozzz2sfVRXbDX9dT1hCGWlNIhOw2uKuefwxzKNr8tT3IxPCiOzqvdQCqdnPR6uaKOQakIrk0R0gtVIqlGyrA4eW+fJx3AY4ceyI0gQIDAQAB";
    /**
     * 服务器异步通知页面路径,需http://格式的完整路径,不能加?id=123这类自定义参数,必须外网可以正常访问
     */
    private String notifyUrl = "http://localhost:9090/success.html";

    /**
     * 页面跳转同步通知页面路径,需http://格式的完整路径,不能加?id=123这类自定义参数,必须外网可以正常访问
     */
    private String returnUrl = "http://localhost:9090/success2.html";

    /**
     * 签名方式
     */
    private String signType = "RSA2";

    /**
     * 字符编码格式
     */
    private String charset = "utf-8";

    /**
     * 支付宝网关
     */
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 支付宝网关
     */
    private String logPath = "C:\\";

    /**
     * 写日志,方便测试(看网站需求,也可以改成把记录存入数据库)
     *
     * @param loginfo:要写入日志里的文本内容 .
     */
    public void logResult(String loginfo) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(logPath + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(loginfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}