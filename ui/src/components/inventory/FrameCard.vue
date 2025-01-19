<template>
  <el-tooltip
    :content="tooltipContent"
    placement="top"
    effect="light"
    :show-after="200"
  >
    <el-card class="frame-card" @click="$emit('click')">
      <div class="frame-header">
        {{ `${frameInfo.level}-${frameInfo.frame}号框` }}
      </div>
      <div class="frame-stats">
        <el-progress type="dashboard" :percentage="frameInfo.progress" />
        <div>匹配: {{ frameInfo.matched }}/{{ frameInfo.checked }}</div>
      </div>
    </el-card>
  </el-tooltip>
</template>

<script setup lang="ts">
import { computed } from 'vue'
interface StatusNum {
  matchStatusNum: number;
  notMatchStatusNum: number;
  fixedMatchStatusNum: number;
  errorStatusNum: number;
}

interface FrameInfo {
  level: number
  frame: number
  progress: number
  checked: number
  matched: number
  statusNum: StatusNum
}

const props = defineProps<{
  frameInfo: FrameInfo
}>()

const tooltipContent = computed(() => {
  const { statusNum } = props.frameInfo
  return `匹配数量: ${statusNum.matchStatusNum}
不匹配数量: ${statusNum.notMatchStatusNum}
待确定数量: ${statusNum.fixedMatchStatusNum}
识别错误数量: ${statusNum.errorStatusNum}`
})
</script>

<style scoped>
.frame-card {
  cursor: pointer;
  transition: all 0.3s;
}

.frame-card:hover {
  transform: translateY(-5px);
}

.frame-header {
  text-align: center;
  margin-bottom: 10px;
}

.frame-stats {
  text-align: center;
}
</style>
