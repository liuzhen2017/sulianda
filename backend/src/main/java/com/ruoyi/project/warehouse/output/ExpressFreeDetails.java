package com.ruoyi.project.warehouse.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressFreeDetails {

    /**
     * 运输方式代码
     */
    public String ServiceCode;
    /**
     * 运输方式中文名
     */

    public String  ServiceCnName;
    /**
     * 运输方式中文名
     */

    public String  ServiceEnName;
    /**
     * 运输方式中文名
     */

    public String  ChargeWeight;
    /**
     * 运输方式中文名
     */

    public String  Effectiveness;

    public String  Formula;
    public String  FreightFee;
    public String  FuelFee;
    public String  RegisteredFee;
    public String  OtherFee;
    public String  OtherFeeDetails;
    public String  TotalFee;
    public String  ProductSort;
    public String  Remark;
    public String  Traceability;
    public String  VolumeCharge;
}
