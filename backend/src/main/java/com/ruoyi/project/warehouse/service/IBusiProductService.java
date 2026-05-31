package com.ruoyi.project.warehouse.service;

import com.ruoyi.project.warehouse.domain.BusiProduct;

import java.util.List;

/**
 * 商品Service接口
 *
 * @author ruoyi
 * @date 2023-01-12
 */
public interface IBusiProductService {
    /**
     * 查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    public BusiProduct selectBusiProductById(Long id);

    /**
     * 查询所有商品
     *
     * @return 商品集合
     */
    public List<BusiProduct> selectRoleAll();

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
     * 批量删除商品
     *
     * @param ids 需要删除的商品主键集合
     * @return 结果
     */
    public int deleteBusiProductByIds(Long[] ids);

    /**
     * 删除商品信息
     *
     * @param id 商品主键
     * @return 结果
     */
    public int deleteBusiProductById(Long id);

    /**
     * 导入商品数据
     *
     * @param busiProductList 商品数据
     * @param operName 操作人
     * @return 结果
     */
    public String importProduct(List<BusiProduct> busiProductList, String operName);
}
