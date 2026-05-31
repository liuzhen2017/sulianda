package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 附加配置信息
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AdditionalInfo05 {


    /**
     * 	string		标签上打印配货信息 (Y:打印 N:不打印) 默认 N:不打印
     */
    private String lable_print_invoiceinfo="N";
    /**
     * 	string		标签上是否打印买家ID (Y:打印 N:不打印) 默认 N:不打印
     */
    private String lable_print_buyerid="N";
    /**
     * 	string		标签上是否打印日期 (Y:打印 N:不打印) 默认 Y:打印
     */
    private String lable_print_datetime="Y";
    /**
     * 	string		报关单上是否打印实际重量 (Y:打印 N:不打印) 默认 N:不打印
     */
    private String customsdeclaration_print_actualweight="Y";
}
