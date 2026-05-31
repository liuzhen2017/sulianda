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
public class ConfigInfo05 {

    /**
     * 1：PNG文件
     * 2：PDF文件
     */
    private String lable_file_type="2";

    /**
     * 纸张类型
     * 1：标签纸(10*10厘米)
     * 2：A4纸(21*29.7厘米)
     */
    private String lable_paper_type ="1";


    /**
     *1：标签
     * 2：报关单
     * 3：配货单
     * 4：标签+报关单
     * 5：标签+配货单
     * 6：标签+报关单+配货单
      */
    private String lable_content_type="1";
    @JSONField(name = "additional_info")
    private AdditionalInfo05 additionalInfo05 =new AdditionalInfo05();
}
