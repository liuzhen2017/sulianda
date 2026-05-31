<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { lineChart,getProfit } from "@/api/chart/chart";
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.getLineChart();
      this.chart = echarts.init(this.$el, 'macarons')
    },getLineChart(){
      let then =this;
      getProfit().then(response => {

        this.setOptions(response.xAxis,response.legend,response.series);
          // this.$emit('lineChartData',response);
      });
    },setOptions(xAxisData, legendData,seriesData) {
      console.log(JSON.stringify(xAxisData));
      console.log(JSON.stringify(legendData));
      console.log(JSON.stringify(seriesData));
      this.chart.setOption({
        xAxis: {
          data: xAxisData,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data:legendData
        },
        series: [
                 // {"data":[15],"name":"充值金额","smooth":true}
                 ,{"data":[1500.94],"type":"line","name":"总快递费","smooth":false}
                 ,{"data":[4],"type":"line","name":"总打包费","smooth":true}
                  ,{"data":[6],"type":"line","name":"订单数量","smooth":true}
                ]
        })
    }
  }
}
</script>
