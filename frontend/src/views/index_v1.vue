	<template>
 <!-- <div class="dashboard-editor-container"> -->

<!--    <panel-group @handleSetLineChartData="handleSetLineChartData" />
 -->
<!--   <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>
 -->
    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col> -->
      <!-- <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col> -->
    <!-- </el-row> -->


<!--  </div> -->
</template>
    <div class="el-login-footer">
      <!-- <span>Copyright © 2018-2022 ruoyi.vip All Rights Reserved.</span> -->
      <span>Copyright © 2018-2022 ruoyi.vip All Rights Reserved.   <span style="color: blue;padding-left: 2.8em"><a href="https://beian.miit.gov.cn">粤ICP备2023005983号-1</a></span></span>
    </div>
<script>
// import PanelGroup from './dashboard/PanelGroup'
// import LineChart from './dashboard/LineChart'
// import RaddarChart from './dashboard/RaddarChart'
// import PieChart from './dashboard/PieChart'
// import BarChart from './dashboard/BarChart'
// import { lineChart } from "@/api/chart/chart";


// export default {
//   name: 'Index',
//   components: {
//     PanelGroup,
//     LineChart,
//     RaddarChart,
//     PieChart,
//     BarChart
//   },
//   data() {
//     return {
//       lineChartDataList:[],
//       lineChartData:{}
//     }
//   },created(){
//     this.lineChartMethod();
//   },
//   methods: {
//     handleSetLineChartData(type) {
//       let data =this.lineChartDataList[type];
//       switch(type){
//         case 'totalPayAmount':
//           this.$refs.totalPayAmount =data;
//         break;
//         case 'totalExpressMoney':
//           this.$refs.totalExpressMoney =data;
//         break;
//         case 'totalPackageMoney':
//           this.$refs.totalPackageMoney =data;
//         break;
//           case 'totalProfitMoney':
//           this.$refs.totalProfitMoney =data;
//         break;
//            case 'totalOderNumber':
//           this.$refs.totalOderNumber =data;
//         break;
//       }
//     },lineChartMethod(){
//         let then =this;
//         lineChart().then(response => {
//             then.lineChartDataList = response;
//         });
//     }
//   }
// }
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
