package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class GoodsDetails06 {
    @JSONField(name = "volume_height")
    private String volume_height;//": "高，单位CM",
    @JSONField(name = "volume_length")
    private String volume_length;//": "长，单位CM",
    @JSONField(name = "volume_width")
    private String volume_width;//": "宽，单位CM",
    @JSONField(name = "volume_weight")
    private String volume_weight;//": "实重"
}
