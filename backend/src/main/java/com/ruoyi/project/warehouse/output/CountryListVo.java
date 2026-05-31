package com.ruoyi.project.warehouse.output;

import com.ruoyi.project.system.domain.SysDictData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 国家列表
 *
 * @author liuzhen
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryListVo {
    /**
     * 代码
     */
    public String code;
    /**
     *中文名称
     */
    public String cnname;
    /**
     * 英文名称
     */
    public String enname;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 备注
     */
    public String note;
    public SysDictData toDicData(){
        return SysDictData.builder()
                .dictValue(this.code)
                .dictLabel(this.cnname)
                .dictType("country")
                .dictCode(1L)
                .build();
    }
}
