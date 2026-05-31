package com.ruoyi.project.warehouse.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.warehouse.domain.BusiPayHistory;
import com.ruoyi.project.warehouse.service.IBusiPayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 账户充值明细Controller
 *
 * @author ruoyi
 * @date 2023-01-16
 */
@RestController
@RequestMapping("/pay/pay")
public class BusiPayHistoryController extends BaseController {
    @Autowired
    private IBusiPayHistoryService busiPayHistoryService;

    /**
     * 查询账户充值明细列表
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:list,warehouse:index:list')")
    @GetMapping("/list")
    @DataScope()
    public TableDataInfo list(BusiPayHistory busiPayHistory) {
        startPage();
        List<BusiPayHistory> list = busiPayHistoryService.selectBusiPayHistoryList(busiPayHistory);
        return getDataTable(list);
    }

    /**
     * 导出账户充值明细列表
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:export,warehouse:index:export')")
    @Log(title = "账户充值明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @DataScope()
    public void export(HttpServletResponse response, BusiPayHistory busiPayHistory) {
        List<BusiPayHistory> list = busiPayHistoryService.selectBusiPayHistoryList(busiPayHistory);
        ExcelUtil<BusiPayHistory> util = new ExcelUtil<BusiPayHistory>(BusiPayHistory.class);
        util.exportExcel(response, list, "账户充值明细数据");
    }

    /**
     * 新增账户充值明细
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:add,warehouse:index:add')")
    @Log(title = "账户充值明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiPayHistory busiPayHistory) {
        busiPayHistory.setCustId(getUserId());
        busiPayHistory.setCreatedBy(getUsername());
        busiPayHistory.setCreatedDate(new Date());

        return toAjax(busiPayHistoryService.insertBusiPayHistory(busiPayHistory));
    }

    /**
     * 修改账户充值明细
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:edit,warehouse:index:edit')")
    @Log(title = "账户充值明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiPayHistory busiPayHistory) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        busiPayHistory.setUpdatedDate(new Date());
        busiPayHistory.setUpdatedBy(loginUser.getUsername());
        return toAjax(busiPayHistoryService.updateBusiPayHistory(busiPayHistory));
    }

    /**
     * 获取账户充值明细详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:query,pay:pay:edit,warehouse:index:query,warehouse:index:edit')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(busiPayHistoryService.selectBusiPayHistoryById(id));
    }

    /**
     * 驳回
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:edit,warehouse:index:edit')")
    @Log(title = "驳回", businessType = BusinessType.UPDATE)
    @PutMapping("/delPay")
    public AjaxResult delPay(@RequestBody BusiPayHistory busiPayHistory) {
        busiPayHistory.setUpdatedBy(getUsername());
        return toAjax(busiPayHistoryService.updateReject(busiPayHistory));
    }

    /**
     * 删除账户充值明细
     */
    @PreAuthorize("@ss.hasAnyPermi('pay:pay:remove,warehouse:index:remove')")
    @Log(title = "账户充值明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busiPayHistoryService.deleteBusiPayHistoryByIds(ids));
    }
}
