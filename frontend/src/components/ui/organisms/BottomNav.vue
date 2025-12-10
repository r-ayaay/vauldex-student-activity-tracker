<template>
  <nav class="fixed bottom-0 left-0 right-0 bg-gray-100 shadow-md z-30 flex">
    <RouterLink
      to="/dashboard"
      class="flex flex-1 flex-col items-center justify-center py-2 text-sm transition"
      :class="{
        'bg-[#1C274C] font-semibold text-white': $route.path === '/dashboard',
        'text-gray-600 bg-gray-100': $route.path !== '/dashboard',
      }"
    >
      <span>Dashboard</span>
    </RouterLink>

    <RouterLink
      to="/activities"
      class="flex flex-1 flex-col items-center justify-center py-3 text-sm transition"
      :class="{
        'bg-[#1C274C] font-semibold text-white': $route.path.startsWith('/activities'),
        'text-gray-600 bg-gray-100': !$route.path.startsWith('/activities'),
      }"
    >
      <span>Activities</span>
    </RouterLink>
  </nav>
</template>
