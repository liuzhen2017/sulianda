package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInput02 {
    @JSONField(name = "CorpBillid")
    private String corpBillId ;//String 50 Y/N 订单号 注意：创建订单后返回，修改订单必传
    @JSONField(name = "CustomerNumber")
    private String customerNumber ;//String 50 Y 客户订单号(可传入贵公司内部单号)
    @JSONField(name = "TradeNo")
    private String tradeNo ;//String 60 N 交易号
    @JSONField(name = "TrackNumber")
    private String trackNumber ;//String 50 N 跟踪号,服务商单号,转单号
    @JSONField(name = "LabelBillid")
    private String labelBillid ;//String 50 N 标签单号
    @JSONField(name = "ChannelCode")
    private String channelCode ;//String 10 Y 渠道代码 可调用[searchStartChannel]方法获 取
    @JSONField(name = "CountryCode")
    private String countryCode ;//String 20 Y 国家/地区二字代码
    @JSONField(name = "HouseCode")
    private String houseCode ;//String 20 N 仓库代码 (OrderType 为仓储订单必 传) 可调用[searchStartHouse]方法获取
    @JSONField(name = "TotalWeight")
    private String totalWeight ;//Double 14,3 Y 订单总重量
    @JSONField(name = "TotalValue")
    private String totalValue ;//Double 14,3 Y 订单总申报价值
    @JSONField(name = "number")
    private String number;//Int Y 总件数（必须等于材积明细件数之和）
    @JSONField(name = "collamt")
    private String collamt ;//Double 14,3 N 代收货款
    @JSONField(name = "Collccycode")
    private String collccycode ;//String 3 N 代收货款币别
    @JSONField(name = "PackageType")
    private String packageType ="0";//String 1 N 物品类别 (G:礼物,D:文件,S:商业样本,O:其它)
    @JSONField(name = "ProductTypeId")
    private String productTypeid ;//String 5 N 物品类别编码 （带电产品咨询代理 k5 上的物品类别 编码，传递对应的编码就能用 k5 的带 电信息和电池金句提交，PackageType;//字段可以不传）
    @JSONField(name = "Battery")
    private String battery ;//String 1 N 是否带电(0:不带电,1:带电)
    @JSONField(name = "GoodsType")
    private String goodsType ;//String 3 N 包裹类型 (WPX:包裹,DOC:文件,PAK:PAK 袋)
    @JSONField(name = "Note")
    private String note ;//String 500 N 订单备注
    @JSONField(name = "VatNumber")
    private String vatNumber ;//String 50 N Vat 增值税号（寄件人）
    @JSONField(name = "VatCorpName")
    private String vatCorpName ;//String 100 N Vat 公司名
    @JSONField(name = "VatCorpAddress")
    private String vatCorpAddress ;//String 100 N Vat 公司地址
    private String eoriNumber ;//String 50 N EORI 企业号，EORI 欧盟税号
    @JSONField(name = "FBAWarehouseCode")
    private String fBAWarehouseCode ;//String 10 N FBA 仓库编号
    @JSONField(name = "SalesPlatform")
    private String salesplatform ;//String 20 N 销售平台（平台标识）例如：速卖通
    @JSONField(name = "TariffType")
    private String tariffType ;//String 10 N 关税类型（快件订单） 1300：预缴增值税 IOSS 1301：预缴增值税 no-IOSS 1302：预缴增值税 other
    @JSONField(name = "Ifret")
    private String ifret ="N";//String 1 N 是否退件 0:是 1:否
    @JSONField(name = "Insurance")
    private JSONObject insurance;
    /**
        快递制单发票信息
     */
    @JSONField(name = "FeeInfo")
    private FeePayData02 payData02;
    /**
     收件人信息
     */
    @JSONField(name = "Recipient")
    private Recipient02 recipient02;

    /**
     发件人信息
     */
    @JSONField(name = "Sender")
    private Sender02 sender02;

    /**
     订单信息
     */
    @JSONField(name = "OrderItems")
    private List<GoodsDetails02> orderItems;

    /**
     材积明细 (OrderType 为快递制单必 传)
     */
    @JSONField(name = "Volumes")
    private List<GoodsDetails02> Volumes;

}
