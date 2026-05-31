<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="充值类型" prop="payType">
			  <el-select v-model="queryParams.payType" placeholder="充值类型" clearable>
			    <el-option
			      v-for="dict in dict.type.pay_type"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['pay:pay:add']"
        >账户充值</el-button>
      </el-col>
		  <!-- <span style="color: red; margin-left:100px;margin-top:-10px; size: 1px;" align="center">实际充值金额需扣(6%)手续费</span> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
     <el-table-column label="充值类型" align="center" prop="payType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_type" :value="scope.row.payType"/>
        </template>
      </el-table-column>
     <el-table-column prop="payUrl" label="充值截图" >
              <template slot-scope="scope">
                 <a :href="resolveResourceUrl(scope.row.payUrl)" target="_blank" title="查看最大化图片">
                         <img :src="resolveResourceUrl(scope.row.payUrl)" style="width:50px; height:50px"/>
                 </a>
              </template>
    </el-table-column>

     <el-table-column label="充值状态" align="center" prop="payStatus" >
				<template slot-scope="scope">
				  <dict-tag :options="dict.type.pay_status" :value="scope.row.payStatus"/>
				</template>
		 </el-table-column>
			 <el-table-column label="充值金额" align="center" prop="money">
			  </el-table-column>
      <el-table-column label="备注" align="center" prop="remakr" />
      <el-table-column label="创建时间" align="center" prop="createdDate" width="180">
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
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createdBy" />
      <el-table-column label="审批人" align="center" prop="updatedBy" />
		  <el-table-column label="审批时间" align="center" prop="approvalDate" width="180">
		    <template slot-scope="scope">
		  			<p>{{ parseTime(scope.row.approvalDate, '{h}:{i}:{s}') }}</p>
		      <span>{{ parseTime(scope.row.approvalDate, '{y}-{m}-{d}') }}</span>
		    </template>
		  </el-table-column>
		  <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
			  <template slot-scope="scope">
				  <el-button
					 v-if="scope.row.payStatus == '0'"
					 size="mini"
					 type="text"
					 icon="el-icon-edit"
					 @click="handleUpdate(scope.row)"
					 v-hasPermi="['pay:pay:edit']"
				   >充值审核</el-button>
					 <el-button
					  v-if="scope.row.payStatus == '0'"
					  size="mini"
					  type="text"
					  icon="el-icon-edit"
					  @click="handleUpdateReject(scope.row)"
					  v-hasPermi="['pay:pay:edit']"
					  >充值驳回</el-button>
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

    <!-- 用户充值 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="充值类型" prop="payType" required>
          <el-radio-group v-model="form.payType">
            <el-radio
              v-for="dict in dict.type.pay_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
			 <el-form-item label="充值截图" prop="payUrl">
				  <image-upload v-model="form.payUrl" :limit="1" />
			 </el-form-item>

			<div id="addWx" v-if="form.payType=='1' || !form.payType" align="center">
				<img :src="wechatQrUrl" width="300px"/>
			</div>
			<div id="addZfb" v-if="form.payType=='2'" align="center">
				<img :src="alipayQrUrl" width="300px"/>
			</div>
			 <el-form-item label="充值金额" prop="money">
			          <el-input-number v-model="form.money" placeholder="充值金额" min="10"></el-input-number>
			 </el-form-item>
			 <span style="color: red; margin-left:100px;margin-top:-10px;" align="center">充值需扣手续费（0.006%）</span>
        <el-form-item label="备注" prop="remakr">
          <el-input v-model="form.remakr" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

		<!-- 充值审核 -->
		<el-dialog :title="title" :visible.sync="openStatus" width="650px" append-to-body>
		  <el-form ref="form" :model="form" :rules="rules" label-width="150px">

			<el-form-item label="充值用户" prop="createdBy">
			        <el-input v-model="form.createdBy" placeholder="充值用户" readonly="true"/>
			 </el-form-item>
			 <span style="color: red; margin-left:100px;margin-top:-10px; size:1px;" align="center" >需扣用户手续费(0.006%),或自行选择实际充值金额</span>
			 <el-form-item label="实际给用户充值金额" prop="money">
			          <el-input v-model="form.money" placeholder="实际给用户充值金额"></el-input>
			 </el-form-item>
		    <el-form-item label="用户备注" prop="remakr">
		      <el-input v-model="form.remakr" placeholder="用户备注" readonly="true"/>
		    </el-form-item>

		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button type="primary" @click="submitForm">充 值</el-button>
		    <el-button @click="cancelA">取 消</el-button>
		  </div>
		</el-dialog>

		<!-- 充值驳回 -->
		<el-dialog :title="title" :visible.sync="openStatus1" width="650px" append-to-body :before-close="cancelB">
		  <el-form ref="form" :model="form" :rules="rules" label-width="150px">


			<el-form-item label="充值状态" prop="payStatus">
			        <template slot-scope="scope">
			          <dict-tag :options="dict.type.pay_status" :value="scope.row.payStatus"/>
			        </template>
			 </el-form-item>

			<el-form-item label="充值用户" prop="createdBy">
			        <el-input v-model="form.createdBy" placeholder="充值用户" readonly="true"/>
			 </el-form-item>
			<el-form-item label="充值金额" prop="money">
			        <el-input v-model="form.money" placeholder="充值金额" readonly="true"/>
			 </el-form-item>
		    <el-form-item label="驳回原因" prop="remakr">
		      <el-input v-model="form.remakr" placeholder="备注" type="textarea" />
		    </el-form-item>

		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button type="primary" @click="handleReject">驳 回</el-button>
		    <el-button @click="cancelB">取 消</el-button>
		  </div>
		</el-dialog>
  </div>
