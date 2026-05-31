package com.ruoyi.project.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.warehouse.domain.BusiExpressCustomer;
import com.ruoyi.project.warehouse.service.IBusiExpressCustomerService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 快递客户Controller
 * 
 * @author liuzhen
 * @date 2023-01-15
 */
@RestController
@RequestMapping("/express/customer")
public class BusiExpressCustomerController extends BaseController
{
    @Autowired
    private IBusiExpressCustomerService expressCustomerService;

    /**
     * 查询快递客户列表
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:list,warehouse:customer:list')")
    @GetMapping("/list")
    @DataScope()
    public TableDataInfo list(BusiExpressCustomer busiExpressCustomer)
    {
        startPage();
        List<BusiExpressCustomer> list = expressCustomerService.selectBusiExpressCustomerList(busiExpressCustomer);
        return getDataTable(list);
    }

    /**
     * 导出快递客户列表
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:export,warehouse:customer:export')")
    @Log(title = "快递客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @DataScope()
    public void export(HttpServletResponse response, BusiExpressCustomer busiExpressCustomer)
    {
        List<BusiExpressCustomer> list = expressCustomerService.selectBusiExpressCustomerList(busiExpressCustomer);
        ExcelUtil<BusiExpressCustomer> util = new ExcelUtil<BusiExpressCustomer>(BusiExpressCustomer.class);
        util.exportExcel(response, list, "快递客户数据");
    }

    /**
     * 获取快递客户详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:query,express:customer:edit,warehouse:customer:query,warehouse:customer:edit')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressCustomerService.selectBusiExpressCustomerById(id));
    }

    @Log(title = "快递客户", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasAnyPermi('express:customer:import,warehouse:customer:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BusiExpressCustomer> util = new ExcelUtil<>(BusiExpressCustomer.class);
        List<BusiExpressCustomer> busiExpressCustomerList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = expressCustomerService.importCustomer(busiExpressCustomerList, operName);
        return success(message);
    }

    @PreAuthorize("@ss.hasAnyPermi('express:customer:import,warehouse:customer:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BusiExpressCustomer> util = new ExcelUtil<>(BusiExpressCustomer.class);
        util.hideColumn("id", "createdDate", "updatedDate", "createdBy", "updatedBy", "deptId");
        util.importTemplateExcel(response, "快递客户数据");
    }

    /**
     * 新增快递客户
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:add,warehouse:customer:add')")
    @Log(title = "快递客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiExpressCustomer busiExpressCustomer)
    {
        return toAjax(expressCustomerService.insertBusiExpressCustomer(busiExpressCustomer));
    }

    /**
     * 修改快递客户
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:edit,warehouse:customer:edit')")
    @Log(title = "快递客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiExpressCustomer busiExpressCustomer)
    {
        return toAjax(expressCustomerService.updateBusiExpressCustomer(busiExpressCustomer));
    }

    /**
     * 删除快递客户
     */
    @PreAuthorize("@ss.hasAnyPermi('express:customer:remove,warehouse:customer:remove')")
    @Log(title = "快递客户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressCustomerService.deleteBusiExpressCustomerByIds(ids));
    }
}
