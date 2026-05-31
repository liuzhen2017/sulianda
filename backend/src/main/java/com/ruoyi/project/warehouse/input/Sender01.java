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
public class Sender01 {
    private String first_name;//Wu Rao",//发件人姓名。可以直接传在这个字段里
    private String company;//4PX",//发件人公司
    private String phone;//13000000000",//发件人手机电话
    private String post_code;//123456",//发件人邮编
    private String country;//CN",//发件人国家/地区
    private String state;//GuangDong",//发件人省份
    private String city;//Shengzhen",//发件人城市
    private String street;//caifugang 4PX 25-26"//发件人详细地址
}
