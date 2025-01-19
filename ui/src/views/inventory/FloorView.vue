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
          :key="floor"
          :label="`第${floor}层`"
          :value="floor"
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
import {ref, computed, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import PageContainer from '@/components/layout/PageContainer.vue'
import ShelfCard from '@/components/inventory/ShelfCard.vue'
import { getInventoryFloorView, getFloors } from '@/js/api/inventoryView.js'

const router = useRouter()
const currentFloor = ref<number>(1)

const floors = ref<number[]>([]);

interface StatusNum {
  matchStatusNum: number;
  notMatchStatusNum: number;
  fixedMatchStatusNum: number;
  errorStatusNum: number;
}

interface ShelfViewByFloor {
  shelfNum: number;
  statusNum: StatusNum;
}

const shelfArray = ref<ShelfViewByFloor[]>([]);

onMounted(async () => {
    floors.value = await getFloors();
    const floorNum = floors.value[0];
    shelfArray.value = await getInventoryFloorView(floorNum);
})


// interface ShelfTotalView {
//   id: number;
//   name: string;
//   matchNum: number;
//   totalNum: number;
//   matchRate: number;
// }

// 使用 computed 计算基于 shelfArray 的新数组
const shelves = computed(() => {
  return shelfArray.value.map(item => ({
    id: item.shelfNum, // shelfId = shelfNum
    name: `${currentFloor.value}层-${item.shelfNum}号书架`,
    matchNum: item.statusNum.matchStatusNum, // matchNum = matchStatusNum
    totalNum: item.statusNum.matchStatusNum
      + item.statusNum.notMatchStatusNum
      + item.statusNum.fixedMatchStatusNum
      + item.statusNum.errorStatusNum, // totalNum = matchStatusNum + notMatchStatusNum + fixedMatchStatusNum + errorStatusNum
    matchRate: item.statusNum.matchStatusNum /
      (item.statusNum.matchStatusNum
      + item.statusNum.notMatchStatusNum
      + item.statusNum.fixedMatchStatusNum
      + item.statusNum.errorStatusNum) // matchRate = matchStatusNum / totalNum
  }));
});

// 处理楼层变化的函数
// 处理楼层变化
const handleFloorChange = async (floorNum:number) => {
  shelfArray.value = await getInventoryFloorView(floorNum); // 获取并设置楼层数据
};

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
