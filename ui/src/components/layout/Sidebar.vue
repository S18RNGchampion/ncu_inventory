<template>
  <el-aside width="200px">
    <el-menu
      default-active="/"
      class="el-menu-vertical"
      :router="true"
      background-color="#304156"
      text-color="#fff"
    >
      <div class="logo-container">
        <img src="@/assets/logo.svg" class="logo-img" />
        <span class="logo-text">若依管理系统</span>
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
            :index="route.path + '/' + child.path"
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
  </el-aside>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const routes = computed(() => {
  return router.options.routes.filter(route => !route.meta?.hidden)
})
</script>

<style scoped>
.el-menu-vertical {
  height: 100%;
  border-right: none;
}

.logo-container {
  height: 50px;
  padding: 10px;
  display: flex;
  align-items: center;
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
}
</style>
