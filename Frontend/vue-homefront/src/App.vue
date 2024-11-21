<script setup>
import AppHeader from '@/components/layout/AppHeader.vue';
import InterestContent from '@/components/interest/InterestContent.vue';

</script>

<template>
  <div class="min-h-screen flex flex-col bg-gray-50 w-full">
    <AppHeader />
    <main class="flex-grow w-full">
      <RouterView></RouterView>
    </main>
  </div>
  <div>
    <InterestContent />
  </div>
  
</template>


<style>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
}
</style>