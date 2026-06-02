package com.ruoyi.project.monitor.service;

import java.util.List;
import com.ruoyi.project.monitor.domain.SysJobLog;

/**
 * 调度日志 服务层
 */
public interface ISysJobLogService
{
    List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    SysJobLog selectJobLogById(Long jobLogId);

    void addJobLog(SysJobLog jobLog);

    int deleteJobLogByIds(Long[] jobLogIds);

    void cleanJobLog();
}
