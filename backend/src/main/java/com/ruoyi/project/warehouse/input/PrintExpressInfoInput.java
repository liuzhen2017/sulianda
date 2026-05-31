package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * 打印资料
 *
 * @author liuzhen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PrintExpressInfoInput extends BaseDto {


    /**
     * 客户参考号
     */
    @JSONField(format = "reference_no")
    private String referenceNo;
    /**
     * ---第三方订单号
     */
    @JSONField(format = "orderNo")
    private String orderNo;
    /**
     * 默认值为1
     */
    @JSONField(format = "config_code")
    private String config_code = "1";

    @JSONField(format = "configInfo")
    private ConfigInfoInput configInfo;

    @JSONField(format = "listorder")
    private ListOrderInput listOrder[];


    public PrintExpressInfoInput02 changePrint02() {
        return PrintExpressInfoInput02.builder()
                .printOrder2(Arrays.asList(PrintOrder2.builder()
                                .corpBillid(this.orderNo)
                        .build()))
                .printContent("1")
                .orderType("2")
                .printPaper("label")
                .build();


    }

    public PrintExpressInfoInput03 changePrint03() {
        return PrintExpressInfoInput03.builder()
                .orderCodeList(this.orderNo)
                .build();


    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getConfig_code() {
        return config_code;
    }

    public void setConfig_code(String config_code) {
        this.config_code = config_code;
    }

    public ConfigInfoInput getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(ConfigInfoInput configInfo) {
        this.configInfo = configInfo;
    }

    public ListOrderInput[] getListOrder() {
        return listOrder;
    }

    public void setListOrder(ListOrderInput[] listOrder) {
        this.listOrder = listOrder;
    }

    public PrintExpressInfoInput05 changePrint05() {
        return PrintExpressInfoInput05.builder()
                .listOrder05(Arrays.asList(
                        ListOrder05.builder()
                                .reference_no(this.referenceNo)
                                .build()
                )).configInfo(new ConfigInfo05())
                .build();
    }
}
