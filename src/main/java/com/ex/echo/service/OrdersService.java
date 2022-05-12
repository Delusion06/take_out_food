package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.OrdersDTO;
import com.ex.echo.entity.Orders;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface OrdersService {

    /**
     * 分页查询(商家)
     *
     * @param page      .
     * @param pageSize  .
     * @param id        .
     * @param beginTime .
     * @param endTime   .
     * @return .
     */
    public Page<OrdersDTO> employeePage(int page, int pageSize, Long id, String beginTime, String endTime);

    /**
     * 分页查询(用户)
     *
     * @param page     .
     * @param pageSize .
     * @param userId   .
     * @return .
     */
    public Page<OrdersDTO> userPage(int page, int pageSize, Long userId);

    /**
     * 将购物车信息生成订单信息
     *
     * @param userId .
     * @return .
     */
    public Orders addShoppingCartToOrders(Long userId);

    /**
     * 修改订单信息ordersStatus
     *
     * @param ordersDTO  .
     * @param employeeId .
     * @return .
     */
    public Boolean editOrdersOrdersStatus(OrdersDTO ordersDTO, Long employeeId);

    /**
     * 将订单中的数据返回到购物车中
     *
     * @param ordersDTO .
     * @param userId    .
     * @return .
     */
    public Boolean deleteOrdersToShoppingCart(OrdersDTO ordersDTO, Long userId);

    /**
     * 根据id获取订单信息
     *
     * @param ordersDTO .
     * @param userId    .
     * @return .
     */
    public OrdersDTO getOrdersById(OrdersDTO ordersDTO, Long userId);

    /**
     * 修改订单信息addressId
     *
     * @param ordersDTO .
     * @param userId    .
     * @return .
     */
    public OrdersDTO editOrdersAddressId(OrdersDTO ordersDTO, Long userId);

    /**
     * 修改订单信息payStatus
     *
     * @param ordersDTO .
     * @return .
     */
    public Boolean editOrdersPayStatus(OrdersDTO ordersDTO);

    /**
     * 再来一单
     *
     * @param ordersDTO .
     * @return .
     */
    public Orders addOrdersAgain(OrdersDTO ordersDTO);

    /**
     * 修改订单信息ordersStatus
     *
     * @param ordersDTO .
     * @return .
     */
    public Boolean editOrdersStatus(OrdersDTO ordersDTO);
}