</template>

<script>
import { listPay, getPay, delPay, addPay, updatePay } from "@/api/pay/pay";

export default {
  name: "Pay",
  dicts: ["pay_type", "pay_status"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      payList: [],
      title: "",
      open: false,
      openStatus: false,
      openStatus1: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
			type: null,
			orderNo: null,
			remakr: null,
      },
      form: { money: 100 },
      rules: {
				money: [{
			            required: true,
			            message: "充值金额",
			            trigger: "blur"
			          }],
      },
      wechatQrUrl: this.resolveResourceUrl(process.env.VUE_APP_WECHAT_QR),
      alipayQrUrl: this.resolveResourceUrl(process.env.VUE_APP_ALIPAY_QR)
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
			this.queryParams.orderNo = "未关联订单";
      listPay(this.queryParams).then(response => {
        this.payList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
		cancelA() {
		  this.openStatus = false;
		  this.reset();
		},
		cancelB() {
		  this.openStatus1 = false;
		  this.reset();
		},
    reset() {
      this.form = {
        id: null,
        custId: null,
        payType: null,
        payMoney: null,
        payUrl: null,
        payStatus: null,
			orderNo: null,
        approvalDate: null,
        money: null,
        remakr: null,
        explainText: null,
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "账户充值";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPay(id).then(response => {
        this.form = response.data;
        this.openStatus = true;
        this.title = "充值审核";
      });
    },
		handleUpdateReject(row) {
		  this.reset();
		  const id = row.id || this.ids;
		  getPay(id).then(response => {
		    this.form = response.data;
		    this.openStatus1 = true;
		    this.title = "充值驳回";
		  });
		},
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePay(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
				  this.openStatus = false;
              this.getList();
            });
          } else {
            addPay(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleReject() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$modal.confirm("是否确认要驳回当前充值记录").then(() => {
            delPay(this.form).then(() => {
              this.getList();
              this.openStatus1 = false;
              this.$modal.msgSuccess("驳回成功");
            });
          }).catch(() => {});
        }
      });
    },
    handleExport() {
      this.download("pay/pay/export", {
        ...this.queryParams
      }, `pay_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
