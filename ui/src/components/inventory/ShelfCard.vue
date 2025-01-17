<template>
  <el-card class="shelf-card" @click="$emit('click')">
    <template #header>
      <div class="shelf-header">
        {{ shelf.name }}
      </div>
    </template>
    <div class="shelf-status">
      <el-progress :percentage="shelf.checkProgress" />
      <div class="shelf-stats">
        <span>总数：{{ shelf.totalBooks }}</span>
        <span>已盘：{{ shelf.checkedBooks }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
interface ShelfInfo {
  id: number
  name: string
  totalBooks: number
  checkedBooks: number
  checkProgress: number
}

defineProps<{
  shelf: ShelfInfo
}>()
</script>

<style scoped>
.shelf-card {
  cursor: pointer;
  transition: all 0.3s;
}

.shelf-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.shelf-header {
  font-weight: bold;
}

.shelf-status {
  margin-top: 10px;
}

.shelf-stats {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  color: #666;
  font-size: 14px;
}
</style>
