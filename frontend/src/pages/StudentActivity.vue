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
        class="w-full bg-[#1C274C] disabled:bg-gray-400 text-white py-2 rounded-md hover:bg-[#142046] transition cursor-pointer"
        :disabled="isPosting || !selectedActivityId"
      >
        {{ isPosting ? 'Logging...' : 'Log Activity' }}
      </button>
    </div>

    <div class="bg-white p-4 rounded-lg shadow-sm flex space-x-3 items-end">
      <div class="flex-1">
        <label class="block text-sm font-medium text-gray-700">Filter by Activity</label>
        <select v-model="filterType" class="w-full border rounded-md p-2">
          <option value="">All</option>
          <option v-for="activity in activityList" :key="activity.id" :value="activity.type">
            {{ activity.type }}
          </option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700">Start Date</label>
        <input type="date" v-model="filterStartDate" class="border rounded-md p-2" />
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700">End Date</label>
        <input type="date" v-model="filterEndDate" class="border rounded-md p-2" />
      </div>

      <button
        @click="applyFilters"
        class="bg-[#1C274C] text-white py-2 px-4 rounded-md hover:bg-[#142046] transition cursor-pointer"
      >
        Apply
      </button>

      <button
        @click="resetFilters"
        class="bg-gray-200 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-300 transition cursor-pointer"
      >
        Reset
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
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in activityLogList" :key="log.id" class="border-t hover:bg-gray-50">
            <td class="p-3 cursor-pointer" @click="editActivity(log)">
              <template v-if="editingActivityId === log.id">
                <select v-model="editingActivityValue" @change="saveActivity(log)">
                  <option v-for="activity in activityList" :key="activity.id" :value="activity.id">
                    {{ activity.type }}
                  </option>
                </select>
              </template>
              <template v-else>
                {{ log.activity.type }}
              </template>
            </td>
            <td class="p-3">{{ log.activity.description }}</td>
            <td class="p-3 cursor-pointer" @click="editTimestamp(log)">
              <template v-if="editingTimestampId === log.id">
                <input
                  type="datetime-local"
                  v-model="editingTimestampValue"
                  @blur="saveTimestamp(log)"
                  class="border rounded p-1 text-sm"
                />
              </template>
              <template v-else>
                {{ new Date(log.createdAt).toLocaleString() }}
              </template>
            </td>

            <td class="p-3 text-red-500 cursor-pointer" @click="deleteActivityLog(log.id)">✕</td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination Controls -->
      <div class="flex justify-between items-center p-3 border-t">
        <button
          @click="changePage(pageNumber - 1)"
          :disabled="pageNumber === 0"
          class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50 cursor-pointer"
        >
          Previous
        </button>

        <div>Page {{ pageNumber + 1 }} of {{ totalPages }}</div>

        <button
          @click="changePage(pageNumber + 1)"
          :disabled="pageNumber + 1 >= totalPages"
          class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50 cursor-pointer"
        >
          Next
        </button>
      </div>
    </div>
  </div>

  <!-- Add Activity Modal -->
  <AddActivityModal v-model="showAddModal" :isSubmitting="isSubmitting" @submit="createActivity" />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '@/api/axios'
import AddActivityModal from '@/components/ui/molecules/modals/AddActivityModal.vue'

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
  number: number // current page (0-based)
  size: number // page size
  totalPages: number
  totalElements: number
  last: boolean
  first: boolean
  numberOfElements: number
}

const activityList = ref<Activity[]>([])
const activityLogList = ref<ActivityLogEntry[]>([])

const pageNumber = ref<number>(0) // instead of ref(0)

const pageSize = ref(5) // entries per page
const totalPages = ref(1)

async function fetchActivities() {
  try {
    const res = await api.get<Activity[]>('/activity')
    activityList.value = res.data
  } catch (err) {
    console.error(err)
  }
}

async function fetchActivityLog() {
  try {
    const params: Record<string, any> = {
      page: pageNumber.value,
      size: pageSize.value,
    }

    if (filterType.value) params.type = filterType.value
    if (filterStartDate.value)
      params.startDate = filterStartDate.value
        ? `${filterStartDate.value}T00:00:00` // <-- add this
        : undefined
    if (filterEndDate.value)
      params.endDate = filterEndDate.value
        ? `${filterEndDate.value}T23:59:59` // <-- and this
        : undefined

    const res = await api.get<PaginatedActivityLog>('/activities', { params })
    activityLogList.value = res.data.content
    pageNumber.value = res.data.number
    totalPages.value = res.data.totalPages
  } catch (err) {
    console.error(err)
  }
}

function changePage(newPage: number) {
  if (newPage < 0 || newPage >= totalPages.value) return
  pageNumber.value = newPage
  fetchActivityLog()
}

const selectedActivityId = ref<string | number>('')
const isPosting = ref(false)
const showAddModal = ref(false)
const isSubmitting = ref(false)

function handleActivitySelection() {
  if (selectedActivityId.value === 'add') {
    showAddModal.value = true
    selectedActivityId.value = ''
  }
}

async function logActivity() {
  if (!selectedActivityId.value || selectedActivityId.value === 'add') return
  try {
    isPosting.value = true
    await api.post('/activities', { activityId: selectedActivityId.value })
    await fetchActivityLog()
  } finally {
    isPosting.value = false
  }
}

async function createActivity(payload: { type: string; description: string }) {
  try {
    isSubmitting.value = true
    await api.post('/activity', payload)
    await fetchActivities()
    showAddModal.value = false
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  fetchActivities()
  fetchActivityLog()
})

async function deleteActivityLog(id: number) {
  try {
    await api.delete(`/activities/${id}`)
    await fetchActivityLog()
  } catch (err) {
    console.error(err)
  }
}

const editingActivityId = ref<number | null>(null)
const editingActivityValue = ref<number | null>(null)

function editActivity(log: ActivityLogEntry) {
  editingActivityId.value = log.id
  editingActivityValue.value = log.activity.id
}

async function saveActivity(log: ActivityLogEntry) {
  if (!editingActivityValue.value) return
  try {
    await api.put(`/activities/${log.id}`, { activityId: editingActivityValue.value })
    editingActivityId.value = null
    await fetchActivityLog()
  } catch (err) {
    console.error(err)
  }
}

const editingTimestampId = ref<number | null>(null)
const editingTimestampValue = ref<string>('')

function editTimestamp(log: ActivityLogEntry) {
  editingTimestampId.value = log.id
  editingTimestampValue.value = log.createdAt.slice(0, 16) // convert to YYYY-MM-DDTHH:mm for datetime-local
}

async function saveTimestamp(log: ActivityLogEntry) {
  if (!editingTimestampValue.value) return
  try {
    await api.put(`/activities/${log.id}`, { createdAt: editingTimestampValue.value })
    editingTimestampId.value = null
    await fetchActivityLog()
  } catch (err) {
    console.error(err)
  }
}

const filterType = ref<string>('') // activity type filter
const filterStartDate = ref<string>('') // yyyy-mm-dd
const filterEndDate = ref<string>('') // yyyy-mm-dd

async function applyFilters() {
  pageNumber.value = 0 // reset to first page when filtering
  await fetchActivityLog()
}

async function resetFilters() {
  filterType.value = ''
  filterStartDate.value = ''
  filterEndDate.value = ''
  pageNumber.value = 0
  await fetchActivityLog()
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
