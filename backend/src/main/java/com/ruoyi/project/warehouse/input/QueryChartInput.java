package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryChartInput {
    /**
     * 如果是年，则只取0 --- 4位数，如果是月，2023则取4位数,如果是周，则取2023-04
     *
     */
    private String date;
    /**
     * 1 年
     * 2 月
     * 3 天
     */
    private String type;
    private String dateFrom;
    public void checkDate(){
        if (StringUtils.isEmpty(type)){
            type ="1";
        }
        if (StringUtils.isEmpty(date)){
            date ="";
        }

        switch (type){
            case "1":
                //%Y-%m
                date ="";
                dateFrom ="%Y";
                break;
            case "2":{
                dateFrom ="%Y-%m";
                break;
            }
            case "3":{
                dateFrom ="'%Y-%m-%d'";
                break;
            }
            default:{
                throw new RuntimeException("时间格式 ‘"+date+"’ 错误!");
            }

        }
    }
}
