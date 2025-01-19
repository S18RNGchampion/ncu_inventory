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
        @click="showBookDetails(book.id)"
      />
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="图书详情"
      width="500px"
      destroy-on-close
    >
      <div v-if="selectedBook" class="book-details">
        <div class="detail-row">
          <span class="label">书名：</span>
          <span class="value">{{ selectedBook.name }}</span>
        </div>
        <div class="detail-row">
          <span class="label">作者：</span>
          <span class="value">{{ selectedBook.author || '暂无' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">ISBN：</span>
          <span class="value">{{ selectedBook.isbn || '暂无' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">索书号：</span>
          <span class="value">{{ selectedBook.callNumber || '暂无' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">条形码：</span>
          <span class="value">{{ selectedBook.barcode || '暂无' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">馆藏地：</span>
          <span class="value">{{ selectedBook.address || '暂无' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">状态：</span>
          <span class="value" :class="statusClass">{{ statusText }}</span>
        </div>
      </div>
    </el-dialog>
  </page-container>
</template>

<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {useRoute } from 'vue-router'
import PageContainer from '@/components/layout/PageContainer.vue'
import BookSpine from '@/components/inventory/BookSpine.vue'
import {getInventoryBooksByFrame, getBookDetailInfo} from "@/js/api/inventoryView";
import { ElMessage } from 'element-plus'

const route = useRoute();  // 获取当前路由对象

// 从路由参数中提取值
const floorId = route.params.floorId;
const shelfId = route.params.shelfId;
const rowNum = route.params.rowNum;
const colNum = route.params.colNum;

export interface BooksResp {
  id: number;
  newbarcode: string;
  status: number;
  createdtime: string;
  floorname: number;
  shelf: string;
  rownum: number;
  colnum: number;
  name: string;
}

const BooksRespList = ref<BooksResp[]>([]);

onMounted(async () => {
  BooksRespList.value = await getInventoryBooksByFrame(floorId,shelfId, rowNum, colNum);
})

const books = computed(() => {
  return BooksRespList.value.map(item => ({
    id: item.id,
    title: item.name ? item.name : '空',  // 如果 item.name 为 null，则赋值 '空'
    status: item.status
  }));
});


export interface BookInfoResp {
  id: number | null; // 对应 Integer，可能为 null
  newbarcode: string | null; // 对应 String，可能为 null
  status: number | null; // 对应 Integer，可能为 null
  createdtime: string | null; // 对应 Date，使用 ISO 格式的字符串表示
  floorname: number; // 对应 int
  shelf: string; // 对应 String
  rownum: number; // 对应 int
  colnum: number; // 对应 int
  address: string | null; // 对应 String，可能为 null
  barcode: string | null; // 对应 String，可能为 null
  propertyNumber: string | null; // 对应 String，可能为 null
  callNumber: string | null; // 对应 String，可能为 null
  name: string; // 对应 String
  author: string | null; // 对应 String，可能为 null
  isbn: string | null; // 对应 String，可能为 null
  publishingHouse: string | null; // 对应 String，可能为 null
  publishYear: string | null; // 对应 Date，可能为 null
  fixedPrice: string | null; // 对应 BigDecimal，使用字符串来表示
  discountedPrice: string | null; // 对应 BigDecimal，使用字符串来表示
  shelfNumber: string | null; // 对应 String，可能为 null
  bookStatus: string | null; // 对应 String，可能为 null
  collectionTime: string | null; // 对应 LocalDateTime，使用 ISO 格式的字符串表示
  carrierType: string | null; // 对应 String，可能为 null
  borrowingAttributes: string | null; // 对应 String，可能为 null
  annex: string | null; // 对应 String，可能为 null
  illustrate: string | null; // 对应 String，可能为 null
}

const frameInfo = computed(() => {
  return `${floorId}层-${shelfId}号书架-${shelfId}号框`
})

const dialogVisible = ref(false)
const selectedBook = ref<BookInfoResp | null>(null)

const showBookDetails = async (bookId: number) => {
  try {
    const response = await getBookDetailInfo(bookId)
    selectedBook.value = response
    dialogVisible.value = true
  } catch (error) {
    console.error('获取图书详情失败:', error)
    ElMessage.error('获取图书详情失败')
  }
}

// 状态相关的计算属性
const statusClass = computed(() => {
  if (!selectedBook.value) return ''
  const statusMap = {
    0: 'status-error',
    1: 'status-match',
    2: 'status-not-match',
    3: 'status-fixed-match'
  }
  return statusMap[selectedBook.value.status as keyof typeof statusMap]
})

const statusText = computed(() => {
  if (!selectedBook.value) return ''
  const statusMap = {
    0: '识别失败',
    1: '匹配',
    2: '未匹配',
    3: '待确定'
  }
  return statusMap[selectedBook.value.status as keyof typeof statusMap]
})

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

.book-details {
  padding: 20px;
}

.detail-row {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
}

.label {
  width: 80px;
  color: #606266;
  font-weight: 500;
}

.value {
  flex: 1;
  color: #303133;
}

/* 状态颜色 */
.status-match {
  color: #67c23a;
}

.status-not-match {
  color: #f56c6c;
}

.status-fixed-match {
  color: #e6a23c;
}

.status-error {
  color: #909399;
}
</style>
