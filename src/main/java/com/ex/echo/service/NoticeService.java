package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.NoticeDTO;
import com.ex.echo.entity.Notice;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface NoticeService{

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @param notice .
     * @return .
     */
    public Page<NoticeDTO> noticeList(int page,int pageSize,String notice);

    /**
     * 通过id查询公告信息
     *
     * @param noticeDTO .
     * @return .
     */
    public Notice getNoticeById(NoticeDTO noticeDTO);

    /**
     * 添加公告信息
     *
     * @param noticeDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean addNotice(NoticeDTO noticeDTO,Long employeeId);

    /**
     * 修改公告信息
     *
     * @param noticeDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean editNotice(NoticeDTO noticeDTO,Long employeeId);
}
