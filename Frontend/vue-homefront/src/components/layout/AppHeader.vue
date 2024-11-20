<script setup>
import { useAuth } from '@/stores/auth';
import { computed } from 'vue';

const { isLoggedIn, logout } = useAuth();

const links = computed(() => {
  const baseLinks = [
    { name: 'Q&A', path: '/qna' }
  ];

  if (isLoggedIn) {
    return [
      { name: '로그아웃', action: logout },
      { name: '마이페이지', path: '/mypage' },
      ...baseLinks
    ];
  } else {
    return [
      { name: '로그인', path: '/login' },
      ...baseLinks
    ];
  }
});
</script>

<template>
    <header class="fixed top-0 left-0 right-0 bg-white border-b z-50">
      <div class="container mx-auto px-4 h-16 flex items-center justify-between">
        <router-link to="/" class="text-2xl font-bold text-orange-500">HOUSE-TOSS</router-link>
        <nav class="flex items-center space-x-6">                       
          <template v-for="link in links" :key="link.name">
            <router-link 
              v-if="link.path"
              :to="link.path"
              class="text-gray-600 hover:text-orange-500 transition-colors"
            >
              {{ link.name }}
            </router-link>
            <button 
              v-else
              @click="link.action"
              class="text-gray-600 hover:text-orange-500 transition-colors"
            >
              {{ link.name }}
            </button>
          </template>
        </nav>
      </div>
    </header>
  </template>
  
