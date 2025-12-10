<template>
  <div class="overflow-x-auto bg-white rounded-lg shadow-sm">
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
</script>
