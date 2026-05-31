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
public class Insurance01 {
    private String first_name;//zhang san,;//收件人姓名
    private String insure_type;//5Y",// 保险类型，保险类型（XY:4PX保价；XP:第三方保险） 5Y, 5元每票 8Y, 8元每票 6P, 0.6%保费
    private String insure_value;//23",//保险价值，相关的4PX保险购买可以咨询我们业务了解
    private String currency;//USD"//保险币别 ，4PX保险必须是USD


}
