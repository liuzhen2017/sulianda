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
		<el-form-item label="下单客户" prop="createdBy">
		   <el-input
		     v-model="queryParams.createdBy"
		     placeholder="请输入下单客户"
		     clearable
		     @keyup.enter.native="handleQuery"
		   />
		 </el-form-item>
		
		<el-form-item label="快递公司">
		  <el-select
		    v-model="queryParams.expressId"
		    placeholder="快递公司"
		    clearable>
		    <el-option
		      v-for="dict in dict.type.busi_express"
		      :key="dict.value"
		      :label="dict.label"
		      :value="dict.value"
		    />
		  </el-select>
		</el-form-item>	
		
		<el-form-item label="采购单号" prop="purchaseNo">
		   <el-input
		     v-model="queryParams.purchaseNo"
		     placeholder="请输入采购单号"
		     clearable
		     @keyup.enter.native="handleQuery"
		   />
		 </el-form-item>
     <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
	 <el-form-item label="生成单号" prop="deliveryNo">
	    <el-input
	      v-model="queryParams.deliveryNo"
	      placeholder="请输入生成单号"
	      clearable
	      @keyup.enter.native="handleQuery"
	    />
	  </el-form-item>
	  <el-form-item label="快递单号" prop="thirdPartyNo">
	     <el-input
	       v-model="queryParams.thirdPartyNo"
	       placeholder="请输入快递单号"
	       clearable
	       @keyup.enter.native="handleQuery"
	     />
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
          v-hasPermi="['order:order:export','warehouse:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
	  <el-table-column prop="purchaseUrl" label="采购图片" >
	            <template slot-scope="scope">
	               <a :href="scope.row.purchaseUrl" target="_blank" title="查看最大化图片">
	                       <img :src="scope.row.purchaseUrl" style="width:50px; height:50px"/>
	               </a>
	            </template>
	  </el-table-column>
	  <el-table-column label="采购单号" align="center" prop="purchaseNo" />
	  
      <el-table-column label="订单号" align="center" prop="orderNo" />
	  <el-table-column label="生成单号" align="center" prop="deliveryNo" />
     <!-- <el-table-column label="订单类型" align="center" prop="type">
				<template slot-scope="scope">
	  			  <dict-tag :options="dict.type.order_type" :value="scope.row.type"/>
	  			</template>
	  </el-table-column> -->

	  <el-table-column label="快递单号" align="center" prop="thirdPartyNo">
		   <template slot-scope="scope">
				<a :href='"https://t.17track.net/zh-cn#nums="+scope.row.thirdPartyNo' type="primary" target="_blank" style="color: blue;">{{scope.row.thirdPartyNo}}</a>
			</template>
 	  </el-table-column>

	<el-table-column label="物流公司" align="center" prop="expressId">
					<template slot-scope="scope">
					  <dict-tag :options="dict.type.busi_express" :value="scope.row.expressId"/>
					</template>
	  </el-table-column>
	  
	  <el-table-column label="手动录入物流公司" align="center" prop="expressCompany" />
	  

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
      <el-table-column label="客户发货数" align="center" prop="custSendNums" />
	  <el-table-column label="仓库发货数" align="center" prop="warehouseSendNums" />

	  <el-table-column label="运费" align="center" prop="expressMoney" >
	    <template slot-scope="scope">
	  	<el-button
	  	   size="mini"
	  	   type="text"
	  	   icon="el-icon-show"
	  	   @click="ExpressMoneyInfo(scope.row)"
	  	   v-hasPermi="['order:order:add','warehouse:order:add']"
	  	 >运费明细</el-button>
	    </template>
	  </el-table-column>
	  
	  <el-table-column label="总金额" align="center">
	  	<template slot-scope="scope">
	  		<span :style="{color:'#FF0000'}" v-if="scope.row.status == '3' || scope.row.status == '5'">{{scope.row.totalMoney}}</span>
	  	</template>
	  </el-table-column>	
	  <el-table-column label="备注" align="center" prop="invoiceNote" />

