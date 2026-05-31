package com.ruoyi.project.warehouse.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuzhen
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsDetails03 {
    private String detailDescription;
    //  	string 	required 	物品描述/英文描述
    private String detailDescriptionCN;
    //  	string 	option 	中文描述
    private String detailQuantity;
    //  	string 	required 	数量 int
    private String detailWorth;
    //  	string 	required 	单个物品申报价
    private String detailCustomLabel;
    //  	string 	option 	物品参考号/SKU
    private String hsCode;
    //  	string 	option 	海关编码
    private String detailWeight;
    //  	string 	option 	重量 KG
    private String origin;
    //  	string 	option 	原产地,默认‘CN’
    private String boxId ;
    // 	int 	option 	包裹号 int
    private String disposition;
    //  	string 	option 	销售类型
    private String enMaterial;
    //  	string 	option 	英文物品材质
    private String cnMaterial ;
    // 	string 	option 	中文物品材质
    private String purpose;
    //  	string 	option 	用途
    private String detailEbayLineId;
    //  	string 	option 	eBay交易行ID
    private String detailEbayTxnId;
    //  	string 	option 	ebay交易号
    private String detailEbayItemId;
    //  	string 	option 	ebay物品号
    private String detailEbayUserId;
    //  	string 	option 	ebay用户buyerID
    private String detailEbayPayDate;
    //  	string 	option 	Ebay付款时间
    private String detailEbaySoldDate;
    //  	string 	option 	Ebay卖出日期
    private String ecommerce_id;
    // 	string 	option 	Amazon产品代号
    private String priceTag;
    // 	string 	option 	吊牌价/MRP价值
    private String shelfLife;
    //  	string 	option 	保质期
}
