<template>
  <div class="book" :class="book.status" @click="$emit('click')">
    <div class="book-spine">{{ book.title }}</div>
  </div>
</template>

<script setup lang="ts">
interface Book {
  id: number
  title: string
  status: 'exists' | 'maybe' | 'notExists'
}

defineProps<{
  book: Book
}>()
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
  background: #fff;
  border: 1px solid #dcdfe6;
}

.book:hover {
  transform: translateY(-5px);
}

.book.exists .book-spine {
  background: #f0f9eb;
  border-color: #67c23a;
}

.book.maybe .book-spine {
  background: #fdf6ec;
  border-color: #e6a23c;
}

.book.notExists .book-spine {
  background: #fef0f0;
  border-color: #f56c6c;
}
</style>
