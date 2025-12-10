<template>
  <div class="space-y-4">
    <!-- Form Card -->
    <div class="bg-white p-4 rounded-lg shadow-sm space-y-3">
      <label class="block text-sm font-medium text-gray-700">Activity Type</label>

      <select
        v-model="selectedActivityId"
        @change="handleActivitySelection"
        class="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
      >
        <option disabled value="">Select activity type</option>

        <option value="add">➕ Add Activity</option>

        <option v-for="activity in activityList" :key="activity.id" :value="activity.id">
          {{ activity.type }} — {{ activity.description }}
        </option>
      </select>

      <button
        @click="logActivity"
        class="w-full bg-[#1C274C] disabled:bg-gray-400 text-white py-2 rounded-md hover:bg-[#142046] transition"
        :disabled="isPosting || !selectedActivityId"
      >
        {{ isPosting ? 'Logging...' : 'Log Activity' }}
      </button>
    </div>

    <!-- Activity Log Table -->
    <div class="overflow-x-auto bg-white rounded-lg shadow-sm">
      <table class="min-w-full text-left text-sm">
        <thead class="bg-gray-100 text-gray-700 uppercase text-xs">
          <tr>
            <th class="p-3">Activity</th>
            <th class="p-3">Description</th>
            <th class="p-3">Timestamp</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="log in activityLogList" :key="log.id" class="border-t hover:bg-gray-50">
            <td class="p-3">{{ log.activity.type }}</td>
            <td class="p-3">{{ log.activity.description }}</td>
            <td class="p-3">{{ new Date(log.createdAt).toLocaleString() }}</td>
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
import { useAuthStore } from '@/stores/auth'
import api from '@/api/axios'

const auth = useAuthStore()

const name = computed(() => auth.user?.name || '')

interface Activity {
  id: number
  type: string
  description: string
}

interface ActivityLogEntry {
  id: number
  activity: Activity
  createdAt: string
}

interface PaginatedActivityLog {
  content: ActivityLogEntry[]
  pageNumber: number
  pageSize: number
  totalPages: number
  totalElements: number
  last: boolean
  first: boolean
  numberOfElements: number
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

const activityLogList = ref<ActivityLogEntry[]>([])

async function fetchActivityLog() {
  try {
    const res = await api.get<PaginatedActivityLog>('/activities')
    activityLogList.value = res.data.content
    console.log('Fetched activity logs:', activityLogList.value)
  } catch (err) {
    console.error(err)
  }
}
onMounted(() => {
  fetchActivities()
  fetchActivityLog()
})

const selectedActivityId = ref<string | number>('')
const isPosting = ref(false)

async function logActivity() {
  if (selectedActivityId.value === 'add') {
    return
  }

  if (!selectedActivityId.value) return

  try {
    isPosting.value = true
    await api.post('/activities', {
      activityId: selectedActivityId.value,
    })

    // Refresh logs after posting
    await fetchActivityLog()
  } catch (err) {
    console.error(err)
  } finally {
    isPosting.value = false
  }
}

const showAddModal = ref(false)
const isSubmitting = ref(false)

function handleActivitySelection() {
  if (selectedActivityId.value === 'add') {
    showAddModal.value = true
    selectedActivityId.value = '' // reset select
  }
}

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
</script>

<style>
@keyframes modalIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-modal {
  animation: modalIn 0.15s ease-out forwards;
}
</style>
