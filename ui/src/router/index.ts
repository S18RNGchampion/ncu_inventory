import { createRouter, createWebHistory } from 'vue-router'
import { House, Document, Reading, DataAnalysis } from '@element-plus/icons-vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
      meta: {
        title: '图书馆总览',
        icon: House
      }
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: () => import('@/views/InventoryView.vue'),
      meta: {
        title: '图书盘点',
        icon: Document
      },
      children: [
        {
          path: 'floor/:floorId',
          name: 'inventoryFloor',
          component: () => import('@/views/inventory/FloorView.vue'),
          meta: {
            title: '楼层盘点'
          }
        },
        {
          path: 'shelf/:floorId/:shelfId',
          name: 'inventoryShelf',
          component: () => import('@/views/inventory/ShelfView.vue'),
          meta: {
            title: '书架盘点',
            hide: true
          }
        },
        {
          path: 'books/:floorId/:shelfId/:frameId',
          name: 'inventoryBooks',
          component: () => import('@/views/inventory/BooksView.vue'),
          meta: {
            title: '图书盘点',
            hide: true
          }
        }
      ]
    },
    {
      path: '/books',
      name: 'books',
      component: () => import('@/views/BooksView.vue'),
      meta: {
        title: '图书管理',
        icon: Reading
      },
      children: [
        {
          path: 'list',
          name: 'booksList',
          component: () => import('@/views/books/ListView.vue'),
          meta: {
            title: '图书列表'
          }
        },
        {
          path: 'category',
          name: 'booksCategory',
          component: () => import('@/views/books/CategoryView.vue'),
          meta: {
            title: '分类管理'
          }
        }
      ]
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: () => import('@/views/StatisticsView.vue'),
      meta: {
        title: '统计分析',
        icon: DataAnalysis
      },
      children: [
        {
          path: 'overview',
          name: 'statisticsOverview',
          component: () => import('@/views/statistics/OverviewView.vue'),
          meta: {
            title: '盘点统计'
          }
        }
      ]
    }
  ]
})

export default router
