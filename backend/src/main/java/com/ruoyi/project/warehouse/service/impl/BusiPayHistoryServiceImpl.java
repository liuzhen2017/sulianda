package com.ruoyi.project.warehouse.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.warehouse.domain.BusiPayHistory;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.mapper.BusiPayHistoryMapper;
import com.ruoyi.project.warehouse.service.IBusiPayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账户充值明细Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-16
 */
@Service
public class BusiPayHistoryServiceImpl implements IBusiPayHistoryService {
    @Autowired
    private BusiPayHistoryMapper busiPayHistoryMapper;

    /**
     * 查询账户充值明细
     *
     * @param id 账户充值明细主键
     * @return 账户充值明细
     */
    @Override
    public BusiPayHistory selectBusiPayHistoryById(Long id) {
        return busiPayHistoryMapper.selectBusiPayHistoryById(id);
    }

    /**
     * 查询账户充值明细列表
     *
     * @param busiPayHistory 账户充值明细
     * @return 账户充值明细
     */
    @Override
    public List<BusiPayHistory> selectBusiPayHistoryList(BusiPayHistory busiPayHistory) {
        return busiPayHistoryMapper.selectBusiPayHistoryList(busiPayHistory);
    }

    /**
     * 新增账户充值明细
     *
     * @param busiPayHistory 账户充值明细
     * @return 结果
     */
    @Override
    public int insertBusiPayHistory(BusiPayHistory busiPayHistory) {
        busiPayHistory.setPayStatus("0");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiPayHistory.setDeptId(loginUser.getDeptId());
        busiPayHistory.setOrderNo("未关联订单");
        busiPayHistory.setMoney(new BigDecimal(busiPayHistory.getMoney().intValue()).subtract(new BigDecimal(busiPayHistory.getMoney().intValue()).multiply(new BigDecimal(0.006))));
        // 充值账单时交易金额为0，只有发生订单扣减时有值
        busiPayHistory.setPayMoney(new BigDecimal(0));
        return busiPayHistoryMapper.insertBusiPayHistory(busiPayHistory);
    }

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 修改账户充值明细
     *
     * @param busiPayHistory 账户充值明细
     * @return 结果
     */
    @Override
    public int updateBusiPayHistory(BusiPayHistory busiPayHistory) {
        busiPayHistory.setPayStatus("1");
        busiPayHistory.setPayMoney(new BigDecimal(0));
        busiPayHistory.setApprovalDate(new Date());

        SysUser user = sysUserMapper.selectUserById(busiPayHistory.getCustId());
        BigDecimal currentBalance = user.getBalance() == null ? BigDecimal.ZERO : user.getBalance();
        BigDecimal nextBalance = currentBalance.add(busiPayHistory.getMoney());
        busiPayHistory.setBalance(nextBalance);
        user.setBalance(nextBalance);
        sysUserMapper.updateUser(user);

        return busiPayHistoryMapper.updateBusiPayHistory(busiPayHistory);
    }

    @Override
    public int updateReject(BusiPayHistory busiPayHistory) {
        // 驳回
        busiPayHistory.setPayStatus("2");
        return busiPayHistoryMapper.updateBusiPayHistory(busiPayHistory);
    }

    /**
     * 批量删除账户充值明细
     *
     * @param ids 需要删除的账户充值明细主键
     * @return 结果
     */
    @Override
    public int deleteBusiPayHistoryByIds(Long[] ids) {
        return busiPayHistoryMapper.deleteBusiPayHistoryByIds(ids);
    }

    /**
     * 删除账户充值明细信息
     *
     * @param id 账户充值明细主键
     * @return 结果
     */
    @Override
    public int deleteBusiPayHistoryById(Long id) {
        return busiPayHistoryMapper.deleteBusiPayHistoryById(id);
    }

    @Override
    public BigDecimal queryTotalAmount() {
        return busiPayHistoryMapper.queryTotalAmount();
    }

    @Override
    public List<Map<String, Object>> queryRechargeHistory(QueryChartInput chartInput) {
        return busiPayHistoryMapper.queryRechargeHistory(chartInput);
    }
}
