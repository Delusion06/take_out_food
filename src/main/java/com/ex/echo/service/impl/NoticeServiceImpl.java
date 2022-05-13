package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.NoticeDTO;
import com.ex.echo.entity.EmployeeInfo;
import com.ex.echo.entity.Notice;
import com.ex.echo.mapper.EmployeeInfoMapper;
import com.ex.echo.mapper.NoticeMapper;
import com.ex.echo.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;

    @Override
    public Page<NoticeDTO> noticeList(int page, int pageSize, String notice) {
        Page<NoticeDTO> noticeDTOPage = new Page<>();
        Page<Notice> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(notice), Notice::getNotice, notice);

        noticeMapper.selectPage(pageInfo, wrapper);

        List<Notice> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo, noticeDTOPage, "records");

        List<NoticeDTO> noticeDTOList = records
                .stream().map(item -> {
                    NoticeDTO noticeDTO = new NoticeDTO();
                    BeanUtils.copyProperties(item, noticeDTO);

                    // 获取更新人姓名
                    long employeeId = item.getUpdateEmployeeId();
                    LambdaQueryWrapper<EmployeeInfo> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(EmployeeInfo::getEmployeeId, employeeId);
                    EmployeeInfo employeeInfo = employeeInfoMapper.selectOne(wrapper1);
                    if (StringUtils.isNotEmpty(employeeInfo.getEmployeeName())) {
                        noticeDTO.setUpdateEmployeeName(employeeInfo.getEmployeeName());
                    }

                    return noticeDTO;
                }).collect(Collectors.toList());
        noticeDTOPage.setRecords(noticeDTOList);
        return noticeDTOPage;
    }

    @Override
    public Notice getNoticeById(NoticeDTO noticeDTO) {
        Notice notice = noticeMapper.selectById(noticeDTO.getId());
        if (Objects.isNull(notice)) {
            return null;
        }
        return notice;
    }

    @Override
    public Boolean addNotice(NoticeDTO noticeDTO, Long employeeId) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO, notice);
        notice.setCreateEmployeeId(employeeId);
        notice.setUpdateEmployeeId(employeeId);
        try {
            noticeMapper.insert(notice);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean editNotice(NoticeDTO noticeDTO, Long employeeId) {
        Notice notice = new Notice();
        notice.setId(noticeDTO.getId());
        notice.setNotice(noticeDTO.getNotice());

        notice.setUpdateEmployeeId(employeeId);
        try {
            noticeMapper.updateById(notice);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean editStatus(NoticeDTO noticeDTO, Long employeeId) {
        if (noticeDTO.getStatus() == 1) {
            LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Notice::getStatus, 1);

            Notice notice = new Notice();
            notice.setStatus(0);
            noticeMapper.update(notice, wrapper);
        }
        Notice newNotice = new Notice();
        newNotice.setId(noticeDTO.getId());
        newNotice.setStatus(noticeDTO.getStatus());
        try {
            noticeMapper.updateById(newNotice);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Notice getNoticeByStatus() {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus,1);
        try {
            return noticeMapper.selectOne(wrapper);
        }catch (Exception e){
            return new Notice();
        }
    }
}
