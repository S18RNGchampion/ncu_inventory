<template>
  <div class="page-header">
    <div class="header-left">
      <el-icon class="fold-icon" @click="toggleSidebar"><Fold /></el-icon>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentRoute }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="header-right">
      <el-tooltip content="搜索" placement="bottom">
        <el-icon class="header-icon"><Search /></el-icon>
      </el-tooltip>
      <el-tooltip content="帮助" placement="bottom">
        <el-icon class="header-icon"><QuestionFilled /></el-icon>
      </el-tooltip>
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="header-icon"><FullScreen /></el-icon>
      </el-tooltip>
      <el-dropdown>
        <span class="user-info">
          <el-avatar :size="30" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <span class="username">admin</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Fold, Search, QuestionFilled, FullScreen } from '@element-plus/icons-vue'

const route = useRoute()
const currentRoute = computed(() => route.meta?.title || route.name)

const toggleSidebar = () => {
  // TODO: 实现侧边栏折叠功能
}
</script>

<style scoped>
.page-header {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.fold-icon {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-icon {
  font-size: 18px;
  color: #666;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
  color: #333;
}
</style>
