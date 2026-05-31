package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInput04 {
    private String channelId ;//String 是 10 产品编号(产品 id)

    private String orderSource ;//String 20 订单来源

    private String orderNumber ;//String 是 50 订单号

    private String dateOfReceipt;// Date 收款到账日期 格式 yyyy-MM-dd

    private String remark ;//String 200 拣货单信息（打印标签选择 打印拣货单显示此字段信 息）

    private Recipient04 receiverInfo;

    private Sender04 senderInfo;
    private PackageInfo04 parcelInfo;
}
