package com.ruoyi.project.warehouse.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 快递客户对象 busi_express_customer
 * 
 * @author liuzhen
 * @date 2023-01-29
 */
public class BusiExpressCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 快递公司ID */
    @Excel(name = "快递公司ID")
    private Long expressId;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 收件人 */
    @Excel(name = "收件人")
    private String addressee;

    /** 公司 */
    @Excel(name = "公司")
    private String company;

    /** 邮编 */
    @Excel(name = "邮编")
    private String zipCode;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 州 */
    @Excel(name = "州")
    private String states;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String tel;

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

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setExpressId(Long expressId) 
    {
        this.expressId = expressId;
    }

    public Long getExpressId() 
    {
        return expressId;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setAddressee(String addressee) 
    {
        this.addressee = addressee;
    }

    public String getAddressee() 
    {
        return addressee;
    }
    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getCompany() 
    {
        return company;
    }
    public void setZipCode(String zipCode) 
    {
        this.zipCode = zipCode;
    }

    public String getZipCode() 
    {
        return zipCode;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setStates(String states) 
    {
        this.states = states;
    }

    public String getStates() 
    {
        return states;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setTel(String tel) 
    {
        this.tel = tel;
    }

    public String getTel() 
    {
        return tel;
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
            .append("expressId", getExpressId())
            .append("address", getAddress())
            .append("addressee", getAddressee())
            .append("company", getCompany())
            .append("zipCode", getZipCode())
            .append("city", getCity())
            .append("states", getStates())
            .append("country", getCountry())
            .append("tel", getTel())
            .append("createdDate", getCreatedDate())
            .append("updatedDate", getUpdatedDate())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("deptId", getDeptId())
            .toString();
    }
}
