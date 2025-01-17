<template>
  <page-container
    :title="frameInfo"
    :show-back="true"
  >
    <div class="books-grid">
      <book-spine
        v-for="book in books"
        :key="book.id"
        :book="book"
        @click="showBookDetails(book)"
      />
    </div>

    <book-details-dialog
      v-model="dialogVisible"
      :book="selectedBook"
      @update:status="updateBookStatus"
    />
  </page-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import {useRoute } from 'vue-router'
import PageContainer from '@/components/layout/PageContainer.vue'
import BookSpine from '@/components/inventory/BookSpine.vue'
import BookDetailsDialog from '@/components/inventory/BookDetailsDialog.vue'

interface Book {
  id: number
  title: string
  callNumber: string
  author: string
  publisher: string
  year: number
  status: 'exists' | 'maybe' | 'notExists'
}

const route = useRoute()
const { floorId, shelfId, frameId } = route.params

const frameInfo = computed(() => {
  return `${floorId}层-${shelfId}号书架-${frameId}号框`
})

// 生成测试数据
const generateBooks = () => {
  const statusOptions: Book['status'][] = ['exists', 'maybe', 'notExists']
  return Array.from({ length: 40 }, (_, i) => ({
    id: i + 1,
    title: `书籍${i + 1}`,
    callNumber: `TP${Math.floor(Math.random() * 1000)}.${Math.floor(Math.random() * 100)}`,
    author: `作者${i + 1}`,
    publisher: `出版社${Math.floor(Math.random() * 10) + 1}`,
    year: 2000 + Math.floor(Math.random() * 24),
    status: statusOptions[Math.floor(Math.random() * 3)]
  }))
}

const books = ref(generateBooks())
const dialogVisible = ref(false)
const selectedBook = ref<Book | null>(null)

const showBookDetails = (book: Book) => {
  selectedBook.value = book
  dialogVisible.value = true
}

const updateBookStatus = (newStatus: Book['status']) => {
  if (selectedBook.value) {
    selectedBook.value.status = newStatus
  }
}
</script>

<style scoped>
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 30px);
  gap: 10px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
