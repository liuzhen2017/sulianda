package com.ruoyi.project.warehouse.service;

import com.ruoyi.project.warehouse.domain.BusiOrder;
import com.ruoyi.project.warehouse.domain.BusiOrderEvent;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.output.AdminReportOutput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2023-01-13
 */
public interface IBusiOrderService {
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
     * 导入订单数据
     *
     * @param busiOrderList 订单数据列表
     * @param operName 操作用户
     * @return 结果
     */
    public String importUser(List<BusiOrder> busiOrderList, String operName, String type);

    /**
     * 修改订单
     *
     * @param busiOrder 订单
     * @return 结果
     */
    public int updateBusiOrder(BusiOrder busiOrder);

    /**
     * 打印面单
     *
     * @param
     * @return 结果
     */
    public String printInfo(Long id);

    /**
     * 操作订单(入库，发货)
     *
     * @param busiOrder
     * @return
     */
    public int operaterBusiOrder(BusiOrder busiOrder);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteBusiOrderByIds(Long[] ids);

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    public int deleteBusiOrderById(Long id);

    AdminReportOutput queryReport();

    List<AdminReportOutput> queryReportHistory(QueryChartInput chartInput);

    List<BusiOrderEvent> selectBusiOrderEventList(Long orderId);
}
