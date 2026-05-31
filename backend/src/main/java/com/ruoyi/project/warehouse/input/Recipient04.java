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
public class Recipient04 {
    private String name ;//String 是 50 收件人姓名
    private String phone ;//String 50 收件人电话
    private String email ;//String 100 收件人邮箱
    private String company ;//String 100 收件人公司
    private String country ;//String 是 10 目的国 id 或目的国二字码
    private String state ;//String 50 收件人州(省)
    private String city ;//String 50 收件人城市
    private String zipCode ;//String 50 邮编
    private String houseNumber ;//String 50 收件人门牌号
    private String address ;//String 是 200 收件人地址
    private String taxNumber ;//String 50 收件人税号


}
