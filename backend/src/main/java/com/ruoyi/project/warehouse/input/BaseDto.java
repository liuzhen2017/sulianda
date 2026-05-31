package com.ruoyi.project.warehouse.input;

import lombok.Data;

/**
 * @author liuzhen
 **/
@Data
public class BaseDto {
    /**
     * 快递渠道ID ==快递渠道表
     */
    private String expressId;

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }
}
