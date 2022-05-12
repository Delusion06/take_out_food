package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 套餐菜品关系DTO
 */
@Data
public class ComboDishDTO implements Serializable {
    private static final long serialVersionUID = -7619114848361050506L;

    private Long id;

    private Long comboId;

    private Long dishId;

    private Integer amount;

    private String name;

    private BigDecimal price;

    private String image;

    private String description;
}
