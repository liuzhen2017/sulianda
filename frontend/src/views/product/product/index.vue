<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="库存" prop="nums">
        <el-input
          v-model="queryParams.nums"
          placeholder="请输入库存"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单价" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入单价"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['product:product:add','warehouse:product:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['product:product:edit','warehouse:product:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['product:product:remove','warehouse:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['product:product:import','warehouse:product:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['product:product:export','warehouse:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库存" align="center" prop="nums" />
      <el-table-column label="商品名称" align="center" prop="name" />
      <el-table-column label="图片路径" align="center" prop="imgUrl" width="100">
				<template slot-scope="scope">
				   <a :href="resolveResourceUrl(scope.row.imgUrl)" target="_blank" title="查看最大化图片">
				           <img :src="resolveResourceUrl(scope.row.imgUrl)" style="width:50px; height:50px"/>
				   </a>
				</template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template slot-scope="scope">
			  <p>{{ parseTime(scope.row.createdTime, '{h}:{i}:{s}') }}</p>
			  <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createdBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
				<p>{{ parseTime(scope.row.updateTime, '{h}:{i}:{s}') }}</p>
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改人" align="center" prop="updateBy" />
      <el-table-column label="英文名称" align="center" prop="enName" />
      <el-table-column label="sku" align="center" prop="sku" />
		  <el-table-column label="单位" align="center" prop="unitCode">
		    <template slot-scope="scope">
		      <dict-tag :options="dict.type.unit" :value="scope.row.unitCode"/>
		    </template>
		  </el-table-column>
      <el-table-column label="单价" align="center" prop="price" />
		  <el-table-column label="币别" align="center" prop="invoiceCurrencyCode">
		    <template slot-scope="scope">
		      <dict-tag :options="dict.type.invoice_currencycode" :value="scope.row.invoiceCurrencyCode"/>
		    </template>
		  </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['product:product:edit','warehouse:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['product:product:remove','warehouse:product:remove']"
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
        drag
      >
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

    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
      <el-form-item label="库存" prop="nums">
        <el-input-number
          v-model="form.nums"
          placeholder="请输入库存数量"
          :min="0"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
			<el-form-item label="上传" prop="imgUrl">
			  <image-upload v-model="form.imgUrl" :limit="1" />
			</el-form-item>


        <el-form-item label="英文名称" prop="enName">
          <el-input v-model="form.enName" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="sku" prop="sku">
          <el-input v-model="form.sku" type="textarea" placeholder="请输入内容" />
        </el-form-item>
			<el-form-item label="单位">
				<el-select v-model="form.unitCode" placeholder="请选择单位" style="width: 100%;height: 10%;">
				  <el-option
					v-for="dict in dict.type.unit"
					:key="dict.value"
					:label="dict.label"
					:value="dict.value"
				  ></el-option>
				</el-select>
			</el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入单价" />
        </el-form-item>
			<el-form-item label="币别">
				<el-select v-model="form.invoiceCurrencyCode" placeholder="请选择币别" style="width: 100%;height: 10%;">
				  <el-option
					v-for="dict in dict.type.invoice_currencycode"
					:key="dict.value"
					:label="dict.label"
					:value="dict.value"
				  ></el-option>
				</el-select>
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
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/product/product";
import { getToken } from "@/utils/auth";

export default {
  name: "Product",
   dicts: ["unit", "invoice_currencycode"],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      productList: [],
      title: "",
      open: false,
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/product/product/importData"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nums: null,
        name: null,
        imgUrl: null,
        enName: null,
        sku: null,
        unitCode: null,
        price: null,
        invoiceCurrencyCode: null
      },
      form: {},
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        custId: null,
        nums: null,
        name: null,
        imgUrl: null,
        createdTime: null,
        createdBy: null,
        updateTime: null,
        updateBy: null,
        deptId: null,
        enName: null,
        sku: null,
        unitCode: null,
        price: null,
        invoiceCurrencyCode: null
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
      this.title = "添加商品";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getProduct(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改商品";
      });
    },
    handleImport() {
      this.upload.title = "商品导入";
      this.upload.open = true;
    },
    importTemplate() {
      this.download("product/product/importTemplate", {}, `product_template_${new Date().getTime()}.xlsx`);
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
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProduct(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(this.form).then(() => {
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
      this.$modal.confirm('是否确认删除商品编号为"' + ids + '"的数据项？').then(function() {
        return delProduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download("product/product/export", {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
