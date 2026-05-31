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
public class GoodsDetails01 {

    private  String packNo ;//String 10 N 箱号（与材积箱号对应）
     private  String declare_product_name_cn;//猫碗,//中文申报品名,必须至少包要含一个中文
     private  String declare_product_name_en;//bowl,//英文申报品名，不能包含中文，及特殊符号
     private  String declare_product_code_qty;//1,//申报数量
     private  String declare_unit_price_export;//9,//出口申报单价（目前建议进出口申报单价传一样）
     private  String currency_export;//USD,//出口货币类型，货币类型保持一致
     private  String declare_unit_price_import;//9,//进口申报单价（目前建议进出口申报单价传一样）
     private  String currency_import;//USD,//进口货币类型,货币类型保持一致
     private  String brand_export;//无,//出口品牌，如无可以填空
     private  String brand_import;//无,//进口品牌，如无可以填空
     private  String hscode_export;//,//出口海关编码，如是必填的，进出口保持一致
     private  String hscode_import;//,//进口海关编码，如是必填的，进出口保持一致
     private  String uses;//YONGTU,//用途   dhl 联邦渠道需要传英文涉及产品A1 A5 E4 E5 E6 E7 （值：必须英文）
     private  String material;//CAIZHI,//材质  dhl 联邦渠道需要传英文涉及产品A1 A5 E4 E5 E6 E7（值：必须英文）
    //材质用途这2个字段，非联邦DHL产品渠道的建议不要传，如要传一定要英
}
