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
public class Sender02 {
    @JSONField(name = "Name")
    private  String name ;//String 100 N 名称
    @JSONField(name = "Company")
    private  String company ;//String 200 N 公司
    @JSONField(name = "Country")
    private  String country ;//String 20 N 国家/地区
    @JSONField(name = "Addres")
    private  String addres ;//String 500 N 地址（快递制单超长时可用换行符分割）
    @JSONField(name = "Mobile")
    private  String mobile ;//String 50 N 手机
    @JSONField(name = "Tel")
    private  String tel ;//String 50 N 电话
    @JSONField(name = "Post")
    private  String post ;//String 20 N 邮编
    @JSONField(name = "Province")
    private  String province ;//String 50 N 省州
    @JSONField(name = "City")
    private  String ctity ;//String 50 N 城市
    @JSONField(name = "Town")
    private  String town ;//String 50 N 乡镇（TNT 制单必传，一般可和城市相同）
    @JSONField(name = "Email")
    private  String email ;//String 50 N 邮箱 Fax ;//String 50 N 传真
}
