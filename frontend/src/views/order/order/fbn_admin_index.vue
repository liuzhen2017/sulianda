<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="订单状态" clearable>
          <el-option v-for="dict in dict.type.order_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="下单客户" prop="createdBy">
        <el-input v-model="queryParams.createdBy" placeholder="请输入下单客户" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="快递公司">
        <el-select v-model="queryParams.expressId" placeholder="快递公司" clearable>
          <el-option v-for="dict in dict.type.busi_express" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="采购单号" prop="purchaseNo">
        <el-input v-model="queryParams.purchaseNo" placeholder="请输入采购单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="生成单号" prop="deliveryNo">
        <el-input v-model="queryParams.deliveryNo" placeholder="请输入生成单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="快递单号" prop="thirdPartyNo">
        <el-input v-model="queryParams.thirdPartyNo" placeholder="请输入快递单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:order:add','warehouse:order:add']">新增订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['order:order:add','warehouse:order:add']">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['order:order:export','warehouse:order:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="purchaseUrl" label="采购图片">
        <template slot-scope="scope">
          <a :href="resolveResourceUrl(scope.row.purchaseUrl)" target="_blank" title="查看最大化图片">
            <img :src="resolveResourceUrl(scope.row.purchaseUrl)" style="width:50px; height:50px" />
          </a>
        </template>
      </el-table-column>
      <el-table-column label="采购单号" align="center" prop="purchaseNo" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="生成单号" align="center" prop="deliveryNo" />
      <el-table-column label="快递单号" align="center" prop="thirdPartyNo">
        <template slot-scope="scope">
          <a :href="'https://t.17track.net/zh-cn#nums=' + scope.row.thirdPartyNo" type="primary" target="_blank" style="color: blue;">{{ scope.row.thirdPartyNo }}</a>
        </template>
      </el-table-column>
      <el-table-column label="物流公司" align="center" prop="expressId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_express" :value="scope.row.expressId" />
        </template>
      </el-table-column>
      <el-table-column label="手动录入物流公司" align="center" prop="expressCompany" />
      <el-table-column label="订单状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.status" :style="{ color: scope.row.status == '1' ? '#0CB618' : '#EA1B29' }" />
        </template>
      </el-table-column>
      <el-table-column label="客户发货数" align="center" prop="custSendNums" />
      <el-table-column label="仓库发货数" align="center" prop="warehouseSendNums" />
      <el-table-column label="运费" align="center" prop="expressMoney">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-show" @click="expressMoneyInfo(scope.row)" v-hasPermi="['order:order:add','warehouse:order:add']">运费明细</el-button>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center">
        <template slot-scope="scope">
          <span :style="{color:'#FF0000'}" v-if="scope.row.status == '3' || scope.row.status == '5'">{{ scope.row.totalMoney }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="invoiceNote" />
      <el-table-column label="下单人" align="center" prop="createdBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == '1'" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate1(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单入库</el-button>
          <el-button v-if="scope.row.status == '2'" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate2(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单发货</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate3(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单修改</el-button>
          <el-button v-if="scope.row.status == '3' || scope.row.status == '5'" size="mini" type="text" icon="el-icon-edit" @click="handleUpdateFaceInfo(scope.row)">打印面单</el-button>
          <el-button v-if="scope.row.status == '3'" size="mini" type="text" icon="el-icon-edit" @click="handleUpdateReject(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单驳回</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleOrderInfo(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单详情</el-button>
          <el-button v-if="scope.row.status == '3' || scope.row.status == '5'" size="mini" type="text" icon="el-icon-edit" @click="handleCutMoney(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">订单扣费</el-button>
          <el-button size="mini" type="text" icon="el-icon-tickets" @click="handleOrderEvents(scope.row)" v-hasPermi="['order:order:query','warehouse:order:query']">业务轨迹</el-button>
          <el-button size="mini" type="text" icon="el-icon-warning-outline" @click="markIssue(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">问题件</el-button>
          <el-button size="mini" type="text" icon="el-icon-circle-check" @click="approveRelease(scope.row)" v-hasPermi="['order:order:edit','warehouse:order:edit']">放行</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['order:order:remove','warehouse:order:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-input v-model="form.type" v-if="false" value="2" type="a"/>
        <el-form-item label="商品数量" prop="custSendNums">
          <el-input v-model="form.custSendNums" placeholder="请输入商品数量" />
        </el-form-item>
        <el-form-item label="采购单号" prop="purchaseNo">
          <el-input v-model="form.purchaseNo" placeholder="请输入采购单号" />
        </el-form-item>
        <el-form-item label="手工单号" prop="thirdPartyNo">
          <el-input v-model="form.thirdPartyNo" placeholder="请输入手工快递单号(可选)" />
        </el-form-item>
        <el-form-item label="快递公司" prop="expressId">
          <el-select v-model="form.expressId" @change="currStationChange" placeholder="请选择快递公司" style="width: 100%;">
            <el-option v-for="dict in dict.type.busi_express" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="运输方式代码" prop="shippingMethod">
          <el-select v-model="form.shippingMethod" filterable clearable placeholder="请选择运输方式代码" style="width: 100%;">
            <el-option v-for="dict in dict.type.shipping_method" :key="dict.code || dict.value" :label="dict.cnname || dict.label" :value="dict.code || dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="目的地国家中文名" prop="countryName">
          <el-input v-model="form.countryName" placeholder="请输入目的地国家中文名" />
        </el-form-item>
        <el-form-item label="收件人国家编码" prop="consigneeCountryCode">
          <el-select v-model="form.consigneeCountryCode" filterable placeholder="请选择收件人国家编码" style="width: 100%;height: 10%;">
            <el-option v-for="dict in dict.type.busi_country" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany">
          <el-input v-model="form.consigneeCompany" placeholder="请输入收件人公司名称" />
        </el-form-item>
        <el-form-item label="收件人姓名" prop="consigneeName">
          <el-input v-model="form.consigneeName" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="收货人地址" prop="consigneeStreet">
          <el-input v-model="form.consigneeStreet" placeholder="请输入收货人地址" />
        </el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity">
          <el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" />
        </el-form-item>
        <el-form-item label="收件人州" prop="consigneeProvince">
          <el-input v-model="form.consigneeProvince" placeholder="请输入收件人州" />
        </el-form-item>
        <el-form-item label="收件人门牌号" prop="consigneeHousenum">
          <el-input v-model="form.consigneeHousenum" placeholder="收件人门牌号" />
        </el-form-item>
        <el-form-item label="收件人联系方式" prop="consigneeTelephone">
          <el-input v-model="form.consigneeTelephone" placeholder="请输入收件人联系方式" />
        </el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail">
          <el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" />
        </el-form-item>
        <el-form-item label="欧盟税改IOSS号" prop="iossNo">
          <el-input v-model="form.iossNo" placeholder="请输入欧盟税改IOSS号" />
        </el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity">
          <el-input v-model="form.invoiceQuantity" placeholder="请输入报关产品数量" />
        </el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName">
          <el-input v-model="form.invoiceEnName" placeholder="请输入报关英文名" />
        </el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName">
          <el-input v-model="form.invoiceCnName" placeholder="请输入报关中文名" />
        </el-form-item>
        <el-form-item label="单位code" prop="unitCode">
          <el-select v-model="form.unitCode" placeholder="请选择单位code" width="100px" align="left">
            <el-option v-for="dict in dict.type.unit" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品单价" prop="invoiceUnitCharge">
          <el-input v-model="form.invoiceUnitCharge" placeholder="请输入商品单价" />
        </el-form-item>
        <el-form-item label="是否带电" prop="charge">
          <el-switch v-model="form.charge" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="是否带磁" prop="magnetism">
          <el-switch v-model="form.magnetism" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="是否液体" prop="liquid">
          <el-switch v-model="form.liquid" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="是否粉末" prop="powder">
          <el-switch v-model="form.powder" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="是否危险品" prop="danger">
          <el-switch v-model="form.danger" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="申报总价" prop="evaluate">
          <el-input v-model="form.evaluate" placeholder="请输入申报总价" />
        </el-form-item>
        <el-form-item label="货品打包长度(cm)" prop="goodsLength">
          <el-input v-model="form.goodsLength" placeholder="请输入货品打包长度" />
        </el-form-item>
        <el-form-item label="货品打包宽度(cm)" prop="goodsWidth">
          <el-input v-model="form.goodsWidth" placeholder="请输入货品打包宽度" />
        </el-form-item>
        <el-form-item label="货品打包高度(cm)" prop="goodsHeight">
          <el-input v-model="form.goodsHeight" placeholder="请输入货品打包高度" />
        </el-form-item>
        <el-form-item label="货品打包重量(kg)" prop="goodsWeight">
          <el-input v-model="form.goodsWeight" placeholder="请输入货品打包重量" />
        </el-form-item>
        <el-form-item label="备注" prop="invoiceNote" width="300px">
          <el-input v-model="form.invoiceNote" placeholder="请输入备注" :maxlength="100" type="textarea" :autosize="{minRows: 2, maxRows: 2}"/>
        </el-form-item>
        <el-form-item label="采购图片" prop="purchaseUrl">
          <image-upload v-model="form.purchaseUrl" :limit="1" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport + '&type=' + upload.type"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus2" width="520px" append-to-body :before-close="cancelA">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="订单状态">
          <el-select v-model="form.status" disabled style="width: 100%;height: 10%;">
            <el-option v-for="dict in dict.type.order_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户发货数" prop="custSendNums" aria-readonly="false">
          <el-input v-model="form.custSendNums" placeholder="客户发货数" readonly="true"/>
        </el-form-item>
        <el-form-item label="仓库发货数" prop="warehouseSendNums">
          <el-input-number v-model="form.warehouseSendNums" controls-position="right" :min="1" align="center"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelA">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus3" width="700x" append-to-body align="center" :before-close="cancelB">
      <el-form ref="form" :model="form" :rules="rules" label-width="300px">
        <span style="color: red;">==============================手动输入快递单号时无需对接第三方系统下单=======================</span>
        <el-form-item label="手动录入快递单号" prop="thirdPartyNo">
          <el-input v-model="form.thirdPartyNo" placeholder="请手动录入快递单号" />
        </el-form-item>
        <el-form-item label="手动录入快递公司" prop="expressCompany">
          <el-input v-model="form.expressCompany" placeholder="请手动录入快递公司" />
        </el-form-item>
        <el-form-item label="商品打包长度" prop="goodsLength">
          <el-input v-model="form.goodsLength" placeholder="请输入商品打包长度" />
        </el-form-item>
        <el-form-item label="商品打包宽度" prop="goodsWidth">
          <el-input v-model="form.goodsWidth" placeholder="请输入商品打包宽度" />
        </el-form-item>
        <el-form-item label="商品打包高度" prop="goodsHeight">
          <el-input v-model="form.goodsHeight" placeholder="请输入商品打包高度" />
        </el-form-item>
        <el-form-item label="商品打包重量(kg)" prop="goodsWeight">
          <el-input v-model="form.goodsWeight" placeholder="请输入商品打包重量" />
        </el-form-item>
        <span style="color: red;">=======================手动输入快递单号时下面快递公司，运输方式等无需填写=======================</span>
        <el-form-item label="物流公司">
          <el-select v-model="form.expressId" @change="currStationChange()" placeholder="请选择物流公司" style="width: 100%;height: 10%;">
            <el-option v-for="item in dict.type.busi_express" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="运输方式代码" prop="shippingMethod">
          <el-select v-model="form.shippingMethod" filterable placeholder="请选择运输方式代码" width="100px" align="left">
            <el-option v-for="dict in dict.type.shipping_method" :key="dict.code || dict.value" :label="dict.cnname || dict.label" :value="dict.code || dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="海关hsCode" prop="hsCode">
          <el-input v-model="form.hsCode" placeholder="请输入海关编码" />
        </el-form-item>
        <el-form-item label="客户信息" prop="customerId">
          <el-select v-model="form.customerId" filterable clearable placeholder="请选择客户信息" style="width: 100%;height: 10%;" @change="handleCustomerChange">
            <el-option v-for="item in customerOptions" :key="item.id" :label="customerOptionLabel(item)" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <span style="color: red;">========================================核对客户数据========================================</span>
        <el-form-item label="收件人姓名" prop="consigneeName">
          <el-input v-model="form.consigneeName" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="收件人国家编码">
          <el-select v-model="form.consigneeCountryCode" placeholder="请选择收件人国家编码" style="width: 100%;height: 10%;">
            <el-option v-for="dict in dict.type.busi_country" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收件人州" prop="consigneeProvince">
          <el-input v-model="form.consigneeProvince" placeholder="请输入收件人州" />
        </el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity">
          <el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" />
        </el-form-item>
        <el-form-item label="收件人街道" prop="consigneeStreet">
          <el-input v-model="form.consigneeStreet" placeholder="请输入收件人街道" />
        </el-form-item>
        <el-form-item label="收件人门牌号" prop="consigneeHousenum">
          <el-input v-model="form.consigneeHousenum" placeholder="收件人门牌号"/>
        </el-form-item>
        <el-form-item label="收件人联系方式" prop="consigneeTelephone">
          <el-input v-model="form.consigneeTelephone" placeholder="请输入收件人联系方式" />
        </el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany">
          <el-input v-model="form.consigneeCompany" placeholder="请输入收件人公司名称" />
        </el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail">
          <el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" />
        </el-form-item>
        <el-form-item label="欧盟税改IOSS号" prop="iossNo">
          <el-input v-model="form.iossNo" placeholder="请输入欧盟税改IOSS号" />
        </el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity">
          <el-input v-model="form.invoiceQuantity" placeholder="请输入报关产品数量" />
        </el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName">
          <el-input v-model="form.invoiceEnName" placeholder="请输入报关英文名" />
        </el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName">
          <el-input v-model="form.invoiceCnName" placeholder="请输入报关中文名" />
        </el-form-item>
        <el-form-item label="单位code" prop="unitCode">
          <el-select v-model="form.unitCode" placeholder="请选择单位code" width="100px" align="left">
            <el-option v-for="dict in dict.type.unit" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品单价" prop="invoiceUnitCharge">
          <el-input v-model="form.invoiceUnitCharge" placeholder="请输入商品单价" />
        </el-form-item>
        <el-form-item label="是否带电" prop="charge">
          <el-switch v-model="form.charge" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="申报总价" prop="evaluate">
          <el-input v-model="form.evaluate" placeholder="请输入申报总价" />
        </el-form-item>
        <el-form-item label="备注" prop="invoiceNote">
          <el-input v-model="form.invoiceNote" placeholder="请输入备注" type="textarea" :autosize="{minRows: 2, maxRows: 2}" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">下 单</el-button>
        <el-button @click="cancelB">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus6" width="700x" append-to-body align="center" :before-close="cancel6">
      <el-form ref="form" :model="form" :rules="rules" label-width="300px">
        <span style="color: red;">==============================手动输入快递单号时无需对接第三方系统下单=======================</span>
        <el-form-item label="手动录入快递单号" prop="thirdPartyNo">
          <el-input v-model="form.thirdPartyNo" placeholder="请手动录入快递单号" />
        </el-form-item>
        <el-form-item label="手动录入快递公司" prop="expressCompany">
          <el-input v-model="form.expressCompany" placeholder="请手动录入快递公司" />
        </el-form-item>
        <el-form-item label="商品打包长度" prop="goodsLength">
          <el-input v-model="form.goodsLength" placeholder="请输入商品打包长度" />
        </el-form-item>
        <el-form-item label="商品打包宽度" prop="goodsWidth">
          <el-input v-model="form.goodsWidth" placeholder="请输入商品打包宽度" />
        </el-form-item>
        <el-form-item label="商品打包高度" prop="goodsHeight">
          <el-input v-model="form.goodsHeight" placeholder="请输入商品打包高度" />
        </el-form-item>
        <el-form-item label="商品打包重量(kg)" prop="goodsWeight">
          <el-input v-model="form.goodsWeight" placeholder="请输入商品打包重量" />
        </el-form-item>
        <el-form-item label="海关hsCode" prop="hsCode">
          <el-input v-model="form.hsCode" placeholder="请输入海关编码" />
        </el-form-item>
        <el-form-item label="客户信息" prop="customerId">
          <el-select v-model="form.customerId" filterable clearable placeholder="请选择客户信息" style="width: 100%;height: 10%;" @change="handleCustomerChange">
            <el-option v-for="item in customerOptions" :key="item.id" :label="customerOptionLabel(item)" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <span style="color: red;">========================================核对客户数据========================================</span>
        <el-form-item label="收件人姓名" prop="consigneeName">
          <el-input v-model="form.consigneeName" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="收件人国家编码">
          <el-select v-model="form.consigneeCountryCode" placeholder="请选择收件人国家编码" style="width: 100%;height: 10%;">
            <el-option v-for="dict in dict.type.busi_country" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收件人州" prop="consigneeProvince">
          <el-input v-model="form.consigneeProvince" placeholder="请输入收件人州" />
        </el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity">
          <el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" />
        </el-form-item>
        <el-form-item label="收件人街道" prop="consigneeStreet">
          <el-input v-model="form.consigneeStreet" placeholder="请输入收件人街道" />
        </el-form-item>
        <el-form-item label="收件人门牌号" prop="consigneeHousenum">
          <el-input v-model="form.consigneeHousenum" placeholder="收件人门牌号"/>
        </el-form-item>
        <el-form-item label="收件人联系方式" prop="consigneeTelephone">
          <el-input v-model="form.consigneeTelephone" placeholder="请输入收件人联系方式" />
        </el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany">
          <el-input v-model="form.consigneeCompany" placeholder="请输入收件人公司名称" />
        </el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail">
          <el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" />
        </el-form-item>
        <el-form-item label="欧盟税改IOSS号" prop="iossNo">
          <el-input v-model="form.iossNo" placeholder="请输入欧盟税改IOSS号" />
        </el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity">
          <el-input v-model="form.invoiceQuantity" placeholder="请输入报关产品数量" />
        </el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName">
          <el-input v-model="form.invoiceEnName" placeholder="请输入报关英文名" />
        </el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName">
          <el-input v-model="form.invoiceCnName" placeholder="请输入报关中文名" />
        </el-form-item>
        <el-form-item label="单位code" prop="unitCode">
          <el-select v-model="form.unitCode" placeholder="请选择单位code" width="100px" align="left">
            <el-option v-for="dict in dict.type.unit" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品单价" prop="invoiceUnitCharge">
          <el-input v-model="form.invoiceUnitCharge" placeholder="请输入商品单价" />
        </el-form-item>
        <el-form-item label="是否带电" prop="charge">
          <el-switch v-model="form.charge" :active-value="'Y'" :inactive-value="'N'"></el-switch>
        </el-form-item>
        <el-form-item label="申报总价" prop="evaluate">
          <el-input v-model="form.evaluate" placeholder="请输入申报总价" />
        </el-form-item>
        <el-form-item label="备注" prop="invoiceNote">
          <el-input v-model="form.invoiceNote" placeholder="请输入备注" type="textarea" :autosize="{minRows: 2, maxRows: 2}" />
        </el-form-item>
        <el-form-item label="采购图片" prop="purchaseUrl">
          <image-upload v-model="form.purchaseUrl" :limit="1" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm2">修 改</el-button>
        <el-button @click="cancel6">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openCutMoney" width="600px" append-to-body :before-close="cancelE">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item label="订单状态">
          <el-select v-model="form.status" disabled style="width: 100%;height: 10%;">
            <el-option v-for="dict in dict.type.order_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品打包长度" prop="goodsLength">
          <el-input v-model="form.goodsLength" placeholder="请输入商品打包长度" />
        </el-form-item>
        <el-form-item label="商品打包宽度" prop="goodsWidth">
          <el-input v-model="form.goodsWidth" placeholder="请输入商品打包宽度" />
        </el-form-item>
        <el-form-item label="商品打包高度" prop="goodsHeight">
          <el-input v-model="form.goodsHeight" placeholder="请输入商品打包高度" />
        </el-form-item>
        <el-form-item label="商品打包重量(kg)" prop="goodsWeight">
          <el-input v-model="form.goodsWeight" placeholder="请输入商品打包重量" />
        </el-form-item>
        <el-form-item label="打包费" prop="packageMoney">
          <el-input v-model="form.packageMoney" placeholder="请输入打包费" />
        </el-form-item>
        <el-form-item label="运费" prop="expressMoney">
          <el-input v-model="form.expressMoney" placeholder="请输入运费" />
        </el-form-item>
        <el-form-item label="盈利(对客户隐藏)" prop="profitMoney">
          <el-input v-model="form.profitMoney" placeholder="请输入盈利" />
        </el-form-item>
        <el-form-item label="税金" prop="taxes">
          <el-input v-model="form.taxes" placeholder="请输入税金" />
        </el-form-item>
        <el-form-item label="到付件" prop="arrivedMoney">
          <el-input v-model="form.arrivedMoney" placeholder="请输入到付件金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">扣 费</el-button>
        <el-button @click="cancelE">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus5" width="500x" append-to-body :before-close="cancelD">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="物流公司" readonly="true">
          <el-select v-model="form.express_id" placeholder="请选择物流公司" style="width: 100%;height: 10%;" readonly="true">
            <el-option v-for="dict in dict.type.busi_express" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收货人姓名" prop="consigneeName"><el-input v-model="form.consigneeName" placeholder="请输入收货人姓名" readonly="true"/></el-form-item>
        <el-form-item label="收货人国家编码">
          <el-select v-model="form.consigneeCountryCode" placeholder="请选择收货人国家编码" style="width: 100%;height: 10%;">
            <el-option v-for="item in dict.type.busi_country" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity"><el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" readonly="true"/></el-form-item>
        <el-form-item label="收货人街道" prop="consigneeStreet"><el-input v-model="form.consigneeStreet" placeholder="请输入收货人街道" readonly="true"/></el-form-item>
        <el-form-item label="收货人联系方式" prop="consigneeTelephone"><el-input v-model="form.consigneeTelephone" placeholder="请输入收货人联系方式" readonly="true"/></el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany"><el-input v-model="form.consigneeCompany" placeholder="请输入收件人公司名称" readonly="true"/></el-form-item>
        <el-form-item label="收件人公司" prop="consigneeProvince"><el-input v-model="form.consigneeProvince" placeholder="请输入收件人公司" readonly="true"/></el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail"><el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" readonly="true"/></el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity"><el-input v-model="form.invoiceQuantity" placeholder="请输入报关产品数量" readonly="true"/></el-form-item>
        <el-form-item label="运输方式代码" prop="shippingMethod"><el-input v-model="form.shippingMethod" placeholder="请输入运输方式代码" readonly="true"/></el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName"><el-input v-model="form.invoiceEnName" placeholder="请输入报关英文名" readonly="true"/></el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName"><el-input v-model="form.invoiceCnName" placeholder="请输入报关中文名" readonly="true"/></el-form-item>
        <el-form-item label="单位code" prop="unitCode"><el-input v-model="form.unitCode" placeholder="请输入单位code" readonly="true"/></el-form-item>
        <el-form-item label="单价(单个商品价格)" prop="invoiceUnitCharge"><el-input v-model="form.invoiceUnitCharge" placeholder="请输入单价(单个商品价格)" readonly="true"/></el-form-item>
        <el-form-item label="备注" prop="invoiceNote"><el-input v-model="form.invoiceNote" placeholder="请输入备注" readonly="true"/></el-form-item>
        <el-form-item label="商品打包长度" prop="goodsLength"><el-input v-model="form.goodsLength" placeholder="请输入商品打包长度" readonly="true"/></el-form-item>
        <el-form-item label="商品打包宽度" prop="goodsWidth"><el-input v-model="form.goodsWidth" placeholder="请输入商品打包宽度" readonly="true"/></el-form-item>
        <el-form-item label="商品打包高度" prop="goodsHeight"><el-input v-model="form.goodsHeight" placeholder="请输入商品打包高度" readonly="true"/></el-form-item>
        <el-form-item label="商品打包重量" prop="goodsHeight"><el-input v-model="form.goodsWeight" placeholder="请输入商品打包重量" readonly="true"/></el-form-item>
        <el-form-item label="打包费" prop="packageMoney"><el-input v-model="form.packageMoney" placeholder="请输入打包费" readonly="true"/></el-form-item>
        <el-form-item label="运费" prop="expressMoney"><el-input v-model="form.expressMoney" placeholder="请输入运费" readonly="true"/></el-form-item>
        <el-form-item label="盈利(对客户隐藏)" prop="profitMoney"><el-input v-model="form.profitMoney" placeholder="请输入盈利" readonly="true"/></el-form-item>
        <el-form-item label="税金" prop="taxes"><el-input v-model="form.taxes" readonly="true"/></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button @click="cancelD">关 闭</el-button></div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openFaceInfo" width="600px" append-to-body :before-close="cancelFaceInfo">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-col :span="2000">
          <el-form-item label="面单详情" prop="rejectRemark" width="300px">
            <a :href="printImageUrl" target="_blank">点击下载</a>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button @click="cancelFaceInfo">关 闭</el-button></div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus4" width="600px" append-to-body :before-close="cancelC">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-col :span="2000">
          <el-form-item label="驳回原因" prop="rejectRemark" width="300px">
            <el-input v-model="form.rejectRemark" placeholder="请输入驳回原因" :maxlength="100" type="textarea" :autosize="{minRows: 2, maxRows: 2}"/>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelC">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="业务轨迹" :visible.sync="openOrderEvents" width="700px" append-to-body>
      <el-table :data="orderEvents">
        <el-table-column label="事件类型" prop="eventType" />
        <el-table-column label="标题" prop="title" />
        <el-table-column label="前状态" prop="beforeStatus" />
        <el-table-column label="后状态" prop="afterStatus" />
        <el-table-column label="详情" prop="detail" />
        <el-table-column label="时间" prop="createdDate" width="180">
          <template slot-scope="scope"><span>{{ parseTime(scope.row.createdDate) }}</span></template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer"><el-button @click="openOrderEvents = false">关 闭</el-button></div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openExpressMoney" width="300px" append-to-body :before-close="cancelExpressMoney">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="运费" prop="expressMoney"><el-input v-model="form.expressMoney" readonly="true" /></el-form-item>
        <el-form-item label="打包费" prop="packageMoney"><el-input v-model="form.packageMoney" readonly="true" /></el-form-item>
        <el-form-item label="总费用" prop="expressMoney"><el-input v-model="form.expressMoney+form.packageMoney+form.taxes+form.arrivedMoney" readonly="true" /></el-form-item>
        <el-form-item label="税金" prop="taxes"><el-input v-model="form.taxes" readonly="true" /></el-form-item>
        <el-form-item label="到付件" prop="arrivedMoney"><el-input v-model="form.arrivedMoney" readonly="true" /></el-form-item>
        <el-form-item label="货品打包长度" prop="goodsLength"><el-input v-model="form.goodsLength" readonly="true" /></el-form-item>
        <el-form-item label="货品打包宽度" prop="goodsWidth"><el-input v-model="form.goodsWidth" readonly="true"/></el-form-item>
        <el-form-item label="货品打包高度" prop="goodsHeight"><el-input v-model="form.goodsHeight" readonly="true" /></el-form-item>
        <el-form-item label="货品打包重量(kg)" prop="goodsWeight"><el-input v-model="form.goodsWeight" readonly="true" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button @click="cancelExpressMoney()">关 闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, printInfo, getShippingMethod, operateOrder, listOrderEvents } from "@/api/order/order";
import { listCustomer } from "@/api/express/customer";
import { getToken } from "@/utils/auth";

export default {
  name: "Order",
  dicts: ['order_type','product','order_status','busi_express','busi_country','shipping_method','unit'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      orderEvents: [],
      openOrderEvents: false,
      customerOptions: [],
      busiExpressOptions: [],
      busiCountryOptions: [],
      title: "",
      open: false,
      openStatus2: false,
      openStatus3: false,
      openStatus4: false,
      openStatus5: false,
      openStatus6: false,
      openCutMoney: false,
      openFaceInfo: false,
      openExpressMoney: false,
      printImageUrl: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        custId: null,
        orderNo: null,
        type: null,
        purchaseNo: null,
        purchaseSource: null,
        purchaseUrl: null,
        deliveryRemark: null,
        deliveryNo: null,
        thirdPartyNo: null,
        status: null,
        productId: null,
        customerId: null,
        productNums: null,
        faceUrl: null,
        expressId: null,
        countryName: null,
        countryEname: null,
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null
      },
      form: { type: undefined, rejectRemark: undefined },
      rules: {
        rejectRemark: [{ required: true, message: '请输入多行文本', trigger: 'blur' }],
      },
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        type: 2,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/order/order/importData"
      },
    };
  },
  created() {
    this.getList();
    this.loadCustomerOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      this.queryParams.type = 2;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadCustomerOptions() {
      listCustomer({ pageNum: 1, pageSize: 999 }).then(response => {
        this.customerOptions = response.rows || [];
      });
    },
    customerOptionLabel(item) {
      return `${item.addressee || ''}${item.company ? ' / ' + item.company : ''}${item.country ? ' / ' + item.country : ''}`;
    },
    handleCustomerChange(customerId) {
      const customer = this.customerOptions.find(item => item.id === customerId);
      if (!customer) {
        return;
      }
      this.form.addressLibrary = customer.id ? String(customer.id) : this.form.addressLibrary;
      this.form.consigneeName = customer.addressee || this.form.consigneeName;
      this.form.consigneeCompany = customer.company || this.form.consigneeCompany;
      this.form.consigneeStreet = customer.address || this.form.consigneeStreet;
      this.form.consigneeCity = customer.city || this.form.consigneeCity;
      this.form.consigneeProvince = customer.states || this.form.consigneeProvince;
      this.form.consigneeMail = customer.zipCode || this.form.consigneeMail;
      this.form.consigneeTelephone = customer.tel || this.form.consigneeTelephone;
      this.form.countryName = customer.country || this.form.countryName;
      const country = (this.dict.type.busi_country || []).find(item => item.label === customer.country || item.value === customer.country);
      this.form.consigneeCountryCode = country ? country.value : this.form.consigneeCountryCode;
      this.form.expressId = customer.expressId || this.form.expressId;
      this.currStationChange();
    },
    selectOrderType() {
      this.orderType = 'packageSend';
      if (this.form.type == '2') {
        this.orderType = 'agentSend';
      }
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelFaceInfo() {
      this.openFaceInfo = false;
      this.reset();
    },
    cancelA() {
      this.openStatus2 = false;
      this.reset();
    },
    cancelB() {
      this.openStatus3 = false;
      this.reset();
    },
    cancel6() {
      this.openStatus6 = false;
      this.reset();
    },
    cancelC() {
      this.openStatus4 = false;
      this.reset();
    },
    cancelD() {
      this.openStatus5 = false;
      this.reset();
    },
    cancelE() {
      this.openCutMoney = false;
      this.reset();
    },
    cancelExpressMoney() {
      this.openExpressMoney = false;
      this.reset();
    },
    handleImport() {
      this.upload.title = "订单导入";
      this.upload.type = 2;
      this.upload.open = true;
    },
    importTemplate() {
      this.download('order/order/importTemplate', {}, `order_template_${new Date().getTime()}.xlsx`)
    },
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleFileUploadProgress() {
      this.upload.isUploading = true;
    },
    handleFileSuccess(response) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    reset() {
      this.form = {
        id: null,
        custId: null,
        orderNo: null,
        type: 2,
        purchaseNo: null,
        purchaseSource: null,
        purchaseUrl: null,
        deliveryRemark: null,
        deliveryNo: null,
        thirdPartyNo: null,
        status: null,
        custSendNums: null,
        productId: null,
        customerId: null,
        productNums: null,
        faceUrl: null,
        expressId: null,
        express_id: null,
        expressCompany: null,
        shippingMethod: null,
        countryName: null,
        countryEname: null,
        consigneeName: null,
        consigneeCompany: null,
        consigneeStreet: null,
        consigneeCity: null,
        consigneeProvince: null,
        consigneeHousenum: null,
        consigneeMail: null,
        consigneeCountryCode: null,
        consigneeTelephone: null,
        invoiceCnName: null,
        invoiceEnName: null,
        unitCode: null,
        invoiceUnitCharge: null,
        invoiceQuantity: null,
        invoiceNote: null,
        charge: 'N',
        magnetism: 'N',
        liquid: 'N',
        powder: 'N',
        danger: 'N',
        evaluate: null,
        iossNo: null,
        hsCode: null,
        goodsLength: null,
        goodsWidth: null,
        goodsHeight: null,
        goodsWeight: null,
        packageMoney: null,
        expressMoney: null,
        profitMoney: null,
        taxes: null,
        arrivedMoney: null,
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null
      };
      this.dict.type.shipping_method = [];
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    purchase_urlBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 2MB')
      }
      return isRightSize
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    currStationChange() {
      if (!this.form.expressId) {
        this.dict.type.shipping_method = [];
        this.form.shippingMethod = null;
        return;
      }
      const id = this.form.expressId
      getShippingMethod(id).then(response => {
        this.dict.type.shipping_method = response.data || [];
        if (this.form.shippingMethod) {
          const exists = this.dict.type.shipping_method.some(item => (item.code || item.value) === this.form.shippingMethod);
          if (!exists) {
            this.form.shippingMethod = null;
          }
        }
      });
    },
    expressMoneyInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openExpressMoney = true;
        this.title = "运费明细";
      });
    },
    handleCutMoney(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openCutMoney = true;
        this.title = "订单扣费";
      });
    },
    handleUpdate1(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus2 = true;
        this.title = "订单入库";
      });
    },
    handleUpdate2(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.busiExpressOptions = response.busiExpresses;
        this.busiCountryOptions = response.countryListVos;
        this.openStatus3 = true;
        this.title = "订单发货";
      });
    },
    handleUpdate3(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus6 = true;
        this.title = "订单修改";
      });
    },
    handleUpdateReject(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus4 = true;
        this.title = "订单驳回";
      });
    },
    handleUpdateFaceInfo(row) {
      this.reset();
      const id = row.id || this.ids
      printInfo(id).then(response => {
        this.printImageUrl = response.data;
        this.openFaceInfo = true;
      });
    },
    handleOrderInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = { ...response.data, express_id: response.data.expressId };
        this.openStatus5 = true;
        this.title = "订单配送详情";
      });
    },
    handleOrderEvents(row) {
      listOrderEvents(row.id).then(response => {
        this.orderEvents = response.data || [];
        this.openOrderEvents = true;
      });
    },
    markIssue(row) {
      operateOrder({
        id: row.id,
        issueType: 'customer_service_issue',
        issueStatus: 'issue_processing',
        checkStatus: 'check_failed',
        checkSummary: row.rejectRemark || '已标记为问题件'
      }).then(() => {
        this.$modal.msgSuccess('已标记为问题件');
        this.getList();
      });
    },
    approveRelease(row) {
      operateOrder({
        id: row.id,
        releaseStatus: 'release_approved',
        issueStatus: 'issue_resolved',
        checkStatus: 'check_passed',
        checkSummary: '已放行'
      }).then(() => {
        this.$modal.msgSuccess('已放行');
        this.getList();
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            (() => {
              if (this.openStatus2) {
                return operateOrder({ id: this.form.id, status: '1', warehouseSendNums: this.form.warehouseSendNums });
              }
              if (this.openStatus3) {
                return operateOrder({ ...this.form, status: '2' });
              }
              if (this.openStatus4) {
                return operateOrder({ id: this.form.id, status: '4', rejectRemark: this.form.rejectRemark });
              }
              return updateOrder(this.form);
            })().then(() => {
              this.$modal.msgSuccess("修改成功");
              this.openStatus2 = false;
              this.openStatus3 = false;
              this.openStatus4 = false;
              this.openCutMoney = false;
              this.getList();
            });
          } else {
            this.form.type = 2;
            addOrder(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    submitForm2() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.rejectRemark = '修改订单';
            updateOrder(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.openStatus6 = false;
              this.getList();
            });
          } else {
            addOrder(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + ids + '"的数据项？').then(function() {
        return delOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('order/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

