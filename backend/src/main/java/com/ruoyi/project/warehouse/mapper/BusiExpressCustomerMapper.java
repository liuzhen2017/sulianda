package com.ruoyi.project.warehouse.mapper;

import java.util.List;
import com.ruoyi.project.warehouse.domain.BusiExpressCustomer;

/**
 * 快递客户Mapper接口
 * 
 * @author liuzhen
 * @date 2023-01-15
 */
public interface BusiExpressCustomerMapper 
{
    /**
     * 查询快递客户
     * 
     * @param id 快递客户主键
     * @return 快递客户
     */
    public BusiExpressCustomer selectBusiExpressCustomerById(Long id);

    /**
     * 查询快递客户列表
     * 
     * @param busiExpressCustomer 快递客户
     * @return 快递客户集合
     */
    public List<BusiExpressCustomer> selectBusiExpressCustomerList(BusiExpressCustomer busiExpressCustomer);

    /**
     * 新增快递客户
     * 
     * @param busiExpressCustomer 快递客户
     * @return 结果
     */
    public int insertBusiExpressCustomer(BusiExpressCustomer busiExpressCustomer);

    /**
     * 修改快递客户
     * 
     * @param busiExpressCustomer 快递客户
     * @return 结果
     */
    public int updateBusiExpressCustomer(BusiExpressCustomer busiExpressCustomer);

    /**
     * 删除快递客户
     * 
     * @param id 快递客户主键
     * @return 结果
     */
    public int deleteBusiExpressCustomerById(Long id);

    /**
     * 批量删除快递客户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiExpressCustomerByIds(Long[] ids);
}
