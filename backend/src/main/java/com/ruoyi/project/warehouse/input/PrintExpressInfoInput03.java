package com.ruoyi.project.warehouse.input;//

import com.alibaba.fastjson2.annotation.JSONField;//
import lombok.AllArgsConstructor;//
import lombok.Builder;//
import lombok.Data;//
import lombok.NoArgsConstructor;//
import lombok.experimental.Accessors;//

import java.util.List;//

/**
 * 打印资料
 * @author liuzhen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain= true)
@Builder
public class PrintExpressInfoInput03 extends BaseDto{

    private  String orderCodeList;//SFC单号
    private  String printType="0";//打印类型
    private  String isPrintDeclare="1";//1
    private  String declare="0";//0
    private  String ismerge="1";//1
    private  String urluserid ="OTY5";//OTY5
    private  String print_type="PDF";//标签类型
    private  String printSize="3";//标签尺寸



























}
