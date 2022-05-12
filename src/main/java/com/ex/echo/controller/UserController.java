package com.ex.echo.controller;

import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.UserDTO;
import com.ex.echo.entity.User;
import com.ex.echo.service.UserService;
import com.ex.echo.service.impl.SmsServiceImpl;
import com.ex.echo.utils.ValidateCodeUtils;
import com.ex.echo.utils.ValidateUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 用户信息管理
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsServiceImpl smsService;

    /**
     * 发送手机短信验证码
     *
     * @param request .
     * @param userDTO .
     * @return .
     * @throws TencentCloudSDKException .
     */
    @RequestMapping(value = "/user/sendMsg", method = RequestMethod.POST)
    public JsonModel<String> sendMsg(HttpServletRequest request, @RequestBody UserDTO userDTO) throws TencentCloudSDKException {
        String phone = userDTO.getPhone();
        boolean result = ValidateUtils.isMobile(phone);
        if (!result) {
            return JsonModel.error("手机号格式错误!");
        }
        try {
            HttpSession session = request.getSession();
            Date lastDate = (Date) session.getAttribute("code_date");
            if (lastDate != null && (System.currentTimeMillis() - lastDate.getTime()) < (1000 * 120)) {
                return JsonModel.error("别点了，等" + (60 - ((System.currentTimeMillis() - lastDate.getTime()) / 1000)) + "秒后再点");
            } else {
                String code = ValidateCodeUtils.generateValidateCode(6).toString();

                log.info("发送手机短信验证码,code:[{}]", code);

                session.setAttribute(Constants.CODE, code);
                session.setAttribute(Constants.CODE_DATE, new Date());
                smsService.sendSmsCode(phone, code);
                return JsonModel.success("发送手机短信验证码成功!");
            }
        } catch (Exception e) {
            return JsonModel.error("发送手机短信验证码失败!");
        }
    }

    /**
     * 用户登录
     *
     * @param request .
     * @param userDTO .
     * @return .
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public JsonModel<User> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        User result = userService.userLogin(userDTO);
        if (Objects.isNull(result)) {
            return JsonModel.error("手机号或密码错误!");
        }
        if (result.getStatus() == 0) {
            return JsonModel.error("用户已注销!");
        }
        request.getSession().setAttribute(Constants.USER, result.getId());
        if (StringUtils.isEmpty(result.getUsername()) || result.getSex() == null || StringUtils.isEmpty(result.getIdCard())) {
            return JsonModel.errorT("用户信息尚未完善,即将前往完善信息");
        }
        return JsonModel.success(result);
    }

    /**
     * 用户退出登录
     *
     * @param request .
     * @return .
     */
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public JsonModel<String> logout(HttpServletRequest request) {
        // 判断是否存在该会话
        if (Objects.isNull(request.getSession().getAttribute(Constants.USER))) {
            return JsonModel.error("尚未登录!");
        }
        request.getSession().removeAttribute(Constants.USER);
        return JsonModel.success("退出成功!");
    }

    /**
     * 根据id查询用户信息
     *
     * @param request .
     * @return .
     */
    @RequestMapping(value = "/user/getById", method = RequestMethod.POST)
    public JsonModel<UserDTO> getUserById(HttpServletRequest request) {
        log.info("根据id查询用户信息.");

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);
        if (userId == null) {
            return JsonModel.error("尚未登录!");
        }
        UserDTO result = userService.getUserById(userId);
        if (Objects.isNull(result)) {
            return JsonModel.error("查无此人!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加用户信息
     *
     * @param request .
     * @param userDTO .
     * @return .
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public JsonModel<String> register(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        HttpSession session = request.getSession();
        String code = session.getAttribute(Constants.CODE).toString();
        String validCode = userDTO.getCode();
        if (!Objects.equals(code, validCode)) {
            return JsonModel.error("验证码不一致!");
        }

        User user = userService.getUserByPhone(userDTO);
        if (Objects.nonNull(user)) {
            return JsonModel.error("该手机号已注册!");
        }

        boolean result = userService.addUser(userDTO);
        if (!result) {
            return JsonModel.error("注册失败!");
        }
        return JsonModel.success("注册成功,即将进入登录页!");
    }

    /**
     * 修改密码
     *
     * @param request .
     * @param userDTO .
     * @return .
     */
    @RequestMapping(value = "/user/editPassword", method = RequestMethod.POST)
    public JsonModel<String> editPassword(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        Long userId = (Long) request.getSession().getAttribute(Constants.USER);
        boolean result = userService.editPassword(userDTO, userId);
        if (!result) {
            return JsonModel.error("修改密码失败!");
        }
        return JsonModel.success("修改密码成功!");
    }

    @RequestMapping(value = "/user/editUser", method = RequestMethod.POST)
    public JsonModel<String> editUser(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        Long userId = (Long) request.getSession().getAttribute(Constants.USER);
        if (userId == null) {
            return JsonModel.error("尚未登录!");
        }
        boolean result = userService.editUser(userDTO, userId);
        if (!result) {
            return JsonModel.error("修改个人信息出错!");
        }
        return JsonModel.success("修改成功!");
    }
}