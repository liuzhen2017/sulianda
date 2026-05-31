package com.ruoyi.project.warehouse.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressFreeOutput {

    List<ExpressFreeDetails> expressFreeDetails;

    public String  TotalFee;
    public String  ProductSort;
    public String  Remark;
    public String  Traceability;
    public String  VolumeCharge;
}
