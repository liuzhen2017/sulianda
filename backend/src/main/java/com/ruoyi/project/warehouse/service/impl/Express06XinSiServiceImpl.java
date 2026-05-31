package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.mchange.util.Base64Encoder;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.OrderInput06;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.ExpressFreeOutput;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author liuzhen
 * @see http://www.sz56t.com:8090/plugins/servlet/mobile#content/view/3473454
 */
@Service
@Slf4j
public class Express06XinSiServiceImpl extends ExpressServiceBaseService {


    private static final String query_express_base_info ="http://159.75.95.216:8082";
    private static final String print_lable_base_info ="http://159.75.95.216:8089";
    private Token token;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class Token{
        private String customer_id;
        private String customer_userid;
        private String ack;
    }

    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){
        return null;
    }

    /**
     * 登陆
     *
     * @return
     */
    private void login(){
        if(ObjectUtil.isNotNull(token)){
            return;
        }
        String url =query_express_base_info+"/selectAuth.htm?username=TEST&password=123456"
                .replace("TEST",getBusiExpress().getAuthorizationNo())
                .replace("123456",getBusiExpress().getAuthorizationCode());
        String s = HttpUtil.get(url);
        if(StringUtils.isEmpty(s)){
            throw new RuntimeException("===异常返回==");
        }
        s =s.replaceAll("'","\"");
        token = JSONObject.parseObject(s, Token.class);
        if(! "true".equals(token.ack)){
            token =null;
            throw new RuntimeException("===登陆失败==");
        }
    }
    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput){

        String url =print_lable_base_info+"/order/FastRpt/PDF_NEW.aspx?Format=A4_EMS_BGD.frx&PrintType=1&order_id="+printExpressInfoInput.getOrderNo();
        String s = HttpUtil.get(url);
        checkResult(s);
        String pdfUrl=print_lable_base_info+ s.split("a href=\"")[1].split("\">here")[0];
        return Arrays.asList(
                PrintExpressOutput.builder()
                        .lableFile(pdfUrl)
                        .build()
        );
    }
    private void checkResult(String result){
        if(StringUtils.isEmpty(result)){
            throw new RuntimeException("=====请求异常==");
        }
        result= URLDecoder.decode(result);
        if(result.indexOf("ack") >0 && ! "true".equals(JSONObject.parseObject(result).getString("ack"))){
            throw new RuntimeException("===异常返回=msg=="+JSONObject.parseObject(result).getString("message"));
        }
    }

    /**
     * 查询国家选项
     * @return
     */
    public List<CountryListVo> queryCountryList(){
        return null;
    }
    /**
     * 查询快递方式
     * @return
     */
    public List<CountryListVo> getShippingMethod(){
        String body = HttpUtil.get(query_express_base_info+"/getProductList.htm");
        if(StringUtils.isEmpty(body)){
            throw new RuntimeException("=====请求异常==");
        }
        body= URLDecoder.decode(body);

        body =  body.replaceAll("product_id","code").replace("product_shortname","cnname");
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
        //登陆一次
        login();
        OrderInput06 orderInput06 = expressOrderInput.toTranOrder06();
        orderInput06.setCustomer_id(token.customer_id);
        orderInput06.setCustomer_userid(token.getCustomer_userid());


        String s = null;
        try {
            s = HttpUtil.get(query_express_base_info + "/createOrderApi.htm?param="+ URLEncoder.encode(JSONObject.toJSONString(orderInput06),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        checkResult(s);
        log.info("=======接口返回=={]=====",s);
        Result result =JSONObject.parseObject(s, Result.class);

        return OrderResultOutput.builder()
                .refrenceNo(expressOrderInput.getReferenceNo())
                .orderId(result.order_id)
                .shippingMethodNo(result.tracking_number)
                .build();

    }




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class Result{
        private String ack;
        private String message;
        /**id需要保存，用于打印标签**/
        private String order_id;
        /**
         * 跟踪号
         */
        private String tracking_number;


    }

}
