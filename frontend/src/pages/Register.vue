<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="w-full max-w-md bg-white p-8 rounded-md shadow-lg">
      <h2 class="text-2xl font-semibold mb-6">Register</h2>

      <form @submit.prevent="handleRegister" class="space-y-4">
        <!-- Name -->
        <div>
          <label class="block text-sm font-medium mb-1">Name</label>
          <input
            type="text"
            v-model="name"
            placeholder="Your full name"
            class="bg-gray-50 border text-gray-900 block w-full text-sm border-gray-300 p-2.5 rounded"
            required
          />
        </div>

        <!-- Email -->
        <div>
          <label class="block text-sm font-medium mb-1">Email</label>
          <input
            type="email"
            v-model="email"
            placeholder="you@example.com"
            class="bg-gray-50 border text-gray-900 block w-full text-sm border-gray-300 p-2.5 rounded"
            required
          />
        </div>

        <!-- Password -->
        <div>
          <label class="block text-sm font-medium mb-1">Password</label>
          <input
            type="password"
            v-model="password"
            placeholder="Password"
            class="bg-gray-50 border text-gray-900 block w-full text-sm border-gray-300 p-2.5 rounded"
            required
          />
        </div>

        <!-- Actions -->
        <button
          type="submit"
          class="w-full text-white bg-blue-600 hover:bg-blue-700 font-medium rounded-sm text-sm px-5 py-2.5 cursor-pointer"
        >
          Register
        </button>
      </form>

      <p v-if="error" class="text-red-500 mt-2">{{ error }}</p>

      <p class="mt-4 text-sm">
        Already have an account?
        <router-link to="/login" class="text-blue-600 hover:underline">Login</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/axios'
import { useAuthStore } from '../stores/auth'

const name = ref('')
const email = ref('')
const password = ref('')
const error = ref('')

const router = useRouter()
const auth = useAuthStore()

function isAxiosError(err: unknown): err is { response?: { data?: { message?: string } } } {
  return typeof err === 'object' && err !== null && 'response' in err
}

async function handleRegister() {
  try {
    await api.post('/auth/register', {
      name: name.value,
      email: email.value,
      password: password.value,
    })

    // After registering, log in using your store
    await auth.login(email.value, password.value)

    router.push('/dashboard')
  } catch (err: unknown) {
    if (isAxiosError(err)) {
      error.value = err.response?.data?.message ?? 'Registration failed'
    } else {
      error.value = 'Registration failed'
    }
  }
}
</script>
