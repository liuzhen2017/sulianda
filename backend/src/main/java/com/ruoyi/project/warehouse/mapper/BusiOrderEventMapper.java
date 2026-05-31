package com.ruoyi.project.warehouse.mapper;

import com.ruoyi.project.warehouse.domain.BusiOrderEvent;

import java.util.List;

public interface BusiOrderEventMapper {
    List<BusiOrderEvent> selectBusiOrderEventList(BusiOrderEvent busiOrderEvent);

    int insertBusiOrderEvent(BusiOrderEvent busiOrderEvent);
}
