<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
     <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['order:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户单号" align="center" prop="deliveryNo" />
	  <el-table-column label="服务类型" align="center" prop="serverType" />
	  <el-table-column label="门店" align="center" prop="store" />
	  <el-table-column label="EORI" align="center" prop="eori" />
	  <el-table-column label="VAT号" align="center" prop="vatNo" />
	  <el-table-column label="IOSS号" align="center" prop="iossNo" />
	  
      <el-table-column label="订单状态" align="center" prop="status">
		  <template slot-scope="scope">
		    <dict-tag :options="dict.type.order_status" :value="scope.row.status"
					:style="{
					                        color:
					                            scope.row.status == '1'
					                                ? '#0CB618'
					                                : '#EA1B29',
					                    }"
			
			/>
		  </template>
	  </el-table-column>
	<el-table-column label="仓库发货数" align="center" prop="warehouseSendNums" />
<!--      <el-table-column label="下单时间" align="center" prop="createdDate" width="180">
        <template slot-scope="scope">
           <p>{{ parseTime(scope.row.createdDate, '{h}:{i}:{s}') }}</p>
            <span>{{ parseTime(scope.row.createdDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" align="center" prop="updatedDate" width="180">
        <template slot-scope="scope">
          <p>{{ parseTime(scope.row.updatedDate, '{h}:{i}:{s}') }}</p>
           <span>{{ parseTime(scope.row.updatedDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="下单人" align="center" prop="createdBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
         <el-button
			v-if="scope.row.status == '1'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate1(scope.row)"
            v-hasPermi="['order:order:edit']"
          >订单入库</el-button>
		  <el-button
		     size="mini"
		     type="text"
		     icon="el-icon-edit"
		     @click="handleUpdate3(scope.row)"
		     v-hasPermi="['order:order:edit']"
		   >订单修改</el-button>
		  <el-button
			 v-if="scope.row.status == '2'"
		     size="mini"
		     type="text"
		     icon="el-icon-edit"
		     @click="handleUpdate2(scope.row)"
		     v-hasPermi="['order:order:edit']"
		   >订单发货</el-button>
		 <el-button
		 	v-if="scope.row.status == '3' || scope.row.status == '5'"
		    size="mini"
		    type="text"
		    icon="el-icon-edit"
		    @click="handleCutMoney(scope.row)"
		    v-hasPermi="['order:order:edit']"
		  >订单扣费</el-button>
			<el-button
				size="mini"
				type="text"
				icon="el-icon-delete"
				@click="handleDelete(scope.row)"
				v-hasPermi="['order:order:remove']"
			>删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="订单类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio
            v-for="dict in dict.type.order_type"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--打包订单 begin-->
        <div id="addPackage" v-if="form.type=='1' || !form.type">

          <el-form-item label="选择产品" prop="type">
            <el-radio-group v-model="form.product">
              <el-radio
                v-for="dict in dict.type.product"
                :key="dict.value"
                :label="dict.value"
              >{{dict.label}}</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="采购单号" prop="purchaseNo">
            <el-input v-model="form.purchaseNo" placeholder="请输入采购单号" />
          </el-form-item>

          <el-form-item label="采购来源" prop="purchaseSource">
            <el-input v-model="form.purchaseSource" placeholder="请输入采购来源" />
          </el-form-item>
          <el-form-item label="备注说明" prop="deliveryRemark">
            <el-input v-model="form.deliveryRemark" placeholder="请输入配送备注" />
          </el-form-item>
          <el-form-item label="配送单号" prop="deliveryNo">
            <el-input v-model="form.deliveryNo" placeholder="请输入配送单号" />
          </el-form-item>
        </div>
        <!--打包订单 end-->

        <!--代发订单 begin-->
        <div id="addAgentSend" v-if="form.type=='2'">
          <el-form-item label="代发单号" prop="purchaseNo">
            <el-input v-model="form.purchaseNo" placeholder="请输入采购单号" />
          </el-form-item>

          <el-form-item label="配送单号" prop="deliveryNo">
            <el-input v-model="form.deliveryNo" placeholder="请输入配送单号" />
          </el-form-item>
        </div>
       <!--代发订单 end-->
        </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
	
	<!-- 入库 -->
	<el-dialog :title="title" :visible.sync="openStatus2" width="520px" append-to-body :before-close="cancelA">
		<el-form ref="form" :model="form" :rules="rules" label-width="120px">

			  <el-form-item label="仓库发货数" prop="warehouseSendNums">
				<el-input-number v-model="form.warehouseSendNums" controls-position="right" :min="1" align="center"/>
			  </el-form-item>
		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm">确 定</el-button>
	    <el-button @click="cancelA">取 消</el-button>
	  </div>
	</el-dialog>
	
	<!-- 发货 -->
	<el-dialog :title="title" :visible.sync="openStatus3" width="520x" append-to-body :before-close="cancelB">
	  <el-form ref="form" :model="form" :rules="rules" label-width="100px">
		  
		  <el-form-item label="发货数量" prop="warehouseSendNums">
		  				<el-input-number v-model="form.warehouseSendNums" controls-position="right" :min="1" align="center"/>
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

		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm">下 单</el-button>
	    <el-button @click="cancelB">取 消</el-button>
	  </div>
	</el-dialog>
	
	<!-- 订单扣费 -->
	<el-dialog :title="title" :visible.sync="openCutMoney" width="600px" append-to-body :before-close="cancelE">
	  <el-form ref="form" :model="form" :rules="rules" label-width="200px">
		  
			<el-form-item label="订单状态">
			 			<el-select v-model="form.status"  disabled style="width: 100%;height: 10%;">
			 			  <el-option
			 				v-for="dict in dict.type.order_status"
			 				:key="dict.value"
			 				:label="dict.label"
			 				:value="dict.value"
			 			  ></el-option>
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
	
	<!-- 订单修改 -->
	<el-dialog :title="title" :visible.sync="openStatus6" width="700x" append-to-body align="center" :before-close="cancel6">
	  <el-form ref="form" :model="form" :rules="rules" label-width="300px">
		  
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
		  
		  <span style="color: red;">========================================核对客户数据========================================</span>
		  <!--收货人信息-->
									<el-form-item label="客户单号" prop="deliveryNo">
		  								<el-input v-model="form.deliveryNo" placeholder="请输入客户单号" />
									</el-form-item>
		  							<el-form-item label="服务类型" prop="serverType">
		  								<el-input v-model="form.serverType" placeholder="请输入服务类型" />
		  							</el-form-item>
		  							<el-form-item label="地址库" prop="addressLibrary">
		  								<el-input v-model="form.addressLibrary" placeholder="请输入地址库" />
		  							</el-form-item>
		  							<el-form-item label="客户信息" prop="customerId">
		  							  <el-select v-model="form.customerId" filterable clearable placeholder="请选择客户信息" style="width: 100%;height: 10%;" @change="handleCustomerChange">
		  							    <el-option
		  							      v-for="item in customerOptions"
		  							      :key="item.id"
		  							      :label="customerOptionLabel(item)"
		  							      :value="item.id"
		  							    ></el-option>
		  							  </el-select>
		  							</el-form-item>
									<el-form-item label="收件人" prop="consigneeName">
									<el-input v-model="form.consigneeName" placeholder="请输入收件人" />
									</el-form-item>
									<el-form-item label="公司" prop="consigneeCompany">
		  								<el-input v-model="form.consigneeCompany" placeholder="请输入公司" />
									</el-form-item>
									<el-form-item label="地址一" prop="addressOno">
		  								<el-input v-model="form.addressOno" placeholder="请输入地址一" />
									</el-form-item>
		  							<el-form-item label="地址二" prop="addressTwo">
		  								<el-input v-model="form.addressTwo" placeholder="请输入地址一" />
		  							</el-form-item>
		  							<el-form-item label="地址三" prop="addressThree">
		  								<el-input v-model="form.addressThree" placeholder="请输入地址一" />
		  							</el-form-item>
		  							<el-form-item label="城市" prop="consigneeCity">
		  								<el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" />
		  							</el-form-item>
		  							<el-form-item label="省份/州" prop="consigneeProvince">
		  								<el-input v-model="form.consigneeProvince" placeholder="请输入收件人省份/州" />
		  							</el-form-item>
		  							<el-form-item label="收件人邮编" prop="consigneeMail">
		  								<el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" />
		  							</el-form-item>
		  							<el-form-item label="国家" prop="countryName">
		  								<el-input v-model="form.countryName" placeholder="请输入国家" />
		  							</el-form-item>
		  							<el-form-item label="电话" prop="consigneeTelephone">
		  								<el-input v-model="form.consigneeTelephone" placeholder="请输入电话" />
		  							</el-form-item>
		  							<el-form-item label="邮箱" prop="email">
		  								<el-input v-model="form.email" placeholder="请输入邮箱" />
		  							</el-form-item>
		  							<el-form-item label="店铺" prop="store">
		  								<el-input v-model="form.store" placeholder="请输入店铺" />
		  							</el-form-item>
		  							<el-form-item label="参考号一" prop="referenceOne">
		  								<el-input v-model="form.referenceOne" placeholder="请输入参考号一" />
		  							</el-form-item>
		  							<el-form-item label="参考号二" prop="referenceTwo">
		  								<el-input v-model="form.referenceTwo" placeholder="请输入参考号二" />
		  							</el-form-item>
		  							<el-form-item label="备注" prop="invoiceNote">
		  								<el-input v-model="form.invoiceNote" placeholder="请输入备注" />
		  							</el-form-item>
		  							<el-form-item label="VAT号" prop="vatNo">
		  								<el-input v-model="form.vatNo" placeholder="请输入VAT号" />
		  							</el-form-item>
		  							<el-form-item label="IOSS号" prop="iossNo">
		  								<el-input v-model="form.iossNo" placeholder="请输入IOSS号" />
		  							</el-form-item>
		  							<el-form-item label="EORI" prop="eori">
		  								<el-input v-model="form.eori" placeholder="请输入EORI" />
		  							</el-form-item>
		  							<el-form-item label="申报币种">
		  						 			<el-select v-model="form.invoiceCurrencycode" placeholder="请选择申报币种" style="width: 100%;height: 10%;">
		  						 			  <el-option
		  						 				v-for="dict in dict.type.invoice_currencycode"
		  						 				:key="dict.value"
		  						 				:label="dict.label"
		  						 				:value="dict.value"
		  						 			  ></el-option>
		  						 			</el-select>
		  							</el-form-item>
		  							<el-form-item label="物品属性">
		  									<el-select v-model="form.attribute" placeholder="请选择物品属性" style="width: 100%;height: 10%;">
		  									  <el-option
		  										v-for="dict in dict.type.attribute"
		  										:key="dict.value"
		  										:label="dict.label"
		  										:value="dict.value"
		  									  ></el-option>
		  									</el-select>
		  							</el-form-item>
		  							<el-form-item label="购买保险">
		  									<el-select v-model="form.payInsurance" placeholder="购买保险" style="width: 100%;height: 10%;">
		  									  <el-option
		  										v-for="dict in dict.type.pay_insurance"
		  										:key="dict.value"
		  										:label="dict.label"
		  										:value="dict.value"
		  									  ></el-option>
		  									</el-select>
		  							</el-form-item>
		  							<el-form-item label="保价" prop="insured">
		  								<el-input v-model="form.insured" placeholder="请输入保价" />
		  							</el-form-item>
		</el-form>
		
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm2">修 改</el-button>
	    <el-button @click="cancel6">取 消</el-button>
	  </div>
	</el-dialog>
	
	<!-- 驳回 -->
<!-- 	<el-dialog :title="title" :visible.sync="openStatus4" width="600px" append-to-body :before-close="cancelC">
	  <el-form ref="form" :model="form" :rules="rules" label-width="200px">
		  
		   <el-col :span="2000">
		              <el-form-item label="驳回原因" prop="rejectRemark" width="300px">
						  <el-input v-model="form.rejectRemark" placeholder="请输入驳回原因" :maxlength="100" type="textarea" 
						  :autosize="{minRows: 2, maxRows: 2}"/>
		              </el-form-item>
		    </el-col> 
		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm">确 定</el-button>
	    <el-button @click="cancelC">取 消</el-button>
	  </div>
	</el-dialog> -->
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, operateOrder } from "@/api/order/order";
import { listCustomer } from "@/api/express/customer";
import { Form } from "element-ui";

export default {
  name: "Order",
  dicts: ['order_type','product','order_status','busi_express','busi_country'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单表格数据
      orderList: [],
      customerOptions: [],
	  // 快递选项
	  busiExpressOptions: [],
	  // 国家选项
	  busiCountryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
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
        status: null,
        productId: null,
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
      // 表单参数
      form: {express_id: undefined,type: undefined,rejectRemark: undefined,},
      // 表单校验
      rules: {
		  rejectRemark: [{
		            required: true,
		            message: '请输入多行文本',
		            trigger: 'blur'
		          }],
		  express_id: [{
		            required: true,
		            message: '请选择物流公司',
		            trigger: 'change'
		          }],
				    type: [{
				            required: true,
				            message: '请选择订单类型',
				            trigger: 'change'
				          }],
						  express_idOptions: [{
						         "label": "选项一",
						         "value": 1,
						       }, {
						         "label": "选项二",
						         "value": 2
						       }],
						   type: [{
						    required: true,
						    message: '请选择订单类型',
						    trigger: 'change'
						  }],
						   typeOptions: [{
						          "label": "打包订单",
						          "value": 1,
						        }, {
						          "label": "代发订单",
						          "value": 2
						        }],
							rejectRemark: [],
      },
    };
  },
  created() {
    this.getList();
    this.loadCustomerOptions();
  },
  methods: {
    /** 查询订单列表 */
    getList() {
      this.loading = true;
	  this.queryParams.type = 1;
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
      this.form.addressOno = customer.address || this.form.addressOno;
      this.form.consigneeCity = customer.city || this.form.consigneeCity;
      this.form.consigneeProvince = customer.states || this.form.consigneeProvince;
      this.form.consigneeMail = customer.zipCode || this.form.consigneeMail;
      this.form.consigneeTelephone = customer.tel || this.form.consigneeTelephone;
      this.form.countryName = customer.country || this.form.countryName;
      this.form.expressId = customer.expressId || this.form.expressId;
    },selectOrderType(){
        console.log('orderType=='+this.orderType)
        this.orderType='packageSend';

        if(this.form.type =='2'){
            this.orderType='agentSend';
        }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
	// 取消修改
	cancel6() {
	  this.openStatus6 = false;
	  this.reset();
	},
	// 取消按钮(订单入库)
	cancelA() {
	  this.openStatus2 = false;
	  this.reset();
	},
	// 取消按钮(订单发货)
	cancelB() {
	  this.openStatus3 = false;
	  this.reset();
	},
	// 取消按钮(订单驳回)
	cancelC() {
	  this.openStatus4 = false;
	  this.reset();
	},
    // 表单重置
    reset() {
      this.form = {
        id: null,
        custId: null,
        orderNo: null,
        type: null,
        purchaseNo: null,
        purchaseSource: null,
        purchaseUrl: null,
        deliveryRemark: null,
        deliveryNo: null,
        status: null,
        productId: null,
        productNums: null,
        faceUrl: null,
        expressId: null,
		countryName: null,
		countryEname: null,
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
	purchase_urlBeforeUpload(file) {
	      let isRightSize = file.size / 1024 / 1024 < 2
	      if (!isRightSize) {
	        this.$message.error('文件大小超过 2MB')
	      }
	      return isRightSize
	},
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    /** 订单入库 */
    handleUpdate1(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.openStatus2 = true;
        this.title = "订单入库";
      });
    },
	/** 订单修改 */
	handleUpdate3(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
	    this.openStatus6 = true;
	    this.title = "订单修改";
	  });
	},
	/** 订单发货 */
	handleUpdate2(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
		this.busiExpressOptions = response.busiExpresses;
		this.busiCountryOptions = response.countryListVos
	    this.openStatus3 = true;
	    this.title = "订单发货";
	  });
	},
	/** 订单驳回操作 */
	handleUpdateReject(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
	    this.openStatus4 = true;
	    this.title = "订单驳回";
	  });
	},
    /** 提交按钮 */
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
            })().then(response => {
              this.$modal.msgSuccess("修改成功");
              this.openStatus2 = false;
			  this.openStatus3 = false;
			  this.openStatus4 = false;
              this.getList();
            });
          } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + ids + '"的数据项？').then(function() {
        return delOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>