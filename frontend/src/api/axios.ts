import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // change later if needed
})

// read token from localStorage if exists
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
