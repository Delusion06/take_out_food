package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Exception
 * @Date: 2022/5/7
 * @Description
 */
@Data
public class ShoppingCartDTO implements Serializable {
    private static final long serialVersionUID = -5737991674288550601L;

    private Long id;

    private Long userId;

    private Long dishId;

    private Long comboId;

    private String dishFlavor;

    private Integer amount;

    private BigDecimal price;

    private String image;

    private String name;
}
