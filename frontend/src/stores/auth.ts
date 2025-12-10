import { defineStore } from 'pinia'
import api from '../api/axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user') || 'null') as null | {
      id: number
      email: string
      name: string
    },
    token: localStorage.getItem('token') || null,
  }),

  actions: {
    async login(email: string, password: string) {
      const res = await api.post('/auth/login', { email, password })

      this.token = res.data.token
      localStorage.setItem('token', res.data.token)

      this.user = {
        id: res.data.user_id,
        email: res.data.email,
        name: res.data.name,
      }

      // Store user in localStorage
      localStorage.setItem('user', JSON.stringify(this.user))
    },
    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
