package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.ComboDTO;
import com.ex.echo.entity.Combo;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface ComboService {

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @param name .
     * @return .
     */
    public Page<ComboDTO> page(int page, int pageSize, String name);

    /**
     * 根据id查询套餐信息
     *
     * @param comboDTO .
     * @return .
     */
    public ComboDTO getComboById(ComboDTO comboDTO);

    /**
     * 添加套餐信息
     *
     * @param comboDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean addCombo(ComboDTO comboDTO,Long employeeId);

    /**
     * 修改套餐信息
     *
     * @param comboDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean editCombo(ComboDTO comboDTO,Long employeeId);

    /**
     * 修改套餐信息status
     *
     * @param comboDTOList .
     * @param employeeId .
     * @return .
     */
    public Boolean editComboStatus(List<ComboDTO> comboDTOList,Long employeeId);

    /**
     * 删除套餐信息
     *
     * @param comboDTOList .
     * @param employeeId .
     * @return .
     */
    public Boolean deleteCombo(List<ComboDTO> comboDTOList,Long employeeId);

    /**
     * 条件查询
     *
     * @param comboDTO .
     * @return .
     */
    public List<Combo> comboList(ComboDTO comboDTO);
}