package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author liuzhen
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfo {

    /**
     * 标签上打印配货信息 (Y:打印 N:不打印) 默认 N:不打印
     */
    @JSONField(format = "lable_print_invoiceinfo")
    private String additionalInfo = "N";

    /**
     * 标签上是否打印买家ID (Y:打印 N:不打印) 默认 N:不打印
     */
    @JSONField(format = "lable_print_buyerid")
    private String lablePrintBuyerid = "N";

    /**
     * 标签上是否打印日期 (Y:打印 N:不打印) 默认 Y:打印
     */
    @JSONField(format = "lable_print_datetime")
    private String lablePrintDatetime = "N";

    /**
     * 标签上是否打印日期 (Y:打印 N:不打印) 默认 Y:打印
     */
    @JSONField(format = "customsdeclaration_print_actualweight")
    private String customsDeclarationPrintActualWeight = "Y";
}
