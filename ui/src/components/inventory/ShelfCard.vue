<template>
  <el-card class="shelf-card" @click="$emit('click')">
    <template #header>
      <div class="shelf-header">
        {{ shelf.name }}
      </div>
    </template>
    <div class="shelf-status">
      <!-- 保留两位小数 -->
      <el-progress :percentage="formattedRate" />
      <div class="shelf-stats" style="display: flex; justify-content: space-between;">
        <span>匹配数：{{ shelf.matchNum }}</span>
        <span>总数量：{{ shelf.totalNum }}</span>
      </div>
    </div>
  </el-card>
</template>

<script>
import { computed } from 'vue';

export default {
  props: {
    shelf: {
      type: Object,
      required: true,
    },
  },
  setup(props) {
    // 格式化 matchRate 为保留两位小数的百分比
    const formattedRate = computed(() => {
      return (parseFloat(props.shelf.matchRate) * 100).toFixed(2);
    });

    return {
      formattedRate,
    };
  },
};
</script>
