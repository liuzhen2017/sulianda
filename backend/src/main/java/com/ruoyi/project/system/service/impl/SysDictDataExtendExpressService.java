package com.ruoyi.project.system.service.impl;

import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.service.SysDictDataExtendService;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzhen
 **/
@Service
public class SysDictDataExtendExpressService implements SysDictDataExtendService {

    @Autowired
    IBusiExpressService expressService;

    @Override
    public List<SysDictData> queryList() {
        List<BusiExpress> busiExpresses = expressService.selectBusiExpressList(new BusiExpress());

        return busiExpresses.stream().map(m->m.toDictData()).collect(Collectors.toList());
    }
}
