package com.ruoyi.project.warehouse.mapper;

import com.ruoyi.project.warehouse.domain.BusiOrder;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.output.AdminReportOutput;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-13
 */
public interface BusiOrderMapper {
    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    public BusiOrder selectBusiOrderById(Long id);

    /**
     * 查询订单列表
     *
     * @param busiOrder 订单
     * @return 订单集合
     */
    public List<BusiOrder> selectBusiOrderList(BusiOrder busiOrder);

    /**
     * 新增订单
     *
     * @param busiOrder 订单
     * @return 结果
     */
    public int insertBusiOrder(BusiOrder busiOrder);

    /**
     * 修改订单
     *
     * @param busiOrder 订单
     * @return 结果
     */
    public int updateBusiOrder(BusiOrder busiOrder);

    /**
     * 删除订单
     *
     * @param id 订单主键
     * @return 结果
     */
    public int deleteBusiOrderById(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiOrderByIds(Long[] ids);

    /**
     * 查询报表
     * @return
     */
    AdminReportOutput queryReport(QueryChartInput chartInput);
    /**
     * 查询报表
     * @return
     */
    List<AdminReportOutput> queryReportHistory(QueryChartInput chartInput);
    
}
