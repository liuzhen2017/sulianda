package com.ruoyi.project.warehouse.service.impl;

import cn.hutool.json.JSONUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.warehouse.domain.BusiExpressCustomer;
import com.ruoyi.project.warehouse.domain.BusiOrder;
import com.ruoyi.project.warehouse.domain.BusiOrderEvent;
import com.ruoyi.project.warehouse.domain.BusiPayHistory;
import com.ruoyi.project.warehouse.domain.BusiPayHistoryIndex;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.input.ExpressCreateConsigneeOrder;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.ExpressOrderInvoiceInput;
import com.ruoyi.project.warehouse.input.ExpressOrderShipperInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
import com.ruoyi.project.warehouse.input.QueryChartInput;
import com.ruoyi.project.warehouse.mapper.BusiExpressCustomerMapper;
import com.ruoyi.project.warehouse.mapper.BusiOrderEventMapper;
import com.ruoyi.project.warehouse.mapper.BusiOrderMapper;
import com.ruoyi.project.warehouse.mapper.BusiPayHistoryIndexMapper;
import com.ruoyi.project.warehouse.mapper.BusiPayHistoryMapper;
import com.ruoyi.project.warehouse.mapper.BusiProductMapper;
import com.ruoyi.project.warehouse.output.AdminReportOutput;
import com.ruoyi.project.warehouse.output.CountryListVo;
import com.ruoyi.project.warehouse.output.OrderResultOutput;
import com.ruoyi.project.warehouse.output.PrintExpressOutput;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import com.ruoyi.project.warehouse.service.IBusiOrderService;
import com.ruoyi.project.warehouse.utils.SnowflakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

@Service
@Slf4j
public class BusiOrderServiceImpl implements IBusiOrderService {
    private static final String STATUS_DRAFT = "0";
    private static final String STATUS_PENDING_WAREHOUSE = "1";
    private static final String STATUS_WAREHOUSED = "2";
    private static final String STATUS_SHIPPED = "3";
    private static final String STATUS_REJECTED = "4";
    private static final String STATUS_CHARGED = "5";

    private static final String CHECK_PENDING = "check_pending";
    private static final String CHECK_PASSED = "check_passed";
    private static final String CHECK_FAILED = "check_failed";

    private static final String ISSUE_PENDING = "issue_pending";
    private static final String ISSUE_PROCESSING = "issue_processing";
    private static final String ISSUE_RESOLVED = "issue_resolved";

    private static final String RELEASE_PENDING = "release_pending";
    private static final String RELEASE_APPROVED = "release_approved";

    private static final String NEED_REPLACE_YES = "Y";
    private static final String NEED_REPLACE_NO = "N";
    private static final String NEED_INTERNAL_YES = "Y";
    private static final String NEED_INTERNAL_NO = "N";
    private static final String NEED_DEDUCT_YES = "Y";
    private static final String NEED_DEDUCT_NO = "N";

    @Autowired
    private BusiOrderMapper busiOrderMapper;

    @Autowired
    private IBusiExpressService busiExpressService;

    @Autowired
    private ISysDictDataService iSysDictDataService;

    @Autowired
    private BusiProductMapper busiProductMapper;

    @Autowired
    private BusiExpressCustomerMapper busiExpressCustomerMapper;

    @Autowired
    private BusiPayHistoryMapper busiPayHistoryMapper;

    @Autowired
    private BusiPayHistoryIndexMapper busiPayHistoryIndexMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private BusiOrderEventMapper busiOrderEventMapper;

    @Override
    public BusiOrder selectBusiOrderById(Long id) {
        return busiOrderMapper.selectBusiOrderById(id);
    }

    @Override
    public List<BusiOrder> selectBusiOrderList(BusiOrder busiOrder) {
        return busiOrderMapper.selectBusiOrderList(busiOrder);
    }

    @Override
    public int insertBusiOrder(BusiOrder busiOrder) {
        if ("1".equals(busiOrder.getType()) || StringUtils.isNotEmpty(busiOrder.getThirdPartyNo())) {
            busiOrder.setOrderNo(StringUtils.defaultIfEmpty(busiOrder.getOrderNo(), String.valueOf(SnowflakeUtils.snowflake())));
        }

        // 数据检查
        boolean dataCheckPassed = performDataCheck(busiOrder);

        if (!dataCheckPassed) {
            // 数据检查失败，设为草稿状态
            busiOrder.setStatus(STATUS_DRAFT);
            busiOrder.setCheckStatus(CHECK_FAILED);
            busiOrder.setCheckSummary("数据检查失败，请检查地址和申报信息");
            log.info("###订单数据检查失败，设为草稿状态: {}", busiOrder.getId());
        }

        fillOrderProductSnapshot(busiOrder);
        fillOrderCustomerSnapshot(busiOrder);
        normalizeOrderDirectFields(busiOrder);
        normalizeWorkflowFields(busiOrder, false);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiOrder.setDeptId(loginUser.getDeptId());
        int rows = busiOrderMapper.insertBusiOrder(busiOrder);
        BusiOrder created = busiOrderMapper.selectBusiOrderById(busiOrder.getId());
        appendOrderEvent(created, "order_created", null, created.getStatus(), "订单已创建", buildOrderDetail(created));
        return rows;
    }

