package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.mapper.BusiExpressMapper;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @see  https://www.sfcservice.com/webservice
 */
@Service
@Slf4j
public class Express03SanTaiServiceImpl extends ExpressServiceBaseService {

    @Resource
    BusiExpressMapper busiExpressMapper;
    private static final String query_express_base_info ="https://www.sfcservice.com/ishipsvc/http-api";
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    RuoYiConfig ruoYiConfig;
    /**
     * 查询快递费
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){

       return null;
    }

    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput){


        String url ="http://www.sfcservice.com/order/print/index/?orderCodeList="+printExpressInfoInput.changePrint03().getOrderCodeList()+"&printType=1&isPrintDeclare=1&declare=0&ismerge=1&urluserid=OTY5&print_type=pdf&printSize=3";
        return Arrays.asList(PrintExpressOutput.builder()
                        .lableFile(url)
                .build());

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
        String body = publicRequest(getBusiExpress(),null,"getShipTypes");
        body = body.replace("method_code","code").replace("en_name","enname")
                .replace("cn_name","cnname")
        ;
        List<CountryListVo> countryListVoList =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        List<CountryListVo> countryListVoList2 =new ArrayList<>();
        countryListVoList = JSONObject.parseObject(body,countryListVoList.getClass());
        for (int i = 0; i < countryListVoList.size(); i++) {
            countryListVoList2.add(JSONObject.parseObject(JSONObject.toJSONString(countryListVoList.get(i)),CountryListVo.class));
        }
        return countryListVoList2;
    }

    public String publicRequest(BusiExpress busiExpress,JSONObject obj,String type){

        if(ObjectUtil.isEmpty(obj)){
            obj =new JSONObject();
        }


        Map<String,String> header =new HashMap<>();
        header.put("userId", "X7468");
        header.put("appKey","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTkuCPxRRPknfMfz88XblpBtaox/G+7V30UtJYXzk7t99RDfOoogJbZH7Gf+e+8E0" +
                "ZEeuuTyBCnEuHykFRk7ZF7sBvrWHCmXtOijXzZsR7fSigxcN2G/S5uBrmDsjT6Jh+Mer//pyjbFdoB5mm8UnoXSM0MIpJUiNUSbz/FyiaXQIDAQAB");
        header.put("token","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHmTwlz7k0ys/vjCMh2619qM6LP9LawxiI1oTNpQkZGrwNDvm5iqCxiF" +
                "QUkL7hznOImTMBK0P7YDhZlX6DL5BDEHNAvmIWPGdGkv2Gc+lj5jUyaN/by1h7kiVFB85zEGUHxtMCKBI+yAP3n00SUIUCZLXxreZtYPyjTqRiGhXprwIDAQAB");

        obj.put("HeaderRequest",header);
        obj.put("divisionId","1");


        String request ="?apiName="+type+"&parameter="+JSONObject.toJSONString(obj).replace("\"","%22");
        String info =HttpUtil.get(query_express_base_info+request);

        info = Convert.unicodeToStr(info);;
        log.info("====response info ={}=======",JSONObject.toJSONString(info));
        if("getShipTypes".equals(type)){
            return info;
        }

        return info;

    }

    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {
        JSONObject data =new JSONObject();
        data.put("addOrderRequestInfo",expressOrderInput.toTranOrder03());
        String body = publicRequest(getBusiExpress(),data,"addOrder");
        JSONObject jsonObject =JSONObject.parseObject(body);
        //TODO 物品价格和 申报价格不匹配
        if(StringUtils.isEmpty(jsonObject.getString("orderCode")) || StringUtils.isNotEmpty(jsonObject.getString("note"))){
            throw new RuntimeException(jsonObject.getString("note"));
        }
        String no =StringUtils.isNotEmpty(jsonObject.getString("trackingNumberUsps"))?jsonObject.getString("trackingNumberUsps"): jsonObject.getString("trackingNumber");
        return OrderResultOutput.builder()
                .orderId(jsonObject.getString("orderCode"))
                .refrenceNo(jsonObject.getString("customerOrderNo"))
                .shippingMethodNo(no)
                .build();
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class Result{
        private String success;
        private String cnmessage;
        private String enmessage;
        private String data;

    }

}
