package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 地址信息DTO
 */
@Data
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 8736276794737347366L;

    private Long id;

    private Integer categoryType;

    private String categoryName;

    private Integer sort;

    private Long createEmployeeId;

    private Long updateEmployeeId;
}
