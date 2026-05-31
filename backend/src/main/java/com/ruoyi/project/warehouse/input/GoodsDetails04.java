package com.ruoyi.project.warehouse.input;

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
@Accessors(chain = true)
public class GoodsDetails04 {
    private String goodsNameCh ;//String 是 200 中文品名
    private String goodsNameEn ;//String 是 200 英文品名
    private String price;// Decimal 是 (18,2) 申报单价
    private String quantity;//  Integer 是 数量
    private String weight;//  Integer 是 单件重量(单位:g)
    private String hscode ;//String 50 海关编码
    private String url ;//String 2000 商品链接
    private String material ;//String 500 商品材质
}
