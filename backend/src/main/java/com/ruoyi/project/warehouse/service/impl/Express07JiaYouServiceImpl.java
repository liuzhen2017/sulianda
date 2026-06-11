package com.ruoyi.project.warehouse.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class Express07JiaYouServiceImpl extends ExpressServiceBaseService {

    @Override
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput) {
        throw unsupported();
    }

    @Override
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput) {
        throw unsupported();
    }

    @Override
    public List<CountryListVo> queryCountryList() {
        return Collections.emptyList();
    }

    @Override
    public List<CountryListVo> getShippingMethod() {
        throw unsupported();
    }

    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {
        throw unsupported();
    }

    private ServiceException unsupported() {
        return new ServiceException("佳邮物流渠道已识别，但暂未接入实现");
    }
}
