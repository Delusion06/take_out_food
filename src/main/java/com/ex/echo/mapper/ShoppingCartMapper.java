package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    @Update("<script>" +
            "update shopping_cart set " +
            "status = '1'," +
            "update_time = now()" +
            "where id in " +
            "<foreach collection='shoppingCartList' item='item' index='index' open='(' separator=',' close=')' > " +
            "#{item.id} " +
            "</foreach>" +
            "</script>")
    void batchUpdate(@Param("shoppingCartList") List<ShoppingCart> shoppingCartList);
}