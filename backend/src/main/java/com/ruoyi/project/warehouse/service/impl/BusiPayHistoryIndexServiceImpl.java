package com.ruoyi.project.warehouse.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.warehouse.mapper.BusiPayHistoryIndexMapper;
import com.ruoyi.project.warehouse.domain.BusiPayHistoryIndex;
import com.ruoyi.project.warehouse.service.IBusiPayHistoryIndexService;

/**
 * 账户充值明细索引Service业务层处理
 * 
 * @author liuzhen
 * @date 2023-04-06
 */
@Service
public class BusiPayHistoryIndexServiceImpl implements IBusiPayHistoryIndexService 
{
    @Autowired
    private BusiPayHistoryIndexMapper busiPayHistoryIndexMapper;

    /**
     * 查询账户充值明细索引
     * 
     * @param id 账户充值明细索引主键
     * @return 账户充值明细索引
     */
    @Override
    public BusiPayHistoryIndex selectBusiPayHistoryIndexById(Long id)
    {
        return busiPayHistoryIndexMapper.selectBusiPayHistoryIndexById(id);
    }

    /**
     * 查询账户充值明细索引列表
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 账户充值明细索引
     */
    @Override
    public List<BusiPayHistoryIndex> selectBusiPayHistoryIndexList(BusiPayHistoryIndex busiPayHistoryIndex)
    {
        return busiPayHistoryIndexMapper.selectBusiPayHistoryIndexList(busiPayHistoryIndex);
    }

    /**
     * 新增账户充值明细索引
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 结果
     */
    @Override
    public int insertBusiPayHistoryIndex(BusiPayHistoryIndex busiPayHistoryIndex)
    {
        return busiPayHistoryIndexMapper.insertBusiPayHistoryIndex(busiPayHistoryIndex);
    }

    /**
     * 修改账户充值明细索引
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 结果
     */
    @Override
    public int updateBusiPayHistoryIndex(BusiPayHistoryIndex busiPayHistoryIndex)
    {
        return busiPayHistoryIndexMapper.updateBusiPayHistoryIndex(busiPayHistoryIndex);
    }

    /**
     * 批量删除账户充值明细索引
     * 
     * @param ids 需要删除的账户充值明细索引主键
     * @return 结果
     */
    @Override
    public int deleteBusiPayHistoryIndexByIds(Long[] ids)
    {
        return busiPayHistoryIndexMapper.deleteBusiPayHistoryIndexByIds(ids);
    }

    /**
     * 删除账户充值明细索引信息
     * 
     * @param id 账户充值明细索引主键
     * @return 结果
     */
    @Override
    public int deleteBusiPayHistoryIndexById(Long id)
    {
        return busiPayHistoryIndexMapper.deleteBusiPayHistoryIndexById(id);
    }
}
