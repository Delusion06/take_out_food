package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.Combo;
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
public interface ComboMapper extends BaseMapper<Combo> {

    /**
     * 批量修改套餐信息表is_deleted字段
     *
     * @param comboIdList .
     * @param employeeId .
     */
    @Update("<script>" +
            "update combo set is_deleted = '1',update_time = now(),update_employee_id = #{employeeId} where id in " +
            "<foreach collection='comboIdList' item='id' index='index' open='(' separator=',' close=')' > " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    void batchUpdate(@Param("comboIdList") List<Long> comboIdList, @Param("employeeId") Long employeeId);
}