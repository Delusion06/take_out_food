package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.OrdersInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Mapper
public interface OrdersInfoMapper extends BaseMapper<OrdersInfo> {

    /**
     * 批量添加订单信息表记录
     *
     * @param ordersInfoList .
     */
    @Insert("<script>" +
            "insert into orders_info(" +
            "orders_id,dish_id,combo_id,dish_flavor,amount,price,shopping_cart_id) " + "VALUES" +
            "<foreach collection='ordersInfoList' item='item' index='index'  separator=','>" +
            "(#{item.ordersId},#{item.dishId},#{item.comboId},#{item.dishFlavor},#{item.amount},#{item.price},#{item.shoppingCartId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("ordersInfoList") List<OrdersInfo> ordersInfoList);
}