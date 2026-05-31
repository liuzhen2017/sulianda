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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['pay:pay:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="交易金额" align="center" prop="payMoney" />
      <el-table-column label="余额" align="center" prop="balance" />
      <el-table-column label="备注" align="center" prop="remakr" />
	  	  
	 <el-table-column label="交易类型" align="center" prop="payType">
	    <template slot-scope="scope">
	      <dict-tag :options="dict.type.pay_type" :value="scope.row.payType"/>
	    </template>
	  </el-table-column>
	  
	  <el-table-column label="关联订单号" align="center" prop="orderNo" />
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
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改账户充值明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户ID" prop="custId">
          <el-input v-model="form.custId" placeholder="请输入客户ID" />
        </el-form-item>
        <el-form-item label="充值类型(1-微信 2-支付宝)" prop="payType">
          <el-select v-model="form.payType" placeholder="请选择充值类型(1-微信 2-支付宝)">
            <el-option
              v-for="dict in dict.type.pay_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支出金额" prop="payMoney">
          <el-input v-model="form.payMoney" placeholder="请输入支出金额" />
        </el-form-item>
        <el-form-item label="充值截图" prop="payUrl">
          <el-input v-model="form.payUrl" placeholder="请输入充值截图" />
        </el-form-item>
        <el-form-item label="充值状态(0-待审核 1-已通过 2-已驳回)" prop="payStatus">
          <el-radio-group v-model="form.payStatus">
            <el-radio
              v-for="dict in dict.type.pay_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批时间" prop="approvalDate">
          <el-date-picker clearable
            v-model="form.approvalDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择审批时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="充值金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入充值金额" />
        </el-form-item>
        <el-form-item label="备注" prop="remakr">
          <el-input v-model="form.remakr" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="说明" prop="explainText">
          <el-input v-model="form.explainText" placeholder="请输入说明" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdDate">
          <el-date-picker clearable
            v-model="form.createdDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="修改时间" prop="updatedDate">
          <el-date-picker clearable
            v-model="form.updatedDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择修改时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建人" prop="createdBy">
          <el-input v-model="form.createdBy" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="修改人" prop="updatedBy">
          <el-input v-model="form.updatedBy" placeholder="请输入修改人" />
        </el-form-item>
        <el-form-item label="部门ID" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPay, getPay, delPay, addPay, updatePay } from "@/api/pay/detail_pay";

export default {
  name: "Pay",
  dicts: ['pay_status', 'pay_type'],
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
      // 账户充值明细表格数据
      payList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        custId: null,
        payType: null,
        payMoney: null,
        payUrl: null,
        payStatus: null,
        approvalDate: null,
        money: null,
        remakr: null,
        explainText: null,
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null,
        deptId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询账户充值明细列表 */
    getList() {
      this.loading = true;
      listPay(this.queryParams).then(response => {
        this.payList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        custId: null,
        payType: null,
        payMoney: null,
        payUrl: null,
        payStatus: null,
        approvalDate: null,
        money: null,
        remakr: null,
        explainText: null,
        createdDate: null,
        updatedDate: null,
        createdBy: null,
        updatedBy: null,
        deptId: null
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加账户充值明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPay(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改账户充值明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePay(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPay(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除账户充值明细编号为"' + ids + '"的数据项？').then(function() {
        return delPay(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('pay/pay/export', {
        ...this.queryParams
      }, `pay_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
