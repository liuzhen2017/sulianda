package com.ruoyi.project.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.service.SysDictDataExtendService;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuzhen
 **/
@Service
public class SysDictDataExtendCountryService implements SysDictDataExtendService {

    @Autowired
    IBusiExpressService expressService;
    @Resource
    RedisTemplate<String,Object> redisTemplate;
    @Override
    public List<SysDictData> queryList() {
        //根据ID查询 实体类
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String expressId = request.getParameter("expressId");
        if(StringUtils.isEmpty(expressId) ||"3".equals(expressId)){
            expressId ="5";
            request.setAttribute("expressId",expressId);
        }
        Object express = redisTemplate.opsForHash().get("express", "country_" + expressId);
        if(ObjectUtil.isNotEmpty(express)){
            return (List<SysDictData>)express;
        }
        List<CountryListVo> countryListVos = expressService.queryCountryList(expressId);
        if(CollectionUtil.isEmpty(countryListVos)){
            return null;
        }
        List<SysDictData> dictDataList= countryListVos.stream().map(c->c.toDicData()).collect(Collectors.toList());
        redisTemplate.opsForHash().put("express", "country_" + expressId,dictDataList);
        return dictDataList;
    }
}
