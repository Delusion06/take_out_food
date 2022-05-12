package com.ex.echo.service;

import com.ex.echo.dto.AddressDTO;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface AddressService {

    /**
     * 根据id查询地址信息
     *
     * @param addressDTO .
     * @return .
     */
    public AddressDTO getAddressById(AddressDTO addressDTO);

    /**
     * 查询默认地址
     *
     * @return .
     */
    public AddressDTO getAddressIsDefault(Long userId);

    /**
     * 添加地址信息
     *
     * @param addressDTO .
     * @param userId .
     * @return .
     */
    public Boolean addAddress(AddressDTO addressDTO,Long userId);

    /**
     * 修改地址信息
     *
     * @param addressDTO .
     * @param userId .
     * @return .
     */
    public Boolean editAddress(AddressDTO addressDTO,Long userId);

    /**
     * 修改地址信息is_default
     *
     * @param addressDTO .
     * @param userId .
     * @return .
     */
    public Boolean editAddressIsDefault(AddressDTO addressDTO,Long userId);

    /**
     * 修改地址信息is_deleted
     *
     * @param addressDTO .
     * @param userId .
     * @return .
     */
    public Boolean editAddressIsDeleted(AddressDTO addressDTO,Long userId);

    /**
     * 条件查询
     *
     * @param userId .
     * @return .
     */
    public List<AddressDTO> addressList(Long userId);

}