package com.ruoyi.project.warehouse.input;

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
@Accessors(chain = true)
@Builder
public class PrintExpressInfoInput02 extends BaseDto{
    @JSONField(name = "OrderType")
    private  String orderType="2";//: "2",/*制单固定 2*/
    @JSONField(name = "PrintPaper")
    private  String printPaper="label";// "label",/*发票传 a4*/
    /**
     * 1：地址标签/快递制单物流标签（LB 单） 2：报关单/快递制单发票 3：配货信息 4：快递制单其他单证 可自由组合用“,”号隔开
     */
    @JSONField(name = "PrintContent")
    private  String printContent="1";

    @JSONField(name = "CorpBillidDatas")
    private List<PrintOrder2> printOrder2;



























}
