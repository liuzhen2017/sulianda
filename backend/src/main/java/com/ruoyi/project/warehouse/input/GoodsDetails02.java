package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsDetails02 {
    @JSONField(name = "PackNo")
    private  String packNo ;//String 10 N 箱号（与材积箱号对应）
    @JSONField(name = "Sku")
    private  String sku ;//String 50 Y 产品 Sku (OrderType 为仓储订单必 传)
    @JSONField(name = "Cnname")
    private  String cnName ;//String 200 Y 产品中文名
    @JSONField(name = "Enname")
    private  String enname ;//String 200 Y 产品英文名
    @JSONField(name = "Price")
    private  String price;//Double 14,3 Y 单价
    @JSONField(name = "SingleWeight")
    private  String singleWeight;// Double 14,3 Y 单件重量
    @JSONField(name = "Num")
    private  String num;// Int Y 数量
    @JSONField(name = "Money")
    private  String money ;//String 20 N 货币单位
    @JSONField(name = "Unit")
    private  String unit ;//String 10 N 计量单位（一般为 PCS）
    @JSONField(name = "ProductLink")
    private  String productLink ;//String 500 N 产品图片链接
    @JSONField(name = "PackageNo")
    private  String packageNo ;//String 3 N 包裹号
    @JSONField(name = "Texture")
    private  String texture ;//String 200 N 材质
    @JSONField(name = "Application")
    private  String application ;//String 200 N 用途
    @JSONField(name = "CustomsCode")
    private  String customsCode ;//String 20 N 海关编码
    @JSONField(name = "TransactionUrl")
    private  String TransactionUrl ;//String 200 N 产品销售链接(WISH 订单必填)
    @JSONField(name = "PeihuoInfo")
    private  String PeihuoInfo ;//String 200 N 配货信息
    @JSONField(name = "Origin")
    private  String Origin ;//String 20 Y/N 产地（国家/地区二字码如 CN，UPS 制 单无纸化必传）
}
