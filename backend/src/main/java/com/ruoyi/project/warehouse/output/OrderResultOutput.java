package com.ruoyi.project.warehouse.output;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
/**
 * @author liuzhen
 **/
@Builder
public class OrderResultOutput {
    /**
     * 订单号--第三方返回
     */
    @JSONField(name = "order_id")
    private String orderId;
    /**
     * 客户参考号--客户订单ID
     */
    @JSONField(name = "refrence_no")
    private String refrenceNo;
    /**
     * 服务商单号(快递跟踪单号，做跳转)
     */
    @JSONField(name = "shipping_method_no")
    private String shippingMethodNo;
    /**
     *渠道转单号
     */
    @JSONField(name = "channel_hawbcode")
    private String channelHawbCode;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefrenceNo() {
        return refrenceNo;
    }

    public void setRefrenceNo(String refrenceNo) {
        this.refrenceNo = refrenceNo;
    }

    public String getShippingMethodNo() {
        return shippingMethodNo;
    }

    public void setShippingMethodNo(String shippingMethodNo) {
        this.shippingMethodNo = shippingMethodNo;
    }

    public String getChannelHawbCode() {
        return channelHawbCode;
    }

    public void setChannelHawbCode(String channelHawbCode) {
        this.channelHawbCode = channelHawbCode;
    }
}
