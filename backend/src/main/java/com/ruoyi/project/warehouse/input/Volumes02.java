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
public class Volumes02 {
    @JSONField(name = "PackNo")
    private  String PackNo;// String 10 N 箱号（与明细箱号对应）
    @JSONField(name = "Weight")
    private  String Weight;// Double 14,3 Y 实重
    @JSONField(name = "Number")
    private  String Number;// Int Y 件数
    @JSONField(name = "Length")
    private  String Length;// Double 14,3 Y/N 长
    @JSONField(name = "Width")
    private  String Width;// Double 14,3 Y/N 宽
    @JSONField(name = "Height")
    private  String Height;// Double 14,3 Y/N 高
    @JSONField(name = "Piece")
    private  String Piece;// Int Y/N 托盘件数（仅 UPS 托盘服务）
    @JSONField(name = "Value")
    private  String Value;// Double 14,2 N 单个包裹申报价值（保险价值，UPS 制 单专用，提交可能产生保险费用）
}
