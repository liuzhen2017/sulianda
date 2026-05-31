package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInput06 {
    private String buyerid;//": ;//";//",
    @JSONField(name = "order_piece")
    private String order_piece;//": ;//"件数，小包默认1，快递需真实填写;//",
    @JSONField(name = "consignee_mobile")
    private String consignee_mobile;//": ;//"手机号,选填。为方便派送最好填写;//",
    @JSONField(name = "order_returnsign")
    private String order_returnsign="N";//": ;//"退回标志，默认N表示不退回，Y标表示退回。中邮可以忽略该属性;//",
    @JSONField(name = "trade_type")
    private String trade_type;//": ;//"ZYXT;//",
    @JSONField(name = "duty_type")
    private String duty_type;//": ;//"DDU或DDP;//",
    @JSONField(name = "battery_type")
    private String battery_type;//": ;//"电池类型代码，联系货代提供;//",
    @JSONField(name = "consignee_name")
    private String consignee_name;//": ;//"收件人,必填;//",
    @JSONField(name = "consignee_companyname")
    private String consignee_companyname;//": ;//"收件公司名,如有最好填写;//",
    @JSONField(name = "consignee_address")
    private String consignee_address;//": ;//"收件地址街道，必填;//",
    @JSONField(name = "consignee_telephone")
    private String consignee_telephone;//": ;//"收件电话，必填;//",
    private String country;//": ;//"收件国家二字代码，必填;//",
    @JSONField(name = "consignee_state")
    private String consignee_state;//": ;//"州/省;//",
    @JSONField(name = "consignee_city")
    private String consignee_city;//": ;//"城市;//",
    @JSONField(name = "consignee_suburb")
    private String consignee_suburb;//": ;//"收件区，选填;//",
    @JSONField(name = "consignee_postcode")
    private String consignee_postcode;//": ;//"邮编，有邮编的国家必填;//",
    @JSONField(name = "consignee_passportno")
    private String consignee_passportno;//": ;//"收件护照号，选填;//",
    @JSONField(name = "consignee_email")
    private String consignee_email;//": ;//"邮箱，选填;//",
    @JSONField(name = "consignee_taxno")
    private String consignee_taxno;//": ;//"收件人税号;//",
    @JSONField(name = "consignee_taxnotype")
    private String consignee_taxnotype;//": ;//"收件人税号类型;//",
    @JSONField(name = "consignee_streetno")
    private String consignee_streetno;//": ;//"街道号;//",
    @JSONField(name = "consignee_doorno")
    private String consignee_doorno;//": ;//"门牌号;//",
    @JSONField(name = "shipper_taxnotype")
    private String shipper_taxnotype;//": ;//"税号类型，邮政产品可选值：IOSS,NO-IOSS,OTHER；DHL可选值：SDT、VAT、FTZ、DAN、EOR、CNP、EIN等(类型说明参照文档底部“DHL发件人税号类型”);//",
    @JSONField(name = "shipper_taxno")
    private String shipper_taxno;//": ;//"发件人税号;//",
    @JSONField(name = "shipper_taxnocountry")
    private String shipper_taxnocountry;//": ;//"发件人税号国家,用国家二字码;//",
    @JSONField(name = "customer_id")
    private String customer_id;//": ;//"客户ID，必填;//",
    @JSONField(name = "customer_userid")
    private String customer_userid;//": ;//"登录人ID，必填;//",
    @JSONField(name = "order_customerinvoicecode")
    private String order_customerinvoicecode;//": ;//"原单号，必填;//",
    @JSONField(name = "product_id")
    private String product_id;//": ;//"运输方式ID，必填;//",
    private String weight;//": ;//"总重，选填，如果sku上有单重可不填该项;//",
    @JSONField(name = "product_imagepath")
    private String product_imagepath;//": ;//"图片地址，多图片地址用分号隔开;//",
    @JSONField(name = "order_transactionurl")
    private String order_transactionurl;//": ;//"产品销售地址;//",
    @JSONField(name = "order_cargoamount")
    private String order_cargoamount;//": ;//"选填；用于DHL/FEDEX运费；或用于白关申报（订单实际金额，特殊渠道使用）；或其他用途;//",
    @JSONField(name = "order_insurance")
    private String order_insurance;//": ;//"保险金额;//",
    @JSONField(name = "cargo_type")
    private String cargo_type;//": ;//"包裹类型，P代表包裹，D代表文件，B代表PAK袋;//",
    @JSONField(name = "order_customnote")
    private String order_customnote;//": ;//"自定义信息;//"

    @JSONField(name = "orderInvoiceParam")
    List<PackageInfo06> packageInfo06;
    @JSONField(name = "orderVolumeParam")
    List<GoodsDetails06> goodsDetails06;
}
