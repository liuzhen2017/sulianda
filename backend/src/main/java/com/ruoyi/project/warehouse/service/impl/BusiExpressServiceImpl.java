package com.ruoyi.project.warehouse.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.input.BaseDto;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.ExpressServiceBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.warehouse.mapper.BusiExpressMapper;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 快递Service业务层处理
 * 
 * @author liuzhen
 * @date 2023-01-12
 */
@Service
public class BusiExpressServiceImpl extends ExpressServiceBaseService implements IBusiExpressService
{
    @Resource
    private BusiExpressMapper busiExpressMapper;
    @Autowired
    Map<String,ExpressServiceBaseService> serviceBaseServiceMap;

    /**
     * 查询快递
     * 
     * @param id 快递主键
     * @return 快递
     */
    @Override
    public BusiExpress selectBusiExpressById(Long id)
    {
        return busiExpressMapper.selectBusiExpressById(id);
    }

    /**
     * 查询快递列表
     * 
     * @param busiExpress 快递
     * @return 快递
     */
    @Override
    public List<BusiExpress> selectBusiExpressList(BusiExpress busiExpress)
    {
        return busiExpressMapper.selectBusiExpressList(busiExpress);
    }

    /**
     * 新增快递
     * 
     * @param busiExpress 快递
     * @return 结果
     */
    @Override
    public int insertBusiExpress(BusiExpress busiExpress)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiExpress.setDeptId(loginUser.getDeptId());
        busiExpress.setCreatedBy(loginUser.getUsername());
        busiExpress.setCreatedDate(new Date());
        busiExpress.setUpdatedBy(loginUser.getUsername());
        return busiExpressMapper.insertBusiExpress(busiExpress);
    }

    /**
     * 修改快递
     * 
     * @param busiExpress 快递
     * @return 结果
     */
    @Override
    public int updateBusiExpress(BusiExpress busiExpress)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiExpress.setUpdatedDate(new Date());
        busiExpress.setUpdatedBy(loginUser.getUsername());
        return busiExpressMapper.updateBusiExpress(busiExpress);
    }

    /**
     * 批量删除快递
     * 
     * @param ids 需要删除的快递主键
     * @return 结果
     */
    @Override
    public int deleteBusiExpressByIds(Long[] ids)
    {
        return busiExpressMapper.deleteBusiExpressByIds(ids);
    }

    /**
     * 删除快递信息
     * 
     * @param id 快递主键
     * @return 结果
     */
    @Override
    public int deleteBusiExpressById(Long id)
    {
        return busiExpressMapper.deleteBusiExpressById(id);
    }


    public ExpressServiceBaseService getBaseService(String expressId){
        //根据ID查询 实体类
        if(StringUtils.isEmpty(expressId)) {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            expressId = request.getParameter("expressId");
            if (StringUtils.isEmpty(expressId)) {
                expressId = "5";
            }
        }
        BusiExpress express = selectBusiExpressById(Long.parseLong(expressId));
        if (express == null) {
            throw new ServiceException("未找到物流渠道配置，expressId=" + expressId);
        }
        ExpressServiceBaseService expressService = getExpressService(express);
        expressService.setBusiExpress(express);
        return expressService;
    }

    @Override
    public List<CountryListVo> getShippingMethod(String expressId) {


        return getBaseService(expressId).getShippingMethod();
    }

    @Override
    public List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput) {
        return getBaseService(printExpressInfoInput.getExpressId()).queryInfo(printExpressInfoInput);
    }

    @Override
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput) {



        return AjaxResult.success(getBaseService(expressFreeInput.getExpressId()).queryExpressFree(expressFreeInput));
    }



    @Override
    public List<CountryListVo> queryCountryList(String expressId) {


        return getBaseService(expressId).queryCountryList();
    }

    @Override
    public OrderResultOutput createOrder(ExpressOrderInput expressOrderInput) {
        return getBaseService(expressOrderInput.getExpressId()).createOrder(expressOrderInput);
    }


    public ExpressServiceBaseService getExpressService(BusiExpress express){
        if (express == null || StringUtils.isEmpty(express.getName())) {
            throw new ServiceException("物流渠道配置不完整，无法匹配服务实现");
        }
        String normalizedName = normalizeExpressName(express.getName());
        for (Map.Entry<String, ExpressServiceBaseService> entry : serviceBaseServiceMap.entrySet()) {
            if (matchesProvider(entry.getKey(), normalizedName)) {
                return entry.getValue();
            }
        }
        throw new ServiceException("未找到物流服务实现，expressId=" + express.getId() + "，渠道名称=" + express.getName());
    }

    private String normalizeExpressName(String name) {
        return StringUtils.trimToEmpty(name)
                .replace("物流", "")
                .replace("国际", "")
                .replace("快递", "")
                .replace("速递", "")
                .replace("（", "(")
                .replace("）", ")")
                .replace(" ", "")
                .toLowerCase(Locale.ROOT);
    }

    private boolean matchesProvider(String beanName, String normalizedName) {
        if (StringUtils.isEmpty(beanName) || StringUtils.isEmpty(normalizedName)) {
            return false;
        }
        String normalizedBean = beanName.toLowerCase(Locale.ROOT);
        if (normalizedName.contains("递四方") || normalizedName.contains("4px")) {
            return normalizedBean.contains("disifang");
        }
        if (normalizedName.contains("捷航")) {
            return normalizedBean.contains("jiehang");
        }
        if (normalizedName.contains("三态") || normalizedName.contains("sfc")) {
            return normalizedBean.contains("santai");
        }
        if (normalizedName.contains("燕文") || normalizedName.contains("yw")) {
            return normalizedBean.contains("yanwen");
        }
        if (normalizedName.contains("义达") || normalizedName.contains("易达") || normalizedName.contains("yd")) {
            return normalizedBean.contains("yida");
        }
        if (normalizedName.contains("信思") || normalizedName.contains("xinsi")) {
            return normalizedBean.contains("xinsi");
        }
        return false;
    }


}
