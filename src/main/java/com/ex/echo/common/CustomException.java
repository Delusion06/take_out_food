package com.ex.echo.common;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1692201139310590555L;

    public CustomException(String message) {
        super(message);
    }
}
