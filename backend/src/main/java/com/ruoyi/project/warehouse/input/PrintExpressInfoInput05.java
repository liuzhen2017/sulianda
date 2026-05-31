package com.ruoyi.project.warehouse.input;//

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * 打印资料
 * @author liuzhen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain= true)
@Builder
public class PrintExpressInfoInput05 extends BaseDto{

    @JSONField(name = "configInfo")
    private ConfigInfo05 configInfo =new ConfigInfo05();


    private String config_code="1";
    @JSONField(name = "listorder")
    private List<ListOrder05> listOrder05;

























}
