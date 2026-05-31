package com.ruoyi.project.warehouse.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;

import java.util.List;

/**
 * 快递基类
 */
public abstract class   ExpressServiceBaseService {

    private BusiExpress busiExpress;
    public void setBusiExpress(BusiExpress busiExpress){
        this.busiExpress =busiExpress;
    }

    public BusiExpress getBusiExpress() {
        return busiExpress;
    }

    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public abstract AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput);

    /**
     * 打印面单
     *
     * @return
     */
    public abstract List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput);

    /**
     * 查询国家选项
     * @return
     */
    public  List<CountryListVo> queryCountryList(){return null;}

    /**
     * 查询渠道信息
     * @return
     */
    public  List<CountryListVo> getShippingMethod() {
        return null;
    }

    /**
     * 创建订单
     * @return
     */
    public abstract OrderResultOutput createOrder(ExpressOrderInput expressOrderInput);


}
