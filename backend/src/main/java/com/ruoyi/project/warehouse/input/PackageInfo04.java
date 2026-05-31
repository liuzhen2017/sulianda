package com.ruoyi.project.warehouse.input;

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
public class PackageInfo04 {
    List<GoodsDetails04> productList;
    private String hasBattery ;//String 是 是否带电 1:是 0:否
    private String currency;// String 是 10 币种代码； 传 USD,EUR,GBP,CNY ,AUD,CAD;
    private String totalPrice ;//Decimal 是 (18,2) 申报总价值
    private String totalQuantity ;//String 是 申报总数量
    private String totalWeight ;//String 是 总重量(单位:g)
    private String height ;//String 包裹高(单位:cm)
    private String width ;//String 包裹宽(单位:cm)
    private String length ;//String 包裹长(单位:cm)
    private String ioss ;//String 50 IOSS 税号

}
