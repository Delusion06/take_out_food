package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.OrdersDTO;
import com.ex.echo.entity.Orders;
import com.ex.echo.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 订单信息管理
 */
@Slf4j
@RestController
public class OrdersController {

    @Autowired
    private OrdersService orderService;

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
    @RequestMapping(value = "/orders/employee/page", method = RequestMethod.GET)
    public JsonModel<Page> employeePage(int page, int pageSize, Long id, String beginTime, String endTime) {
        log.info("分页查询(商家),page:[{}],pageSize:[{}],id:[{}],beginTime:[{}],endTime:[{}]", page, pageSize, id, beginTime, endTime);

        Page<OrdersDTO> result = orderService.employeePage(page, pageSize, id, beginTime, endTime);
        return JsonModel.success(result);
    }

    /**
     * 分页查询(用户)
     *
     * @param page     .
     * @param pageSize .
     * @return .
     */
    @RequestMapping(value = "/orders/user/page", method = RequestMethod.GET)
    public JsonModel<Page> userPage(HttpServletRequest request, int page, int pageSize) {
        log.info("分页查询(商家),page:[{}],pageSize:[{}]", page, pageSize);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        Page<OrdersDTO> result = orderService.userPage(page, pageSize, userId);
        return JsonModel.success(result);
    }

    /**
     * 将购物车信息生成订单信息
     *
     * @param request .
     * @return .
     */
    @RequestMapping(value = "/orders/addShoppingCartToOrders", method = RequestMethod.POST)
    public JsonModel<Orders> addShoppingCartToOrders(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        Orders result = orderService.addShoppingCartToOrders(userId);
        if (Objects.isNull(result)) {
            return JsonModel.error("添加订单失败!");
        }
        log.info("将购物车信息生成订单信息,result:[{}]", result);
        return JsonModel.success(result);
    }

    /**
     * 修改订单信息ordersStatus
     *
     * @param request   .
     * @param ordersDTO .
     * @return .
     */
    @RequestMapping(value = "/orders/editOrdersStatus", method = RequestMethod.POST)
    public JsonModel<String> editOrdersOrdersStatus(HttpServletRequest request, @RequestBody OrdersDTO ordersDTO) {
        log.info("修改订单信息ordersStatus,ordersDTO:[{}]", ordersDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = orderService.editOrdersOrdersStatus(ordersDTO, employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功");
    }

    /**
     * 将订单中的数据返回到购物车中
     *
     * @param request   .
     * @param ordersDTO .
     * @return .
     */
    @RequestMapping(value = "/orders/deleteOrdersToShoppingCart", method = RequestMethod.POST)
    public JsonModel<String> deleteOrdersToShoppingCart(HttpServletRequest request, @RequestBody OrdersDTO ordersDTO) {
        log.info("将订单中的数据返回到购物车中,ordersDTO:[{}]", ordersDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        Boolean result = orderService.deleteOrdersToShoppingCart(ordersDTO, userId);
        if (!result) {
            return JsonModel.error("失败!");
        }
        return JsonModel.success("成功!");
    }

    /**
     * 根据id获取订单信息
     *
     * @param request   .
     * @param ordersDTO .
     * @return .
     */
    @RequestMapping(value = "/orders/getById", method = RequestMethod.POST)
    public JsonModel<OrdersDTO> getOrdersById(HttpServletRequest request, @RequestBody OrdersDTO ordersDTO) {
        log.info("根据id获取订单信息,ordersDTO:[{}]", ordersDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        OrdersDTO result = orderService.getOrdersById(ordersDTO, userId);
        if (Objects.isNull(result)) {
            return JsonModel.error("查询失败!");
        }
        return JsonModel.success(result);
    }

    /**
     * 再来一单
     *
     * @param request   .
     * @param ordersDTO .
     * @return .
     */
    @RequestMapping(value = "/orders/again", method = RequestMethod.POST)
    public JsonModel<Orders> addOrdersAgain(HttpServletRequest request, @RequestBody OrdersDTO ordersDTO) {
        log.info("再来一单,ordersDTO:[{}]", ordersDTO);

        Orders result = orderService.addOrdersAgain(ordersDTO);
        if (Objects.isNull(result)) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success(result);
    }

    /**
     * 修改订单信息ordersStatus
     *
     * @param request   .
     * @param ordersDTO .
     * @return .
     */
    @RequestMapping(value = "/orders/user/editOrdersStatus", method = RequestMethod.POST)
    public JsonModel<String> editOrdersStatus(HttpServletRequest request, @RequestBody OrdersDTO ordersDTO) {
        log.info("修改订单信息ordersStatus和payStatus,ordersDTO:[{}]",ordersDTO);

        Boolean result = orderService.editOrdersStatus(ordersDTO);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }
}