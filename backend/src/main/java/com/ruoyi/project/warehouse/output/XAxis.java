package com.ruoyi.project.warehouse.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liuzhen
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XAxis {
    Object[] data;
    String name="日期";
}
