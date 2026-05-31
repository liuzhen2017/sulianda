package com.ruoyi.project.warehouse.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.mapper.BusiProductMapper;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
@Service
public class BusiProductServiceImpl implements IBusiProductService 
{
    @Autowired
    private BusiProductMapper busiProductMapper;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询商品
     * 
     * @param id 商品主键
     * @return 商品
     */
    @Override
    public BusiProduct selectBusiProductById(Long id)
    {
        return busiProductMapper.selectBusiProductById(id);
    }

    /**
     * 查询商品列表
     * 
     * @param busiProduct 商品
     * @return 商品
     */
    @Override
    public List<BusiProduct> selectBusiProductList(BusiProduct busiProduct)
    {
        return busiProductMapper.selectBusiProductList(busiProduct);
    }

    @Override
    public List<BusiProduct> selectRoleAll() {
        return selectBusiProductList(new BusiProduct());
    }

    /**
     * 新增商品
     * 
     * @param busiProduct 商品
     * @return 结果
     */
    @Override
    public int insertBusiProduct(BusiProduct busiProduct)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiProduct.setDeptId(loginUser.getDeptId());
        return busiProductMapper.insertBusiProduct(busiProduct);
    }

    /**
     * 修改商品
     * 
     * @param busiProduct 商品
     * @return 结果
     */
    @Override
    public int updateBusiProduct(BusiProduct busiProduct)
    {
        busiProduct.setUpdateTime(DateUtils.getNowDate());
        return busiProductMapper.updateBusiProduct(busiProduct);
    }

    /**
     * 批量删除商品
     * 
     * @param ids 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteBusiProductByIds(Long[] ids)
    {
        return busiProductMapper.deleteBusiProductByIds(ids);
    }

    /**
     * 删除商品信息
     * 
     * @param id 商品主键
     * @return 结果
     */
    @Override
    public int deleteBusiProductById(Long id)
    {
        return busiProductMapper.deleteBusiProductById(id);
    }

    @Override
    public String importProduct(List<BusiProduct> busiProductList, String operName)
    {
        if (busiProductList == null || busiProductList.isEmpty())
        {
            throw new ServiceException("导入商品数据不能为空！");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BusiProduct busiProduct : busiProductList)
        {
            try
            {
                if (busiProduct.getName() == null || busiProduct.getName().trim().isEmpty())
                {
                    throw new ServiceException("商品名称不能为空");
                }
                normalizeProductDictValue(busiProduct, "unit", busiProduct.getUnitCode(), true);
                normalizeProductDictValue(busiProduct, "invoice_currencycode", busiProduct.getInvoiceCurrencyCode(), false);
                busiProduct.setCustId(loginUser.getUserId());
                busiProduct.setDeptId(loginUser.getDeptId());
                busiProduct.setCreatedBy(operName);
                busiProduct.setUpdateBy(operName);
                busiProduct.setCreatedTime(new Date());
                busiProduct.setUpdateTime(DateUtils.getNowDate());
                busiProductMapper.insertBusiProduct(busiProduct);
                successNum++;
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、商品 " + (busiProduct.getName() == null ? "未命名" : busiProduct.getName()) + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，商品数据已全部导入成功！共 " + successNum + " 条。");
        return successMsg.toString();
    }

    private void normalizeProductDictValue(BusiProduct busiProduct, String dictType, String rawValue, boolean unit)
    {
        if (rawValue == null || rawValue.trim().isEmpty())
        {
            return;
        }
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictType(dictType);
        sysDictData.setDictValue(rawValue);
        List<SysDictData> dictValueList = dictDataService.selectDictDataList(sysDictData);
        if (!dictValueList.isEmpty())
        {
            return;
        }
        sysDictData = new SysDictData();
        sysDictData.setDictType(dictType);
        sysDictData.setDictLabel(rawValue);
        List<SysDictData> dictLabelList = dictDataService.selectDictDataList(sysDictData);
        if (dictLabelList.isEmpty())
        {
            throw new ServiceException((unit ? "单位" : "币别") + "不存在: " + rawValue);
        }
        if (unit)
        {
            busiProduct.setUnitCode(dictLabelList.get(0).getDictValue());
        }
        else
        {
            busiProduct.setInvoiceCurrencyCode(dictLabelList.get(0).getDictValue());
        }
    }
}
