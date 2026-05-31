<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="订单状态" clearable>
          <el-option
            v-for="dict in dict.type.order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="快递公司" prop="expressId">
        <el-select v-model="queryParams.expressId" placeholder="快递公司" clearable>
          <el-option
            v-for="dict in dict.type.busi_express"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:order:add']">新增订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['order:order:export']">导出</el-button>
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
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="订单状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.order_status"
            :value="scope.row.status"
            :style="{ color: scope.row.status == '1' ? '#0CB618' : '#EA1B29' }"
          />
        </template>
      </el-table-column>
      <el-table-column label="快递单号" align="center" prop="thirdPartyNo">
        <template slot-scope="scope">
          <a :href="'https://t.17track.net/zh-cn#nums=' + scope.row.thirdPartyNo" target="_blank" style="color: blue;" title="跳转外部链接查询订单轨迹">{{ scope.row.thirdPartyNo }}</a>
        </template>
      </el-table-column>
      <el-table-column label="采购单号" align="center" prop="purchaseNo" />
      <el-table-column label="生成单号" align="center" prop="deliveryNo" />
      <el-table-column label="商品数量" align="center" prop="custSendNums" />
      <el-table-column label="仓库发货数" align="center" prop="warehouseSendNums" />
      <el-table-column label="运费" align="center" prop="expressMoney">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-show" @click="ExpressMoneyInfo(scope.row)" v-hasPermi="['order:order:add']">运费明细</el-button>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center">
        <template slot-scope="scope">
          <span :style="{ color: '#FF0000' }" v-if="scope.row.status == '3' || scope.row.status == '5'">{{ scope.row.totalMoney }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="invoiceNote" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleOrderInfo(scope.row)" v-hasPermi="['order:order:edit']">订单详情</el-button>
          <el-button v-if="scope.row.status == '1'" size="mini" type="text" icon="el-icon-edit" @click="handleOrderUpdate(scope.row)" v-hasPermi="['order:order:edit']">订单修改</el-button>
          <el-button v-if="scope.row.status == '4'" size="mini" type="text" icon="el-icon-edit" @click="handleReject(scope.row)" v-hasPermi="['order:order:edit']">驳回原因</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

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
          <el-select v-model="form.expressId" placeholder="请选择快递公司" clearable>
            <el-option v-for="dict in dict.type.busi_express" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="运输方式代码" prop="shippingMethod">
          <el-input v-model="form.shippingMethod" placeholder="请输入运输方式代码" />
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
        <el-form-item label="上传" prop="purchaseUrl">
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
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
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

    <el-dialog :title="title" :visible.sync="openStatus5" width="500x" append-to-body :before-close="cancelD">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="收货人姓名" prop="consigneeName"><el-input v-model="form.consigneeName" readonly="true"/></el-form-item>
        <el-form-item label="收货人国家编码"><el-select v-model="form.consigneeCountryCode" style="width: 100%;height: 10%;"><el-option v-for="dict in dict.type.busi_country" :key="dict.value" :label="dict.label" :value="dict.value"/></el-select></el-form-item>
        <el-form-item label="收件人州" prop="consigneeProvince"><el-input v-model="form.consigneeProvince" readonly="true"/></el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity"><el-input v-model="form.consigneeCity" readonly="true"/></el-form-item>
        <el-form-item label="收货人地址" prop="consigneeStreet"><el-input v-model="form.consigneeStreet" readonly="true"/></el-form-item>
        <el-form-item label="收货人联系方式" prop="consigneeTelephone"><el-input v-model="form.consigneeTelephone" readonly="true"/></el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany"><el-input v-model="form.consigneeCompany" readonly="true"/></el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail"><el-input v-model="form.consigneeMail" readonly="true"/></el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity"><el-input v-model="form.invoiceQuantity" readonly="true"/></el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName"><el-input v-model="form.invoiceEnName" readonly="true"/></el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName"><el-input v-model="form.invoiceCnName" readonly="true"/></el-form-item>
        <el-form-item label="单位code" prop="unitCode"><el-input v-model="form.unitCode" readonly="true"/></el-form-item>
        <el-form-item label="商品单价" prop="invoiceUnitCharge"><el-input v-model="form.invoiceUnitCharge" readonly="true"/></el-form-item>
        <el-form-item label="备注" prop="invoiceNote"><el-input v-model="form.invoiceNote" readonly="true" type="textarea" :autosize="{minRows: 2, maxRows: 2}" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button @click="cancelD">关 闭</el-button></div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openStatus6" width="500x" append-to-body :before-close="cancelE">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="订单状态"><el-select v-model="form.status" disabled style="width: 100%;height: 10%;"><el-option v-for="dict in dict.type.order_status" :key="dict.value" :label="dict.label" :value="dict.value"/></el-select></el-form-item>
        <el-form-item label="收货人姓名" prop="consigneeName"><el-input v-model="form.consigneeName" /></el-form-item>
        <el-form-item label="收货人国家编码"><el-select v-model="form.consigneeCountryCode" style="width: 100%;height: 10%;"><el-option v-for="dict in dict.type.busi_country" :key="dict.value" :label="dict.label" :value="dict.value"/></el-select></el-form-item>
        <el-form-item label="收件人州" prop="consigneeProvince"><el-input v-model="form.consigneeProvince" /></el-form-item>
        <el-form-item label="收件人城市" prop="consigneeCity"><el-input v-model="form.consigneeCity" /></el-form-item>
        <el-form-item label="收货人地址" prop="consigneeStreet"><el-input v-model="form.consigneeStreet" /></el-form-item>
        <el-form-item label="收货人联系方式" prop="consigneeTelephone"><el-input v-model="form.consigneeTelephone" /></el-form-item>
        <el-form-item label="收件人公司名称" prop="consigneeCompany"><el-input v-model="form.consigneeCompany" /></el-form-item>
        <el-form-item label="收件人邮编" prop="consigneeMail"><el-input v-model="form.consigneeMail" /></el-form-item>
        <el-form-item label="欧盟税改IOSS号" prop="iossNo"><el-input v-model="form.iossNo" /></el-form-item>
        <el-form-item label="报关产品数量" prop="invoiceQuantity"><el-input v-model="form.invoiceQuantity" /></el-form-item>
        <el-form-item label="报关英文名" prop="invoiceEnName"><el-input v-model="form.invoiceEnName" /></el-form-item>
        <el-form-item label="报关中文名" prop="invoiceCnName"><el-input v-model="form.invoiceCnName" /></el-form-item>
        <el-form-item label="商品单价" prop="invoiceUnitCharge"><el-input v-model="form.invoiceUnitCharge" /></el-form-item>
        <el-form-item label="单位code" prop="unitCode"><el-select v-model="form.unitCode" width="100px" align="left"><el-option v-for="dict in dict.type.unit" :key="dict.value" :label="dict.label" :value="dict.value"></el-option></el-select></el-form-item>
        <el-form-item label="申报总价" prop="evaluate"><el-input v-model="form.evaluate" /></el-form-item>
        <el-form-item label="备注" prop="invoiceNote"><el-input v-model="form.invoiceNote" :maxlength="100" type="textarea" :autosize="{minRows: 2, maxRows: 2}"/></el-form-item>
        <el-form-item label="采购图片" prop="purchaseUrl"><image-upload v-model="form.purchaseUrl" :limit="1" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelE">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openReject" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-col :span="2000">
          <el-form-item label="驳回原因" prop="rejectRemark" width="300px">
            <el-input v-model="form.rejectRemark" placeholder="请输入驳回原因" :maxlength="100" type="textarea" :autosize="{minRows: 2, maxRows: 2}"/>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button @click="cancelReject()">关 闭</el-button></div>
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
import { listOrder, getOrder, delOrder, addOrder, updateOrder} from "@/api/order/user_order";
import { getToken } from "@/utils/auth";

export default {
  name: "Order",
  dicts: ['order_type','order_status','busi_express','busi_country','unit'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      title: "",
      open: false,
      openStatus5: false,
      openStatus6: false,
      openReject: false,
      openExpressMoney: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        status: null,
        expressId: null,
        purchaseNo: null,
        orderNo: null,
        deliveryNo: null,
        thirdPartyNo: null,
      },
      form: {},
      rules: {},
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/order/order/importData"
      },
    };
  },
  created() {
    this.getList();
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
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelD() {
      this.openStatus5 = false;
      this.reset();
    },
    cancelE() {
      this.openStatus6 = false;
      this.reset();
    },
    cancelReject() {
      this.openReject = false;
      this.reset();
    },
    handleImport() {
      this.upload.title = "订单导入";
      this.upload.open = true;
    },
    importTemplate() {
      this.download('order/order/importTemplate', {}, `order_template_${new Date().getTime()}.xlsx`)
    },
    cancelExpressMoney() {
      this.openExpressMoney = false;
      this.reset();
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
        faceUrl: null,
        expressId: null,
        shippingMethod: null,
        countryName: null,
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
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null
      };
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增订单";
    },
    handleOrderInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus5 = true;
        this.title = "订单配送详情";
      });
    },
    handleOrderUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus6 = true;
        this.title = "订单修改";
      });
    },
    handleReject(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openReject = true;
        this.title = "订单驳回原因";
      });
    },
    ExpressMoneyInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openExpressMoney = true;
        this.title = "运费明细";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrder(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.openStatus6 = false;
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
