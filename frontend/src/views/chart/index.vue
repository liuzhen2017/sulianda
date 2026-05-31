<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

		<el-form-item label="统计类型" >
		  <el-select v-model="queryParams.type" placeholder="统计类型" clearable>
		    <el-option
		      v-for="dict in dict.type.date_type"
		      :key="dict.value"
		      :label="dict.label"
		      :value="dict.value"
		    />
		  </el-select>
      <el-form-item label="时间" >
          <el-input
            v-model="queryParams.date"
            placeholder="请输入时间,如果是年,不用输入,如果是月,则指定年份2023,如果是月则是 指定月份 2023-12"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
		</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payList" @selection-change="handleSelectionChange">
       <el-table-column label="统计时间" align="center" prop="dateStr" />
       <el-table-column label="总订单数" align="center" prop="totalOderNumber" />
       <el-table-column label="总快递费" align="center" prop="totalExpressMoney" />
       <el-table-column label="总打包费" align="center" prop="totalPackageMoney" />
       <el-table-column label="总利润" align="center" prop="totalProfitMoney" />
    </el-table>


  </div>
</template>

<script>
import { getProfitList } from "@/api/chart/chart";

export default {
  name: "Pay",
  dicts: ['date_type'],
  data() {
    return {
      // 遮罩层
      loading: true,

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
        date:"",
        type: '',
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
      getProfitList(this.queryParams).then(response => {
        this.payList = response.rows;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    }
  }
};
</script>
