<script setup>
import { useAuth } from "@/stores/auth";
import { computed } from "vue";
import { StarFilled } from '@ant-design/icons-vue';
import { useInterestDrawer } from '@/stores/interestDrawer';

const auth = useAuth();
const interestDrawer = useInterestDrawer();

const links = computed(() => {
  const baseLinks = [
    { name: 'Q&A', path: '/qna' }
  ];
  if (auth.isLoggedIn) {
    return [
      { name: '로그아웃', action: auth.logout },
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

const toggleInterestDrawer = () => {
  if (!auth.isLoggedIn) {
    // 로그인되지 않은 경우에도 drawer를 열어서 로그인 유도 메시지를 보여줍니다
    interestDrawer.toggleDrawer();
  } else {
    interestDrawer.toggleDrawer();
  }
};

const greeting = computed(() => {
  console.log(auth.user);
  return auth.isLoggedIn && auth.user ? `안녕하세요, ${auth.user['userName']}님!` : '공백';
});
</script>

<template>
  <header class="fixed top-0 left-0 right-0 bg-white border-b z-50">
    <div class="container mx-auto px-4 h-16 flex items-center justify-between">
      <a href="/" class="text-2xl font-bold text-orange-500">
        HOUSE-TOSS
      </a>
      <div class="flex items-center space-x-6">
        <span v-if="auth.isLoggedIn" class="text-gray-600">{{ greeting }}</span>
        <nav class="flex items-center space-x-6">
          <button 
            @click="toggleInterestDrawer" 
            class="flex items-center space-x-2 text-black hover:bg-gray-50 px-3 py-2 rounded-md"
          >
            <StarFilled :class="{ 'text-orange-500': interestDrawer.isVisible }" />
            <span class="text-sm"> 관심 매물</span>
          </button>
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
    </div>
  </header>
</template>