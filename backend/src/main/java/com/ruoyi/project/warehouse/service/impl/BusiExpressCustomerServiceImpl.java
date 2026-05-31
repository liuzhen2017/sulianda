package com.ruoyi.project.warehouse.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.mapper.BusiExpressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.warehouse.mapper.BusiExpressCustomerMapper;
import com.ruoyi.project.warehouse.domain.BusiExpressCustomer;
import com.ruoyi.project.warehouse.service.IBusiExpressCustomerService;

/**
 * 快递客户Service业务层处理
 * 
 * @author liuzhen
 * @date 2023-01-15
 */
@Service
public class BusiExpressCustomerServiceImpl implements IBusiExpressCustomerService 
{
    @Autowired
    private BusiExpressCustomerMapper busiExpressCustomerMapper;

    @Autowired
    private BusiExpressMapper busiExpressMapper;

    /**
     * 查询快递客户
     * 
     * @param id 快递客户主键
     * @return 快递客户
     */
    @Override
    public BusiExpressCustomer selectBusiExpressCustomerById(Long id)
    {
        return busiExpressCustomerMapper.selectBusiExpressCustomerById(id);
    }

    /**
     * 查询快递客户列表
     * 
     * @param busiExpressCustomer 快递客户
     * @return 快递客户
     */
    @Override
    public List<BusiExpressCustomer> selectBusiExpressCustomerList(BusiExpressCustomer busiExpressCustomer)
    {
        return busiExpressCustomerMapper.selectBusiExpressCustomerList(busiExpressCustomer);
    }

    /**
     * 新增快递客户
     * 
     * @param busiExpressCustomer 快递客户
     * @return 结果
     */
    @Override
    public int insertBusiExpressCustomer(BusiExpressCustomer busiExpressCustomer)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiExpressCustomer.setDeptId(loginUser.getDeptId());
        busiExpressCustomer.setCreatedBy(loginUser.getUsername());
        busiExpressCustomer.setCreatedDate(new Date());
        busiExpressCustomer.setUpdatedBy(loginUser.getUsername());
        return busiExpressCustomerMapper.insertBusiExpressCustomer(busiExpressCustomer);
    }

    /**
     * 修改快递客户
     * 
     * @param busiExpressCustomer 快递客户
     * @return 结果
     */
    @Override
    public int updateBusiExpressCustomer(BusiExpressCustomer busiExpressCustomer)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiExpressCustomer.setDeptId(loginUser.getDeptId());
        busiExpressCustomer.setUpdatedBy(loginUser.getUsername());
        busiExpressCustomer.setUpdatedDate(new Date());
        return busiExpressCustomerMapper.updateBusiExpressCustomer(busiExpressCustomer);
    }

    /**
     * 批量删除快递客户
     * 
     * @param ids 需要删除的快递客户主键
     * @return 结果
     */
    @Override
    public int deleteBusiExpressCustomerByIds(Long[] ids)
    {
        return busiExpressCustomerMapper.deleteBusiExpressCustomerByIds(ids);
    }

    /**
     * 删除快递客户信息
     * 
     * @param id 快递客户主键
     * @return 结果
     */
    @Override
    public int deleteBusiExpressCustomerById(Long id)
    {
        return busiExpressCustomerMapper.deleteBusiExpressCustomerById(id);
    }

    @Override
    public String importCustomer(List<BusiExpressCustomer> busiExpressCustomerList, String operName)
    {
        if (busiExpressCustomerList == null || busiExpressCustomerList.isEmpty())
        {
            throw new ServiceException("导入快递客户数据不能为空！");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BusiExpressCustomer busiExpressCustomer : busiExpressCustomerList)
        {
            try
            {
                if (busiExpressCustomer.getExpressId() == null)
                {
                    throw new ServiceException("快递公司ID不能为空");
                }
                BusiExpress busiExpress = busiExpressMapper.selectBusiExpressById(busiExpressCustomer.getExpressId());
                if (busiExpress == null)
                {
                    throw new ServiceException("快递公司不存在: " + busiExpressCustomer.getExpressId());
                }
                busiExpressCustomer.setDeptId(loginUser.getDeptId());
                busiExpressCustomer.setCreatedBy(operName);
                busiExpressCustomer.setUpdatedBy(operName);
                busiExpressCustomer.setCreatedDate(new Date());
                busiExpressCustomer.setUpdatedDate(new Date());
                busiExpressCustomerMapper.insertBusiExpressCustomer(busiExpressCustomer);
                successNum++;
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、快递客户 " + (busiExpressCustomer.getAddressee() == null ? "未命名" : busiExpressCustomer.getAddressee()) + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，快递客户数据已全部导入成功！共 " + successNum + " 条。");
        return successMsg.toString();
    }
}
