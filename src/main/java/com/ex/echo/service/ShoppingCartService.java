package com.ex.echo.service;

import com.ex.echo.dto.ShoppingCartDTO;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface ShoppingCartService {

    /**
     * 添加购物车信息
     *
     * @param shoppingCartDTO .
     * @param userId          .
     * @return .
     */
    public ShoppingCartDTO addShoppingCart(ShoppingCartDTO shoppingCartDTO, Long userId);

    /**
     * 修改购物车信息amount
     *
     * @param shoppingCartDTO .
     * @param userId          .
     * @return .
     */
    public ShoppingCartDTO editShoppingCartAmount(ShoppingCartDTO shoppingCartDTO, Long userId);

    /**
     * 修改购物车信息status
     *
     * @param userId .
     * @return .
     */
    public Boolean editShoppingCartStatus(Long userId);

    /**
     * 条件查询
     *
     * @param userId .
     * @return .
     */
    public List<ShoppingCartDTO> shoppingCartList(Long userId);
}