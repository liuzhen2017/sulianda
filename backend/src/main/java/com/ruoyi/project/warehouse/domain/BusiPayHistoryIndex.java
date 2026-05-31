package com.ruoyi.project.warehouse.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 账户充值明细索引对象 busi_pay_history_index
 * 
 * @author liuzhen
 * @date 2023-04-06
 */
public class BusiPayHistoryIndex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long custId;

    /** 系统订单号 */
    @Excel(name = "系统订单号")
    private String orderNo;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal money;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdDate;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    public BusiPayHistoryIndex() {
    }

    public BusiPayHistoryIndex(String orderNo) {
        this.orderNo = orderNo;
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
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
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
            .append("orderNo", getOrderNo())
            .append("money", getMoney())
            .append("createdDate", getCreatedDate())
            .append("updatedDate", getUpdatedDate())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("deptId", getDeptId())
            .toString();
    }
}
