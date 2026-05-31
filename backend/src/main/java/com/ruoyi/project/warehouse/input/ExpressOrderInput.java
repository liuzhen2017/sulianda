package com.ruoyi.project.warehouse.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressOrderInput extends BaseDto {

    /**
     * 下单 订单ID--下订单完后，会返回该ID
     */
    @JSONField(name = "reference_no")
    private String referenceNo;

    /**
     * 运输方式代码
     */
    @JSONField(name = "shipping_method")
    private String shippingMethod;


    /**
     * 服务商单号
     */
    @JSONField(name = "shipping_method_no")
    private String shippingMethodNo;

    /**
     * 订单重量，单位KG，默认为0.2
     */
    @JSONField(name = "order_weight")
    private String orderWeight="0.2";
    /**
     * 订单长
     */
    @JSONField(name = "order_weight")
    private String orderLength="1";
    /**
     * 订单长
     */
    @JSONField(name = "order_height")
    private String orderHeight="1";

    /**
     * 外包装件数,默认1
     */
    @JSONField(name = "order_pieces")
    private String orderPieces="1";

    /**
     * 货物类型
     * W：包裹
     * D：文件
     * B：袋子
     */
    @JSONField(name = "cargotype")
    private String cargoType="W";

    /**
     * 包裹申报种类
     * 1：Gif礼品
     * 2：CommercialSample 商品货样
     * 3：Document 文件
     * 4：Other 其他
     * 默认4
     */
    @JSONField(name = "mail_cargo_type")
    private String mailCargoType="4";
    /**
     * 订单备注
     */
    @JSONField(name = "order_info")
    private String order_info="4";

    private String iossNo;//IM0123456789",//欧盟税改 IOSS号
    /**
     * 发件人信息
     */
    private ExpressOrderShipperInput shipper;
    /**
     * 收件人信息
     */
    private ExpressCreateConsigneeOrder consignee;

    /**
     * 海关申报信息
     */
    private ExpressOrderInvoiceInput invoice[];
    /**
     * 申报总价
     */
    private String evaluate;
    /**
     * 额外服务信息
     */
    @JSONField(name = "extra_service")
    private ExpressOrderExtraServiceInput extraService;
    /**
     * 保险机制
     */
    @JSONField(name = "insurance_value")
    private Integer insuranceValue =0;

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getOrderLength() {
        return orderLength;
    }

    public void setOrderLength(String orderLength) {
        this.orderLength = orderLength;
    }

    public String getOrderHeight() {
        return orderHeight;
    }

    public void setOrderHeight(String orderHeight) {
        this.orderHeight = orderHeight;
    }

    public String getIossNo() {
        return iossNo;
    }

    public void setIossNo(String iossNo) {
        this.iossNo = iossNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getShippingMethodNo() {
        return shippingMethodNo;
    }

    public void setShippingMethodNo(String shippingMethodNo) {
        this.shippingMethodNo = shippingMethodNo;
    }

    public String getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(String orderWeight) {
        this.orderWeight = orderWeight;
    }

    public String getOrderPieces() {
        return orderPieces;
    }

    public void setOrderPieces(String orderPieces) {
        this.orderPieces = orderPieces;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getMailCargoType() {
        return mailCargoType;
    }

    public void setMailCargoType(String mailCargoType) {
        this.mailCargoType = mailCargoType;
    }

    public String getOrder_info() {
        return order_info;
    }

    public void setOrder_info(String order_info) {
        this.order_info = order_info;
    }

    public ExpressOrderShipperInput getShipper() {
        return shipper;
    }

    public void setShipper(ExpressOrderShipperInput shipper) {
        this.shipper = shipper;
    }

    public ExpressCreateConsigneeOrder getConsignee() {
        return consignee;
    }

    public void setConsignee(ExpressCreateConsigneeOrder consignee) {
        this.consignee = consignee;
    }

    public ExpressOrderInvoiceInput[] getInvoice() {
        return invoice;
    }

    public void setInvoice(ExpressOrderInvoiceInput[] invoice) {
        this.invoice = invoice;
    }

    public ExpressOrderExtraServiceInput getExtraService() {
        return extraService;
    }

    public void setExtraService(ExpressOrderExtraServiceInput extraService) {
        this.extraService = extraService;
    }

    public Integer getInsuranceValue() {
        return insuranceValue;
    }

    public void setInsuranceValue(Integer insuranceValue) {
        this.insuranceValue = insuranceValue;
    }

    public OrderInput03 toTranOrder03(){
        GoodsDetails03[] goodsDetails03s =new GoodsDetails03[1];
        goodsDetails03s[0]=GoodsDetails03.builder()
                .detailDescription(this.invoice[0].getInvoiceCnName())
                .detailDescriptionCN(this.invoice[0].getInvoiceCnName())
                .detailQuantity(this.invoice[0].getInvoiceQuantity())
                .detailWorth(this.invoice[0].getInvoiceUnitCharge())
                .hsCode(this.invoice[0].getHsCode())
                .build();
        return OrderInput03.builder()
                .customerOrderNo(this.referenceNo)
                .opDivision("1")
                .recipientName(this.consignee.getConsigneeName())
                .recipientCountry(this.consignee.getConsigneeCountryCode())
                .shippingMethod(this.shippingMethod)
                .recipientState(this.consignee.getConsigneeProvince())
                .recipientCity(this.consignee.getConsigneeCity())
                .recipientAddress(this.consignee.getConsigneeStreet())
                .doorplate(this.consignee.getConsigneeDoorplate())
                .recipientZipCode(this.consignee.getConsigneePostCode())
                .recipientPhone(this.consignee.getConsigneeTelephone())
                .recipientEmail(this.consignee.getConsigneeEmail())
                .recipientOrganization(this.consignee.getConsigneeCompany())

                .goodsDeclareWorth(this.invoice[0].getInvoiceUnitCharge())
                //出口报关
                .exportDeclaration("")
                .orderStatus("confirmed")
                .evaluate("0")
                .goodsDeclareWorth(this.evaluate)
                .pieceNumber(this.orderPieces)
                .goodsQuantity(this.orderPieces)
                .goodsLength(this.getOrderWeight())
                .goodsDetails(goodsDetails03s)

                .shipperState(this.shipper.getShipperProvince())
                .shipperCity(this.shipper.getShipperCity())
                .shipperCompanyName(this.shipper.getShipperCompany())
                .shipperName(this.shipper.getShipperName())
                .shipperEmail(this.shipper.getShipperEmail())
                .shipperAddress(this.shipper.getShipperStreet())
                .shipperPhone(this.shipper.getShipperTelephone())
                .shipperZipCode(this.shipper.getShipperPostcode())



                .build();
    }
    public OrderInput02 toTranOrder02(){
        //String 3 N 包裹类型 (WPX:包裹,DOC:文件,PAK:PAK 袋)
        String goodsType ="WPX";
        if(this.cargoType.equals("D")){
            goodsType= "DOC";
        }else if(this.cargoType.equals("P")){
            goodsType= "PAK";
        }

//
//        /**
//         材积明细 (OrderType 为快递制单必 传)
//         */
//        @JSONField(name = "Volumes")
//        private List<GoodsDetails02> Volumes;

        List<GoodsDetails02> list =new ArrayList<>();
        list.add(GoodsDetails02.builder()
//                        .application(this.invoice[0].getInvoiceCnName())
                        .cnName(this.invoice[0].getInvoiceCnName())
                        .enname(this.invoice[0].getInvoiceEnName())
                        .sku(this.invoice[0].getSku())
                        .price(this.invoice[0].getInvoiceUnitCharge())
                        .singleWeight("1")
                        .num("1")
                        .texture(this.invoice[0].getInvoiceMaterial())
                        .TransactionUrl(this.invoice[0].getInvoiceUrl())
                        .Origin("CN")



                .build());


        return OrderInput02.builder()
                .customerNumber(this.referenceNo)
                //是否带电
                .battery(this.invoice[0].getBattery())
                //String 10 Y 渠道代码 可调用[searchStartChannel]方法获 取
                .channelCode(this.shippingMethod)
                //代收货款
                .collamt("0")
                .packageType("0")
                .countryCode(this.consignee.getConsigneeCountryCode())
                //仓库代码 (OrderType 为仓储订单必 传) 可调用[searchStartHouse]方法获取
                .houseCode(this.consignee.getConsigneeCountryCode())

                //体积
                //总长
                .totalWeight(this.orderWeight)
                //总价值
//                .totalValue(this.or)
                .number("1")
                //String 3 N 包裹类型 (WPX:包裹,DOC:文件,PAK:PAK 袋)
                .goodsType(goodsType)
                .note(this.order_info)

                //收件人信息
                .recipient02(Recipient02.builder()
                        .name(this.consignee.getConsigneeName())
                        .company(this.consignee.getConsigneeCompany())
                        .addres1(this.consignee.getConsigneeStreet())
                        .tel(this.consignee.getConsigneeTelephone())
                        .province(this.consignee.getConsigneeProvince())
                        .city(this.consignee.getConsigneeCity())
                        .post(this.consignee.getConsigneePostCode())
                        .houseNum(this.consignee.getHouseNum())
                        .build())
                //寄件人信息
                .sender02(Sender02.builder()
                        .name(this.shipper.getShipperName())
                        .company(this.shipper.getShipperCompany())
                        .addres(this.shipper.getShipperStreet())
                        .tel(this.shipper.getShipperTelephone())
                        .province(this.shipper.getShipperProvince())
                        .ctity(this.shipper.getShipperCity())
                        .post(this.shipper.getShipperPostcode())
                        .build())

                //订单信息
                .orderItems(list)
                .build();

    }
    public OrderInput04 toTranOrder04(){
        String normalizedWeight = normalizeDecimal(this.orderWeight);
        String normalizedLength = normalizeDecimal(this.orderLength);
        String normalizedHeight = normalizeDecimal(this.orderHeight);
        String normalizedWidth = normalizeDecimal(this.orderHeight);
        String normalizedPrice = normalizeDecimal(this.invoice[0].getInvoiceUnitCharge());
        String normalizedQuantity = normalizeInteger(this.invoice[0].getInvoiceQuantity());
        String normalizedWeightInGram = normalizeInteger(toGram(this.orderWeight));
        return OrderInput04.builder()
                //产品ID
                .channelId(this.shippingMethod)
                //来源
//                .orderSource()
                .orderNumber(this.referenceNo)
                .remark(this.order_info)


                //收件人信息
                .receiverInfo(Recipient04.builder()
                        .name(this.consignee.getConsigneeName())
                        .country(this.consignee.getConsigneeCountryCode())
                        .company(this.consignee.getConsigneeCompany())
                        .state(this.consignee.getConsigneeProvince())
                        .city(this.consignee.getConsigneeCity())
                        .address(this.consignee.getConsigneeStreet())
                        .email(this.consignee.getConsigneeEmail())
                        .phone(this.consignee.getConsigneeTelephone())
                        .zipCode(this.consignee.getConsigneePostCode())
                        .houseNumber(this.consignee.getHouseNum())
                        .build())

                //发件人信息
                .senderInfo(Sender04.builder()
                        .name(this.shipper.getShipperName())
                        .country(this.shipper.getShipperCountryCode())
                        .company(this.shipper.getShipperCompany())
                        .state(this.shipper.getShipperProvince())
                        .city(this.shipper.getShipperCity())
                        .address(this.shipper.getShipperStreet())
                        .email(this.shipper.getShipperEmail())
                        .phone(this.shipper.getShipperTelephone())
                        .zipCode(this.shipper.getShipperPostcode())
                        .houseNumber(this.shipper.getShipperAreaCode())
                        .build())
                //包裹状态
                .parcelInfo(PackageInfo04.builder()
                        .currency(this.invoice[0].getInvoiceCurrencyCode())
                        .height(normalizedHeight)
                        .width(normalizedWidth)
                        .length(normalizedLength)
                        .totalPrice(normalizedPrice)
                        .totalQuantity(normalizedQuantity)
                        .totalWeight(normalizedWeightInGram)
                        .hasBattery("Y".equals(this.invoice[0].getBattery())? "1":"0")
                        .productList(Arrays.asList(GoodsDetails04.builder()
                                        .goodsNameCh(this.invoice[0].getInvoiceCnName())
                                        .goodsNameEn(this.invoice[0].getInvoiceEnName())
                                        .quantity(normalizedQuantity)
                                        .price(normalizedPrice)
                                        .url(this.invoice[0].getInvoiceUrl())
                                        .weight(normalizedWeightInGram)
                                        .material(this.invoice[0].getInvoiceMaterial())
                                        .hscode(this.invoice[0].getHsCode())

                                .build()))
                        .build())
                .build();
    }

    private String normalizeDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "0";
        }
        return new BigDecimal(value.trim()).stripTrailingZeros().toPlainString();
    }

    private String normalizeInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "0";
        }
        return new BigDecimal(value.trim()).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private String toGram(String weightInKg) {
        if (weightInKg == null || weightInKg.trim().isEmpty()) {
            return "0";
        }
        return new BigDecimal(weightInKg.trim()).multiply(new BigDecimal("1000")).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    public OrderInput01 toTranOrder01() {
        return OrderInput01.builder()
                .ref_no(this.referenceNo)
                .business_type("BDS")
                .duty_type("U")
                .ioss_no(this.iossNo)
                .parcel_qty("1")
                .logistics_service_info(ProductChannel01.builder()
                        .logistics_product_code(this.shippingMethod)
                        .build())
                .isInsure("N")

                .deliverTypeInfo01(DeliverTypeInfo01.builder()
                        .deliver_type("2")
                        .build())
                //发件人信息
                .sender(Sender01.builder()
                        .first_name(this.shipper.getShipperName())
                        .company(this.shipper.getShipperCompany())
                        .phone(this.shipper.getShipperTelephone())
                        .country(this.shipper.getShipperCountryCode())
                        .city(this.shipper.getShipperCity())
                        .street(this.shipper.getShipperStreet())
                        .post_code(this.shipper.getShipperPostcode())
                        .state(this.shipper.getShipperProvince())
                        .build())
                //收件人信息
                .recipient_info(Recipient01.builder()
                        .first_name(this.consignee.getConsigneeName())
//                        .company(this.shipper.getShipperCompany())
                        .phone(this.consignee.getConsigneeTelephone())
                        .country(this.consignee.getConsigneeCountryCode())
                        .city(this.consignee.getConsigneeCity())
                        .street(this.consignee.getConsigneeStreet())
                        .post_code(this.consignee.getConsigneePostCode())
                        .state(this.consignee.getConsigneeProvince())
                        .build())
                //退件信息
                .return_info(ReturnInfo01.builder().build())
                //包裹信息
                .parcelInfo(Arrays.asList(PackageInfo01.builder()
                                .currency(this.invoice[0].getInvoiceCurrencyCode())
                                .weight(this.orderWeight)
                                .parcel_value(this.invoice[0].getInvoiceUnitCharge())
                                .include_battery(this.invoice[0].getBattery())
                                .declare_product_info(Arrays.asList(GoodsDetails01.builder()
                                        .declare_product_name_cn(this.invoice[0].getInvoiceCnName())
                                        .declare_product_name_en(this.invoice[0].getInvoiceEnName())
                                        .declare_product_code_qty(this.invoice[0].getInvoiceQuantity())
                                                .currency_export(this.invoice[0].getInvoiceCurrencyCode())
                                                .currency_import(this.invoice[0].getInvoiceCurrencyCode())
                                        .declare_unit_price_export(this.invoice[0].getInvoiceUnitCharge())
                                        .declare_unit_price_import(this.invoice[0].getInvoiceUnitCharge())
                                                .material(this.invoice[0].getInvoiceMaterial())
                                        .build()))
                        .build()))
                .build();
    }
    public OrderInput06 toTranOrder06(){
        String cargoType ="";
        switch (this.cargoType){
            case "W": cargoType="P";break;
            case "B": cargoType="B";break;
            case "D": cargoType="D";break;

        }

        return OrderInput06.builder()
                .cargo_type(cargoType)
                .order_customerinvoicecode(this.referenceNo)
                .product_id(this.shippingMethod)
                .weight(this.orderWeight)
                .order_piece(this.orderPieces)
                .order_returnsign("N")
                //可选值
                .trade_type("")
                .duty_type("DDU")
                //电池类型
                .battery_type("")
                //收件人
                .consignee_mobile(this.consignee.getConsigneeMobile())
                .consignee_name(this.consignee.getConsigneeName())
                .consignee_companyname(this.consignee.getConsigneeCompany())
                .consignee_address(this.consignee.getConsigneeStreet())
                .consignee_telephone(this.consignee.getConsigneeTelephone())
                .country(this.consignee.getConsigneeCountryCode())
                .consignee_state(this.consignee.getConsigneeProvince())
                .consignee_city(this.consignee.getConsigneeCity())
                .consignee_postcode(this.consignee.getConsigneePostCode())
                .consignee_email(this.consignee.getConsigneeEmail())
//                .consignee_taxno(this.consignee.getConsigneeAreaCode())
                .consignee_doorno(this.consignee.getHouseNum())
                .consignee_streetno(this.consignee.getHouseNum())

                //保险信息
                //保险金额
//                .order_insurance()
                //报关信息
                .packageInfo06(Arrays.asList(PackageInfo06.builder()
                        .invoice_amount(this.invoice[0].getInvoiceUnitCharge())
                        .invoice_pcs(this.invoice[0].getInvoiceQuantity())
                        .invoice_title(this.invoice[0].getInvoiceEnName())
                        .invoice_weight(this.orderWeight)
                        .sku(this.invoice[0].getInvoiceCnName())
//                        .sku_code()
                        .hs_code(this.invoice[0].getHsCode())
                        .invoice_rule(this.invoice[0].getInvoiceSpec())
                        .invoice_currency(this.invoice[0].getInvoiceCurrencyCode())
//                        .invoice_taxno(this.invoice[0].gett)
                        .invoice_material(this.invoice[0].getInvoiceMaterial())
                        .build()))
                //总体积
                .goodsDetails06(Arrays.asList(GoodsDetails06.builder()
                        .volume_height(this.getOrderHeight())
                        .volume_length(this.getOrderLength())
                        .volume_weight(this.getOrderWeight())
                        .volume_width(this.orderWeight)
                        .build()) )
                .build();
    }
}
