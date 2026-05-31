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
public class Series {
    String  name= "pageA";
    String  type= "line";
    boolean smooth=true;
    String barWidth= "60%";
    List<Object> data;
}
