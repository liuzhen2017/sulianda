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
import com.ruoyi.project.warehouse.domain.BusiProduct;
import com.ruoyi.project.warehouse.service.IBusiProductService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 商品Controller
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
@RestController
@RequestMapping("/product/product")
public class BusiProductController extends BaseController
{
    @Autowired
    private IBusiProductService busiProductService;

    /**
     * 查询商品列表
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:list,warehouse:product:list')")
    @GetMapping("/list")
    @DataScope()
    public TableDataInfo list(BusiProduct busiProduct)
    {
        startPage();
        List<BusiProduct> list = busiProductService.selectBusiProductList(busiProduct);
        return getDataTable(list);
    }

    /**
     * 导出商品列表
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:export,warehouse:product:export')")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @DataScope()
    public void export(HttpServletResponse response, BusiProduct busiProduct)
    {
        List<BusiProduct> list = busiProductService.selectBusiProductList(busiProduct);
        ExcelUtil<BusiProduct> util = new ExcelUtil<BusiProduct>(BusiProduct.class);
        util.exportExcel(response, list, "商品数据");
    }

    /**
     * 获取商品详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:query,product:product:edit,warehouse:product:query,warehouse:product:edit')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(busiProductService.selectBusiProductById(id));
    }

    @Log(title = "商品", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasAnyPermi('product:product:import,warehouse:product:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BusiProduct> util = new ExcelUtil<>(BusiProduct.class);
        List<BusiProduct> busiProductList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = busiProductService.importProduct(busiProductList, operName);
        return success(message);
    }

    @PreAuthorize("@ss.hasAnyPermi('product:product:import,warehouse:product:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BusiProduct> util = new ExcelUtil<>(BusiProduct.class);
        util.hideColumn("custId", "createdTime", "createdBy", "deptId");
        util.importTemplateExcel(response, "商品数据");
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:add,warehouse:product:add')")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiProduct busiProduct)
    {
        busiProduct.setCreatedBy(getUsername());
        busiProduct.setCustId(getUserId());
        busiProduct.setUpdateBy(getUsername());
        return toAjax(busiProductService.insertBusiProduct(busiProduct));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:edit,warehouse:product:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiProduct busiProduct)
    {
        return toAjax(busiProductService.updateBusiProduct(busiProduct));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasAnyPermi('product:product:remove,warehouse:product:remove')")
    @Log(title = "商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiProductService.deleteBusiProductByIds(ids));
    }
}
