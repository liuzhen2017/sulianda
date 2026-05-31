package com.ruoyi.project.warehouse.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fpx.api.constants.AmbientEnum;
import com.fpx.api.model.AffterentParam;
import com.fpx.api.utils.ApiHttpClientUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.OrderInput01;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.mapper.BusiExpressMapper;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.ExpressFreeOutput;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @see http://open.4px.com/apiInfo/merchant
 */
@Service
@Slf4j
public class Express01DisifangServiceImpl extends ExpressServiceBaseService {

    @Resource
    BusiExpressMapper busiExpressMapper;
    private static final String query_express_base_info ="https://open.4px.com/router/api/service";


    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){

        String body = publicRequest(getBusiExpress(),null,"feetrail");
        ExpressFreeOutput expressFree = JSONObject.parseObject(body,ExpressFreeOutput.class);
        return AjaxResult.success(expressFree);
    }

    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput){
        Map<String,Object> obj =new HashMap<>();
        obj.put("request_no", Arrays.asList(printExpressInfoInput.getListOrder()[0].getReferenceNo()));
        Object cacheObject = redisCache.getCacheObject(printExpressInfoInput.getListOrder()[0].getReferenceNo());
        obj.put("logistics_product_code",cacheObject);
        obj.put("label_size","label_80x90");
        String body = publicRequest(getBusiExpress(),obj,"ds.xms.label.getlist");

        return Arrays.asList(PrintExpressOutput.builder()
                        .lableFile(JSONObject.parseObject(body).getString("logistics_label"))
                .build());
    }

    /**
     * 查询快递方式
     * @return
     */
    public List<CountryListVo> getShippingMethod(){
        Map<String,Object> obj =new HashMap<>();
        obj.put("transport_mode","1");
        String body = publicRequest(getBusiExpress(),obj,"ds.xms.logistics_product.getlist");
        body =body.replaceAll("logistics_product_code","code")
                .replaceAll("logistics_product_name_cn","cnname")
                .replaceAll("logistics_product_name_en","enname")

        ;
        List<CountryListVo> countryListVoList =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        return countryListVoList;
    }
    /**
     * 查询国家选项
     * @return
     */
    public List<CountryListVo> queryCountryList(){
        //todo 没有国家
        String body = publicRequest(getBusiExpress(),null,"type");
        List<CountryListVo> countryListVoList =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        return countryListVoList;
    }

    @Autowired
    private RedisCache redisCache;
    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {
        OrderInput01 input01 = expressOrderInput.toTranOrder01();
        input01.setEori_no((input01.getIoss_no()+"").replace("/",""));
        Map<String,Object> obj=   (Map<String,Object>) JSON.parse(JSONObject.toJSONString(input01));
        String body = publicRequest(getBusiExpress(),obj,"ds.xms.order.create");
        JSONObject res =JSONObject.parseObject(body);
        redisCache.setCacheObject(res.getString("ref_no"),expressOrderInput.getShippingMethod(), 15, TimeUnit.DAYS);
        return OrderResultOutput.builder()
                .refrenceNo(res.getString("ref_no"))
                .orderId(res.getString("ds_consignment_no"))
                .shippingMethodNo("logistics_channel_no")
                .build();
    }

    public String publicRequest(BusiExpress busiExpress, Map<String,Object> obj, String type){
        AffterentParam affterentParam =new AffterentParam();
        affterentParam.setAppKey(busiExpress.getAuthorizationNo());
        affterentParam.setAppSecret(busiExpress.getAuthorizationCode());
        affterentParam.setFormat("json");
        affterentParam.setMethod(type);
        affterentParam.setVersion("1.0.0");
        affterentParam.setLanguage("cn");
        String response = ApiHttpClientUtils.apiJsongPost(affterentParam,obj, AmbientEnum.FORMAT_ADDRESS);
        JSONObject jsonObject =JSONObject.parseObject(response);
        if(!jsonObject.getString("result").equals("1")){
            throw new RuntimeException(jsonObject.getJSONArray("errors").getJSONObject(0).getString("error_msg"));
        }
        return  jsonObject.getString("data");

    }
    @Data
    public class Result{
        String  result;
        String msg;
        String data;
    }
}
