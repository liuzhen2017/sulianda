package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInput03 {

    private String	  customerOrderNo;
    // 	string 	option 	客户参考号
    private String	  opDivision="1";
    // 	int 	required 	操作分拨中心,查看分拨中心列表
    private String	  isFba;
    // 	int 	option 	是否是FBA 0/1
    private String	  warehouseName="0";
    // 	string 	option 	仓储分拨中心 'FBA','',(MXFBA墨西哥fba用于传'其它私人地址')
    private String	  orderStatus;
    // 	string 	required 	订单状态:confirmed(已确认)、preprocess(预处理)
    private String	  recipientName;
    // 	string 	required 	收件人
    private String	  recipientCountry;
    // 	string 	required 	收件国家
    private String	  shippingMethod;
    // 	string 	required 	运输方式
    private String	  subShipType;
    // 	string 	option 	运输子方式
    private String	  trackingNumber1;
    // 	string 	option 	跟踪号
    private String	  recipientState;
    // 	string 	option 	收件州省
    private String	  recipientCity;
    // 	string 	option 	收件城市
    private String	  recipientAddress;
    // 	string 	required 	收件地址
    private String	  doorplate;
    // 	string 	option 	门牌号 门牌号信息请和地址信息一起推送到“recipientAddress”字段；【门牌号字段可用渠道：EUEXPIE,EUEXPIEB,EUEXP4,EUEXP6】
    private String	  smallLangAddress;
    // 	string 	option 	小语种地址
    private String	  recipientZipCode;
    // 	string 	option 	收件邮编
    private String	  recipientPhone;
    // 	string 	option 	收件电话
    private String	  recipientEmail;
    // 	string 	option 	收件邮箱
    private String	  recipientOrganization;
    // 	string 	option 	收件公司
    private String	  exportDeclaration;
    // 	int 	option 	是否出口一般贸易报关 0/1
    private String	  is_Customs;
    // 	int 	option 	是否单独报关 0/1
    private String	  customeCode;
    // 	string 	option 	报关单号
    private String	  isSignature;
    // 	int 	option 	是否电子签名 0/1
    private String	  isReturn;
    // 	int 	option 	是否退件 0/1
    private String	  withBattery;
    // 	int 	option 	是否带电池 0/1
    @JSONField(name ="type_of_battery")
    private String	  typeOfBattery;
    // 	string 	option 	电池类型'PI966', 'PI967', 'PI969', 'PI970', '无电池', ''
    private String	  batteryTypeStr;
    // 	int 	option 	电池类型：默认0/内置电池为1 /配套电池为2 /纯电池为3
    private String	  fbaNumber;
    // 	string 	option 	仓储号/FBA号
    private String	  fbaWarehouseCode;
    // 	string 	option 	仓储代码
    private String	  taxType;
    // 	string 	option 	税号类型,1(自身税号),2(用SFC税号清关)
    private String	  taxesNumber;
    // 	string 	option 	税号
    private String	  mark;
    // 	string 	option 	税号标注 '',A-Z
    private String	  salesLink;
    // 	string 	option 	销售链接
    private String	  erp_source;
    // 	string 	option 	ERP来源
    private String	  shippingWorth;
    // 	string 	option 	销售价值
    private String	  dutyforward;
    // 	int 	option 	关税预付 0/1
    private String	  codAmount ;//	string 	option 	代收货款金额
    private String	  goodsDeclareWorth;
    // 	string 	required 	总申报价值 float
    private String	  evaluate;
    // 	string 	option 	总投保价值 float
    private String	  goodsWeight;
    // 	string 	option 	重量 float KG
    private String	  pieceNumber;
    // 	int 	option 	件数/箱数 int
    private String	  goodsQuantity;
    // 	int 	option 	订单总数量 int
    private String	  goodsLength;
    // 	string 	option 	订单长度 CM
    private String	  goodsWidth;
    // 	string 	option 	订单宽度 CM
    private String	  goodsHeight;
    // 	string 	option 	订单高度 CM
    private String	  goodsDescription;
    // 	string 	required 	订单描述
    private String	  shipperAddressType;
    // 	int 	option 	是否取寄件人默认地址 0/1
    private String	  shipperState;
    // 	string 	option 	寄件人省份
    private String	  shipperCity;
    // 	string 	option 	寄件人城市
    private String	  shipperCompanyName;
    // 	string 	option 	寄件人公司
    private String	  shipperName;
    // 	string 	option 	寄件人
    private String	  shipperEmail;
    // 	string 	option 	寄件人mail
    private String	  shipperAddress;
    // 	string 	option 	寄件人addr
    private String	  shipperPhone;
    // 	string 	option 	寄件人phone
    private String	  shipperZipCode;
    // 	string 	option 	寄件人邮编
    private String	  isRemoteConfirm;
    // 	int 	option 	接口是否返回偏远收费提示 0/1
    private String	  isGetArea;
    // 	int 	option 	接口是否返回分区区号(如:RM2R-3的3) 0/1
    private String	  platformCode;
    // 	string 	option 	SFC对应的平台订单号
    private String	  notifyUrl;
    // 	string 	option 	SFC对应的的url
    private String	  ebayIdentify;
    // 	string 	option 	SFC对应的EBAY帐号ID
    private String	  IDCR;
    // 	string 	option 	身份证号/企业登记号/istore排序号
    private String	  oriGST;
    // 	string 	option 	印度GST/gst编号
    private String	  oriCompany;
    // 	string 	option 	印度接口公司
    private String	  salesPlatform;
    // 	string 	option 	销售平台
    private String	  vendor_id;
    // 	string 	option 	Vendor(供应商)ID
    @JSONField(name = "gst_exemption_code")
    private String	  gstExemptionCode;
    // 	string 	option 	GSTexemption Code
    private String	  abn;
    // 	string 	option 	ABN(进口商)
    private String	  areaCode;
    // 	string 	option 	分区打印shipping_desc
    private String	  postscript;
    // 	string 	option 	用于区分义乌两个仓YW2
    private String	  common;
    // 	string 	option 	公用字段(KREXP/KREXPSEA韩国专线用于传个人识别编号或出生年月日;JPEXP日本专线用于传店铺名;KREXPHX用于个人通关编码;TREXPTH用于身份证ID编码;)
    private String	  iossNo;
    // 	string 	option 	IOSS号
    private String	  platformOrderNo;
    // 	string 	option 	平台订单号
    private String	  invoice;
    // 	string 	option 	发票号
    private GoodsDetails03[]	  goodsDetails;
    // 	array 	required 	物品详情，二维数组，每个子数组对应一个物品，查看下面的goodsDetails
}
