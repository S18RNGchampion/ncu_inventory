<template>
  <page-container title="楼层盘点">

    <template #actions>
      <el-select
        v-model="currentFloor"
        placeholder="请选择楼层"
        @change="handleFloorChange"
        size="large"
        style="width: 200px; margin-right: 950px;"
      >
        <el-option
          v-for="floor in floors"
          :key="floor.id"
          :label="floor.name"
          :value="floor.id"
        />
      </el-select>
      <el-button-group>
        <el-button
          type="primary"
          :icon="ArrowUp"
          @click="changeFloor(currentFloor + 1)"
          :disabled="currentFloor >= 18"
        >
          上一层
        </el-button>
        <el-button
          type="primary"
          :icon="ArrowDown"
          @click="changeFloor(currentFloor - 1)"
          :disabled="currentFloor <= 1"
        >
          下一层
        </el-button>
      </el-button-group>
    </template>

    <div class="shelves-grid" v-if="currentFloor">
      <shelf-card
        v-for="shelf in shelves"
        :key="shelf.id"
        :shelf="shelf"
        @click="goToShelf(shelf.id)"
      />
    </div>
  </page-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import PageContainer from '@/components/layout/PageContainer.vue'
import ShelfCard from '@/components/inventory/ShelfCard.vue'

const router = useRouter()
const currentFloor = ref<number>(1)

// 生成楼层数据
const floors = Array.from({ length: 18 }, (_, i) => ({
  id: i + 1,
  name: `${i + 1}层`
}))

// 生成书架数据
const shelves = computed(() => {
  return Array.from({ length: 18 }, (_, i) => ({
    id: i + 1,
    name: `${currentFloor.value}层-${i + 1}号书架`,
    totalBooks: 1600,
    checkedBooks: Math.floor(Math.random() * 1600),
    checkProgress: Math.floor(Math.random() * 100)
  }))
})

const handleFloorChange = (floorId: number) => {
  router.push(`/inventory/floor/${floorId}`)
}

const changeFloor = (floor: number) => {
  if (floor >= 1 && floor <= 18) {
    currentFloor.value = floor
    handleFloorChange(floor)
  }
}

const goToShelf = (shelfId: number) => {
  router.push(`/inventory/shelf/${currentFloor.value}/${shelfId}`)
}
</script>

<style scoped>

.shelves-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}
</style>
