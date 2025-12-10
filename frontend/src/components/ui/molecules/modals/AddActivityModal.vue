<template>
  <div
    v-if="modelValue"
    class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center p-4 z-50"
    @click.self="close"
  >
    <div
      class="bg-white w-full max-w-sm rounded-lg p-5 shadow-lg transform transition-all scale-95 opacity-0 animate-modal"
    >
      <h2 class="text-lg font-semibold mb-4">Add Activity</h2>

      <div class="space-y-3">
        <div>
          <label class="text-sm">Activity Type</label>
          <input
            v-model="form.type"
            type="text"
            class="w-full border rounded-md p-2"
            placeholder="e.g. Sports"
          />
        </div>

        <div>
          <label class="text-sm">Description</label>
          <textarea
            v-model="form.description"
            class="w-full border rounded-md p-2"
            rows="2"
            placeholder="Short description"
          ></textarea>
        </div>

        <button
          @click="submit"
          class="w-full bg-[#1C274C] text-white py-2 rounded-md hover:bg-[#142046] transition"
          :disabled="isSubmitting"
        >
          {{ isSubmitting ? 'Saving...' : 'Save Activity' }}
        </button>

        <button class="w-full py-2 rounded-md text-gray-600 hover:bg-gray-100" @click="close">
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

// Props
const props = defineProps<{
  modelValue: boolean
  isSubmitting: boolean
}>()

// Emits
const emit = defineEmits(['update:modelValue', 'submit'])

const form = ref({
  type: '',
  description: '',
})

// Close modal helper
function close() {
  emit('update:modelValue', false)
  form.value = { type: '', description: '' }
}

// Submit form
function submit() {
  emit('submit', { ...form.value })
}
</script>

<style scoped>
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
