<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width: 720px;">
      <el-form-item label="物流公司" prop="expressId">
        <el-select v-model="form.expressId" filterable clearable placeholder="请选择物流公司" style="width: 100%;" @change="handleExpressChange">
          <el-option v-for="item in expressOptions" :key="item.id" :label="item.name" :value="String(item.id)" />
        </el-select>
      </el-form-item>
      <el-form-item label="运输方式代码" prop="shippingMethod">
        <el-select v-model="form.shippingMethod" filterable clearable placeholder="请选择运输方式代码" style="width: 100%;">
          <el-option v-for="item in shippingMethodOptions" :key="item.code || item.value" :label="item.cnname || item.label || item.code || item.value" :value="item.code || item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="目的国家编码" prop="countryCode">
        <el-input v-model="form.countryCode" placeholder="请输入两位国家编码，如 US" />
      </el-form-item>
      <el-form-item label="目的地邮编" prop="postCode">
        <el-input v-model="form.postCode" placeholder="请输入目的地邮编" />
      </el-form-item>
      <el-form-item label="重量(KG)" prop="weight">
        <el-input v-model="form.weight" placeholder="请输入重量" />
      </el-form-item>
      <el-form-item label="长(CM)" prop="length">
        <el-input v-model="form.length" placeholder="请输入长度" />
      </el-form-item>
      <el-form-item label="宽(CM)" prop="width">
        <el-input v-model="form.width" placeholder="请输入宽度" />
      </el-form-item>
      <el-form-item label="高(CM)" prop="height">
        <el-input v-model="form.height" placeholder="请输入高度" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleTrial">运费试算</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-card v-if="result" shadow="never" style="margin-top: 16px;">
      <pre class="result-pre">{{ formattedResult }}</pre>
    </el-card>
  </div>
</template>

<script>
import { listExpress } from "@/api/express/express"
import { getShippingMethod } from "@/api/order/order"
import request from "@/utils/request"

export default {
  name: "ExpressFee",
  data() {
    return {
      expressOptions: [],
      shippingMethodOptions: [],
      submitting: false,
      result: null,
      form: {
        expressId: null,
        shippingMethod: null,
        countryCode: "US",
        postCode: "",
        weight: "1",
        length: "10",
        width: "10",
        height: "10"
      },
      rules: {
        expressId: [{ required: true, message: "请选择物流公司", trigger: "change" }],
        shippingMethod: [{ required: true, message: "请选择运输方式代码", trigger: "change" }],
        countryCode: [{ required: true, message: "请输入目的国家编码", trigger: "blur" }],
        postCode: [{ required: true, message: "请输入目的地邮编", trigger: "blur" }],
        weight: [{ required: true, message: "请输入重量", trigger: "blur" }]
      }
    }
  },
  computed: {
    formattedResult() {
      return JSON.stringify(this.result, null, 2)
    }
  },
  created() {
    this.loadExpressOptions()
  },
  methods: {
    loadExpressOptions() {
      listExpress({ pageNum: 1, pageSize: 1000, isEnable: 1 }).then(response => {
        const rows = response.rows || response.data || []
        this.expressOptions = rows
      })
    },
    handleExpressChange(value) {
      this.shippingMethodOptions = []
      this.form.shippingMethod = null
      if (!value) {
        return
      }
      getShippingMethod(value).then(response => {
        this.shippingMethodOptions = response.data || []
      })
    },
    handleTrial() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        this.submitting = true
        this.result = null
        request({
          url: '/express/express/query-express-free',
          method: 'get',
          params: {
            expressId: this.form.expressId,
            shippingMethod: this.form.shippingMethod,
            countryCode: this.form.countryCode,
            postCode: this.form.postCode,
            weight: this.form.weight,
            length: this.form.length,
            width: this.form.width,
            height: this.form.height
          }
        }).then(response => {
          this.result = response.data || response
          this.$modal.msgSuccess('试算完成')
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    resetForm() {
      this.form = {
        expressId: null,
        shippingMethod: null,
        countryCode: "US",
        postCode: "",
        weight: "1",
        length: "10",
        width: "10",
        height: "10"
      }
      this.shippingMethodOptions = []
      this.result = null
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    }
  }
}
</script>

<style scoped>
.result-pre {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
