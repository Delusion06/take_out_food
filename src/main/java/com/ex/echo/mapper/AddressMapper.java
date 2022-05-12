package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}