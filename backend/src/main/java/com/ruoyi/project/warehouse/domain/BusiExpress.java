package com.ruoyi.project.warehouse.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.SysDictData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 快递对象 busi_express
 * 
 * @author liuzhen
 * @date 2023-01-29
 */
public class BusiExpress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID")
    private Long id;

    /** 快递名称 */
    @Excel(name = "快递名称")
    private String name;

    /** 授权账号 */
    @Excel(name = "授权账号")
    private String authorizationNo;

    /** 授权码 */
    @Excel(name = "授权码")
    private String authorizationCode;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private String isEnable;

    /** api路径 */
    @Excel(name = "api路径")
    private String apiUrl;

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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAuthorizationNo(String authorizationNo) 
    {
        this.authorizationNo = authorizationNo;
    }

    public String getAuthorizationNo() 
    {
        return authorizationNo;
    }
    public void setAuthorizationCode(String authorizationCode) 
    {
        this.authorizationCode = authorizationCode;
    }

    public String getAuthorizationCode() 
    {
        return authorizationCode;
    }
    public void setIsEnable(String isEnable) 
    {
        this.isEnable = isEnable;
    }

    public String getIsEnable() 
    {
        return isEnable;
    }
    public void setApiUrl(String apiUrl) 
    {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl() 
    {
        return apiUrl;
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
            .append("name", getName())
            .append("authorizationNo", getAuthorizationNo())
            .append("authorizationCode", getAuthorizationCode())
            .append("isEnable", getIsEnable())
            .append("apiUrl", getApiUrl())
            .append("createdDate", getCreatedDate())
            .append("updatedDate", getUpdatedDate())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("deptId", getDeptId())
            .toString();
    }

    public SysDictData toDictData() {
        return SysDictData.builder()
                .dictType("express")
                .dictCode(this.id)
                .dictLabel(this.name)
                .dictValue(this.id+"")
                .build();
    }
}
