package com.ruoyi.project.warehouse.output;

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
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrintExpressOutput {
    @JSONField(name = "lable_file_type")
    private String lableFileType;
    @JSONField(name = "lable_file")
    private String lableFile;

    public String getLableFileType() {
        return lableFileType;
    }

    public void setLableFileType(String lableFileType) {
        this.lableFileType = lableFileType;
    }

    public String getLableFile() {
        return lableFile;
    }

    public void setLableFile(String lableFile) {
        this.lableFile = lableFile;
    }
}
