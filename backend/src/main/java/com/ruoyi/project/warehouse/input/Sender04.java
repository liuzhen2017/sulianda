package com.ruoyi.project.warehouse.input;

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
public class Sender04 {
    private String name ;//String 50 发件人姓名
    private String phone ;//String 50 发件人电话
    private String company ;//String 100 发件人公司
    private String email ;//String 100 发件人邮箱
    private String country ;//String 10 发件人国家
    private String state ;//String 50 发件人州(省)
    private String city ;//String 50 发件人城市
    private String zipCode ;//String 50 发件人邮编
    private String houseNumber ;//String 50 发件人门牌号
    private String address ;//String 200 发件人地址
    private String taxNumber ;//String 50 发件人税号
}
