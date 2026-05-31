package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.output.AdminReportOutput;
import com.ruoyi.project.warehouse.service.IBusiOrderService;
import com.ruoyi.project.warehouse.service.IBusiPayHistoryService;
import com.ruoyi.project.warehouse.service.IChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 快递客户Service业务层处理
 * 
 * @author liuzhen
 * @date 2023-01-15
 */
@Service
public class ChartServiceImpl implements IChartService
{
    @Resource
    IBusiPayHistoryService payHistoryService;
    @Autowired
    IBusiOrderService orderService;
    @Override

    public JSONObject lineChart(QueryChartInput chartInput) {
        JSONObject resultData =new JSONObject();
        BigDecimal amount = payHistoryService.queryTotalAmount();
        resultData.set("totalPayAmount",amount);

        AdminReportOutput adminReportOutput = orderService.queryReport();
        // 查询订单数量
        resultData.set("totalOderNumber",adminReportOutput.getTotalOderNumber());
        //运费
        resultData.set("totalExpressMoney",adminReportOutput.getTotalExpressMoney());
        //打包费
        resultData.set("totalPackageMoney",adminReportOutput.getTotalPackageMoney());
        //查询盈利金额
        resultData.set("totalProfitMoney",adminReportOutput.getTotalProfitMoney());

        return resultData;
    }

    @Override
    public List<AdminReportOutput> queryProfit(QueryChartInput chartInput) {
        ////按照每月，显示出来单量多少，运费多少，打包费多少，总的盈利是多少

        List<AdminReportOutput> adminReportOutputList =orderService.queryReportHistory(chartInput);
        return adminReportOutputList;
    }


}
