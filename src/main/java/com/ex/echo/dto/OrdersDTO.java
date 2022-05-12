package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 订单信息DTO
 */
@Data
public class OrdersDTO implements Serializable {
    private static final long serialVersionUID = 5110504410665720161L;

    private Long id;

    private Integer ordersStatus;

    private Long userId;

    private Long addressId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private BigDecimal price;

    private String remark;

    private Integer payStatus;

    /**
     * user表
     */
    private String username;

    /**
     * address表
     */
    private String phone;

    private String detail;

    private String consignee;


    private List<OrdersInfoDTO> ordersInfoDTOList;
}