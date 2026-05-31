package com.ruoyi.project.warehouse.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.warehouse.domain.BusiOrder;
import com.ruoyi.project.warehouse.domain.BusiOrderEvent;
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.service.IBusiOrderService;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import com.ruoyi.project.warehouse.service.impl.BusiExpressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单Controller
 *
 * @author ruoyi
 * @date 2023-01-13
 */
@RestController
@RequestMapping("/order/order")
public class BusiOrderController extends BaseController {
    @Autowired
    private IBusiOrderService busiOrderService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:list,warehouse:order:list')")
    @GetMapping("/list")
    @DataScope()
    public TableDataInfo list(BusiOrder busiOrder) {
        startPage();
        List<BusiOrder> list = busiOrderService.selectBusiOrderList(busiOrder);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:export,warehouse:order:export')")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @DataScope()
    public void export(HttpServletResponse response, BusiOrder busiOrder) {
        List<BusiOrder> list = busiOrderService.selectBusiOrderList(busiOrder);
        ExcelUtil<BusiOrder> util = new ExcelUtil<BusiOrder>(BusiOrder.class);
        util.exportExcel(response, list, "订单数据");
    }

    @Autowired
    private IBusiProductService productService;

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:query,order:order:edit,order:order:add,warehouse:order:query,warehouse:order:edit,warehouse:order:add')")
    @GetMapping(value = {"/", "/{id}"})
    public AjaxResult getInfo(@PathVariable(value = "id", required = false) Long id) {
        AjaxResult ajax = AjaxResult.success();
        List<BusiProduct> roles = productService.selectRoleAll();
        ajax.put("busiProducts", roles.stream().collect(Collectors.toList()));
        ajax.put(AjaxResult.DATA_TAG, busiOrderService.selectBusiOrderById(id));
        return ajax;
    }


    /**
     * 打印面单
     */
    @Log(title = "打印面单", businessType = BusinessType.OPERATER)
    @GetMapping("/printInfo/{id}")
    public AjaxResult printInfo(@PathVariable(value = "id", required = false) Long id) {
        return AjaxResult.success("ok", busiOrderService.printInfo(id));
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BusiOrder> util = new ExcelUtil<>(BusiOrder.class);
        util.importTemplateExcel(response, "订单数据");
    }

    @Log(title = "订单管理", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('order:order:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<BusiOrder> util = new ExcelUtil<>(BusiOrder.class);
        List<BusiOrder> busiOrderList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = busiOrderService.importUser(busiOrderList, operName);
        return success(message);
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:add,warehouse:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiOrder busiOrder) {
        busiOrder.setCreatedBy(getUsername());
        busiOrder.setCustId(getUserId());
            return toAjax(busiOrderService.insertBusiOrder(busiOrder));
    }

    @Log(title = "操作订单(入库，发货)", businessType = BusinessType.OTHER)
    @PutMapping("/operater")
    public AjaxResult operaterBusiOrder(@RequestBody BusiOrder busiOrder) {
        return toAjax(busiOrderService.operaterBusiOrder(busiOrder));
    }

    @PreAuthorize("@ss.hasAnyPermi('order:order:query,warehouse:order:query')")
    @GetMapping("/{id}/events")
    public AjaxResult listEvents(@PathVariable Long id) {
        List<BusiOrderEvent> events = busiOrderService.selectBusiOrderEventList(id);
        return AjaxResult.success(events);
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:edit,warehouse:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiOrder busiOrder) {
        return toAjax(busiOrderService.updateBusiOrder(busiOrder));
    }

    @Autowired
    BusiExpressServiceImpl busiExpressService;


    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasAnyPermi('order:order:remove,warehouse:order:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busiOrderService.deleteBusiOrderByIds(ids));
    }
}
