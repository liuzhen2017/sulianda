package com.ruoyi.project.warehouse.mapper;

import java.util.List;
import com.ruoyi.project.warehouse.domain.BusiPayHistoryIndex;

/**
 * 账户充值明细索引Mapper接口
 * 
 * @author liuzhen
 * @date 2023-04-06
 */
public interface BusiPayHistoryIndexMapper 
{
    /**
     * 查询账户充值明细索引
     * 
     * @param id 账户充值明细索引主键
     * @return 账户充值明细索引
     */
    public BusiPayHistoryIndex selectBusiPayHistoryIndexById(Long id);

    /**
     * 查询账户充值明细索引列表
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 账户充值明细索引集合
     */
    public List<BusiPayHistoryIndex> selectBusiPayHistoryIndexList(BusiPayHistoryIndex busiPayHistoryIndex);

    /**
     * 新增账户充值明细索引
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 结果
     */
    public int insertBusiPayHistoryIndex(BusiPayHistoryIndex busiPayHistoryIndex);

    /**
     * 修改账户充值明细索引
     * 
     * @param busiPayHistoryIndex 账户充值明细索引
     * @return 结果
     */
    public int updateBusiPayHistoryIndex(BusiPayHistoryIndex busiPayHistoryIndex);

    /**
     * 删除账户充值明细索引
     * 
     * @param id 账户充值明细索引主键
     * @return 结果
     */
    public int deleteBusiPayHistoryIndexById(Long id);

    /**
     * 批量删除账户充值明细索引
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiPayHistoryIndexByIds(Long[] ids);
}
