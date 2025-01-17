<template>
  <page-container title="书架盘点">
    <template #actions>
      <el-select
        v-model="currentShelf"
        placeholder="请选择书架"
        @change="handleShelfChange"
        size="large"
        style="width: 200px; margin-right: 950px;"
      >
        <el-option
          v-for="shelf in shelves"
          :key="shelf.id"
          :label="`${shelf.id}号书架`"
          :value="shelf.id"
        />
      </el-select>
      <el-button-group>
        <el-button
          type="primary"
          :icon="ArrowUp"
          @click="changeShelf(Number(shelfId) + 1)"
          :disabled="Number(shelfId) >= 18"
        >
          上一架
        </el-button>
        <el-button
          type="primary"
          :icon="ArrowDown"
          @click="changeShelf(Number(shelfId) - 1)"
          :disabled="Number(shelfId) <= 1"
        >
          下一架
        </el-button>
      </el-button-group>
    </template>

    <div class="shelf-frames">
      <div v-for="level in 5" :key="level" class="shelf-level">
        <div class="level-label">第{{ level }}层</div>
        <div class="frames-row">
          <frame-card
            v-for="frame in 8"
            :key="frame"
            :frame-info="{
              level,
              frame,
              progress: getRandomProgress(),
              checked: getRandomChecked()
            }"
            @click="goToBooks(level, frame)"
          />
        </div>
      </div>
    </div>
  </page-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import PageContainer from '@/components/layout/PageContainer.vue'
import FrameCard from '@/components/inventory/FrameCard.vue'

const router = useRouter()
const route = useRoute()
const floorId = route.params.floorId
const shelfId = route.params.shelfId
const currentShelf = ref(Number(shelfId))

// 生成书架数据
const shelves = Array.from({ length: 18 }, (_, i) => ({
  id: i + 1,
  name: `${i + 1}号书架`
}))

const handleShelfChange = (newShelfId: number) => {
  router.push(`/inventory/shelf/${floorId}/${newShelfId}`)
}

const changeShelf = (newShelfId: number) => {
  if (newShelfId >= 1 && newShelfId <= 18) {
    currentShelf.value = newShelfId
    handleShelfChange(newShelfId)
  }
}

const goToBooks = (level: number, frame: number) => {
  const frameId = `${level}-${frame}`
  router.push(`/inventory/books/${floorId}/${shelfId}/${frameId}`)
}

const getRandomProgress = () => Math.floor(Math.random() * 100)
const getRandomChecked = () => Math.floor(Math.random() * 40)
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
