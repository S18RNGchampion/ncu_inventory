<template>
 <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header" v-if="showHeader">
      <el-page-header
        v-if="showBack"
        @back="handleBack"
        :title="title"
      />
      <h2 v-else class="page-title">{{ title }}</h2>

      <!-- 头部右侧操作区 -->
      <div class="header-actions">
        <slot name="actions"></slot>
      </div>
    </div>

    <!-- 页面主体内容 -->
    <div class="page-content" :class="{ 'no-padding': noPadding }">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

withDefaults(defineProps<{
  title?: string
  showBack?: boolean
  showHeader?: boolean
  noPadding?: boolean
}>(), {
  title: '',
  showBack: false,
  showHeader: true,
  noPadding: false
})

const router = useRouter()
const handleBack = () => router.back()
</script>

<style scoped>
.page-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
}

.page-header {
  padding: 16px 20px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 16px;
  font-weight: 500;
  color: #1f2f3d;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.page-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.page-content.no-padding {
  padding: 0;
}
</style>
