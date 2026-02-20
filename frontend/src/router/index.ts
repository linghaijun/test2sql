import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/query'
  },
  {
    path: '/query',
    name: 'Query',
    component: () => import('@/views/QueryView.vue'),
    meta: { title: 'Text2SQL - 查询' }
  },
  {
    path: '/database',
    name: 'Database',
    component: () => import('@/views/DatabaseView.vue'),
    meta: { title: 'Text2SQL - 数据库管理' }
  },
  {
    path: '/history',
    name: 'History',
    component: () => import('@/views/HistoryView.vue'),
    meta: { title: 'Text2SQL - 查询历史' }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundView.vue'),
    meta: { title: 'Text2SQL - 404' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = (to.meta.title as string) || 'Text2SQL'
  next()
})

export default router
