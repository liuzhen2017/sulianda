package com.ruoyi.project.warehouse.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.output.AdminReportOutput;

import java.util.List;

/**
 * 商品Service接口
 *
 * @author ruoyi
 * @date 2023-01-12
 */
public interface IChartService {
    /**
     * 01 查询充值金额
     * 02 查询盈利金额
     * 03 查询订单数量
     * 04 查询发货数量
     */
    JSONObject lineChart(QueryChartInput chartInput);

    /**
     * 查询充值，下订单, 盈利 折线图
     *
     * @return
     */
    public List<AdminReportOutput> queryProfit(QueryChartInput chartInput);

}
