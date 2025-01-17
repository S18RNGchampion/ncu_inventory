<template>
  <page-container title="数据概览">
    <el-row :gutter="20">
      <!-- 堆叠柱状图：盘点进度 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>盘点进度</span>
              <el-tag type="success">完成率：{{ completionRate }}%</el-tag>
            </div>
          </template>
          <div class="chart-container" ref="stackBarChartRef"></div>
        </el-card>
      </el-col>
      <!-- 扇形图：总体盘点比例 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>总体盘点比例</span>
            </div>
          </template>
          <div class="chart-container" ref="pieChartRef"></div>
        </el-card>
      </el-col>
    </el-row>
  </page-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'
import PageContainer from '@/components/layout/PageContainer.vue'

const stackBarChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
const floors = Array.from({ length: 18 }, (_, i) => i + 1)

// 生成随机数据（实际项目中应该从API获取）
const checkedData = floors.map(() => Math.floor(Math.random() * 800))
const uncheckedData = floors.map(() => Math.floor(Math.random() * 400))

// 计算总数据
const totalChecked = computed(() => checkedData.reduce((sum, curr) => sum + curr, 0))
const totalUnchecked = computed(() => uncheckedData.reduce((sum, curr) => sum + curr, 0))
const totalBooks = computed(() => totalChecked.value + totalUnchecked.value)
const completionRate = computed(() => Math.round((totalChecked.value / totalBooks.value) * 100))

// 初始化图表
let stackBarChart: echarts.ECharts
let pieChart: echarts.ECharts

onMounted(() => {
  // 初始化堆叠柱状图
  stackBarChart = echarts.init(stackBarChartRef.value!)
  stackBarChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['已盘点', '未盘点']
    },
    xAxis: {
      type: 'category',
      data: floors.map(f => `${f}层`)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '已盘点',
        type: 'bar',
        stack: 'total',
        data: checkedData
      },
      {
        name: '未盘点',
        type: 'bar',
        stack: 'total',
        data: uncheckedData
      }
    ]
  })

  // 初始化扇形图
  pieChart = echarts.init(pieChartRef.value!)
  pieChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['已盘点', '未盘点']
    },
    series: [
      {
        name: '盘点情况',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {d}%'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: totalChecked.value, name: '已盘点' },
          { value: totalUnchecked.value, name: '未盘点' }
        ]
      }
    ]
  })
})

// 监听窗口大小变化
window.addEventListener('resize', () => {
  stackBarChart?.resize()
  pieChart?.resize()
})
</script>

<style scoped>
.chart-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.total-statistics {
  display: flex;
  justify-content: space-around;
  padding: 20px;
  height: 300px;
  align-items: center;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

:deep(.el-card__header) {
  padding: 10px 20px;
}
</style>
