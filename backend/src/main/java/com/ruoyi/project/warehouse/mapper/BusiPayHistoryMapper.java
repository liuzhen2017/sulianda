package com.ruoyi.project.warehouse.mapper;

import com.ruoyi.project.warehouse.domain.BusiPayHistory;
import com.ruoyi.project.warehouse.input.QueryChartInput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户充值明细Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-16
 */
public interface BusiPayHistoryMapper {
    /**
     * 查询账户充值明细
     *
     * @param id 账户充值明细主键
     * @return 账户充值明细
     */
    public BusiPayHistory selectBusiPayHistoryById(Long id);

    /**
     * 查询账户充值明细列表
     *
     * @param busiPayHistory 账户充值明细
     * @return 账户充值明细集合
     */
    public List<BusiPayHistory> selectBusiPayHistoryList(BusiPayHistory busiPayHistory);

    public BusiPayHistory selectLastAndBusiPayHistoryById(Long id);

    /**
     * 新增账户充值明细
     *
     * @param busiPayHistory 账户充值明细
     * @return 结果
     */
    public int insertBusiPayHistory(BusiPayHistory busiPayHistory);

    /**
     * 修改账户充值明细
     *
     * @param busiPayHistory 账户充值明细
     * @return 结果
     */
    public int updateBusiPayHistory(BusiPayHistory busiPayHistory);

    /**
     * 删除账户充值明细
     *
     * @param id 账户充值明细主键
     * @return 结果
     */
    public int deleteBusiPayHistoryById(Long id);

    /**
     * 批量删除账户充值明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiPayHistoryByIds(Long[] ids);

    /**
     * 查询已经审批后的充值金额
     * @return
     */
    BigDecimal queryTotalAmount();

    List<Map<String,Object>> queryRechargeHistory(QueryChartInput chartInput);
}
