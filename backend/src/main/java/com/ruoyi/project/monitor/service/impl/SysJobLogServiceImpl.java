package com.ruoyi.project.monitor.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.monitor.domain.SysJobLog;
import com.ruoyi.project.monitor.mapper.SysJobLogMapper;
import com.ruoyi.project.monitor.service.ISysJobLogService;

/**
 * 调度日志 服务层处理
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService
{
    @Autowired
    private SysJobLogMapper jobLogMapper;

    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog)
    {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    @Override
    public SysJobLog selectJobLogById(Long jobLogId)
    {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }

    @Override
    public int deleteJobLogByIds(Long[] jobLogIds)
    {
        return jobLogMapper.deleteJobLogByIds(jobLogIds);
    }

    @Override
    public void cleanJobLog()
    {
        jobLogMapper.cleanJobLog();
    }
}
