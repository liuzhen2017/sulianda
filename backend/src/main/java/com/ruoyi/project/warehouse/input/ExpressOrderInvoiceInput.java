package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 海关申报信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressOrderInvoiceInput {


    /**
     * 商品英文品名
     */
    @JSONField(name = "invoice_enname")
    private String invoiceEnName;
    /**
     * 是否带电 Y N
     */
    private String battery="N";

    /**
     * 数量
     */
    @JSONField(name = "invoice_quantity")
    private String invoiceQuantity;

    /**
     * 单价(单个商品价格)
     */
    @JSONField(name = "invoice_unitcharge")
    private String invoiceUnitCharge;

    /**
     * sku
     */
    @JSONField(name = "sku")
    private String sku;

    /**
     * 中文品名
     */
    @JSONField(name = "invoice_cnname")
    private String invoiceCnName;

    /**
     * 单位
     * MTR：米
     * PCE：件
     * SET：套
     * 默认PCE
     */
    @JSONField(name = "unit_code")
    private String unitCode="PCE";

    /**
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
    @JSONField(name = "invoice_currencycode")
    private String invoiceCurrencyCode="USD";

    /**
     * hs_code
     * 海关协制编号
     */
    @JSONField(name = "hs_code")
    private String hsCode;

    /**
     * hs_code
     * 配货信息
     */
    @JSONField(name = "invoice_note")
    private String invoiceNote;

    /**
     * hs_code
     * 规格
     */
    @JSONField(name = "invoice_spec")
    private String invoiceSpec;
    /**
     * 材质
     */
    @JSONField(name = "invoice_material")
    private String invoiceMaterial;
    /**
     * 商品图片地址
     */
    @JSONField(name = "invoice_info")
    private String invoiceInfo;
    /**
     * 销售地址
     */
    @JSONField(name = "invoice_url")
    private String invoiceUrl;

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getInvoiceEnName() {
        return invoiceEnName;
    }

    public void setInvoiceEnName(String invoiceEnName) {
        this.invoiceEnName = invoiceEnName;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public String getInvoiceUnitCharge() {
        return invoiceUnitCharge;
    }

    public void setInvoiceUnitCharge(String invoiceUnitCharge) {
        this.invoiceUnitCharge = invoiceUnitCharge;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getInvoiceCnName() {
        return invoiceCnName;
    }

    public void setInvoiceCnName(String invoiceCnName) {
        this.invoiceCnName = invoiceCnName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getInvoiceCurrencyCode() {
        return invoiceCurrencyCode;
    }

    public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
        this.invoiceCurrencyCode = invoiceCurrencyCode;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public String getInvoiceSpec() {
        return invoiceSpec;
    }

    public void setInvoiceSpec(String invoiceSpec) {
        this.invoiceSpec = invoiceSpec;
    }

    public String getInvoiceMaterial() {
        return invoiceMaterial;
    }

    public void setInvoiceMaterial(String invoiceMaterial) {
        this.invoiceMaterial = invoiceMaterial;
    }

    public String getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }
}
