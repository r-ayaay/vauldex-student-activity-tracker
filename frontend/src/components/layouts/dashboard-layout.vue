<template>
  <div class="sticky top-0 z-30 bg-white shadow-sm">
    <Header></Header>
  </div>

  <TopNav class="hidden lg:flex"></TopNav>
  <main class="p-4 max-w-3xl mx-auto lg:max-w-full lg:p-0">
    <RouterView />
  </main>

  <!-- Sticky Bottom Navigation -->
  <BottomNav class="lg:hidden" />
</template>

<script setup lang="ts">
import Header from '@/components/ui/organisms/Header.vue'
import BottomNav from '@/components/ui/organisms/BottomNav.vue'
import TopNav from '@/components/ui/organisms/TopNav.vue'
</script>
