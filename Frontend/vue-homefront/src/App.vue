<script setup>
import Header from './components/Header.vue';
</script>

<template>
  <div class="min-h-screen flex flex-col bg-gray-50 w-full">
    <Header />
    <main class="flex-grow w-full">
      <RouterView></RouterView>
    </main>
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