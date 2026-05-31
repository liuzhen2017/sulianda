package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.ExpressFreeOutput;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhen
 * @see http://customer.ydhex.com/usercenter/manager/api_document.aspx
 */
@Service
@Slf4j
public class Express05YiDaServiceImpl extends ExpressServiceBaseService {


    private static final String query_express_base_info ="http://customer.ydhex.com/webservice/PublicService.asmx/ServiceInterfaceUTF8 ";

    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){
        Map<String,Object> dataMap =new HashMap<>();
        dataMap.put("country_code",expressFreeInput.getCountryCode());
        dataMap.put("weight",expressFreeInput.getWeight());
        dataMap.put("cargoType","w");
        dataMap.put("shipping_method",expressFreeInput.getShippingMethod());
        String body = publicRequest(getBusiExpress(),dataMap,"feetrail");
        ExpressFreeOutput expressFree = JSONObject.parseObject(body,ExpressFreeOutput.class);
        return AjaxResult.success(expressFree);
    }

    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput){
        ;
        String body = publicRequest(getBusiExpress(),printExpressInfoInput.changePrint05(),"getnewlabel");
        return JSONUtil.toList(body, PrintExpressOutput.class);
    }


    /**
     * 查询国家选项
     * @return
     */
    public List<CountryListVo> queryCountryList(){
        String body = publicRequest(getBusiExpress(),null,"getcountry");
        List<CountryListVo> countryListVoList =new ArrayList<>();
        List<CountryListVo> countryListVoList2 =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        for (int i = 0; i < countryListVoList.size(); i++) {
            countryListVoList2.add(JSONObject.parseObject(JSONObject.toJSONString(countryListVoList.get(i)),CountryListVo.class));
        }
        return countryListVoList2;
    }
    /**
     * 查询快递方式
     * @return
     */
    public List<CountryListVo> getShippingMethod(){
        String body = publicRequest(getBusiExpress(),null,"getshippingmethod");
        List<CountryListVo> countryListVoList =new ArrayList<>();
        List<CountryListVo> countryListVoList2 =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        for (int i = 0; i < countryListVoList.size(); i++) {
            countryListVoList2.add(JSONObject.parseObject(JSONObject.toJSONString(countryListVoList.get(i)),CountryListVo.class));
        }
        return countryListVoList2;
    }

    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {

        String body = publicRequest(getBusiExpress(),expressOrderInput,"createorder");

        return JSONObject.parseObject(body,OrderResultOutput.class);
    }

    public String publicRequest(BusiExpress busiExpress, Object obj, String type){
        Map<String,Object> queryInfo =new HashMap<>();
        queryInfo.put("appToken",busiExpress.getAuthorizationNo());
        queryInfo.put("appKey",busiExpress.getAuthorizationCode());
        queryInfo.put("serviceMethod",type);
        queryInfo.put("paramsJson", JSONObject.toJSONString(obj));
        log.info("====request info ={}=======",JSONObject.toJSONString(queryInfo));
        String info = HttpUtil.get(query_express_base_info, queryInfo);
        log.info("====response info ={}=======",info);
        Result result =JSONObject.parseObject(info,Result.class);
        if ("0".equals(result.getSuccess())){
            throw new RuntimeException("请求失败!,msg="+result.getCnmessage());
        }
        if("createorder".equals(type)){
            JSONObject jsonObject =JSONObject.parseObject(result.getData());
            //jsonObject.put("shipping_method_no",result.orderId);
            result.data= JSONUtils.toJSONString(jsonObject);
        }
        return result.getData();

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class Result{
        private String success;
        private String cnmessage;
        private String enmessage;
        private String data;
        @JSONField(name = "order_id")
        private String orderId;

    }

}
