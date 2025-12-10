<template>
  <div class="space-y-4">
    <div class="bg-white p-4 rounded-lg shadow-sm space-y-3">
      <button
        @click="openModal"
        class="w-full bg-[#1C274C] disabled:bg-gray-400 text-white py-2 rounded-md hover:bg-[#142046] transition cursor-pointer"
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
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="activity in activityList" :key="activity.id" class="border-t hover:bg-gray-50">
            <td class="p-3">{{ activity.id }}</td>

            <!-- Editable Type -->
            <td class="p-3">
              <template v-if="editingTypeId === activity.id">
                <input
                  v-model="editingTypeValue"
                  @blur="saveType(activity)"
                  @keyup.enter="saveType(activity)"
                  class="border rounded p-1 text-sm w-full"
                  autofocus
                />
              </template>
              <template v-else>
                <span class="cursor-pointer hover:underline" @click="editType(activity)">
                  {{ activity.type }}
                </span>
              </template>
            </td>

            <!-- Editable Description -->
            <td class="p-3">
              <template v-if="editingDescriptionId === activity.id">
                <input
                  v-model="editingDescriptionValue"
                  @blur="saveDescription(activity)"
                  @keyup.enter="saveDescription(activity)"
                  class="border rounded p-1 text-sm w-full"
                  autofocus
                />
              </template>
              <template v-else>
                <span class="cursor-pointer hover:underline" @click="editDescription(activity)">
                  {{ activity.description }}
                </span>
              </template>
            </td>

            <td class="p-3 text-red-500 cursor-pointer" @click="deleteActivity(activity.id)">✕</td>
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

async function deleteActivity(id: number) {
  try {
    await api.delete(`/activity/${id}`)
    await fetchActivities()
  } catch (err) {
    console.error(err)
  }
}

const editingTypeId = ref<number | null>(null)
const editingTypeValue = ref<string>('')

const editingDescriptionId = ref<number | null>(null)
const editingDescriptionValue = ref<string>('')

function editType(activity: Activity) {
  editingTypeId.value = activity.id
  editingTypeValue.value = activity.type
}

async function saveType(activity: Activity) {
  if (editingTypeValue.value === activity.type) {
    editingTypeId.value = null
    return
  }

  try {
    await api.put(`/activity/${activity.id}`, { type: editingTypeValue.value })
    editingTypeId.value = null
    await fetchActivities()
  } catch (err) {
    console.error(err)
  }
}

function editDescription(activity: Activity) {
  editingDescriptionId.value = activity.id
  editingDescriptionValue.value = activity.description
}

async function saveDescription(activity: Activity) {
  if (editingDescriptionValue.value === activity.description) {
    editingDescriptionId.value = null
    return
  }

  try {
    await api.put(`/activity/${activity.id}`, { description: editingDescriptionValue.value })
    editingDescriptionId.value = null
    await fetchActivities()
  } catch (err) {
    console.error(err)
  }
}
</script>
