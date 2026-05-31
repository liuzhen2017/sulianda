package com.ruoyi.project.warehouse.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.system.domain.SysDictData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品对象 busi_product
 *
 * @author liuzhen
 * @date 2023-02-07
 */
public class BusiProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long custId;

    /**
     * 库存
     */
    @Excel(name = "库存")
    private Long nums;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String name;

    /**
     * 图片路径
     */
    @Excel(name = "图片路径")
    private String imgUrl;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createdBy;

    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    private Long deptId;

    /**
     * 英文名称
     */
    @Excel(name = "英文名称")
    private String enName;

    /**
     * sku
     */
    @Excel(name = "sku")
    private String sku;

    /**
     * 单位
     * MTR：米
     * PCE：件
     * SET：套
     * 默认PCE
     */
    @Excel(name = "单位 MTR：米 PCE：件 SET：套 默认PCE")
    private String unitCode;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private BigDecimal price;

    /**
     * 币别
     * USD：美元
     * JPY：日元
     * RMB：人民币
     * EUR：欧元
     * GBP：英镑
     * HKD：港币
     * RUB：俄罗斯卢布
     * SGD：新加坡元
     * 默认USD
     */
    @Excel(name = "币别 USD：美元JPY：日元RMB：人民币EUR：欧元GBP：英镑HKD：港币 RUB：俄罗斯卢布 SGD：新加坡元 默认USD")
    private String invoiceCurrencyCode;

    public BusiProduct() {
    }

    public BusiProduct(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setNums(Long nums) {
        this.nums = nums;
    }

    public Long getNums() {
        return nums;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getEnName() {
        return enName;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
        this.invoiceCurrencyCode = invoiceCurrencyCode;
    }

    public String getInvoiceCurrencyCode() {
        return invoiceCurrencyCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("custId", getCustId())
                .append("nums", getNums())
                .append("name", getName())
                .append("imgUrl", getImgUrl())
                .append("createdTime", getCreatedTime())
                .append("createdBy", getCreatedBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("deptId", getDeptId())
                .append("enName", getEnName())
                .append("sku", getSku())
                .append("unitCode", getUnitCode())
                .append("price", getPrice())
                .append("invoiceCurrencyCode", getInvoiceCurrencyCode())
                .toString();
    }

    public SysDictData toDictData() {
        return SysDictData.builder()
                .dictCode(this.id)
                .dictType("product")
                .dictValue(this.id + "")
                .dictLabel(this.name)
                .build();
    }
}
