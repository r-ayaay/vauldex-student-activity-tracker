// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import DashboardLayout from '../components/layouts/dashboard-layout.vue'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue'
import StudentActivity from '../pages/StudentActivity.vue'
import Activity from '@/pages/Activity.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { requiresGuest: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => Register,
      meta: { requiresGuest: true },
    },
    {
      path: '',
      component: DashboardLayout,
      children: [
        { path: 'dashboard', name: 'ActivityOverview', component: StudentActivity },
        { path: 'activities', name: 'activities', component: Activity },
      ],
      meta: { requiresAuth: true },
    },

    // default redirect
    { path: '/:pathMatch(.*)*', redirect: '/login' },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  // If route requires auth and user is NOT logged in
  if (to.meta.requiresAuth && !auth.token) {
    return '/login'
  }

  // If the route is login/register but user is already logged in
  if (to.meta.requiresGuest && auth.token) {
    return '/dashboard'
  }
})

export default router
