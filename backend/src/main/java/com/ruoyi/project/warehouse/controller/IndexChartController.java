package com.ruoyi.project.warehouse.controller;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import com.ruoyi.project.warehouse.service.IChartService;
import com.ruoyi.project.warehouse.service.impl.Express01DisifangServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报表Controller
 * 
 * @author liuzhen
 * @date 2023-01-12
 */
@RestController
@RequestMapping("/chart-index")
public class IndexChartController extends BaseController
{

    @Autowired
    IChartService chartService;
    /**
     * 01 查询订单数量
     * 02 查询充值金额
     * 03 运费
     * 04 打包费
     * 05 查询盈利金额
     */
    @GetMapping("/")
    public JSONObject lineChart(QueryChartInput chartInput)
    {
        //总系统需要按月份按年份去总结，总出了多少单，充值多少运费，运费多少钱，打包多少钱，盈利多少
        chartInput.checkDate();
        return chartService.lineChart(chartInput);
    }

    /**
     * 01 查询订单数量
     * 02 查询充值金额
     * 03 运费
     * 04 打包费
     * 05 查询盈利金额
     */
    @GetMapping("/get-profit")
    public TableDataInfo queryProfit(QueryChartInput chartInput){
        //总系统需要按月份按年份去总结，总出了多少单。充值多少运费。运费多少钱，打包多少钱，盈利多少
        chartInput.checkDate();
        return getDataTable(chartService.queryProfit(chartInput));
    }
}
