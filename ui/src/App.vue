<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <AppSidebar />
    </el-aside>
    <el-container>
      <el-header height="50px">
        <PageHeader />
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import AppSidebar from '@/components/layout/AppSidebar.vue'
import PageHeader from '@/components/layout/PageHeader.vue'
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  height: 100vh;
}

.layout-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.layout-container > .el-container {
  height: 100%;
}

.el-aside {
  background-color: #304156;
  height: 100%;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
