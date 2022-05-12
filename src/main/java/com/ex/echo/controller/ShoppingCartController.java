package com.ex.echo.controller;

import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.ShoppingCartDTO;
import com.ex.echo.service.ShoppingCartService;
import com.ex.echo.utils.ValidateUtils;
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
 * @Description: 购物车信息管理
 */
@Slf4j
@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车信息
     *
     * @param shoppingCartDTO .
     * @return .
     */
    @RequestMapping(value = "/shoppingCart/add", method = RequestMethod.POST)
    public JsonModel<ShoppingCartDTO> addShoppingCart(HttpServletRequest request, @RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车信息,shoppingCartDTO:[{}]", shoppingCartDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        ShoppingCartDTO result = shoppingCartService.addShoppingCart(shoppingCartDTO, userId);
        if (Objects.isNull(result)) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success(result);
    }

    /**
     * 修改购物车信息
     *
     * @param request .
     * @param shoppingCartDTO .
     * @return .
     */
    @RequestMapping(value = "/shoppingCart/sub", method = RequestMethod.POST)
    public JsonModel<ShoppingCartDTO> subShoppingCart(HttpServletRequest request, @RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("修改购物车信息,shoppingCartDTO:[{}]",shoppingCartDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        ShoppingCartDTO result = shoppingCartService.editShoppingCartAmount(shoppingCartDTO,userId);
        if (ValidateUtils.isNull(result)){
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success(result);
    }

    /**
     * 清空购物车信息
     *
     * @return .
     */
    @RequestMapping(value = "/shoppingCart/clean", method = RequestMethod.POST)
    public JsonModel<String> clean(HttpServletRequest request) {
        log.info("清空购物车信息.");

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        boolean result = shoppingCartService.editShoppingCartStatus(userId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 获取购物车信息
     *
     * @return .
     */
    @RequestMapping(value = "/shoppingCart/list", method = RequestMethod.POST)
    public JsonModel<List<ShoppingCartDTO>> list(HttpServletRequest request) {
        log.info("获取购物车信息.");

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        List<ShoppingCartDTO> result = shoppingCartService.shoppingCartList(userId);
        return JsonModel.success(result);
    }
}