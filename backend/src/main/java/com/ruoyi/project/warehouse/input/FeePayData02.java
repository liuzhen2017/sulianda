package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FeePayData02 {
    @JSONField(name = "SalesPlatform")
    private String FeePayType;// 2 Y 支付方式 [ PP:预付,CC:到付, TP:第三方] 根据渠道要求不同也可以为空值
    @JSONField(name = "SalesPlatform")
    private String FeePayAccountNumber;// 50 Y/N 支付账号 支付方式为 TP 时必传
    @JSONField(name = "SalesPlatform")
    private String FeePayCountryCode;// 5 Y/N 支付账号对应国家/地区编码 支付方式为 TP 时必传
    @JSONField(name = "SalesPlatform")
    private String FeePayPostCode;// 15 Y/N 支付账号对应邮编 支付方式为 TP 并且渠道 FEDEX 时必传
}
