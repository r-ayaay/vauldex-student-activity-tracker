<template>
  <div class="sticky top-0 z-30 bg-white shadow-sm">
    <Header></Header>
  </div>

  <main class="p-4 max-w-3xl mx-auto">
    <RouterView />
  </main>
</template>

<script setup lang="ts">
import Header from '@/components/ui/organisms/Header.vue'
</script>
