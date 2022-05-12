package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.DishDTO;
import com.ex.echo.service.DishService;
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
 * @Description: 菜品信息管理
 */
@Slf4j
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @param name .
     * @return .
     */
    @RequestMapping(value = "/dish/page", method = RequestMethod.GET)
    public JsonModel<Page> page(int page, int pageSize, String name) {
        log.info("分页查询,page:[{}],pageSize:[{}],name:[{}]",page,pageSize,name);

        Page<DishDTO> result = dishService.page(page, pageSize, name);
        return JsonModel.success(result);
    }

    /**
     * 根据id查询菜品信息
     *
     * @param dishDTO .
     * @return .
     */
    @RequestMapping(value = "/dish/getById", method = RequestMethod.POST)
    public JsonModel<DishDTO> getDishById(@RequestBody DishDTO dishDTO) {
        log.info("根据id查询菜品信息,dishDTO:[{}]",dishDTO);

        DishDTO result = dishService.getDishById(dishDTO);
        if (Objects.isNull(result)){
            return JsonModel.error("查询出错!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加菜品信息
     *
     * @param dishDTO .
     * @return .
     */
    @RequestMapping(value = "/dish/add", method = RequestMethod.POST)
    public JsonModel<String> addDish(HttpServletRequest request, @RequestBody DishDTO dishDTO) {
        log.info("添加菜品信息,dishDTO:[{}]",dishDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = dishService.addDish(dishDTO,employeeId);
        if (!result) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success("添加成功!");
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO .
     * @return .
     */
    @RequestMapping(value = "/dish/edit",method = RequestMethod.POST)
    public JsonModel<String> editDish(HttpServletRequest request, @RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息,dishDTO:[{}]",dishDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = dishService.editDish(dishDTO,employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 修改菜品信息status
     *
     * @param dishDTOList .
     * @return .
     */
    @RequestMapping(value = "/dish/editStatus",method = RequestMethod.POST)
    public JsonModel<String> editDishStatus(HttpServletRequest request, @RequestBody List<DishDTO> dishDTOList) {
        log.info("修改菜品信息status,dishDTOList:[{}]",dishDTOList);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = dishService.editDishStatus(dishDTOList,employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 删除菜品信息
     *
     * @param dishDTOList .
     * @return .
     */
    @RequestMapping(value = "/dish/delete",method = RequestMethod.POST)
    public JsonModel<String> delete(HttpServletRequest request, @RequestBody List<DishDTO> dishDTOList) {
        log.info("删除菜品信息,dishDTOList:[{}]",dishDTOList);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = dishService.deleteDish(dishDTOList,employeeId);
        if (result) {
            return JsonModel.success("删除成功");
        }
        return JsonModel.error("删除失败");
    }

    /**
     * 条件查询
     *
     * @param dishDTO .
     * @return .
     */
    @RequestMapping(value = "/dish/list", method = RequestMethod.POST)
    public JsonModel<List<DishDTO>> list(HttpServletRequest request, @RequestBody DishDTO dishDTO) {
        log.info("条件查询,dishDTO:[{}]",dishDTO);

        List<DishDTO> result = dishService.dishList(dishDTO);
        return JsonModel.success(result);
    }
}