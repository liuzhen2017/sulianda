package com.ruoyi.project.warehouse.service;

import java.util.List;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;

/**
 * 快递Service接口
 * 
 * @author liuzhen
 * @date 2023-01-12
 */
public interface IBusiExpressService 
{
    /**
     * 查询快递
     * 
     * @param id 快递主键
     * @return 快递
     */
    public BusiExpress selectBusiExpressById(Long id);

    /**
     * 查询快递列表
     * 
     * @param busiExpress 快递
     * @return 快递集合
     */
    public List<BusiExpress> selectBusiExpressList(BusiExpress busiExpress);

    /**
     * 新增快递
     * 
     * @param busiExpress 快递
     * @return 结果
     */
    public int insertBusiExpress(BusiExpress busiExpress);

    /**
     * 修改快递
     * 
     * @param busiExpress 快递
     * @return 结果
     */
    public int updateBusiExpress(BusiExpress busiExpress);

    /**
     * 批量删除快递
     * 
     * @param ids 需要删除的快递主键集合
     * @return 结果
     */
    public int deleteBusiExpressByIds(Long[] ids);

    /**
     * 删除快递信息
     * 
     * @param id 快递主键
     * @return 结果
     */
    public int deleteBusiExpressById(Long id);

    AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput);

    List<PrintExpressOutput> queryInfo(PrintExpressInfoInput printExpressInfoInput);

    List<CountryListVo> queryCountryList(String expressId);
    List<CountryListVo> getShippingMethod(String expressId);
    /**
     * 创建订单
     *
     * @return
     */
    public abstract OrderResultOutput createOrder(ExpressOrderInput expressOrderInput);
}
