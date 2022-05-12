package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Exception
 * @Date: 2022/5/7
 * @Description: 用户信息DTO
 */
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -5947100100361302392L;

    private Long id;

    private String username;

    private String password;

    private String phone;

    private Integer sex;

    private String idCard;

    private Integer status;

    private String code;
}
