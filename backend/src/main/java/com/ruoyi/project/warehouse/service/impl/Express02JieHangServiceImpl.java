package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.mapper.BusiExpressMapper;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.ExpressFreeOutput;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class Express02JieHangServiceImpl extends ExpressServiceBaseService {

    @Resource
    BusiExpressMapper busiExpressMapper;
//    private static final String query_express_base_info ="http://xt.jiehang.net/PostInterfaceService?method=#";
    private static final String query_express_base_info ="http://xt.jiehang.net/PostInterfaceService?method=#";

    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){
        JSONObject map =new JSONObject();
        map.put("CountryCode",expressFreeInput.getCountryCode());
        map.put("Weight",expressFreeInput.getWeight());
        map.put("PostCode",expressFreeInput.getPostCode());
        String body = publicRequest(getBusiExpress(),map,"searchPrice");
        ExpressFreeOutput expressFree = JSONObject.parseObject(body,ExpressFreeOutput.class);
//        HttpUtil.get(GET_EXPRESS_FREE_URL, queryInfo);
        return AjaxResult.success(expressFree);
    }

    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput){
        String body = publicRequest(getBusiExpress(), JSONObject.parseObject(JSONObject.toJSONString(printExpressInfoInput.changePrint02())),"printOrderLabel");
        return Arrays.asList(PrintExpressOutput.builder()
                        .lableFile(body)
                .build());
    }


    List<CountryListVo> countryListVoList =new ArrayList<>();
    /**
     * 查询快递方式
     * @return
     */
    public List<CountryListVo> getShippingMethod(){
        if(! countryListVoList.isEmpty()){
            return countryListVoList;
        }
        List<String> channelData = FileUtil.readLines("./data/channel_code_02_jiehang.json", StandardCharsets.UTF_8);

        for (String channel : channelData) {
            String[] split = channel.split(";");
            countryListVoList.add(CountryListVo.builder()
                            .cnname(split[1])
                            .enname(split[1])
                            .code(split[0])
                    .build());
        }
        return countryListVoList;
    }
    /**
     * 查询国家选项
     * @return
     */
    public List<CountryListVo> queryCountryList(){

        return null;
    }


    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("OrderType","1");
        jsonObject.put("OrderDatas",Arrays.asList(expressOrderInput.toTranOrder02()));
        String body = publicRequest(getBusiExpress(),jsonObject,"createOrder");
        List<CreateOrderResult> createOrderResults = JSONUtil.toList(body, CreateOrderResult.class);

        return OrderResultOutput.builder()
                .orderId(createOrderResults.get(0).corpBillid)
                .refrenceNo(createOrderResults.get(0).customerNumber)
                .shippingMethodNo(createOrderResults.get(0).trackNumber)
                .build();


    }

    public String publicRequest(BusiExpress busiExpress,  JSONObject queryInfo, String type){

        Map<String,Object> verify =new HashMap<>();
        verify.put("Clientid",busiExpress.getAuthorizationNo());
        verify.put("Token",busiExpress.getAuthorizationCode());
        queryInfo.put("Verify",verify);
        queryInfo.put("OrderType",1);
        String info = HttpUtil.post(query_express_base_info.replace("#",type), JSONObject.toJSONString(queryInfo));
        JSONObject object = JSONObject.parseObject(info);

        if ("error".equals(object.getString("statusCode"))){
            throw new RuntimeException("请求失败!,msg="+object.getString("returnDatas"));
        }
        if("createOrder".equals(type)) {
            return object.getString("returnDatas");
        }else if("printOrderLabel".equals(type)){
            return object.getString("url");
        }

        return "";

    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class CreateOrderResult{
        /**
         * 单个订单状态 (success：成功，error：失败)
         */
        private String statusCode;
        /**
         *公司订单号 (需要保存,后续打印,获取转单号有用到该字段)
         */
        private String corpBillid;
        /**
         * 客户订单号
         */
        private String trackNumber;
        /**
         * 转单号,服务商单号,跟踪号
         */
        private String customerNumber;
        /**
         * 子单号
         */
        private String childTrackingNumber;
        /**
         * UPS 短单号
         */
        private String shipmentid;
        /**
         *
         */
        private String message;
    }

}
