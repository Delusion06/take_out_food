package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.NoticeDTO;
import com.ex.echo.entity.Notice;
import com.ex.echo.service.NoticeService;
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
 * @Description: 公告信息管理
 */
@Slf4j
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页查询
     *
     * @param page     .
     * @param pageSize .
     * @param notice   .
     * @return .
     */
    @RequestMapping(value = "/notice/page", method = RequestMethod.GET)
    public JsonModel<Page> page(int page, int pageSize, String notice) {
        Page<NoticeDTO> result = noticeService.noticeList(page, pageSize, notice);
        return JsonModel.success(result);
    }

    /**
     * 根据id查询公告信息
     *
     * @param noticeDTO .
     * @return .
     */
    @RequestMapping(value = "/notice/getById", method = RequestMethod.POST)
    public JsonModel<Notice> getNoticeById(HttpServletRequest request, @RequestBody NoticeDTO noticeDTO) {
        Notice result = noticeService.getNoticeById(noticeDTO);
        if (Objects.isNull(result)) {
            return JsonModel.error("查询出错!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加公告信息
     *
     * @param request   .
     * @param noticeDTO .
     * @return .
     */
    @RequestMapping(value = "/notice/add", method = RequestMethod.POST)
    public JsonModel<String> addNotice(HttpServletRequest request, @RequestBody NoticeDTO noticeDTO) {
        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = noticeService.addNotice(noticeDTO,employeeId);
        if (!result) {
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success("添加成功");
    }

    /**
     * 修改公告信息
     *
     * @param request   .
     * @param noticeDTO .
     * @return .
     */
    @RequestMapping(value = "/notice/edit", method = RequestMethod.POST)
    public JsonModel<String> editNotice(HttpServletRequest request, @RequestBody NoticeDTO noticeDTO) {
        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = noticeService.editNotice(noticeDTO,employeeId);
        if (!result) {
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }
}
