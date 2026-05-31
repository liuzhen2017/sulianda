package com.ruoyi.project.warehouse.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 账户充值明细对象 busi_pay_history
 * 
 * @author ruoyi
 * @date 2023-02-10
 */
public class BusiPayHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long custId;

    /** 充值类型(1-微信 2-支付宝) */
    @Excel(name = "充值类型(1-微信 2-支付宝)")
    private String payType;

    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal balance;

    /** 交易金额 */
    @Excel(name = "交易金额")
    private BigDecimal payMoney;

    /** 系统订单号 */
    @Excel(name = "系统订单号")
    private String orderNo;

    /** 充值截图 */
    @Excel(name = "充值截图")
    private String payUrl;

    /** 充值状态(0-待审核 1-已通过 2-已驳回) */
    @Excel(name = "充值状态(0-待审核 1-已通过 2-已驳回)")
    private String payStatus;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalDate;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal money;

    /** 备注 */
    @Excel(name = "备注")
    private String remakr;

    /** 说明 */
    @Excel(name = "说明")
    private String explainText;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdDate;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedDate;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updatedBy;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    public BusiPayHistory(Long custId) {
        this.custId = custId;
    }

    public BusiPayHistory(Long custId, String payStatus) {
        this.custId = custId;
        this.payStatus = payStatus;
    }

    public BusiPayHistory() {
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCustId(Long custId) 
    {
        this.custId = custId;
    }

    public Long getCustId() 
    {
        return custId;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setBalance(BigDecimal balance) 
    {
        this.balance = balance;
    }

    public BigDecimal getBalance() 
    {
        return balance;
    }
    public void setPayMoney(BigDecimal payMoney) 
    {
        this.payMoney = payMoney;
    }

    public BigDecimal getPayMoney() 
    {
        return payMoney;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setPayUrl(String payUrl) 
    {
        this.payUrl = payUrl;
    }

    public String getPayUrl() 
    {
        return payUrl;
    }
    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }
    public void setApprovalDate(Date approvalDate) 
    {
        this.approvalDate = approvalDate;
    }

    public Date getApprovalDate() 
    {
        return approvalDate;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }
    public void setRemakr(String remakr) 
    {
        this.remakr = remakr;
    }

    public String getRemakr() 
    {
        return remakr;
    }
    public void setExplainText(String explainText) 
    {
        this.explainText = explainText;
    }

    public String getExplainText() 
    {
        return explainText;
    }
    public void setCreatedDate(Date createdDate) 
    {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() 
    {
        return createdDate;
    }
    public void setUpdatedDate(Date updatedDate) 
    {
        this.updatedDate = updatedDate;
    }

    public Date getUpdatedDate() 
    {
        return updatedDate;
    }
    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }
    public void setUpdatedBy(String updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() 
    {
        return updatedBy;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("custId", getCustId())
            .append("payType", getPayType())
            .append("balance", getBalance())
            .append("payMoney", getPayMoney())
            .append("orderNo", getOrderNo())
            .append("payUrl", getPayUrl())
            .append("payStatus", getPayStatus())
            .append("approvalDate", getApprovalDate())
            .append("money", getMoney())
            .append("remakr", getRemakr())
            .append("explainText", getExplainText())
            .append("createdDate", getCreatedDate())
            .append("updatedDate", getUpdatedDate())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("deptId", getDeptId())
            .toString();
    }
}
