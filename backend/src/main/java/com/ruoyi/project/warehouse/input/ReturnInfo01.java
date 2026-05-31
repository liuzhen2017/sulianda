package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class ReturnInfo01 {
    /**
     * 境内/国内异常处理策略(Y：退件--实际是否支持退件，以及退件策略、费用，参考报价表；N：销毁；U：其他--等待客户指令) 默认值：N；
     */
    private String is_return_on_domestic="U";
    /**
     * 境外/国外异常处理策略(Y：退件--实际是否支持退件，以及退件策略、费用，参考报价表；N：销毁；U：其他--等待客户指令) 默认值：N；
     */
    private String is_return_on_oversea ="U";
}
