package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 查询快递费
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetExpressFreeInput extends BaseDto {
    /**
     * 	目的国家二字代码
     */
    private String countryCode;
    /**
     * 	重量（KG）
     */
    private String weight;
    /**
     * 		运输方式代码
     */
    private String shippingMethod;
    /**
     * 	目的地邮编
     */
    private String postCode;
    /**
     * 	起运地 默认为：用户绑定的起运地
     */
    private String location;
    /**
     * 	货物类型（W:包裹 D:文件） 默认为：W
     */
    private String cargoType="w";
    /**
     * 	长（CM）
     */
    private String length;
    /**
     * 	宽（CM）
     */
    private String width;
    /**
     * 	高（CM）
     */
    private String height;
    /**
     * 	额外服务代码集合
     */
    private String extraService ;
}
