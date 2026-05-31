package com.ruoyi.project.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.warehouse.input.ExpressOrderInput;
import com.ruoyi.project.warehouse.input.GetExpressFreeInput;
import com.ruoyi.project.warehouse.input.PrintExpressInfoInput;
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
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.warehouse.domain.BusiExpress;
import com.ruoyi.project.warehouse.service.IBusiExpressService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 快递Controller
 * 
 * @author liuzhen
 * @date 2023-01-12
 */
@RestController
@RequestMapping("/express/express")
public class BusiExpressController extends BaseController
{
    @Autowired
    private IBusiExpressService busiExpressService;

    /**
     * 查询快递列表
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:list,warehouse:express:list')")
    @GetMapping("/list")
    @DataScope()
    public TableDataInfo list(BusiExpress busiExpress)
    {
        startPage();
        List<BusiExpress> list = busiExpressService.selectBusiExpressList(busiExpress);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasAnyPermi('express:express:list,warehouse:express:list')")
    @GetMapping("/page-list")
    @DataScope()
    public TableDataInfo pageList(BusiExpress busiExpress)
    {
        return list(busiExpress);
    }

    @PreAuthorize("@ss.hasAnyPermi('express:express:list,warehouse:express:list')")
    @GetMapping("/basic-list")
    @DataScope()
    public AjaxResult basicList(BusiExpress busiExpress)
    {
        List<BusiExpress> list = busiExpressService.selectBusiExpressList(busiExpress);
        return AjaxResult.success(list);
    }

    /**
     * 导出快递列表
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:export,warehouse:express:export')")
    @Log(title = "快递", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @DataScope()
    public void export(HttpServletResponse response, BusiExpress busiExpress)
    {
        List<BusiExpress> list = busiExpressService.selectBusiExpressList(busiExpress);
        ExcelUtil<BusiExpress> util = new ExcelUtil<BusiExpress>(BusiExpress.class);
        util.exportExcel(response, list, "快递数据");
    }

    /**
     * 获取快递详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:query,express:express:edit,warehouse:express:query,warehouse:express:edit')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiExpressService.selectBusiExpressById(id));
    }

    /**
     * 新增快递
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:add,warehouse:express:add')")
    @Log(title = "快递", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiExpress busiExpress)
    {
        return toAjax(busiExpressService.insertBusiExpress(busiExpress));
    }

    /**
     * 修改快递
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:edit,warehouse:express:edit')")
    @Log(title = "快递", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiExpress busiExpress)
    {
        return toAjax(busiExpressService.updateBusiExpress(busiExpress));
    }

    /**
     * 删除快递
     */
    @PreAuthorize("@ss.hasAnyPermi('express:express:remove,warehouse:express:remove')")
    @Log(title = "快递", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiExpressService.deleteBusiExpressByIds(ids));
    }



    @Log(title = "查询快递费", businessType = BusinessType.OTHER)
    @GetMapping("/query-express-free")
    public AjaxResult queryExpressFree(GetExpressFreeInput expressFreeInput){
        return busiExpressService.queryExpressFree(expressFreeInput);
    }

    @Log(title = "获取国家标签", businessType = BusinessType.OTHER)
    @GetMapping("/country-lists")
    public AjaxResult queryCountryList(){
        return AjaxResult.success(busiExpressService.queryCountryList(null));
    }


    @Log(title = "查询快递费", businessType = BusinessType.OTHER)
    @GetMapping("/shipping-method")
    public AjaxResult getShippingMethod(String id){
        return AjaxResult.success(busiExpressService.getShippingMethod(id));
    }

    @Log(title = "打印面单", businessType = BusinessType.OTHER)
    @GetMapping("/print-info")
    public AjaxResult queryInfo(PrintExpressInfoInput printExpressInfoInput){
        return AjaxResult.success(busiExpressService.queryInfo(printExpressInfoInput));
    }

    @Log(title = "创建订单", businessType = BusinessType.INSERT)
    @GetMapping("/createOrder")
    public AjaxResult createOrder(ExpressOrderInput expressOrderInput){
        return AjaxResult.success(busiExpressService.createOrder(expressOrderInput));
    }

}
