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
@Accessors(chain = true)
@Builder
public class Recipient02 {
    @JSONField(name = "Name")
    private String name;//String 100 Y 名称
    @JSONField(name = "Company")
    private String company;//String 200 N 公司
    @JSONField(name = "Addres1")
    private String addres1;//String 200 Y 地址 1
    @JSONField(name = "Addres2")
    private String addres2;//String 200 N 地址 2
    @JSONField(name = "Addres3")
    private String addres3;//String 200 N 地址 3
    @JSONField(name = "HouseNum")
    private String houseNum;//String 10 N 门牌号
    @JSONField(name = "Tel")
    private String tel;//String 50 Y 电话 二选一必传，快递制单必传 电话
    @JSONField(name = "Province")
    private String province;//String 60 Y 省州
    @JSONField(name = "City")
    private String city;//String 60 Y 城市
    @JSONField(name = "Post")
    private String post;//String 20 Y 邮编
    @JSONField(name = "Email")
    private String Email ;//String 100 N 邮箱
    @JSONField(name = "Fax")
    private String fax;//String 50 N 传真
    @JSONField(name = "Contaxno")
    private String contaxno;//String 200 N 税号
    @JSONField(name = "Area")
    private String area;//String 100 N 区
}
