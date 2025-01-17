<template>
  <el-menu
    class="sidebar-menu"
    :default-active="activeMenu"
    :collapse="isCollapse"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409EFF"
    router
  >
    <div class="logo-container">
      <img src="@/assets/logo.svg" class="logo-img" />
      <span class="logo-text">图书盘点系统</span>
    </div>

    <template v-for="route in routes" :key="route.path">
      <!-- 有子路由的菜单项 -->
      <el-sub-menu v-if="route.children" :index="route.path">
        <template #title>
          <el-icon v-if="route.meta?.icon">
            <component :is="route.meta.icon"></component>
          </el-icon>
          <span>{{ route.meta?.title }}</span>
        </template>
        <el-menu-item
          v-for="child in route.children"
          :key="child.path"
          :index="`${route.path}/${child.path}`"
        >
          {{ child.meta?.title }}
        </el-menu-item>
      </el-sub-menu>

      <!-- 没有子路由的菜单项 -->
      <el-menu-item v-else :index="route.path">
        <el-icon v-if="route.meta?.icon">
          <component :is="route.meta.icon"></component>
        </el-icon>
        <span>{{ route.meta?.title }}</span>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)

const routes = computed(() => {
  return router.options.routes.filter(route => {
    if (route.meta?.title && !route.meta?.hide) {
      if (route.children) {
        route.children = route.children.filter(child => !child.meta?.hide)
      }
      return true
    }
    return false
  })
})

const activeMenu = computed(() => {
  return route.path
})
</script>

<style scoped>
.sidebar-menu {
  height: 100%;
  border-right: none;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 200px;
}

.logo-container {
  height: 50px;
  padding: 10px;
  display: flex;
  align-items: center;
  background-color: #304156;
  color: white;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.logo-text {
  font-size: 16px;
  font-weight: bold;
  color: #bfcbd9;
}

:deep(.el-menu-item.is-active) {
  background-color: #263445;
}

:deep(.el-sub-menu__title:hover),
:deep(.el-menu-item:hover) {
  background-color: #263445 !important;
}
</style>
