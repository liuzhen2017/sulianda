package com.ruoyi.project.warehouse.mapper;

import java.util.List;
import com.ruoyi.project.warehouse.domain.BusiExpress;

/**
 * 快递Mapper接口
 * 
 * @author liuzhen
 * @date 2023-01-12
 */
public interface BusiExpressMapper 
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
     * 删除快递
     * 
     * @param id 快递主键
     * @return 结果
     */
    public int deleteBusiExpressById(Long id);

    /**
     * 批量删除快递
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiExpressByIds(Long[] ids);
}
