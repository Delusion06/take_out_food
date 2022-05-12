package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Exception
 * @Date: 2022/5/9
 * @Description
 */
@Data
public class AddressDTO implements Serializable {
    private static final long serialVersionUID = 7980391806623451882L;

    private Long id;

    private Long userId;

    private String consignee;

    private Integer sex;

    private String phone;

    private String detail;

    private String label;

    private Integer isDefault;

    private Integer isDeleted;
}
