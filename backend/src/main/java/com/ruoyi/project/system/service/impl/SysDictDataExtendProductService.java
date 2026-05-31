package com.ruoyi.project.system.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.service.SysDictDataExtendService;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzhen
 **/
@Service
public class SysDictDataExtendProductService implements SysDictDataExtendService {

    @Autowired
    IBusiProductService busiProductService;

    @Override
    public List<SysDictData> queryList() {
        List<BusiProduct> busiProducts = busiProductService.selectBusiProductList(new BusiProduct());
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return busiProducts.stream().filter(b ->b.getCreatedBy().equals(loginUser.getUsername()))
                .map(m->m.toDictData()).collect(Collectors.toList());
    }
}
