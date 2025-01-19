<template>
  <page-container title="书架盘点">
    <template #actions>
      <el-select v-model="currentShelf" placeholder="请选择书架" @change="handleShelfChange" size="large"
        style="width: 200px; margin-right: 950px;">
        <el-option v-for="shelf in shelves" :key="shelf" :label="shelf" :value="shelf" />
      </el-select>
      <el-button-group>
        <el-button type="primary" :icon="ArrowUp" @click="changeShelf(getNextShelf(currentShelf, 1))"
          :disabled="!hasNextShelf(currentShelf, 1)">
          上一架
        </el-button>
        <el-button type="primary" :icon="ArrowDown" @click="changeShelf(getNextShelf(currentShelf, -1))"
          :disabled="!hasNextShelf(currentShelf, -1)">
          下一架
        </el-button>
      </el-button-group>
    </template>

    <div class="shelf-frames">
      <div v-for="level in 5" :key="level" class="shelf-level">
        <div class="level-label">第{{ level }}层</div>
        <div class="frames-row">
          <frame-card v-for="frame in 8" :key="frame" :frame-info="{
            level,
            frame,
            progress: getFrameProgress(level, frame),
            checked: getFrameChecked(level, frame),
            matched: getFrameMatched(level, frame),
            statusNum: getFrameStatus(level, frame)
          }" @click="goToBooks(level, frame)" />
        </div>
      </div>
    </div>
  </page-container>
</template>

<script setup lang="ts">
import { ref, onMounted} from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import PageContainer from '@/components/layout/PageContainer.vue'
import FrameCard from '@/components/inventory/FrameCard.vue'
import { getShelvesList, getFloorShelfInventoryStatus } from '@/js/api/inventoryView'

interface StatusNum {
  matchStatusNum: number;
  notMatchStatusNum: number;
  fixedMatchStatusNum: number;
  errorStatusNum: number;
}

interface FrameStatus {
  rownum: number;
  colnum: number;
  statusNum: StatusNum;
}

const router = useRouter()
const route = useRoute()
const floorId = route.params.floorId as string
const shelfId = route.params.shelfId as string
const currentShelf = ref(shelfId)

const shelves = ref<string[]>([])
const shelfStatus = ref<FrameStatus[]>([])

// 获取书架数据
const loadShelfData = async () => {
  try {
    shelves.value = await getShelvesList(floorId)
    await loadShelfStatus()
  } catch (error) {
    console.error('Failed to load shelves:', error)
  }
}

// 获取书架盘点状态
const loadShelfStatus = async () => {
  try {
    shelfStatus.value = await getFloorShelfInventoryStatus(floorId, currentShelf.value)
  } catch (error) {
    console.error('Failed to load shelf status:', error)
  }
}

onMounted(loadShelfData)

// 获取指定格子的盘点进度
const getFrameProgress = (level: number, frame: number) => {
  const frameStatus = shelfStatus.value.find(status => status.rownum === level && status.colnum === frame)
  if (!frameStatus) return 0

  const { statusNum } = frameStatus
  const total = statusNum.matchStatusNum + statusNum.notMatchStatusNum +
                statusNum.fixedMatchStatusNum + statusNum.errorStatusNum
  if (total === 0) return 0

  // 计算匹配的比例作为进度
  return Math.round((statusNum.matchStatusNum / total) * 100)
}

// 获取指定格子的已盘点数量
const getFrameChecked = (level: number, frame: number) => {
  const frameStatus = shelfStatus.value.find(status => status.rownum === level && status.colnum === frame)
  if (!frameStatus) return 0

  const { statusNum } = frameStatus
  // 返回该格子的总盘点数量
  return statusNum.matchStatusNum + statusNum.notMatchStatusNum +
         statusNum.fixedMatchStatusNum + statusNum.errorStatusNum
}

// 获取指定格子的匹配数量
const getFrameMatched = (level: number, frame: number) => {
  const frameStatus = shelfStatus.value.find(status => status.rownum === level && status.colnum === frame)
  if (!frameStatus) return 0
  return frameStatus.statusNum.matchStatusNum
}

// 获取指定格子的状态数据
const getFrameStatus = (level: number, frame: number) => {
  const frameStatus = shelfStatus.value.find(status => status.rownum === level && status.colnum === frame)
  if (!frameStatus) return {
    matchStatusNum: 0,
    notMatchStatusNum: 0,
    fixedMatchStatusNum: 0,
    errorStatusNum: 0
  }
  return frameStatus.statusNum
}

// 处理书架变更
const handleShelfChange = async (newShelfId: string) => {
  currentShelf.value = newShelfId
  await loadShelfStatus()
  router.push(`/inventory/shelf/${floorId}/${newShelfId}`)
}

// 获取下一个书架ID
const getNextShelf = (currentShelf: string, direction: number) => {
  const currentIndex = shelves.value.indexOf(currentShelf)
  const nextIndex = currentIndex + direction
  return shelves.value[nextIndex]
}

// 检查是否有下一个书架
const hasNextShelf = (currentShelf: string, direction: number) => {
  const currentIndex = shelves.value.indexOf(currentShelf)
  const nextIndex = currentIndex + direction
  return nextIndex >= 0 && nextIndex < shelves.value.length
}

// 切换书架
const changeShelf = (newShelfId: string) => {
  if (newShelfId) {
    handleShelfChange(newShelfId)
  }
}

const goToBooks = (level: number, frame: number) => {
  const frameId = `${level}-${frame}`
  router.push(`/inventory/books/${floorId}/${currentShelf.value}/${frameId}`)
}
</script>

<style scoped>
.shelf-container {
  padding: 20px;
}

.shelf-frames {
  margin-top: 20px;
}

.shelf-level {
  margin-bottom: 30px;
}

.level-label {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.frames-row {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 15px;
}

.frame-card {
  cursor: pointer;
  transition: all 0.3s;
}

.frame-card:hover {
  transform: translateY(-5px);
}

.frame-header {
  text-align: center;
  margin-bottom: 10px;
}

.frame-stats {
  text-align: center;
}
</style>
