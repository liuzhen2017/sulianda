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
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConfigInfoInput {
    /**
     * 标签文件类型
     * 1：PNG文件
     * 2：PDF文件
     */
    @JSONField(format = "lableFileType")
    private String lableFileType="1";

    /**
     * 纸张类型
     * 1：标签纸(10*10厘米)
     * 2：A4纸(21*29.7厘米)
     */
    @JSONField(format = "lable_paper_type")
    private String lablePaperType="1";

    /**
     * 标签内容类型代码
     * 1：标签
     * 2：报关单
     * 3：配货单
     * 4：标签+报关单
     * 5：标签+配货单
     * 6：标签+报关单+配货单
     */
    @JSONField(format = "lable_content_type")
    private String lableContentType;

    @JSONField(format = "additional_info")
    AdditionalInfo additionalInfo;

}
