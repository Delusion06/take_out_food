package com.ex.echo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Echo
 * @Date: 2022/04/29/ 13:41
 * @Description: 订单信息详情
 */
@Data
public class OrdersInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    private Long ordersId;

    /**
     * 购物车id
     */
    private Long shoppingCartId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 套餐id
     */
    private Long comboId;

    /**
     * 口味
     */
    private String dishFlavor;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 是否删除
     */
    private Integer isDeleted;
}