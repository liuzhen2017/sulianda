package com.ruoyi.project.warehouse.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 busi_order
 *
 * @author ruoyi
 * @date 2023-03-09
 */
@Data
public class BusiOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户ID
     */
    //@Excel(name = "客户ID")
    private Long custId;

    /**
     * 第三方订单号
     */
    //@Excel(name = "第三方订单号")
    private String orderNo;

    /**
     * 快递订单号
     */
    @Excel(name = "快递订单号(导入时填写第三方下单订单号,不填写则根据系统下单分配快递单号)", width = 80)
    private String thirdPartyNo;

    /**
     * FBA订单时作为客户单号
     */
    //@Excel(name = "FBA订单时作为客户单号")
    private String deliveryNo;

    /**
     * 订单类型(1-FBA 2-FBN)
     */
    //@Excel(name = "订单类型(1-FBA 2-FBN)")
    private String type;

    /**
     * 商品信息(兼容旧导入使用)
     */
    private String productName;

    /**==================FBA============*/
    /**
     * 服务类型
     */
    private String serverType;

    /**
     * 地址库
     */
    private String addressLibrary;

    /**
     * 地址一
     */
    private String addressOne;

    /**
     * 地址二
     */
    private String addressTwo;

    /**
     * 地址三
     */
    private String addressThree;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 门店
     */
    private String store;

    /**
     * 参考号一
     */
    private String referenceOne;

    /**
     * 参考号二
     */
    private String referenceTwo;

    /**
     * EORI
     */
    private String eori;

    /**
     * 物品属性
     */
    private String attribute;

    /**
     * 购买保险
     */
    private String payInsurance;

    /**
     * 保价
     */
    private BigDecimal insured;

    /**==================FBA============*/

    /**
     * 采购单号
     */
    @Excel(name = "采购单号")
    private String purchaseNo;

    /**
     * 采购来源
     */
    //@Excel(name = "采购来源")
    private String purchaseSource;

    /**
     * 采购文件URL
     */
    @Excel(name = "采购图片地址")
    private String purchaseUrl;
    private String hsCode;

    /**
     * 配送备注
     */
    //@Excel(name = "配送备注")
    private String deliveryRemark;

    /**
     * 订单业务状态(0-草稿箱 1-待入库 2-已入库 3-已发货 4-被驳回 5-已扣费)
     */
    //@Excel(name = "订单业务状态(0-草稿箱 1-待入库 2-已入库 3-已发货 4-被驳回)")
    private String status;

    /**
     * 商品ID
     */
    //@Excel(name = "商品ID")
    private Long productId;

    /**
     * 客户信息ID
     */
    private Long customerId;

    /**
     * 商品数量
     */
    //@Excel(name = "商品数量")
    private Long productNums;


    /**
     * 客户发货数
     */
    @Excel(name = "客户发货数")
    private Long custSendNums;

    /**
     * 面单URL
     */
    //@Excel(name = "面单URL")
    private String faceUrl;

    /**
     * 物流公司ID
     */
    //@Excel(name = "物流公司ID")
    private Long expressId;

    /**
     * 运费
     */
    //@Excel(name = "运费")
    private BigDecimal expressMoney;

    /**
     * 总费用
     */
    //@Excel(name = "总费用")
    private BigDecimal totalMoney;

    /**
     * 快递公司
     */
    //@Excel(name = "快递公司")
    private String expressCompany;

    /**
     * 快递渠道
     */
    //@Excel(name = "快递渠道")
    private String expressChannel;

    /**
     * 打包费
     */
    //@Excel(name = "打包费")
    private Long packageMoney;

    /**
     * 盈利费
     */
    //@Excel(name = "盈利费")
    private Long profitMoney;

    /**
     * 税金
     */
    //@Excel(name = "税金")
    private BigDecimal taxes;

    /**
     * 到付件
     */
    private Long arrivedMoney;

    /**
     * 目的地国家中文名
     */
    @Excel(name = "目的地国家中文名")
    private String countryName;

    /**
     * 目的地国家英文名
     */
    //@Excel(name = "目的地国家英文名")
    private String countryEname;

    /**
     * 检查状态(check_pass/check_pending/check_failed)
     */
    private String checkStatus;

    /**
     * 检查摘要
     */
    private String checkSummary;

    /**
     * 异常类型
     */
    private String issueType;

    /**
     * 问题件状态
     */
    private String issueStatus;

    /**
     * 放行状态
     */
    private String releaseStatus;

    /**
     * 驳回原因
     */
    //@Excel(name = "驳回原因")
    private String rejectRemark;

    /**
     * 仓库发货数(入库时填写)
     */
    //@Excel(name = "仓库发货数(入库时填写)")
    private Long warehouseSendNums;

    /**
     * 货品长度(cm)
     */
    //@Excel(name = "货品长度(cm)")
    private BigDecimal goodsLength;

    /**
     * 货品宽度(cm)
     */
    //@Excel(name = "货品宽度(cm)")
    private BigDecimal goodsWidth;

    /**
     * 货品高度(cm)
     */
    //@Excel(name = "货品高度(cm)")
    private BigDecimal goodsHeight;

    /**
     * 货品重量(kg)
     */
    //@Excel(name = "货品重量(kg)")
    private BigDecimal goodsWeight;

    /**
     * 发件人姓名
     */
    //@Excel(name = "发件人姓名")
    private String shipperName;

    /**
     * 发件人国家编码
     */
    //@Excel(name = "发件人国家编码")
    private String shipperCountryCode;

    /**
     * 发件人地址
     */
    //@Excel(name = "发件人地址")
    private String shipperStreet;

    /**
     * 发件人电话
     */
    //@Excel(name = "发件人电话")
    private String shipperTelephone;

    /**
     * 发件人城市
     */
    //@Excel(name = "发件人城市")
    private String shipperCity;

    /**
     * 地址库编码
     */
    //@Excel(name = "地址库编码")
    private String addressCode;

    /**
     * 收件人姓名
     */
    @Excel(name = "收件人姓名")
    private String consigneeName;

    /**
     * 收件人公司
     */
    @Excel(name = "收件人公司名称")
    private String consigneeCompany;

    /**
     * 收件人地址
     */
    @Excel(name = "收件人地址")
    private String consigneeStreet;

    /**
     * 收件人城市
     */
    @Excel(name = "收件人城市")
    private String consigneeCity;

    /**
     * 收件人省份/州
     */
    @Excel(name = "收件人省份/州")
    private String consigneeProvince;

    /**
     * 收件人门牌号
     */
    @Excel(name = "收件人门牌号")
    private String consigneeHousenum;

    /**
     * 收件人邮编
     */
    @Excel(name = "收件人邮编")
    private String consigneeMail;

    /**
     * 收货人国家编码
     */
    @Excel(name = "收件人国家编码")
    private String consigneeCountryCode;

    /**
     * 收件人电话
     */
    @Excel(name = "收件人联系方式")
    private String consigneeTelephone;

    /**
     * 报关中文名
     */
    @Excel(name = "报关中文名")
    private String invoiceCnName;

    /**
     * 报关英文名
     */
    @Excel(name = "报关英文名")
    private String invoiceEnName;

    /**
     * 运输方式代码
     */
    //@Excel(name = "运输方式代码")
    private String shippingMethod;

    /**
     * 单位code
     */
    @Excel(name = "输入：米，件，套)，三选一", dictType = "unit", width = 20)
    private String unitCode;

    /**
     * 单价(单个商品价格)
     */
    @Excel(name = "单价(单个商品价格)")
    private String invoiceUnitCharge;

    /**
     * 数量(箱数)
     */
    @Excel(name = "报关产品数量")
    private String invoiceQuantity;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String invoiceNote;

    /**
     * 带电(N-否 Y-是)
     */
    //@Excel(name = "带电(N-否 Y-是)")
    private String charge;

    /**
     * 带磁(N-否 Y-是)
     */
    //@Excel(name = "带磁(N-否 Y-是)")
    private String magnetism;

    /**
     * 液体(N-否 Y-是)
     */
    //@Excel(name = "液体(N-否 Y-是)")
    private String liquid;

    /**
     * 粉末(N-否 Y-是)
     */
    //@Excel(name = "粉末(N-否 Y-是)")
    private String powder;

    /**
     * 危险品(N-否 Y-是)
     */
    //@Excel(name = "危险品(N-否 Y-是)")
    private String danger;

    /**
     * 报关方式
     */
    //@Excel(name = "报关方式")
    private String invoiceType;

    /**
     * 申报总价
     */
    @Excel(name = "申报总价")
    private String evaluate;

    /**
     * 欧盟税改IOSS号
     */
    @Excel(name = "欧盟税改IOSS号")
    private String iossNo;

    /**
     * 交税方式
     */
    //@Excel(name = "交税方式")
    private String taxesType;

    /**
     * 交货条款
     */
    //@Excel(name = "交货条款")
    private String deliveryTerms;

    /**
     * VAT税号
     */
    //@Excel(name = "VAT税号")
    private String vatNo;

    /**
     * 申报币种
     */
    //@Excel(name = "申报币种")
    private String invoiceCurrencycode;

    /**
     * ponumber
     */
    //@Excel(name = "ponumber")
    private String poNumber;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdDate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedDate;

    /**
     * 创建人
     */
    //@Excel(name = "创建人")
    private String createdBy;

    /**
     * 修改人
     */
    //@Excel(name = "修改人")
    private String updatedBy;

    /**
     * 部门ID
     */
    //@Excel(name = "部门ID")
    private Long deptId;


}
