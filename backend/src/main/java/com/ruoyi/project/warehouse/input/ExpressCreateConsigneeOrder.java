package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 收件人信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressCreateConsigneeOrder {


    /**
     * 收件人姓名
     */
    @JSONField(name = "consignee_name")
    private String consigneeName ="邹总";
    /**
     * 收件人国家二字代码
     */
    @JSONField(name = "consignee_countrycode")
    private String consigneeCountryCode;
    /**
     * 收件人地址
     */
    @JSONField(name = "consignee_street")
    private String consigneeStreet;
    /**
     * 收件人电话
     */
    @JSONField(name = "consignee_telephone")
    private String consigneeTelephone="";

    /**
     * 收件人公司
     */
    @JSONField(name = "consignee_company")
    private String consigneeCompany;
//    /**
//     * 收件人国家
//     */
//    @JSONField(name = "consignee_countrycode")
//    private String consigneeCuntryCode;
    /**
     * 收件人省
     */
    @JSONField(name = "consignee_province")
    private String consigneeProvince; /**
     * 收件人城市
     */
    @JSONField(name = "consignee_city")
    private String consigneeCity;
    /**
     * 收件人邮编
     */
    @JSONField(name = "consignee_postcode")
    private String consigneePostCode="1195547492@qq.com";

    /**
     * 收件人门牌号
     */
    @JSONField(name = "consignee_doorplate")
    private String consigneeDoorplate;
    /**
     * 收件人区域代码
     */
    @JSONField(name = "consignee_areacode")
    private String consigneeAreaCode;

    /**
     * 收件人区域代码
     */
    @JSONField(name = "consignee_mobile")
    private String consigneeMobile;

    /**
     * 收件人邮箱
     */
    @JSONField(name = "consignee_email")
    private String consigneeEmail;
    /**
     * 收件人传真
     */
    @JSONField(name = "consignee_fax")
    private String consigneeFax;

    /**
     * 证件类型代码
     * ID：身份证
     * PP：护照
     */
    @JSONField(name = "consignee_certificatetype")
    private String consigneeCertificateType;

    /**
     * 证件号码
     */
    @JSONField(name = "consignee_certificatecode")
    private String consigneeCertificateCode;

    /**
     * 证件有效期
     */
    @JSONField(name = "consignee_credentials_period")
    private String consigneeCredentialsPeriod;

    /**
     * 证件有效期
     */
    @JSONField(name = "consignee_tariff")
    private String consigneeTariff;
    private String houseNum;//String 10 N 门牌号

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeCountryCode() {
        return consigneeCountryCode;
    }

    public void setConsigneeCountryCode(String consigneeCountryCode) {
        this.consigneeCountryCode = consigneeCountryCode;
    }

    public String getConsigneeStreet() {
        return consigneeStreet;
    }

    public void setConsigneeStreet(String consigneeStreet) {
        this.consigneeStreet = consigneeStreet;
    }

    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    public void setConsigneeTelephone(String consigneeTelephone) {
        this.consigneeTelephone = consigneeTelephone;
    }

    public String getConsigneeCompany() {
        return consigneeCompany;
    }

    public void setConsigneeCompany(String consigneeCompany) {
        this.consigneeCompany = consigneeCompany;
    }

//    public String getConsigneeCuntryCode() {
//        return consigneeCuntryCode;
//    }
//
//    public void setConsigneeCuntryCode(String consigneeCuntryCode) {
//        this.consigneeCuntryCode = consigneeCuntryCode;
//    }

    public String getConsigneeProvince() {
        return consigneeProvince;
    }

    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsigneePostCode() {
        return consigneePostCode;
    }

    public void setConsigneePostCode(String consigneePostCode) {
        this.consigneePostCode = consigneePostCode;
    }

    public String getConsigneeDoorplate() {
        return consigneeDoorplate;
    }

    public void setConsigneeDoorplate(String consigneeDoorplate) {
        this.consigneeDoorplate = consigneeDoorplate;
    }

    public String getConsigneeAreaCode() {
        return consigneeAreaCode;
    }

    public void setConsigneeAreaCode(String consigneeAreaCode) {
        this.consigneeAreaCode = consigneeAreaCode;
    }

    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }

    public String getConsigneeEmail() {
        return consigneeEmail;
    }

    public void setConsigneeEmail(String consigneeEmail) {
        this.consigneeEmail = consigneeEmail;
    }

    public String getConsigneeFax() {
        return consigneeFax;
    }

    public void setConsigneeFax(String consigneeFax) {
        this.consigneeFax = consigneeFax;
    }

    public String getConsigneeCertificateType() {
        return consigneeCertificateType;
    }

    public void setConsigneeCertificateType(String consigneeCertificateType) {
        this.consigneeCertificateType = consigneeCertificateType;
    }

    public String getConsigneeCertificateCode() {
        return consigneeCertificateCode;
    }

    public void setConsigneeCertificateCode(String consigneeCertificateCode) {
        this.consigneeCertificateCode = consigneeCertificateCode;
    }

    public String getConsigneeCredentialsPeriod() {
        return consigneeCredentialsPeriod;
    }

    public void setConsigneeCredentialsPeriod(String consigneeCredentialsPeriod) {
        this.consigneeCredentialsPeriod = consigneeCredentialsPeriod;
    }

    public String getConsigneeTariff() {
        return consigneeTariff;
    }

    public void setConsigneeTariff(String consigneeTariff) {
        this.consigneeTariff = consigneeTariff;
    }
}
