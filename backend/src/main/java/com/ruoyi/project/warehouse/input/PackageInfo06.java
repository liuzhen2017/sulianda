package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PackageInfo06 {

    @JSONField(name = "invoice_amount")
    private String invoice_amount;//": ;//"申报总价值，必填;//",
    @JSONField(name = "invoice_pcs")
    private String invoice_pcs;//": ;//"件数，必填;//",
    @JSONField(name = "invoice_title")
    private String invoice_title;//": ;//"英文品名，必填;//",
    @JSONField(name = "invoice_weight")
    private String invoice_weight;//": ;//"单件重;//",
    private String sku;//": ;//"中文品名;//",
    @JSONField(name = "sku_code")
    private String sku_code;//": ;//"配货信息;//",
    @JSONField(name = "hs_code")
    private String hs_code;//": ;//"海关编码;//",
    @JSONField(name = "transaction_url")
    private String transaction_url;//": ;//"销售地址;//",
    @JSONField(name = "invoiceunit_code")
    private String invoiceunit_code;//": ;//"申报单位;//",
    @JSONField(name = "invoice_imgurl")
    private String invoice_imgurl;//": ;//"图片地址;//",
    @JSONField(name = "invoice_brand")
    private String invoice_brand;//": ;//"品牌;//",
    @JSONField(name = "invoice_rule")
    private String invoice_rule;//": ;//"规格;//",
    @JSONField(name = "invoice_currency")
    private String invoice_currency;//": ;//"申报币种;//",
    @JSONField(name = "invoice_taxno")
    private String invoice_taxno;//": ;//"税则号;//",
    @JSONField(name = "origin_country")
    private String origin_country;//": ;//"原产国;//",
    @JSONField(name = "invoice_material")
    private String invoice_material;//": ;//"材质;//",
    @JSONField(name = "invoice_purpose")
    private String invoice_purpose;//": ;//"用途;//"
}
