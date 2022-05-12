package com.ex.echo.controller;

import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.AddressDTO;
import com.ex.echo.service.AddressService;
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
 * @Description: 地址信息管理
 */
@Slf4j
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 根据id查询地址信息
     *
     * @param request    .
     * @param addressDTO .
     * @return .
     */
    @RequestMapping(value = "/address/getById", method = RequestMethod.POST)
    public JsonModel<AddressDTO> getAddressById(HttpServletRequest request, @RequestBody AddressDTO addressDTO) {
        log.info("根据id查询地址信息,addressDTO:[{}]", addressDTO);

        AddressDTO result = addressService.getAddressById(addressDTO);
        if (Objects.isNull(result)) {
            return JsonModel.error("查询出错!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加地址信息
     */
    @RequestMapping(value = "/address/add", method = RequestMethod.POST)
    public JsonModel<String> addAddress(HttpServletRequest request, @RequestBody AddressDTO addressDTO) {
        log.info("添加地址信息,addressDTO:[{}]", addressDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        boolean result = addressService.addAddress(addressDTO, userId);
        if (!result) {
            return JsonModel.error("添加出错!");
        }
        return JsonModel.success("添加成功!");
    }

    /**
     * 修改地址信息
     *
     * @param request .
     * @param addressDTO .
     * @return .
     */
    @RequestMapping(value = "/address/edit", method = RequestMethod.POST)
    public JsonModel<String> editAddress(HttpServletRequest request, @RequestBody AddressDTO addressDTO){
        log.info("修改地址信息,addressDTO:[{}]", addressDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        boolean result = addressService.editAddress(addressDTO,userId);
        if (!result) {
            return JsonModel.error("修改出错!");
        }
        return JsonModel.success("修改成功!");
    }

    /**
     * 设置默认地址
     */
    @RequestMapping(value = "/address/editDefault", method = RequestMethod.POST)
    public JsonModel<String> editAddressDefault(HttpServletRequest request, @RequestBody AddressDTO addressDTO) {
        log.info("设置默认地址,addressDTO:[{}]", addressDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        boolean result = addressService.editAddressIsDefault(addressDTO,userId);
        if (!result){
            return JsonModel.error("设置出错!");
        }
        return JsonModel.success("设置成功!");
    }

    /**
     * 删除地址信息
     *
     * @param request .
     * @param addressDTO .
     * @return .
     */
    @RequestMapping(value = "/address/delete", method = RequestMethod.POST)
    public JsonModel<String> deleteAddress(HttpServletRequest request, @RequestBody AddressDTO addressDTO) {
        log.info("删除地址信息,addressDTO:[{}]",addressDTO);

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        boolean result = addressService.editAddressIsDeleted(addressDTO,userId);
        if (!result){
            return JsonModel.error("删除出错!");
        }
        return JsonModel.success("删除成功!");
    }
    /**
     * 查询默认地址
     */
    @RequestMapping(value = "/address/default", method = RequestMethod.POST)
    public JsonModel<AddressDTO> getDefault(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        AddressDTO addressDTO = addressService.getAddressIsDefault(userId);

        if (Objects.isNull(addressDTO)) {
            return JsonModel.error("暂无默认地址!");
        }
        return JsonModel.success(addressDTO);
    }

    /**
     * 查询指定用户的全部地址
     */
    @RequestMapping(value = "/address/list", method = RequestMethod.POST)
    public JsonModel<List<AddressDTO>> list(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        List<AddressDTO> result = addressService.addressList(userId);
        return JsonModel.success(result);
    }
}