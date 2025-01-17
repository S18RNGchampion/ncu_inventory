<template>
  <el-card class="frame-card" @click="$emit('click')">
    <div class="frame-header">
      {{ `${frameInfo.level}-${frameInfo.frame}号框` }}
    </div>
    <div class="frame-stats">
      <el-progress type="dashboard" :percentage="frameInfo.progress" />
      <div>已盘: {{ frameInfo.checked }}/40</div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
interface FrameInfo {
  level: number
  frame: number
  progress: number
  checked: number
}

defineProps<{
  frameInfo: FrameInfo
}>()
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