    @Override
    public String importUser(List<BusiOrder> busiOrderList, String operName) {
        if (StringUtils.isNull(busiOrderList) || busiOrderList.size() == 0) {
            throw new ServiceException("导入订单数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BusiOrder busiOrder : busiOrderList) {
            try {
                if (StringUtils.isEmpty(busiOrder.getConsigneeCountryCode()) && StringUtils.isNotEmpty(busiOrder.getCountryName())) {
                    String countryName = busiOrder.getCountryName();
                    List<CountryListVo> countryListVos = busiExpressService.queryCountryList(null);
                    Optional<CountryListVo> first = countryListVos.stream().filter(countryListVo -> countryName.equals(countryListVo.getCnname()) || countryName.equalsIgnoreCase(countryListVo.getEnname()) || countryName.equalsIgnoreCase(countryListVo.getCode())).findFirst();
                    if (first.isPresent()) {
                        busiOrder.setConsigneeCountryCode(first.get().getCode());
                    }
                }

                String productName = busiOrder.getProductName();
                if (StringUtils.isNotEmpty(productName) && busiOrder.getProductId() == null) {
                    List<BusiProduct> busiProductList = busiProductMapper.selectBusiProductList(new BusiProduct(productName));
                    if (!busiProductList.isEmpty()) {
                        busiOrder.setProductId(busiProductList.get(0).getId());
                        if (StringUtils.isEmpty(busiOrder.getPurchaseUrl())) {
                            busiOrder.setPurchaseUrl(busiProductList.get(0).getImgUrl());
                        }
                    }
                }

                SysDictData sysDictData = new SysDictData();
                sysDictData.setDictType("unit");
                sysDictData.setDictLabel(busiOrder.getUnitCode());
                List<SysDictData> sysDiceDataList = iSysDictDataService.selectDictDataList(sysDictData);
                if (!sysDiceDataList.isEmpty()) {
                    busiOrder.setUnitCode(sysDiceDataList.get(0).getDictValue());
                }
                busiOrder.setCustId(getUserId());
                busiOrder.setType("2");
                busiOrder.setCreatedBy(operName);
                this.insertBusiOrder(busiOrder);
                successNum++;
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + busiOrder.getCreatedBy() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateBusiOrder(BusiOrder busiOrder) {
        BusiOrder order = busiOrderMapper.selectBusiOrderById(busiOrder.getId());
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        fillOrderProductSnapshot(busiOrder);
        fillOrderCustomerSnapshot(busiOrder);
        normalizeOrderDirectFields(busiOrder);
        normalizeWorkflowFields(busiOrder, true);

        String beforeStatus = order.getStatus();
        Long custId = order.getCustId();
        Long effectiveExpressId = busiOrder.getExpressId() != null ? busiOrder.getExpressId() : order.getExpressId();
        String effectiveThirdPartyNo = StringUtils.isNotEmpty(busiOrder.getThirdPartyNo()) ? busiOrder.getThirdPartyNo() : order.getThirdPartyNo();
        String effectiveRejectRemark = StringUtils.isNotEmpty(busiOrder.getRejectRemark()) ? busiOrder.getRejectRemark() : order.getRejectRemark();

        if (STATUS_PENDING_WAREHOUSE.equals(busiOrder.getStatus()) && busiOrder.getWarehouseSendNums() != null && busiOrder.getWarehouseSendNums() > 0) {
            busiOrder.setStatus(STATUS_WAREHOUSED);
            if ("2".equals(order.getType())) {
                Long productId = order.getProductId();
                BusiProduct busiProduct = busiProductMapper.selectBusiProductById(productId);
                if (busiProduct != null) {
                    long currentNums = busiProduct.getNums() == null ? 0L : busiProduct.getNums();
                    busiProduct.setNums(currentNums + busiOrder.getWarehouseSendNums());
                    busiProductMapper.updateBusiProduct(busiProduct);
                }
            }
        } else if (STATUS_PENDING_WAREHOUSE.equals(busiOrder.getStatus()) && STATUS_PENDING_WAREHOUSE.equals(order.getStatus())) {
            busiOrder.setStatus(STATUS_PENDING_WAREHOUSE);
        } else if (STATUS_WAREHOUSED.equals(order.getStatus())) {
            // 检查是否欠费
            if (NEED_INTERNAL_YES.equals(busiOrder.getNeedInternalOrder())) {
                // 生成内部制单
                String internalOrderNo = "INT-" + SnowflakeUtils.snowflake();
                busiOrder.setInternalOrderNo(internalOrderNo);
                log.info("###生成内部制单: {}", internalOrderNo);
            }

            SysUser sysUser = sysUserMapper.selectUserById(custId);
            if (sysUser.getBalance().doubleValue() == 0) {
                throw new ServiceException("客户账户余额不足，下单失败！");
            }
            SysUser sysUser = sysUserMapper.selectUserById(custId);
            if (sysUser.getBalance().doubleValue() == 0) {
                throw new ServiceException("客户账户余额不足，下单失败！");
            }

            busiOrder.setStatus(STATUS_SHIPPED);
            if (!"1".equals(order.getType())) {
                if ("修改订单".equals(effectiveRejectRemark)) {
                    log.info("###修改订单直接改库，不用调用第三方接口: {}", System.currentTimeMillis());
                    busiOrder.setUpdatedBy(getUsername());
                    busiOrder.setUpdatedDate(new Date());
                    int rows = busiOrderMapper.updateBusiOrder(busiOrder);
                    BusiOrder updated = busiOrderMapper.selectBusiOrderById(busiOrder.getId());
                    appendOrderEvent(updated, "order_updated", beforeStatus, updated.getStatus(), "订单已修改", buildOrderDetail(updated));
                    return rows;
                }

                if (StringUtils.isEmpty(effectiveThirdPartyNo) && effectiveExpressId != null) {
                    BusiOrder payloadOrder = mergeForThirdParty(order, busiOrder, effectiveExpressId);
                    ExpressOrderInput expressOrderInput = new ExpressOrderInput();
                    expressOrderInput.setOrderHeight(String.valueOf(payloadOrder.getGoodsHeight()));
                    expressOrderInput.setOrderLength(String.valueOf(payloadOrder.getGoodsLength()));
                    expressOrderInput.setOrderWeight(String.valueOf(payloadOrder.getGoodsWeight()));

                    ExpressCreateConsigneeOrder expressCreateConsigneeOrder = new ExpressCreateConsigneeOrder();
                    expressCreateConsigneeOrder.setConsigneeCity(payloadOrder.getConsigneeCity());
                    expressCreateConsigneeOrder.setConsigneeName(payloadOrder.getConsigneeName());
                    expressCreateConsigneeOrder.setConsigneeCountryCode(payloadOrder.getConsigneeCountryCode());
                    expressCreateConsigneeOrder.setConsigneeStreet(payloadOrder.getConsigneeStreet());
                    expressCreateConsigneeOrder.setConsigneeTelephone(payloadOrder.getConsigneeTelephone());
                    expressCreateConsigneeOrder.setConsigneeCompany(payloadOrder.getConsigneeCompany());
                    expressCreateConsigneeOrder.setConsigneePostCode(payloadOrder.getConsigneeMail());
                    expressCreateConsigneeOrder.setConsigneeProvince(payloadOrder.getConsigneeProvince());
                    expressCreateConsigneeOrder.setHouseNum(payloadOrder.getConsigneeHousenum());

                    ExpressOrderInvoiceInput[] expressOrderInvoiceInput = new ExpressOrderInvoiceInput[1];
                    ExpressOrderInvoiceInput invoiceInput = new ExpressOrderInvoiceInput();
                    invoiceInput.setInvoiceEnName(payloadOrder.getInvoiceEnName());
                    invoiceInput.setInvoiceCnName(payloadOrder.getInvoiceCnName());
                    expressOrderInput.setIossNo(payloadOrder.getIossNo());
                    expressOrderInput.setEvaluate(payloadOrder.getEvaluate());
                    invoiceInput.setUnitCode(payloadOrder.getUnitCode());
                    invoiceInput.setBattery(payloadOrder.getCharge());
                    invoiceInput.setInvoiceQuantity(payloadOrder.getInvoiceQuantity());
                    invoiceInput.setInvoiceUnitCharge(payloadOrder.getInvoiceUnitCharge());
                    invoiceInput.setInvoiceNote(payloadOrder.getInvoiceNote());
                    invoiceInput.setHsCode(payloadOrder.getHsCode());
                    expressOrderInvoiceInput[0] = invoiceInput;

                    ExpressOrderShipperInput expressOrderShipperInput = new ExpressOrderShipperInput();
                    expressOrderInput.setShippingMethod(payloadOrder.getShippingMethod());
                    expressOrderInput.setShipper(expressOrderShipperInput);
                    expressOrderInput.setConsignee(expressCreateConsigneeOrder);
                    expressOrderInput.setInvoice(expressOrderInvoiceInput);
                    expressOrderInput.setReferenceNo("SLD-" + SnowflakeUtils.snowflake());
                    expressOrderInput.setExpressId(String.valueOf(effectiveExpressId));

                    log.info("###下单请求: {}", JSONUtil.toJsonStr(expressOrderInput));
                    OrderResultOutput returnOrder = busiExpressService.createOrder(expressOrderInput);
                    log.info("###下单返回: {}", JSONUtil.toJsonStr(returnOrder));
                    busiOrder.setThirdPartyNo(returnOrder.getShippingMethodNo());
                    busiOrder.setOrderNo(returnOrder.getOrderId());
                    busiOrder.setDeliveryNo(returnOrder.getRefrenceNo());
                } else {
                    log.info("###手动录入订单号不调用第三方处理: {}", System.currentTimeMillis());
                }

                // 处理换单逻辑
                handleReplaceOrder(order, busiOrder);

                // 处理扣件检查
                handleDeductCheck(order, busiOrder);
            }
        } else if (STATUS_SHIPPED.equals(busiOrder.getStatus()) || STATUS_CHARGED.equals(order.getStatus())) {
            busiOrder.setStatus(STATUS_CHARGED);
            settleOrderCharge(order, busiOrder, custId);
        } else if (STATUS_SHIPPED.equals(order.getStatus())) {
            busiOrder.setStatus(STATUS_REJECTED);
            markRejectedWorkflow(busiOrder);
            if ("2".equals(order.getType())) {
                Long productId = order.getProductId();
                BusiProduct busiProduct = busiProductMapper.selectBusiProductById(productId);
                long currentNums = busiProduct.getNums() == null ? 0L : busiProduct.getNums();
                long warehouseNums = order.getWarehouseSendNums() == null ? 0L : order.getWarehouseSendNums();
                busiProduct.setNums(currentNums + warehouseNums);
                busiProductMapper.updateBusiProduct(busiProduct);
            }
        }

        busiOrder.setUpdatedBy(getUsername());
        busiOrder.setUpdatedDate(new Date());
        int rows = busiOrderMapper.updateBusiOrder(busiOrder);
        BusiOrder updated = busiOrderMapper.selectBusiOrderById(busiOrder.getId());
        appendOrderEvent(updated, resolveEventType(beforeStatus, updated.getStatus()), beforeStatus, updated.getStatus(), resolveEventTitle(beforeStatus, updated.getStatus(), updated), buildOrderDetail(updated));
        return rows;
    }

    @Override
    public int resubmitDraftOrder(Long id) {
        BusiOrder draftOrder = busiOrderMapper.selectBusiOrderById(id);
        if (draftOrder == null) {
            throw new ServiceException("草稿订单不存在");
        }

        if (!STATUS_DRAFT.equals(draftOrder.getStatus())) {
            throw new ServiceException("只有草稿状态的订单才能重新提交");
        }

        // 重新进行数据检查
        boolean dataCheckPassed = performDataCheck(draftOrder);

        if (dataCheckPassed) {
            // 检查通过，转为正常待入库状态
            draftOrder.setStatus(STATUS_PENDING_WAREHOUSE);
            draftOrder.setCheckStatus(CHECK_PASSED);
            draftOrder.setCheckSummary("数据检查通过");
            draftOrder.setUpdatedBy(getUsername());
            draftOrder.setUpdatedDate(new Date());

            int rows = busiOrderMapper.updateBusiOrder(draftOrder);
            BusiOrder updated = busiOrderMapper.selectBusiOrderById(id);
            appendOrderEvent(updated, "draft_resubmitted", STATUS_DRAFT, updated.getStatus(), "草稿订单重新提交通过", buildOrderDetail(updated));

            return rows;
        } else {
            // 检查仍然失败，更新错误信息
            draftOrder.setUpdatedBy(getUsername());
            draftOrder.setUpdatedDate(new Date());
            busiOrderMapper.updateBusiOrder(draftOrder);

            throw new ServiceException("草稿订单重新提交检查失败：" + draftOrder.getCheckSummary());
        }
    }

    @Override
    public String printInfo(Long id) {
        BusiOrder order = selectBusiOrderById(id);
        PrintExpressInfoInput printExpressInfoInput = new PrintExpressInfoInput();
        String printOrderNo = StringUtils.defaultIfEmpty(order.getOrderNo(), order.getThirdPartyNo());
        printExpressInfoInput.setOrderNo(printOrderNo);
        if (order.getExpressId() != null && 4 == order.getExpressId().intValue()) {
            printExpressInfoInput.setOrderNo(StringUtils.defaultIfEmpty(order.getThirdPartyNo(), printOrderNo));
        }
        printExpressInfoInput.setExpressId(String.valueOf(order.getExpressId()));
        printExpressInfoInput.setReferenceNo(order.getDeliveryNo());
        List<PrintExpressOutput> printExpressOutputs = busiExpressService.queryInfo(printExpressInfoInput);
        return printExpressOutputs.get(0).getLableFile();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int operaterBusiOrder(BusiOrder busiOrder) {
        BusiOrder current = busiOrderMapper.selectBusiOrderById(busiOrder.getId());
        if (current == null) {
            throw new ServiceException("订单不存在");
        }

        boolean statusOperation = StringUtils.isNotEmpty(busiOrder.getStatus())
                || busiOrder.getWarehouseSendNums() != null
                || busiOrder.getExpressId() != null
                || StringUtils.isNotEmpty(busiOrder.getThirdPartyNo())
                || busiOrder.getExpressMoney() != null
                || busiOrder.getPackageMoney() != null
                || busiOrder.getTaxes() != null
                || busiOrder.getArrivedMoney() != null;

        if (statusOperation) {
            return updateBusiOrder(busiOrder);
        }

        if (StringUtils.isNotEmpty(busiOrder.getReleaseStatus())) {
            current.setReleaseStatus(busiOrder.getReleaseStatus());
        }
        if (StringUtils.isNotEmpty(busiOrder.getIssueStatus())) {
            current.setIssueStatus(busiOrder.getIssueStatus());
        }
        if (StringUtils.isNotEmpty(busiOrder.getIssueType())) {
            current.setIssueType(busiOrder.getIssueType());
        }
        if (StringUtils.isNotEmpty(busiOrder.getCheckStatus())) {
            current.setCheckStatus(busiOrder.getCheckStatus());
        }
        if (StringUtils.isNotEmpty(busiOrder.getCheckSummary())) {
            current.setCheckSummary(busiOrder.getCheckSummary());
        }
        if (busiOrder.getRejectRemark() != null) {
            current.setRejectRemark(busiOrder.getRejectRemark());
        }
        if (StringUtils.isNotEmpty(busiOrder.getNeedReplace())) {
            current.setNeedReplace(busiOrder.getNeedReplace());
        }
        if (StringUtils.isNotEmpty(busiOrder.getReplaceReason())) {
            current.setReplaceReason(busiOrder.getReplaceReason());
        }
        if (StringUtils.isNotEmpty(busiOrder.getNeedInternalOrder())) {
            current.setNeedInternalOrder(busiOrder.getNeedInternalOrder());
        }
        if (StringUtils.isNotEmpty(busiOrder.getInternalOrderNo())) {
            current.setInternalOrderNo(busiOrder.getInternalOrderNo());
        }
        if (StringUtils.isNotEmpty(busiOrder.getNeedDeduct())) {
            current.setNeedDeduct(busiOrder.getNeedDeduct());
        }
        if (StringUtils.isNotEmpty(busiOrder.getDeductReason())) {
            current.setDeductReason(busiOrder.getDeductReason());
        }
        current.setUpdatedBy(getUsername());
        current.setUpdatedDate(new Date());
        normalizeWorkflowFields(current, true);
        int rows = busiOrderMapper.updateBusiOrder(current);
        BusiOrder updated = busiOrderMapper.selectBusiOrderById(current.getId());
        appendOrderEvent(updated, "order_operated", current.getStatus(), updated.getStatus(), "订单骨架字段已更新", buildOrderDetail(updated));
        return rows;
    }

    @Override
    public int deleteBusiOrderByIds(Long[] ids) {
        return busiOrderMapper.deleteBusiOrderByIds(ids);
    }

    @Override
    public int deleteBusiOrderById(Long id) {
        return busiOrderMapper.deleteBusiOrderById(id);
    }

    @Override
    public AdminReportOutput queryReport() {
        AdminReportOutput report = busiOrderMapper.queryReport(null);
        return report == null ? new AdminReportOutput() : report;
    }

    @Override
    public List<AdminReportOutput> queryReportHistory(QueryChartInput chartInput) {
        return busiOrderMapper.queryReportHistory(chartInput);
    }

    @Override
    public List<BusiOrderEvent> selectBusiOrderEventList(Long orderId) {
        BusiOrderEvent query = new BusiOrderEvent();
        query.setOrderId(orderId);
        return busiOrderEventMapper.selectBusiOrderEventList(query);
    }

    private BusiOrder mergeForThirdParty(BusiOrder current, BusiOrder incoming, Long effectiveExpressId) {
        BusiOrder merged = new BusiOrder();
        merged.setGoodsHeight(incoming.getGoodsHeight() != null ? incoming.getGoodsHeight() : current.getGoodsHeight());
        merged.setGoodsLength(incoming.getGoodsLength() != null ? incoming.getGoodsLength() : current.getGoodsLength());
        merged.setGoodsWeight(incoming.getGoodsWeight() != null ? incoming.getGoodsWeight() : current.getGoodsWeight());
        merged.setConsigneeCity(StringUtils.defaultIfEmpty(incoming.getConsigneeCity(), current.getConsigneeCity()));
        merged.setConsigneeName(StringUtils.defaultIfEmpty(incoming.getConsigneeName(), current.getConsigneeName()));
        merged.setConsigneeCountryCode(StringUtils.defaultIfEmpty(incoming.getConsigneeCountryCode(), current.getConsigneeCountryCode()));
        merged.setConsigneeStreet(StringUtils.defaultIfEmpty(incoming.getConsigneeStreet(), current.getConsigneeStreet()));
        merged.setConsigneeTelephone(StringUtils.defaultIfEmpty(incoming.getConsigneeTelephone(), current.getConsigneeTelephone()));
        merged.setConsigneeCompany(StringUtils.defaultIfEmpty(incoming.getConsigneeCompany(), current.getConsigneeCompany()));
        merged.setConsigneeMail(StringUtils.defaultIfEmpty(incoming.getConsigneeMail(), current.getConsigneeMail()));
        merged.setConsigneeProvince(StringUtils.defaultIfEmpty(incoming.getConsigneeProvince(), current.getConsigneeProvince()));
        merged.setConsigneeHousenum(StringUtils.defaultIfEmpty(incoming.getConsigneeHousenum(), current.getConsigneeHousenum()));
        merged.setInvoiceEnName(StringUtils.defaultIfEmpty(incoming.getInvoiceEnName(), current.getInvoiceEnName()));
        merged.setInvoiceCnName(StringUtils.defaultIfEmpty(incoming.getInvoiceCnName(), current.getInvoiceCnName()));
        merged.setIossNo(StringUtils.defaultIfEmpty(incoming.getIossNo(), current.getIossNo()));
        merged.setEvaluate(StringUtils.defaultIfEmpty(incoming.getEvaluate(), current.getEvaluate()));
        merged.setUnitCode(StringUtils.defaultIfEmpty(incoming.getUnitCode(), current.getUnitCode()));
        merged.setCharge(StringUtils.defaultIfEmpty(incoming.getCharge(), current.getCharge()));
        merged.setInvoiceQuantity(StringUtils.defaultIfEmpty(incoming.getInvoiceQuantity(), current.getInvoiceQuantity()));
        merged.setInvoiceUnitCharge(StringUtils.defaultIfEmpty(incoming.getInvoiceUnitCharge(), current.getInvoiceUnitCharge()));
        merged.setInvoiceNote(StringUtils.defaultIfEmpty(incoming.getInvoiceNote(), current.getInvoiceNote()));
        merged.setHsCode(StringUtils.defaultIfEmpty(incoming.getHsCode(), current.getHsCode()));
        merged.setShippingMethod(StringUtils.defaultIfEmpty(incoming.getShippingMethod(), current.getShippingMethod()));
        merged.setExpressId(effectiveExpressId);
        return merged;
    }

    private void settleOrderCharge(BusiOrder order, BusiOrder busiOrder, Long custId) {
        double expressMoney = busiOrder.getExpressMoney() == null ? 0 : busiOrder.getExpressMoney().doubleValue();
        long packageMoney = busiOrder.getPackageMoney() == null ? 0 : busiOrder.getPackageMoney();
        double cutMoney = expressMoney + packageMoney;
        if (busiOrder.getTaxes() != null) {
            cutMoney += busiOrder.getTaxes().doubleValue();
        }
        if (busiOrder.getArrivedMoney() != null) {
            cutMoney += busiOrder.getArrivedMoney();
        }

        SysUser sysUser = sysUserMapper.selectUserById(custId);
        if (sysUser.getBalance().doubleValue() == 0) {
            throw new ServiceException("客户账户余额不足，下单失败！");
        }
        if (new BigDecimal(cutMoney).compareTo(sysUser.getBalance()) == 1) {
            throw new ServiceException("客户账户余额不足，下单失败！");
        }

        BusiPayHistory busiPayHistory = new BusiPayHistory();
        busiPayHistory.setCustId(custId);
        busiPayHistory.setPayMoney(BigDecimal.valueOf(cutMoney));
        busiPayHistory.setBalance(sysUser.getBalance().subtract(BigDecimal.valueOf(cutMoney)));
        busiPayHistory.setRemakr("下单扣减");
        busiPayHistory.setDeptId(order.getDeptId());
        busiPayHistory.setCreatedBy(getUsername());
        busiPayHistory.setUpdatedBy(getUsername());
        busiPayHistory.setCreatedDate(new Date());
        busiPayHistory.setUpdatedDate(new Date());
        busiPayHistory.setOrderNo(order.getThirdPartyNo());
        busiPayHistoryMapper.insertBusiPayHistory(busiPayHistory);

        BusiPayHistoryIndex busiPayHistoryIndex = new BusiPayHistoryIndex();
        busiPayHistoryIndex.setCustId(order.getCustId());
        busiPayHistoryIndex.setOrderNo(order.getThirdPartyNo());
        busiPayHistoryIndex.setMoney(BigDecimal.valueOf(cutMoney));
        busiPayHistoryIndex.setDeptId(order.getDeptId());
        busiPayHistoryIndex.setCreatedBy(getUsername());
        busiPayHistoryIndex.setUpdatedBy(getUsername());
        busiPayHistoryIndexMapper.insertBusiPayHistoryIndex(busiPayHistoryIndex);

        busiOrder.setTotalMoney(BigDecimal.valueOf(cutMoney));
        sysUser.setBalance(sysUser.getBalance().subtract(BigDecimal.valueOf(cutMoney)));
        sysUserMapper.updateUser(sysUser);
    }

    private void normalizeWorkflowFields(BusiOrder busiOrder, boolean keepStatus) {
        if (!keepStatus && StringUtils.isEmpty(busiOrder.getStatus())) {
            busiOrder.setStatus(STATUS_PENDING_WAREHOUSE);
        }
        if (StringUtils.isEmpty(busiOrder.getCheckStatus())) {
            busiOrder.setCheckStatus(CHECK_PENDING);
        }
        if (StringUtils.isEmpty(busiOrder.getIssueStatus())) {
            busiOrder.setIssueStatus(ISSUE_PENDING);
        }
        if (StringUtils.isEmpty(busiOrder.getReleaseStatus())) {
            busiOrder.setReleaseStatus(RELEASE_PENDING);
        }
        if (StringUtils.isNotEmpty(busiOrder.getRejectRemark())) {
            busiOrder.setCheckStatus(CHECK_FAILED);
            if (StringUtils.isEmpty(busiOrder.getIssueType())) {
                busiOrder.setIssueType("reject_reason");
            }
            if (StringUtils.isEmpty(busiOrder.getIssueStatus()) || ISSUE_PENDING.equals(busiOrder.getIssueStatus())) {
                busiOrder.setIssueStatus(ISSUE_PROCESSING);
            }
            if (StringUtils.isEmpty(busiOrder.getCheckSummary())) {
                busiOrder.setCheckSummary(busiOrder.getRejectRemark());
            }
        } else if (StringUtils.isEmpty(busiOrder.getCheckSummary())) {
            busiOrder.setCheckSummary("待检查");
        }

        if (STATUS_CHARGED.equals(busiOrder.getStatus()) && StringUtils.isEmpty(busiOrder.getCheckStatus())) {
            busiOrder.setCheckStatus(CHECK_PASSED);
        }
        if (STATUS_SHIPPED.equals(busiOrder.getStatus()) && StringUtils.isEmpty(busiOrder.getIssueType())) {
            busiOrder.setIssueType("tracking");
        }
    }

    private void markRejectedWorkflow(BusiOrder busiOrder) {
        busiOrder.setCheckStatus(CHECK_FAILED);
        if (StringUtils.isEmpty(busiOrder.getCheckSummary())) {
            busiOrder.setCheckSummary(StringUtils.defaultIfEmpty(busiOrder.getRejectRemark(), "订单被驳回"));
        }
        if (StringUtils.isEmpty(busiOrder.getIssueType())) {
            busiOrder.setIssueType("warehouse_reject");
        }
        busiOrder.setIssueStatus(ISSUE_PROCESSING);
        busiOrder.setReleaseStatus(RELEASE_PENDING);
    }

    private String resolveEventType(String beforeStatus, String afterStatus) {
        if (STATUS_WAREHOUSED.equals(afterStatus) && !STATUS_WAREHOUSED.equals(beforeStatus)) {
            return "order_warehoused";
        }
        if (STATUS_SHIPPED.equals(afterStatus) && !STATUS_SHIPPED.equals(beforeStatus)) {
            return "order_shipped";
        }
        if (STATUS_REJECTED.equals(afterStatus) && !STATUS_REJECTED.equals(beforeStatus)) {
            return "order_rejected";
        }
        if (STATUS_CHARGED.equals(afterStatus) && !STATUS_CHARGED.equals(beforeStatus)) {
            return "order_charged";
        }
        return "order_updated";
    }

    /**
     * 数据检查 - 检查地址和申报信息
     */
    private boolean performDataCheck(BusiOrder busiOrder) {
        // 检查地址信息
        boolean addressValid = StringUtils.isNotEmpty(busiOrder.getConsigneeName())
                && StringUtils.isNotEmpty(busiOrder.getConsigneeStreet())
                && StringUtils.isNotEmpty(busiOrder.getConsigneeCity())
                && StringUtils.isNotEmpty(busiOrder.getConsigneeCountryCode())
                && StringUtils.isNotEmpty(busiOrder.getConsigneeTelephone());

        // 检查申报信息
        boolean declarationValid = StringUtils.isNotEmpty(busiOrder.getInvoiceCnName())
                && StringUtils.isNotEmpty(busiOrder.getInvoiceEnName())
                && StringUtils.isNotEmpty(busiOrder.getInvoiceQuantity())
                && StringUtils.isNotEmpty(busiOrder.getInvoiceUnitCharge());

        if (!addressValid) {
            busiOrder.setCheckSummary("地址信息不完整，请检查收件人姓名、地址、城市、国家和电话");
            return false;
        }

        if (!declarationValid) {
            busiOrder.setCheckSummary("申报信息不完整，请检查报关中文名、英文名、数量和单价");
            return false;
        }

        return true;
    }

    /**
     * 生成内部制单
     */
    private String generateInternalOrder(BusiOrder order) {
        String internalOrderNo = "INT-" + SnowflakeUtils.snowflake();
        log.info("###生成内部制单: {} for order: {}", internalOrderNo, order.getId());

        // 这里可以添加更多的内部制单逻辑
        // 例如：创建内部制单记录、特殊收费逻辑等

        return internalOrderNo;
    }

    /**
     * 处理换单逻辑
     */
    private void handleReplaceOrder(BusiOrder order, BusiOrder incoming) {
        if (NEED_REPLACE_YES.equals(incoming.getNeedReplace())) {
            log.info("###需要换单: {} reason: {}", order.getId(), incoming.getReplaceReason());

            // 清空原有的快递单号和面单
            order.setThirdPartyNo(null);
            order.setFaceUrl(null);

            // 重新生成快递单号
            if (order.getExpressId() != null) {
                ExpressOrderInput expressOrderInput = new ExpressOrderInput();
                // 构建快递订单输入...
                // 这里可以复用之前的快递订单创建逻辑
            }
        }
    }

    /**
     * 处理扣件检查逻辑
     */
    private void handleDeductCheck(BusiOrder order, BusiOrder incoming) {
        if (NEED_DEDUCT_YES.equals(incoming.getNeedDeduct())) {
            log.info("###需要扣件处理: {} reason: {}", order.getId(), incoming.getDeductReason());

            // 扣件逻辑可能包括：
            // 1. 调整发货数量
            // 2. 标记订单状态
            // 3. 记录扣件原因
            // 4. 可能需要退款或补发

            // 如果扣件，可能需要调整实际发货数量
            if (order.getWarehouseSendNums() != null && order.getWarehouseSendNums() > 0) {
                long originalNums = order.getWarehouseSendNums();
                // 这里可以根据扣件原因调整数量
                // order.setWarehouseSendNums(originalNums - deductedNums);
            }
        }
    }

    private String resolveEventTitle(String beforeStatus, String afterStatus, BusiOrder order) {
        if (STATUS_WAREHOUSED.equals(afterStatus) && !STATUS_WAREHOUSED.equals(beforeStatus)) {
            return "订单已入库";
        }
        if (STATUS_SHIPPED.equals(afterStatus) && !STATUS_SHIPPED.equals(beforeStatus)) {
            return "订单已发货";
        }
        if (STATUS_REJECTED.equals(afterStatus) && !STATUS_REJECTED.equals(beforeStatus)) {
            return "订单已驳回";
        }
        if (STATUS_CHARGED.equals(afterStatus) && !STATUS_CHARGED.equals(beforeStatus)) {
            return "订单已扣费";
        }
        if (StringUtils.isNotEmpty(order.getReleaseStatus()) && RELEASE_APPROVED.equals(order.getReleaseStatus())) {
            return "订单已放行";
        }
        if (StringUtils.isNotEmpty(order.getIssueStatus()) && ISSUE_RESOLVED.equals(order.getIssueStatus())) {
            return "问题件已处理完成";
        }
        return "订单信息已更新";
    }

    private String buildOrderDetail(BusiOrder order) {
        StringBuilder detail = new StringBuilder();
        if (StringUtils.isNotEmpty(order.getCheckStatus())) {
            detail.append("checkStatus=").append(order.getCheckStatus()).append("; ");
        }
        if (StringUtils.isNotEmpty(order.getIssueType())) {
            detail.append("issueType=").append(order.getIssueType()).append("; ");
        }
        if (StringUtils.isNotEmpty(order.getIssueStatus())) {
            detail.append("issueStatus=").append(order.getIssueStatus()).append("; ");
        }
        if (StringUtils.isNotEmpty(order.getReleaseStatus())) {
            detail.append("releaseStatus=").append(order.getReleaseStatus()).append("; ");
        }
        if (StringUtils.isNotEmpty(order.getRejectRemark())) {
            detail.append("rejectRemark=").append(order.getRejectRemark()).append("; ");
        }
        if (StringUtils.isNotEmpty(order.getThirdPartyNo())) {
            detail.append("thirdPartyNo=").append(order.getThirdPartyNo()).append("; ");
        }
        return detail.toString();
    }

    private void appendOrderEvent(BusiOrder order, String eventType, String beforeStatus, String afterStatus, String title, String detail) {
        if (order == null || order.getId() == null) {
            return;
        }
        BusiOrderEvent event = new BusiOrderEvent();
        event.setOrderId(order.getId());
        event.setOrderNo(order.getOrderNo());
        event.setEventType(eventType);
        event.setBeforeStatus(beforeStatus);
        event.setAfterStatus(afterStatus);
        event.setTitle(title);
        event.setDetail(detail);
        event.setCreatedDate(new Date());
        event.setUpdatedDate(new Date());
        event.setCreatedBy(StringUtils.defaultIfEmpty(getUsername(), order.getUpdatedBy()));
        event.setUpdatedBy(StringUtils.defaultIfEmpty(getUsername(), order.getUpdatedBy()));
        event.setDeptId(order.getDeptId());
        busiOrderEventMapper.insertBusiOrderEvent(event);
    }

    private void fillOrderProductSnapshot(BusiOrder busiOrder) {
        Long productId = busiOrder.getProductId();
        if (productId == null || StringUtils.isNotEmpty(busiOrder.getPurchaseUrl())) {
            return;
        }
        BusiProduct busiProduct = busiProductMapper.selectBusiProductById(productId);
        if (busiProduct != null) {
            busiOrder.setPurchaseUrl(busiProduct.getImgUrl());
        }
    }

    private void fillOrderCustomerSnapshot(BusiOrder busiOrder) {
        Long customerId = busiOrder.getCustomerId();
        if (customerId == null) {
            return;
        }
        BusiExpressCustomer customer = busiExpressCustomerMapper.selectBusiExpressCustomerById(customerId);
        if (customer == null) {
            throw new ServiceException("客户信息不存在");
        }
        if (StringUtils.isEmpty(busiOrder.getAddressLibrary())) {
            busiOrder.setAddressLibrary(String.valueOf(customer.getId()));
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeName())) {
            busiOrder.setConsigneeName(customer.getAddressee());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeCompany())) {
            busiOrder.setConsigneeCompany(customer.getCompany());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeStreet())) {
            busiOrder.setConsigneeStreet(customer.getAddress());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeCity())) {
            busiOrder.setConsigneeCity(customer.getCity());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeProvince())) {
            busiOrder.setConsigneeProvince(customer.getStates());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeMail())) {
            busiOrder.setConsigneeMail(customer.getZipCode());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeTelephone())) {
            busiOrder.setConsigneeTelephone(customer.getTel());
        }
        if (StringUtils.isEmpty(busiOrder.getCountryName())) {
            busiOrder.setCountryName(customer.getCountry());
        }
        if (busiOrder.getExpressId() == null) {
            busiOrder.setExpressId(customer.getExpressId());
        }
        if (StringUtils.isEmpty(busiOrder.getConsigneeCountryCode()) && StringUtils.isNotEmpty(customer.getCountry())) {
            List<CountryListVo> countryListVos = busiExpressService.queryCountryList(customer.getExpressId() == null ? null : String.valueOf(customer.getExpressId()));
            Optional<CountryListVo> match = countryListVos.stream().filter(country -> customer.getCountry().equals(country.getCnname()) || customer.getCountry().equalsIgnoreCase(country.getEnname()) || customer.getCountry().equalsIgnoreCase(country.getCode())).findFirst();
            match.ifPresent(country -> busiOrder.setConsigneeCountryCode(country.getCode()));
        }
    }

    private void normalizeOrderDirectFields(BusiOrder busiOrder) {
        if (StringUtils.isNotEmpty(busiOrder.getCountryName()) && StringUtils.isEmpty(busiOrder.getConsigneeCountryCode())) {
            busiOrder.setConsigneeCountryCode(busiOrder.getCountryName());
        }
        if (StringUtils.isNotEmpty(busiOrder.getAddressOne()) && StringUtils.isEmpty(busiOrder.getConsigneeStreet())) {
            busiOrder.setConsigneeStreet(busiOrder.getAddressOne());
        }
        if (StringUtils.isNotEmpty(busiOrder.getConsigneeStreet()) && StringUtils.isEmpty(busiOrder.getAddressOne())) {
            busiOrder.setAddressOne(busiOrder.getConsigneeStreet());
        }
    }
}
