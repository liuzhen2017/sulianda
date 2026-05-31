package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 备注
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressOrderExtraServiceInput {
    //必填 end
    /**
     * 额外服务类型代码
     */
    @JSONField(name = "extra_servicecode")
    private String extraServiceCode;

    /**
     * 额外服务值（有些额外服务类型必须传此值
     */
    @JSONField(name = "extra_servicevalue")
    private String extraServiceValue;

    /**
     * 备注
     */
    @JSONField(name = "extra_servicenote")
    private String extraServiceNote;


}
