package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class DeliverTypeInfo01 {
//    String(32)	是	1	到仓方式（1:上门揽收；2:快递到仓；3:自送到仓；5:自送门店）;
    private String deliver_type ="2";//
}
