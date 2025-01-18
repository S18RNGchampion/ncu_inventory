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
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container" ref="pieChartRef1"></div>
        </el-card>
      </el-col>
      <!-- 堆叠柱状图：盘点进度 -->


      <!-- 扇形图：所有书籍状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>图书状态分布</span>
              <el-tooltip content="展示所有图书的状态分布情况">
                <el-icon>
                  <QuestionFilled/>
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
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container" ref="barChartRef"></div>
        </el-card>
      </el-col>
    </el-row>
  </page-container>
</template>

<script setup lang="ts">
import {ref, onMounted, watch, computed} from 'vue'
import {QuestionFilled} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import PageContainer from '@/components/layout/PageContainer.vue'
import {getTotalView} from '@/js/api/TotalView'

const pieChartRef1 = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
const barChartRef = ref<HTMLElement>()

interface StatusNum {
  matchStatusNum: number;
  notMatchStatusNum: number;
  fixedMatchStatusNum: number;
  errorStatusNum: number;
}

interface TotalData {
  totalNum: number;
  inventoryNum: number;
  statusNum: StatusNum;
  floorStatusList: Record<string, StatusNum>;
}

// 存储后端返回的数据
const totalData = ref<TotalData>({
  totalNum: 0,
  inventoryNum: 0,
  statusNum: {
    matchStatusNum: 0,
    notMatchStatusNum: 0,
    fixedMatchStatusNum: 0,
    errorStatusNum: 0
  },
  floorStatusList: {}
})

// 计算总数据
const totalChecked = computed(() => totalData.value.inventoryNum)
const totalUnchecked = computed(() => totalData.value.totalNum - totalData.value.inventoryNum)
const totalBooks = computed(() => totalData.value.totalNum)

// 初始化图表
let pieChart: echarts.ECharts
let barChart: echarts.ECharts

onMounted(async () => {
  const result = await getTotalView();
  totalData.value = result;

  // 初始化盘点情况饼图
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
          {value: totalChecked.value, name: '已盘点'},
          {value: totalUnchecked.value, name: '未盘点'}
        ]
      }
    ]
  })

  // 初始化图书状态分布饼图
  pieChart = echarts.init(pieChartRef.value!)
  pieChart.setOption({
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
        {value: totalData.value.statusNum.matchStatusNum, name: '匹配'},
        {value: totalData.value.statusNum.fixedMatchStatusNum, name: '待确定'},
        {value: totalData.value.statusNum.notMatchStatusNum, name: '未匹配'},
        {value: totalData.value.statusNum.errorStatusNum, name: '识别失败'}
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

  // 初始化各楼层状态柱状图
  const floorData = totalData.value.floorStatusList;
  const floors = Object.keys(floorData).sort((a, b) => Number(a) - Number(b));

  barChart = echarts.init(barChartRef.value!)
  barChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['匹配', '待确定', '未匹配', '识别失败']
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
        name: '匹配',
        type: 'bar',
        data: floors.map(floor => floorData[floor]?.matchStatusNum || 0)
      },
      {
        name: '待确定',
        type: 'bar',
        data: floors.map(floor => floorData[floor]?.fixedMatchStatusNum || 0)
      },
      {
        name: '未匹配',
        type: 'bar',
        data: floors.map(floor => floorData[floor]?.notMatchStatusNum || 0)
      },
      {
        name: '识别失败',
        type: 'bar',
        data: floors.map(floor => floorData[floor]?.errorStatusNum || 0)
      }
    ]
  })
})

// 更新折线图数据


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
