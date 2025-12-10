<template>
  <div class="sticky top-0 z-30 bg-white shadow-sm">
    <Header></Header>
  </div>

  <main class="p-4 max-w-3xl mx-auto">
    <RouterView />
  </main>

  <!-- Sticky Bottom Navigation -->
  <BottomNav />
</template>

<script setup lang="ts">
import Header from '@/components/ui/organisms/Header.vue'
import BottomNav from '@/components/ui/organisms/BottomNav.vue'
</script>
