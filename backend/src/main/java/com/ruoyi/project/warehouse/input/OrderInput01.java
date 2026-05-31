package com.ruoyi.project.warehouse.input;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInput01 {
    private String ref_no;//TEST-1234567",//每个走货账号下-客户单号保持唯一
    private String business_type;//BDS",//直发业务类型 BDS
    private String duty_type;//P",//税费费用承担方式,可以咨询我们业务了解
    private String vat_no;//",//VAT税号,如发的欧盟有IOSS号可不填
    private String eori_no;//",//欧盟入关时需要EORI号码，用于商品货物的清关,欧盟有传IOSS号可不填,非欧盟不用填
    private String ioss_no;//IM0123456789",//欧盟税改 IOSS号
    private String parcel_qty;//",//包裹件数（一个订单有多少件包裹，就填写多少件数，请如实填写包裹件数，//否则DHL无法返回准确的子单号数和子单号标签；DHL产品必填，如产品代码A1/A5；）
    private String freight_charges;//",//运费(客户填写自己估算的运输费用；支持的币种，根据物流产品+收件人国家配置)
    private String currency_freight;//",//运费币种(按照ISO标准三字码；支持的币种，根据物流产品+收件人国家配置)
    private String declare_insurance;//",//申报保险费（是否必填，根据物流产品+目的国配置；根据欧盟IOSS政策，//货值/运费/保险费可单独申报）支持小数点后2位
    private String currency_declare_insurance;//",//申报保险费币种（按照ISO标准，币种需和进出口国申报币种一致）

    @JSONField(name = "recipient_info")
    private Recipient01 recipient_info;


    private Sender01 sender;
    @JSONField(name = "parcel_list")
    private List<PackageInfo01> parcelInfo;
    @JSONField(name = "insurance_info")
    private Insurance01 insurance_info;
    @JSONField(name = "is_insure")
    private String isInsure ="N"; //是否投保，这个是购买4PX的保险,货币类型必须是USD，和其他申报品货币类型不关联
    @JSONField(name = "logistics_service_info")
    private ProductChannel01 logistics_service_info;
    @JSONField(name = "return_info")
    private ReturnInfo01 return_info;
    @JSONField(name = "deliver_type_info")
    private DeliverTypeInfo01 deliverTypeInfo01;

    public String getEori_no() {
        return eori_no;
    }

    public void setEori_no(String eori_no) {
        this.eori_no = eori_no;
    }

    public String getIoss_no() {
        return ioss_no;
    }

    public void setIoss_no(String ioss_no) {
        this.ioss_no = ioss_no;
    }
}
