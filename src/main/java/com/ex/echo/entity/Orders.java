package com.ex.echo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Echo
 * @Date: 2022/04/29/ 13:41
 * @Description: 订单信息
 */
@Data
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单状态,1为待付款,2为备餐中,3为已派送,4为已完成,5为已取消
     */
    private Integer ordersStatus;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 地址id
     */
    private Long addressId;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 结账时间
     */
    private LocalDateTime checkoutTime;

    /**
     * 支付方式,1为微信,2为支付宝
     */
    private Integer payMethod;

    /**
     * 实收金额
     */
    private BigDecimal price;

    /**
     * 备注
     */
    private String remark;

    /**
     * 支付状态,1为待支付,2为正在支付,3为已支付
     */
    private Integer payStatus;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 是否删除
     */
    private Integer isDeleted;
}