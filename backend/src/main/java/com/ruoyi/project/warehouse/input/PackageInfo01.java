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
public class PackageInfo01 {
    
    private String weight;//":169,//预报重，单位g克，不能有小数
    private String parcel_value;//":9,//申报总价值=申报单价X申报数量
    private String currency;//USD",//申报币种，非欧盟国家目前币种都是USD ，欧盟国家支持EUR            //如果报错不支持USD，先看是不是发的欧盟，如发的欧盟就改成EUR//特别注意,货币类型换了后对应的数值也要换算//货币类型保持一致，比如走欧洲的只支持欧元，所有的货币类型都要保持一致
    private String include_battery;//N",//是否带电，N否
    private List<GoodsDetails01> declare_product_info;
}
