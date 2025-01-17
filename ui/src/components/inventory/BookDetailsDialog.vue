<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="emit('update:modelValue', $event)"
    :title="book?.title"
    width="500px"
  >
    <template v-if="book">
      <div class="book-details">
        <p><strong>索书号：</strong>{{ book.callNumber }}</p>
        <p><strong>作者：</strong>{{ book.author }}</p>
        <p><strong>出版社：</strong>{{ book.publisher }}</p>
        <p><strong>出版年份：</strong>{{ book.year }}</p>
        <p><strong>状态：</strong>{{ bookStatusMap[book.status] }}</p>
      </div>
      <div class="status-actions">
        <el-radio-group :model-value="book.status" @change="handleStatusChange">
          <el-radio-button label="exists">存在</el-radio-button>
          <el-radio-button label="maybe">可能存在</el-radio-button>
          <el-radio-button label="notExists">不存在</el-radio-button>
        </el-radio-group>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
interface Book {
  id: number
  title: string
  callNumber: string
  author: string
  publisher: string
  year: number
  status: 'exists' | 'maybe' | 'notExists'
}

defineProps<{
  modelValue: boolean
  book: Book | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'update:status': [status: Book['status']]
}>()

const bookStatusMap = {
  exists: '存在',
  maybe: '可能存在',
  notExists: '不存在'
} as const

const handleStatusChange = (status: Book['status']) => {
  emit('update:status', status)
}
</script>

<style scoped>
.book-details {
  margin-bottom: 20px;
}

.book-details p {
  margin: 10px 0;
}

.status-actions {
  text-align: center;
}
</style>
