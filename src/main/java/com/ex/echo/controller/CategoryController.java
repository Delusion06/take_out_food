package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.CategoryDTO;
import com.ex.echo.entity.Category;
import com.ex.echo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 分类信息管理
 */
@Slf4j
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param page     .
     * @param pageSize .
     * @return .
     */
    @RequestMapping(value = "/category/page", method = RequestMethod.GET)
    public JsonModel<Page> page(int page, int pageSize) {
        log.info("分页查询,page=[{}],pageSize=[{}]", page, pageSize);

        Page<Category> result = categoryService.categoryList(page, pageSize);
        return JsonModel.success(result);
    }

    /**
     * 添加分类信息
     *
     * @param categoryDTO .
     * @return .
     */
    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public JsonModel<String> addCategory(HttpServletRequest request, @RequestBody CategoryDTO categoryDTO) {
        log.info("添加分类信息,categoryDTO:[{}]", categoryDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = categoryService.addCategory(categoryDTO, employeeId);
        if (!result) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success("添加成功!");
    }

    /**
     * 修改分类信息
     *
     * @param categoryDTO .
     * @return .
     */
    @RequestMapping(value = "/category/edit", method = RequestMethod.POST)
    public JsonModel<String> editCategory(HttpServletRequest request, @RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类信息,categoryDTO:[{}]", categoryDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = categoryService.editCategory(categoryDTO, employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 删除分类信息
     *
     * @param categoryDTO .
     * @return .
     */
    @RequestMapping(value = "/category/delete", method = RequestMethod.POST)
    public JsonModel<String> deleteCategory(HttpServletRequest request, @RequestBody CategoryDTO categoryDTO) {
        log.info("删除分类信息,categoryDTO:[{}]", categoryDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = categoryService.deleteCategory(categoryDTO, employeeId);
        if (!result) {
            return JsonModel.error("删除失败!");
        }
        return JsonModel.success("删除成功!");
    }

    /**
     * 条件查询
     *
     * @param categoryDTO .
     * @return .
     */
    @RequestMapping(value = "/category/list", method = RequestMethod.POST)
    public JsonModel<List<Category>> list(@RequestBody CategoryDTO categoryDTO) {
        log.info("条件查询,categoryDTO:[{}]", categoryDTO);

        List<Category> result = categoryService.categoryList(categoryDTO);
        return JsonModel.success(result);
    }
}