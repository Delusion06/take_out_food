package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Exception
 * @Date: 2022/5/9
 * @Description
 */
@Data
public class OrdersInfoDTO implements Serializable {
    private static final long serialVersionUID = 1471106394850967950L;

    private Long id;

    private Long ordersId;

    private Long dishId;

    private Long comboId;

    private String dishFlavor;

    private Integer amount;

    private BigDecimal price;

    private String name;

    private String image;
}
