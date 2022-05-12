package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.ComboDTO;
import com.ex.echo.entity.Combo;
import com.ex.echo.service.ComboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 套餐信息管理
 */
@Slf4j
@RestController
public class ComboController {

    @Autowired
    private ComboService comboService;

    /**
     * 分页查询
     *
     * @param page     .
     * @param pageSize .
     * @param name     .
     * @return .
     */
    @RequestMapping(value = "/combo/page", method = RequestMethod.GET)
    public JsonModel<Page> page(int page, int pageSize, String name) {
        log.info("分页查询,page:[{}],pageSize:[{}],name:[{}]", page, pageSize, name);

        Page<ComboDTO> result = comboService.page(page, pageSize, name);
        return JsonModel.success(result);
    }

    /**
     * 根据id查询套餐信息
     *
     * @param comboDTO .
     * @return .
     */
    @RequestMapping(value = "/combo/getById", method = RequestMethod.POST)
    public JsonModel<ComboDTO> getComboById(HttpServletRequest request, @RequestBody ComboDTO comboDTO) {
        log.info("根据id查询套餐信息,comboDTO:[{}]", comboDTO);

        ComboDTO result = comboService.getComboById(comboDTO);
        if (Objects.isNull(result)) {
            return JsonModel.error("查询出错!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加套餐信息
     *
     * @param comboDTO .
     * @return .
     */
    @RequestMapping(value = "/combo/add", method = RequestMethod.POST)
    public JsonModel<String> addCombo(HttpServletRequest request, @RequestBody ComboDTO comboDTO) {
        log.info("添加套餐,comboDTO:[{}]", comboDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = comboService.addCombo(comboDTO, employeeId);
        if (!result) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success("添加成功!");
    }

    /**
     * 修改套餐信息
     *
     * @param comboDTO .
     * @return .
     */
    @RequestMapping(value = "/combo/edit", method = RequestMethod.POST)
    public JsonModel<String> editCombo(HttpServletRequest request, @RequestBody ComboDTO comboDTO) {
        log.info("修改套餐信息,comboDTO:[{}]", comboDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = comboService.editCombo(comboDTO, employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 修改套餐信息status
     *
     * @param comboDTOList .
     * @return .
     */
    @RequestMapping(value = "/combo/editStatus", method = RequestMethod.POST)
    public JsonModel<String> editComboStatus(HttpServletRequest request, @RequestBody List<ComboDTO> comboDTOList) {
        log.info("修改套餐信息status,comboDTOList:[{}]", comboDTOList);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = comboService.editComboStatus(comboDTOList, employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 删除套餐信息
     *
     * @param comboDTOList .
     * @return .
     */
    @RequestMapping(value = "/combo/delete", method = RequestMethod.POST)
    public JsonModel<String> delete(HttpServletRequest request, @RequestBody List<ComboDTO> comboDTOList) {
        log.info("删除套餐信息,comboDTOList:[{}]", comboDTOList);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = comboService.deleteCombo(comboDTOList, employeeId);
        if (result) {
            return JsonModel.success("删除成功!");
        }
        return JsonModel.error("删除失败!");
    }

    /**
     * 条件查询
     *
     * @param comboDTO .
     * @return .
     */
    @RequestMapping(value = "/combo/list", method = RequestMethod.POST)
    public JsonModel<List<Combo>> list(HttpServletRequest request, @RequestBody ComboDTO comboDTO) {
        log.info("条件查询,comboDTO:[{}]", comboDTO);

        List<Combo> result = comboService.comboList(comboDTO);
        return JsonModel.success(result);
    }
}    