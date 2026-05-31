package com.ruoyi.project.warehouse.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liuzhen
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminReportOutput {
    /**
     * 总利润
     */
    private BigDecimal totalProfitMoney =BigDecimal.ZERO;
    /**
     * 总订单数
     */
    private BigDecimal totalOderNumber =BigDecimal.ZERO;
    /**
     * 总快递费
     */
    private BigDecimal totalExpressMoney =BigDecimal.ZERO;
    /**
     * 总打包费
     */
    private BigDecimal totalPackageMoney =BigDecimal.ZERO;

    private String dateStr;
}
