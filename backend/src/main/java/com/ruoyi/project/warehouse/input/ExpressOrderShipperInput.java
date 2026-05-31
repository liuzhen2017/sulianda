package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 发件人信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressOrderShipperInput {

    /**
     * 发件人信息
     */
    @JSONField(name = "shipper_name")
    private String shipperName="邹**";

    /**
     * 发件人国家二字代码
     */
    @JSONField(name = "shipper_countrycode")
    private String shipperCountryCode="CN";
    /**
     * 发件人地址
     */
    @JSONField(name = "shipper_street")
    private String shipperStreet="深圳市龙华区龙华街道和联社区山咀头二区二巷2号2-1-1";
    /**
     * 发件人电话
     */
    @JSONField(name = "shipper_telephone")
    private String shipperTelephone="17727974034";

    /**
     * 发件人公司
     */
    @JSONField(name = "shipper_company")
    private String shipperCompany="深圳市速联达国际物流有限公司";
    /**
     * 发件人省
     */
    @JSONField(name = "shipper_province")
    private String shipperProvince="广东省";
    /**
     * 发件人市
     */
    @JSONField(name = "shipper_city")
    private String shipperCity="深圳";

    /**
     * 发件人邮编
     */
    @JSONField(name = "shipper_postcode")
    private String shipperPostcode="518000";

    /**
     * 发件人区域代码
     */
    @JSONField(name = "shipper_areacode")
    private String shipperAreaCode;

    /**
     * 发件人手机
     */
    @JSONField(name = "shipper_mobile")
    private String shipperMobile;

    /**
     * 发件人邮箱
     */
    @JSONField(name = "shipper_email")
    private String shipperEmail;

    /**
     * 发件人邮箱
     */
    @JSONField(name = "shipper_fax")
    private String shipperFax;


    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperCountryCode() {
        return shipperCountryCode;
    }

    public void setShipperCountryCode(String shipperCountryCode) {
        this.shipperCountryCode = shipperCountryCode;
    }

    public String getShipperStreet() {
        return shipperStreet;
    }

    public void setShipperStreet(String shipperStreet) {
        this.shipperStreet = shipperStreet;
    }

    public String getShipperTelephone() {
        return shipperTelephone;
    }

    public void setShipperTelephone(String shipperTelephone) {
        this.shipperTelephone = shipperTelephone;
    }

    public String getShipperCompany() {
        return shipperCompany;
    }

    public void setShipperCompany(String shipperCompany) {
        this.shipperCompany = shipperCompany;
    }

    public String getShipperProvince() {
        return shipperProvince;
    }

    public void setShipperProvince(String shipperProvince) {
        this.shipperProvince = shipperProvince;
    }

    public String getShipperCity() {
        return shipperCity;
    }

    public void setShipperCity(String shipperCity) {
        this.shipperCity = shipperCity;
    }

    public String getShipperPostcode() {
        return shipperPostcode;
    }

    public void setShipperPostcode(String shipperPostcode) {
        this.shipperPostcode = shipperPostcode;
    }

    public String getShipperAreaCode() {
        return shipperAreaCode;
    }

    public void setShipperAreaCode(String shipperAreaCode) {
        this.shipperAreaCode = shipperAreaCode;
    }

    public String getShipperMobile() {
        return shipperMobile;
    }

    public void setShipperMobile(String shipperMobile) {
        this.shipperMobile = shipperMobile;
    }

    public String getShipperEmail() {
        return shipperEmail;
    }

    public void setShipperEmail(String shipperEmail) {
        this.shipperEmail = shipperEmail;
    }

    public String getShipperFax() {
        return shipperFax;
    }

    public void setShipperFax(String shipperFax) {
        this.shipperFax = shipperFax;
    }
}
