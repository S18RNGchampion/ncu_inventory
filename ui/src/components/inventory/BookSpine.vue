<template>
  <div class="book" :class="statusClass" @click="$emit('click')" :title="statusText">
    <div class="book-spine">{{ book.title }}</div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Book {
  id: number
  title: string
  status: number
}

const props = defineProps<{
  book: Book
}>()

// 计算状态对应的类名
const statusClass = computed(() => ({
  'status-match': props.book.status === 1,
  'status-not-match': props.book.status === 2,
  'status-fixed-match': props.book.status === 3,
  'status-error': props.book.status === 0
}))

// 计算状态对应的文字说明
const statusText = computed(() => {
  const statusMap = {
    0: '识别失败',
    1: '匹配',
    2: '未匹配',
    3: '待确定'
  }
  return statusMap[props.book.status as keyof typeof statusMap]
})
</script>

<style scoped>
.book {
  height: 200px;
  width: 30px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.book-spine {
  writing-mode: vertical-rl;
  text-orientation: mixed;
  height: 100%;
  padding: 5px;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border: 1px solid #dcdfe6;
}

.book:hover {
  transform: translateY(-5px);
}

/* 状态颜色 */
.status-match .book-spine {
  background: #f0f9eb;
  border-color: #67c23a;
}

.status-not-match .book-spine {
  background: #ffffff;
  border-color: #dcdfe6;
}

.status-fixed-match .book-spine {
  background: #fdf6ec;
  border-color: #e6a23c;
}

.status-error .book-spine {
  background: #fef0f0;
  border-color: #f56c6c;
}
</style>
