package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}