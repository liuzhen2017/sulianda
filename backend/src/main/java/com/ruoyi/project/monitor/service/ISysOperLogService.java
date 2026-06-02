package com.ruoyi.project.monitor.service;

import java.util.List;
import com.ruoyi.project.monitor.domain.SysOperLog;

/**
 * 操作日志 服务层
 */
public interface ISysOperLogService
{
    void insertOperlog(SysOperLog operLog);

    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    int deleteOperLogByIds(Long[] operIds);

    SysOperLog selectOperLogById(Long operId);

    void cleanOperLog();
}
