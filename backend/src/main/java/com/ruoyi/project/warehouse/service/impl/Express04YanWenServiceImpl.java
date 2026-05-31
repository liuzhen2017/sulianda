package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.OrderInput04;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.ExpressFreeOutput;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class Express04YanWenServiceImpl extends ExpressServiceBaseService {

    @Autowired
    private ServerConfig serverConfig;

//    private static final String base_url_dev = "https://ejf-fat.yw56.com.cn";
    private static final String base_url_prod = "Https://open.yw56.com.cn";
//    private static final String user_Name_dev = "100000";
//    private static final String user_pwd_dev = "D6140AA383FD8515B09028C586493DDB";

    /**
     * 查询快递费
     *
     * @param expressFreeInput
     * @return
     */
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput) {

        String body = publicRequest(getBusiExpress(), null, "feetrail");
        ExpressFreeOutput expressFree = JSONObject.parseObject(body, ExpressFreeOutput.class);
        return AjaxResult.success(expressFree);
    }

    /**
     * 打印面单
     *
     * @return
     */
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput) {
        Map<String, Object> map = new HashMap<>();
        map.put("waybillNumber", printExpressInfoInput.getOrderNo());
        String body = publicRequest(super.getBusiExpress(), map, "express.order.label.get");
        JSONObject jsonObject = JSONObject.parseObject(body);
        String base64String = jsonObject.getString("base64String");

        String fileNameTemp = UUID.randomUUID().toString() + ".pdf";
        String filePath = RuoYiConfig.getUploadPath();
        //base64解码 对字节数组字符串进行base64解码并生成文件

        base64StringToPdf(base64String,filePath + "/temp/" + fileNameTemp);

        String url = serverConfig.getUrl() + "/temp/" + fileNameTemp;
        return Arrays.asList(PrintExpressOutput.builder()
                .lableFile(url)
                .build());
    }

    public static void base64StringToPdf(String base64Content, String filePath) {

        byte[] bytes = Base64Utils.decodeFromString(base64Content);

        File file = new File(filePath);
        File path = file.getParentFile();
        if (!path.exists()) {
            boolean b = path.mkdirs();
        }
        try (
                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
                BufferedInputStream bis = new BufferedInputStream(byteInputStream);
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询国家选项
     *
     * @return
     */
    public List<CountryListVo> queryCountryList() {
        Map<String, Object> map = new HashMap<>();
        map.put("sex", "test");
        String body = publicRequest(super.getBusiExpress(), map, "common.country.getlist");
        return JSONUtil.toList(body, CountryListVo.class);
    }

    /**
     * 查询快递方式
     *
     * @return
     */
    public List<CountryListVo> getShippingMethod() {
        Map<String, Object> map = new HashMap<>();
        map.put("transport_mode", "1");
        String body = publicRequest(getBusiExpress(), map, "express.channel.getlist");


        body = body.replaceAll("nameCh", "cnname").replaceAll("nameEh", "enname").replaceAll("id", "code");
        return JSONUtil.toList(body, CountryListVo.class);
    }


    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {

        OrderInput04 orderInput04 = expressOrderInput.toTranOrder04();
        Map<String, Object> data = JSONObject.parseObject(JSONObject.toJSONString(orderInput04), new HashMap<>().getClass());
        String body = publicRequest(getBusiExpress(), data, "express.order.create");
        JSONObject jsonObject = JSONObject.parseObject(body);

        return OrderResultOutput.builder()
                .refrenceNo(expressOrderInput.getReferenceNo())
                .orderId(jsonObject.getString("orderNumber"))
                .shippingMethodNo(jsonObject.getString("waybillNumber"))
                .build();
    }

    public String publicRequest(BusiExpress busiExpress, Map<String, Object> obj, String type) {

        long timeMillis = System.currentTimeMillis();
        Map<String, Object> queryInfo = new TreeMap<>();
        queryInfo.put("user_id", busiExpress.getAuthorizationNo());
        queryInfo.put("method", type);
        queryInfo.put("format", "json");
        queryInfo.put("timestamp", timeMillis);


        StringBuffer signStr = new StringBuffer();
        signStr.append(busiExpress.getAuthorizationCode());
        signStr.append(busiExpress.getAuthorizationNo());


        signStr.append(JSONObject.toJSONString(obj));
        signStr.append("json");
        signStr.append(type);
        signStr.append(timeMillis);
        signStr.append("V1.0");
        signStr.append(busiExpress.getAuthorizationCode());

        try {
            queryInfo.put("sign", Md5Utils.to_MD5(signStr.toString()));
            queryInfo.put("version", "V1.0");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("=====request info ={}========", JSONObject.toJSONString(queryInfo));
        String resultStr = HttpUtil.post(base_url_prod + "/api/order?" + URLUtil.buildQuery(queryInfo, StandardCharsets.UTF_8), JSONObject.toJSONString(obj));

        Result result = JSONObject.parseObject(resultStr, Result.class);
        log.info("=====response info ={}========", JSONObject.toJSONString(resultStr));
        if (result.code.equals("0") && result.getSuccess().equals("true")) {
            return result.getData();
        }
        throw new RuntimeException("请求错误,错误信息为:" + result.getMessage());

    }

    @Data
    public class Result {
        String success;
        String code;
        String message;
        String data;
    }

}
