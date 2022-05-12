package com.ex.echo.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface SmsService {

    public void sendSmsCode(String phoneNumber, String smsCode) throws TencentCloudSDKException;
}