<!--      <el-table-column label="下单时间" align="center" prop="createdDate" width="180">
        <template slot-scope="scope">
           <p>{{ parseTime(scope.row.createdDate, '{h}:{i}:{s}') }}</p>
            <span>{{ parseTime(scope.row.createdDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
<!--      <el-table-column label="最后操作时间" align="center" prop="updatedDate" width="180">
        <template slot-scope="scope">
          <p>{{ parseTime(scope.row.updatedDate, '{h}:{i}:{s}') }}</p>
           <span>{{ parseTime(scope.row.updatedDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="下单人" align="center" prop="createdBy" />
<!--      <el-table-column label="操作人" align="center" prop="updatedBy" />
 -->      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
         <el-button
			v-if="scope.row.status == '1'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate1(scope.row)"
            v-hasPermi="['order:order:edit','warehouse:order:edit']"
          >订单入库</el-button>
		  <el-button
			 v-if="scope.row.status == '2'"
		     size="mini"
		     type="text"
		     icon="el-icon-edit"
		     @click="handleUpdate2(scope.row)"
		     v-hasPermi="['order:order:edit','warehouse:order:edit']"
		   >订单发货</el-button>
			 <el-button
			    size="mini"
			    type="text"
			    icon="el-icon-edit"
			    @click="handleUpdate3(scope.row)"
			    v-hasPermi="['order:order:edit','warehouse:order:edit']"
			  >订单修改</el-button>
		   <el-button
		      v-if="scope.row.status == '3' || scope.row.status == '5'"
		      size="mini"
		      type="text"
		      icon="el-icon-edit"
		      @click="handleUpdateFaceInfo(scope.row)"
		    >打印面单</el-button>
		   <el-button
		      v-if="scope.row.status == '3'"
		      size="mini"
		      type="text"
		      icon="el-icon-edit"
		      @click="handleUpdateReject(scope.row)"
		      v-hasPermi="['order:order:edit','warehouse:order:edit']"
		    >订单驳回</el-button>
			<el-button
			   size="mini"
			   type="text"
			   icon="el-icon-edit"
			   @click="handleOrderInfo(scope.row)"
			   v-hasPermi="['order:order:edit','warehouse:order:edit']"
			 >订单详情</el-button>
			 <el-button
			 	v-if="scope.row.status == '3' || scope.row.status == '5'"
			    size="mini"
			    type="text"
			    icon="el-icon-edit"
			    @click="handleCutMoney(scope.row)"
			    v-hasPermi="['order:order:edit','warehouse:order:edit']"
			  >订单扣费</el-button>
			 <el-button
			   size="mini"
			   type="text"
			   icon="el-icon-tickets"
			   @click="handleOrderEvents(scope.row)"
			   v-hasPermi="['order:order:query','warehouse:order:query']"
			 >业务轨迹</el-button>
			 <el-button
			   size="mini"
			   type="text"
			   icon="el-icon-warning-outline"
			   @click="markIssue(scope.row)"
			   v-hasPermi="['order:order:edit','warehouse:order:edit']"
			 >问题件</el-button>
			 <el-button
			   size="mini"
			   type="text"
			   icon="el-icon-circle-check"
			   @click="approveRelease(scope.row)"
			   v-hasPermi="['order:order:edit','warehouse:order:edit']"
			 >放行</el-button>
			 <el-button
			   size="mini"
			   type="text"
			   icon="el-icon-delete"
			   @click="handleDelete(scope.row)"
			   v-hasPermi="['order:order:remove','warehouse:order:remove']"
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

	<!-- 发货 -->
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
		  			  <el-option
		  				v-for="item in dict.type.busi_express"
		  				:key="item.value"
		  				:label="item.label"
		  				:value="item.value"
		  			  ></el-option>
		  			</el-select>
		  </el-form-item>

		  <el-form-item label="运输方式代码" prop="shippingMethod" >
		  			 <el-select v-model="form.shippingMethod" filterable placeholder="请选择运输方式代码" width=100px align="left">
		  				<el-option
		  				v-for="dict in dict.type.shipping_method"
		  				:key="dict.value"
		  				:label="dict.cnname"
		  				:value="dict.code"
		  				></el-option>
		  			  </el-select>
		  </el-form-item>
		  
		  <el-form-item label="海关hsCode" prop="hsCode">
			<el-input v-model="form.hsCode" placeholder="请输入海关编码" />
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

		  <span style="color: red;">========================================核对客户数据========================================</span>
		  <!--收货人信息-->
		  		<el-form-item label="收件人姓名" prop="consigneeName">
		  		   <el-input v-model="form.consigneeName" placeholder="请输入收件人姓名" />
		  		 </el-form-item>
		  		 <el-form-item label="收件人国家编码">
		  		  			<el-select v-model="form.consigneeCountryCode" placeholder="请选择收件人国家编码" style="width: 100%;height: 10%;">
		  		  			  <el-option
		  		  				v-for="dict in dict.type.busi_country"
		  		  				:key="dict.value"
		  		  				:label="dict.label"
		  		  				:value="dict.value"
		  		  			  ></el-option>
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
		  		  <el-form-item label="单位code" prop="unitCode" >
		  		  			  <el-select v-model="form.unitCode" placeholder="请选择单位code" width=100px align="left">
		  		  				<el-option
		  		  				v-for="dict in dict.type.unit"
		  		  				:key="dict.value"
		  		  				:label="dict.label"
		  		  				:value="dict.value"
		  		  				></el-option>
		  		  			  </el-select>
		  		  </el-form-item>
		  		 		
		  		  
		  		  <el-form-item label="商品单价" prop="invoiceUnitCharge">
		  		    <el-input v-model="form.invoiceUnitCharge" placeholder="请输入商品单价" />
		  		  </el-form-item>
		  		  
		  		   <el-form-item label="是否带电" prop="charge">
		  		            <el-switch v-model="form.charge" :active-value='"Y"' :inactive-value='"N"'></el-switch>
		  		   </el-form-item>
		  		   
		  		   <el-form-item label="申报总价" prop="evaluate">
		  		            <el-input v-model="form.evaluate" placeholder="请输入申报总价" />
		  		   </el-form-item>
					<el-form-item label="备注" prop="invoiceNote">
						<el-input v-model="form.invoiceNote" placeholder="请输入备注"  type="textarea" :autosize="{minRows: 2, maxRows: 2}" />
					</el-form-item>
		</el-form>
		
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm">下 单</el-button>
	    <el-button @click="cancelB">取 消</el-button>
	  </div>
	</el-dialog>
	
	<!-- 订单修改 -->
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
			  <el-option
			    v-for="item in customerOptions"
			    :key="item.id"
			    :label="customerOptionLabel(item)"
			    :value="item.id"
			  ></el-option>
			</el-select>
		  </el-form-item>

		  <span style="color: red;">========================================核对客户数据========================================</span>
		  <!--收货人信息-->
		  		<el-form-item label="收件人姓名" prop="consigneeName">
		  		   <el-input v-model="form.consigneeName" placeholder="请输入收件人姓名" />
		  		 </el-form-item>
		  		 <el-form-item label="收件人国家编码">
		  		  			<el-select v-model="form.consigneeCountryCode" placeholder="请选择收件人国家编码" style="width: 100%;height: 10%;">
		  		  			  <el-option
		  		  				v-for="dict in dict.type.busi_country"
		  		  				:key="dict.value"
		  		  				:label="dict.label"
		  		  				:value="dict.value"
		  		  			  ></el-option>
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
		  		  <el-form-item label="单位code" prop="unitCode" >
		  		  			  <el-select v-model="form.unitCode" placeholder="请选择单位code" width=100px align="left">
		  		  				<el-option
		  		  				v-for="dict in dict.type.unit"
		  		  				:key="dict.value"
		  		  				:label="dict.label"
		  		  				:value="dict.value"
		  		  				></el-option>
		  		  			  </el-select>
		  		  </el-form-item>
		  		 		
		  		  
		  		  <el-form-item label="商品单价" prop="invoiceUnitCharge">
		  		    <el-input v-model="form.invoiceUnitCharge" placeholder="请输入商品单价" />
		  		  </el-form-item>
		  		  
		  		   <el-form-item label="是否带电" prop="charge">
		  		            <el-switch v-model="form.charge" :active-value='"Y"' :inactive-value='"N"'></el-switch>
		  		   </el-form-item>
		  		   
		  		   <el-form-item label="申报总价" prop="evaluate">
		  		            <el-input v-model="form.evaluate" placeholder="请输入申报总价" />
		  		   </el-form-item>
				   <el-form-item label="备注" prop="invoiceNote">
				   	<el-input v-model="form.invoiceNote" placeholder="请输入备注"  type="textarea" :autosize="{minRows: 2, maxRows: 2}" />
				   </el-form-item>
		</el-form>
		
	  <div slot="footer" class="dialog-footer">
	    <el-button type="primary" @click="submitForm2">修 改</el-button>
	    <el-button @click="cancel6">取 消</el-button>
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
	

	<!-- 订单配送详情 -->
	<el-dialog :title="title" :visible.sync="openStatus5" width="500x" append-to-body :before-close="cancelD">
	  <el-form ref="form" :model="form" :rules="rules" label-width="150px">
		  <el-form-item label="物流公司" readonly="true">
		  			<el-select v-model="form.express_id" placeholder="请选择物流公司" style="width: 100%;height: 10%;" readonly="true">
		  			  <el-option
		  				v-for="dict in dict.type.busi_express"
		  				:key="dict.value"
		  				:label="dict.label"
		  				:value="dict.value"
		  			  ></el-option>
		  			</el-select>
		  </el-form-item>

	      <!--收货人信息-->
		  <el-form-item label="收货人姓名" prop="consigneeName">
		    <el-input v-model="form.consigneeName" placeholder="请输入收货人姓名" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收货人国家编码">
		   			<el-select v-model="form.consigneeCountryCode" placeholder="请选择收货人国家编码" style="width: 100%;height: 10%;">
		   			  <el-option
		   				v-for="item in dict.type.busi_country"
		   				:key="item.value"
		   				:label="item.label"
		   				:value="item.value"
		   			  ></el-option>
		   			</el-select>
		   </el-form-item>
		  <el-form-item label="收件人城市" prop="consigneeCity">
		    <el-input v-model="form.consigneeCity" placeholder="请输入收件人城市" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收货人街道" prop="consigneeStreet">
		    <el-input v-model="form.consigneeStreet" placeholder="请输入收货人街道" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收货人联系方式" prop="consigneeTelephone">
		    <el-input v-model="form.consigneeTelephone" placeholder="请输入收货人联系方式" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收件人公司名称" prop="consigneeCompany">
		    <el-input v-model="form.consigneeCompany" placeholder="请输入收件人公司名称" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收件人公司" prop="consigneeProvince">
		    <el-input v-model="form.consigneeProvince" placeholder="请输入收件人公司" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="收件人邮编" prop="consigneeMail">
		    <el-input v-model="form.consigneeMail" placeholder="请输入收件人邮编" readonly="true"/>
		  </el-form-item>


		  <!--海关信息-->
		 <!-- <el-form-item label="报关产品英文名称" prop="invoiceEnname">
		    <el-input v-model="form.invoiceEnname" placeholder="请输入报关产品英文名称" readonly="true"/>
		  </el-form-item> -->
		  <el-form-item label="报关产品数量" prop="invoiceQuantity">
		    <el-input v-model="form.invoiceQuantity" placeholder="请输入报关产品数量" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="运输方式代码" prop="shippingMethod">
		    <el-input v-model="form.shippingMethod" placeholder="请输入运输方式代码" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="报关英文名" prop="invoiceEnName">
		    <el-input v-model="form.invoiceEnName" placeholder="请输入报关英文名" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="报关中文名" prop="invoiceCnName">
		    <el-input v-model="form.invoiceCnName" placeholder="请输入报关中文名" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="单位code" prop="unitCode">
		    <el-input v-model="form.unitCode" placeholder="请输入单位code" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="单价(单个商品价格)" prop="invoiceUnitCharge">
		    <el-input v-model="form.invoiceUnitCharge" placeholder="请输入单价(单个商品价格)" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="备注" prop="invoiceNote">
		    <el-input v-model="form.invoiceNote" placeholder="请输入备注" readonly="true"/>
		  </el-form-item>


		  <el-form-item label="商品打包长度" prop="goodsLength">
		    <el-input v-model="form.goodsLength" placeholder="请输入商品打包长度" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="商品打包宽度" prop="goodsWidth">
		    <el-input v-model="form.goodsWidth" placeholder="请输入商品打包宽度" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="商品打包高度" prop="goodsHeight">
		    <el-input v-model="form.goodsHeight" placeholder="请输入商品打包高度" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="商品打包重量" prop="goodsHeight">
		    <el-input v-model="form.goodsWeight" placeholder="请输入商品打包重量" readonly="true"/>
		  </el-form-item>

		  <el-form-item label="打包费" prop="packageMoney">
		    <el-input v-model="form.packageMoney" placeholder="请输入打包费" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="运费" prop="expressMoney">
		    <el-input v-model="form.expressMoney" placeholder="请输入运费" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="盈利(对客户隐藏)" prop="profitMoney">
		    <el-input v-model="form.profitMoney" placeholder="请输入盈利" readonly="true"/>
		  </el-form-item>
		  <el-form-item label="税金" prop="taxes">
		    <el-input v-model="form.taxes" readonly="true"/>
		  </el-form-item>
		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button @click="cancelD">关 闭</el-button>
	  </div>
	</el-dialog>

	<!-- 打印面单 -->
	<el-dialog :title="title" :visible.sync="openFaceInfo" width="600px" append-to-body :before-close="cancelFaceInfo">
	  <el-form ref="form" :model="form" :rules="rules" label-width="200px">

		   <el-col :span="2000">
		              <el-form-item label="面单详情" prop="rejectRemark" width="300px">
              <a :href="printImageUrl" target="_blank">点击下载</a>
		              </el-form-item>

		    </el-col>
		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button @click="cancelFaceInfo">关 闭</el-button>
	  </div>
	</el-dialog>

	<!-- 驳回 -->
	<el-dialog :title="title" :visible.sync="openStatus4" width="600px" append-to-body :before-close="cancelC">
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
	</el-dialog>
	
	<el-dialog title="业务轨迹" :visible.sync="openOrderEvents" width="700px" append-to-body>
	  <el-table :data="orderEvents">
	    <el-table-column label="事件类型" prop="eventType" />
	    <el-table-column label="标题" prop="title" />
	    <el-table-column label="前状态" prop="beforeStatus" />
	    <el-table-column label="后状态" prop="afterStatus" />
	    <el-table-column label="详情" prop="detail" />
	    <el-table-column label="时间" prop="createdDate" width="180">
	      <template slot-scope="scope">
	        <span>{{ parseTime(scope.row.createdDate) }}</span>
	      </template>
	    </el-table-column>
	  </el-table>
	  <div slot="footer" class="dialog-footer">
	    <el-button @click="openOrderEvents = false">关 闭</el-button>
	  </div>
	</el-dialog>
	
	<!-- 运费明细 -->
	<el-dialog :title="title" :visible.sync="openExpressMoney" width="300px" append-to-body :before-close="cancelExpressMoney">
	  <el-form ref="form" :model="form" :rules="rules" >
		 <el-form-item label="运费" prop="expressMoney" >
		   <el-input v-model="form.expressMoney" readonly="true" />
		 </el-form-item>
		 <el-form-item label="打包费" prop="packageMoney" >
		   <el-input v-model="form.packageMoney"  readonly="true" />
		 </el-form-item>
		 <el-form-item label="总费用" prop="expressMoney" >
		   <el-input v-model="form.expressMoney+form.packageMoney+form.taxes+form.arrivedMoney" readonly="true" />
		 </el-form-item>
		 <el-form-item label="税金" prop="taxes" >
		   <el-input v-model="form.taxes" readonly="true" />
		 </el-form-item>
		 <el-form-item label="到付件" prop="arrivedMoney" >
		   <el-input v-model="form.arrivedMoney" readonly="true" />
		 </el-form-item>
		 
		 <el-form-item label="货品打包长度" prop="goodsLength" >
		   <el-input v-model="form.goodsLength" readonly="true" />
		 </el-form-item>
		 
		 <el-form-item label="货品打包宽度" prop="goodsWidth" >
		   <el-input v-model="form.goodsWidth" readonly="true"/>
		 </el-form-item>
		 
		 <el-form-item label="货品打包高度" prop="goodsHeight">
		   <el-input v-model="form.goodsHeight" readonly="true" />
		 </el-form-item>
		 
		 <el-form-item label="货品打包重量(kg)" prop="goodsWeight">
		   <el-input v-model="form.goodsWeight" readonly="true" />
		 </el-form-item>
		  
		</el-form>
	  <div slot="footer" class="dialog-footer">
	    <el-button @click="cancelExpressMoney()">关 闭</el-button>
	  </div>
	</el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, printInfo, getShippingMethod, operateOrder, listOrderEvents } from "@/api/order/order";
import { listCustomer } from "@/api/express/customer";
import { Form } from "element-ui";

export default {
  name: "Order",
  dicts: ['order_type','product','order_status','busi_express','busi_country','shipping_method','unit'],
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
      orderEvents: [],
      openOrderEvents: false,
	  // 快递选项
	  busiExpressOptions: [],
	  // 国家选项
	  busiCountryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
	 printImageUrl:null,
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
      // 表单参数
      form: {type: undefined,rejectRemark: undefined,},
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
	// 关闭打印面单弹框
	cancelFaceInfo(){
		this.openFaceInfo =false;
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
	// 取消修改
	cancel6() {
	  this.openStatus6 = false;
	  this.reset();
	},
	// 取消按钮(订单驳回)
	cancelC() {
	  this.openStatus4 = false;
	  this.reset();
	},
	// 订单配送详情
	cancelD() {
	  this.openStatus5 = false;
	  this.reset();
	},
	// 订单扣费
	cancelE() {
	  this.openCutMoney = false;
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
        customerId: null,
        productNums: null,
        faceUrl: null,
        expressId: null,
				express_id: null,
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
	// 关闭按钮
	cancelExpressMoney() {
	  this.openExpressMoney = false;
	  this.reset();
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


	/**监听快递公司选择*/
	currStationChange() {
		console.log(this.form.expressId)
	    // this.reset();
	    // const id = row.id || this.ids
		const id = this.form.expressId
	    getShippingMethod(id).then(response => {
		  // this.form = response.data;
		  this.dict.type.shipping_method = response.data;
		  //console.log(JSON.stringify(response));
	    });
	},
	/** 运费明细 */
	ExpressMoneyInfo(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
	    this.openExpressMoney = true;
	    this.title = "运费明细";
	  });
	},
	/** 订单扣费 */
	handleCutMoney(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
	    this.openCutMoney = true;
	    this.title = "订单扣费";
	  });
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
	/** 打印面单 */
	handleUpdateFaceInfo(row) {
	  this.reset();
	  const id = row.id || this.ids
	  printInfo(id).then(response => {
	    this.printImageUrl = response.data;
	    this.openFaceInfo = true;

	    //this.title = "打印面单";
	  });
	},




	/** 订单配送详情 */
	handleOrderInfo(row) {
	  this.reset();
	  const id = row.id || this.ids
	  getOrder(id).then(response => {
	    this.form = response.data;
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
			  this.openCutMoney = false;
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
	/** 提交按钮 */
	submitForm2() {
	  this.$refs["form"].validate(valid => {
	    if (valid) {
	      if (this.form.id != null) {
			this.form.rejectRemark = '修改订单';
	        updateOrder(this.form).then(response => {
	          this.$modal.msgSuccess("修改成功");
	          this.openStatus6 = false;
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
