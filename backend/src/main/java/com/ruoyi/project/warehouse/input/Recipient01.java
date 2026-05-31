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
public class Recipient01 {
    private String first_name;//zhang san,;//收件人姓名
    private String phone;//1234567890,;//收件人电话
    private String post_code;//13021,;//收件人邮编
    private String country;//US,;//收件人国家
    private String state;//NY,;//New York 如果洲省有缩写的,可以填入缩写
    private String city;//Auburn,;//收件人城市
    private String street;//str 123,;//收件人详细地址,
    private String house_number;//,;//收件人门牌号;//【只适用DHL到德国的渠道：如门牌号没填值,但详细地址有门牌号，可单独用空格分开，我们会抓取作为门牌号】
    private String email;//;//收件人邮箱


}
