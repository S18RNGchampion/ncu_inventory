<template>
  <page-container title="数据概览">
    <el-row :gutter="20">
      <!-- 总体盘点数据统计 -->
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>总体盘点情况</span>
            </div>
          </template>
          <div class="total-statistics">
            <div class="stat-item">
              <div class="stat-value">{{ totalChecked }}</div>
              <div class="stat-label">已盘点数量</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ totalUnchecked }}</div>
              <div class="stat-label">未盘点数量</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ totalBooks }}</div>
              <div class="stat-label">总藏书数量</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>图书盘点状况</span>
              <el-tooltip content="展示所有图书的状态分布情况">
                <el-icon>
                  <QuestionFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container" ref="pieChartRef1"></div>
        </el-card>
      </el-col>
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

      <!-- 扇形图：所有书籍状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>图书状态分布</span>
              <el-tooltip content="展示所有图书的状态分布情况">
                <el-icon>
                  <QuestionFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container" ref="pieChartRef"></div>
        </el-card>
      </el-col>

      <!-- 柱状图：每层书籍数量和状态 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>各楼层图书状态</span>
              <el-tooltip content="展示每层图书的数量和状态分布">
                <el-icon>
                  <QuestionFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container" ref="barChartRef"></div>
        </el-card>
      </el-col>

      <!-- 折线图：状态变化趋势 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>状态变化趋势</span>
              <el-select v-model="selectedFloor" placeholder="选择楼层" style="width: 120px">
                <el-option v-for="floor in floors" :key="floor" :label="`${floor}层`" :value="floor" />
              </el-select>
            </div>
          </template>
          <div class="chart-container" ref="lineChartRef"></div>
        </el-card>
      </el-col>


    </el-row>
  </page-container>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { QuestionFilled } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import PageContainer from '@/components/layout/PageContainer.vue'

const pieChartRef1 = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
const barChartRef = ref<HTMLElement>()
const lineChartRef = ref<HTMLElement>()
const stackBarChartRef = ref<HTMLElement>()
const selectedFloor = ref(1)
const floors = Array.from({ length: 18 }, (_, i) => i + 1)
const completionRate = ref(68)
// 生成随机数据（实际项目中应该从API获取）
const checkedData = floors.map(() => Math.floor(Math.random() * 800))
const uncheckedData = floors.map(() => Math.floor(Math.random() * 400))

// 计算总数据
const totalChecked = computed(() => checkedData.reduce((sum, curr) => sum + curr, 0))
const totalUnchecked = computed(() => uncheckedData.reduce((sum, curr) => sum + curr, 0))
const totalBooks = computed(() => totalChecked.value + totalUnchecked.value)

// 初始化图表
let pieChart: echarts.ECharts
let barChart: echarts.ECharts
let lineChart: echarts.ECharts
let stackBarChart: echarts.ECharts

onMounted(() => {

  pieChart = echarts.init(pieChartRef1.value!)
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

  // 初始化扇形图
  pieChart = echarts.init(pieChartRef.value!)
  pieChart.setOption({
    title: {
      text: '图书状态分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      type: 'pie',
      radius: '50%',
      data: [
        { value: 15000, name: '存在' },
        { value: 3000, name: '可能存在' },
        { value: 2000, name: '不存在' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  })

  // 初始化柱状图
  barChart = echarts.init(barChartRef.value!)
  barChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['存在', '可能存在', '不存在']
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
        name: '存在',
        type: 'bar',
        data: floors.map(() => Math.floor(Math.random() * 1000))
      },
      {
        name: '可能存在',
        type: 'bar',
        data: floors.map(() => Math.floor(Math.random() * 200))
      },
      {
        name: '不存在',
        type: 'bar',
        data: floors.map(() => Math.floor(Math.random() * 100))
      }
    ]
  })

  // 初始化折线图
  updateLineChart()

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
        data: floors.map(() => Math.floor(Math.random() * 800))
      },
      {
        name: '未盘点',
        type: 'bar',
        stack: 'total',
        data: floors.map(() => Math.floor(Math.random() * 400))
      }
    ]
  })
})

// 更新折线图数据
const updateLineChart = () => {
  if (!lineChart) {
    lineChart = echarts.init(lineChartRef.value!)
  }

  const dates = Array.from({ length: 7 }, (_, i) => {
    const date = new Date()
    date.setDate(date.getDate() - i)
    return date.toLocaleDateString()
  }).reverse()

  lineChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['存在', '可能存在', '不存在']
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '存在',
        type: 'line',
        data: dates.map(() => Math.floor(Math.random() * 1000))
      },
      {
        name: '可能存在',
        type: 'line',
        data: dates.map(() => Math.floor(Math.random() * 200))
      },
      {
        name: '不存在',
        type: 'line',
        data: dates.map(() => Math.floor(Math.random() * 100))
      }
    ]
  })
}

// 监听楼层变化
watch(selectedFloor, () => {
  updateLineChart()
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
  height: 200px;
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
