<template>
  <div class="space-y-4">
    <div class="bg-white p-4 rounded-lg shadow-sm space-y-3">
      <button
        @click="openModal"
        class="w-full bg-[#1C274C] disabled:bg-gray-400 text-white py-2 rounded-md hover:bg-[#142046] transition"
      >
        Add Activity
      </button>
    </div>
    <div class="overflow-x-auto rounded-lg shadow-sm">
      <table class="min-w-full text-left text-sm">
        <thead class="bg-gray-100 text-gray-700 uppercase text-xs">
          <tr>
            <th class="p-3">ID</th>
            <th class="p-3">Type</th>
            <th class="p-3">Description</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="activity in activityList" :key="activity.id" class="border-t hover:bg-gray-50">
            <td class="p-3">{{ activity.id }}</td>
            <td class="p-3">{{ activity.type }}</td>
            <td class="p-3">{{ activity.description }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Add Activity Modal -->
  <AddActivityModal v-model="showAddModal" :isSubmitting="isSubmitting" @submit="createActivity" />
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import api from '@/api/axios'

interface Activity {
  id: number
  type: string
  description: string
}

const activityList = ref<Activity[]>([])

async function fetchActivities() {
  try {
    const res = await api.get<Activity[]>('/activity')
    activityList.value = res.data
    console.log('Fetched activities:', activityList.value)
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  fetchActivities()
})

const showAddModal = ref(false)
const isSubmitting = ref(false)

import AddActivityModal from '@/components/ui/molecules/modals/AddActivityModal.vue'

async function createActivity(payload: { type: string; description: string }) {
  try {
    isSubmitting.value = true

    await api.post('/activity', payload)

    await fetchActivities()

    showAddModal.value = false
  } catch (err) {
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

function openModal() {
  showAddModal.value = true
}
</script>
