<template>
  <header class="flex items-center justify-between p-4 bg-white shadow-sm">
    <h1 class="text-lg font-semibold text-[#1C274C]">Student Activity Tracker</h1>

    <div class="flex items-center gap-4">
      <span class="text-sm text-gray-700">Welcome, {{ name }}</span>

      <button
        @click="logout"
        class="px-3 py-1.5 rounded-md text-sm font-medium text-[#1C274C] border border-[#1C274C] hover:bg-[#f0f2f8] transition"
      >
        Logout
      </button>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()

const name = computed(() => auth.user?.name || '')
function logout() {
  auth.logout()
  window.location.href = '/login'
}
</script>
