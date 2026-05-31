package com.ruoyi.project.warehouse.mapper;

import java.util.List;
import com.ruoyi.project.warehouse.domain.BusiProduct;

/**
 * 商品Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
public interface BusiProductMapper 
{
    /**
     * 查询商品
     * 
     * @param id 商品主键
     * @return 商品
     */
    public BusiProduct selectBusiProductById(Long id);

    /**
     * 查询商品列表
     * 
     * @param busiProduct 商品
     * @return 商品集合
     */
    public List<BusiProduct> selectBusiProductList(BusiProduct busiProduct);

    /**
     * 新增商品
     * 
     * @param busiProduct 商品
     * @return 结果
     */
    public int insertBusiProduct(BusiProduct busiProduct);

    /**
     * 修改商品
     * 
     * @param busiProduct 商品
     * @return 结果
     */
    public int updateBusiProduct(BusiProduct busiProduct);

    /**
     * 删除商品
     * 
     * @param id 商品主键
     * @return 结果
     */
    public int deleteBusiProductById(Long id);

    /**
     * 批量删除商品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiProductByIds(Long[] ids);
}
