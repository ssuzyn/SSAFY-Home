<script setup>
import { useAuth } from "@/stores/auth";
import { computed } from "vue";
import { StarFilled } from '@ant-design/icons-vue';
import { useInterestDrawer } from '@/stores/interestDrawer';
import { useRoute } from 'vue-router';
import { message } from "ant-design-vue";

const auth = useAuth();
const interestDrawer = useInterestDrawer();
const route = useRoute();

const links = computed(() => {
  const baseLinks = [
    { name: 'Q&A', path: '/qna' },
  ];
  if (auth.isLoggedIn) {
    return [
      { name: '로그아웃', action: auth.logout },
      { name: '마이페이지', path: '/mypage' },
      { name: '부동산 뉴스', path: '/real-estate-news' },
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
    interestDrawer.toggleDrawer();
  } else {
    interestDrawer.toggleDrawer();
  }
};

const greeting = computed(() => {
  return auth.isLoggedIn && auth.user ? `안녕하세요, ${auth.user['userName']}님!` : '공백';
});

const totalInterests = computed(() => {
  return interestDrawer.totalCount ? interestDrawer.totalCount() : 0;
});
</script>

<template>
  <header class="fixed top-0 left-0 right-0 bg-white border-b z-50">
    <div class="container mx-auto px-4 h-16 flex items-center justify-between">
      <router-link
        to="/"
        class="text-4xl font-bold text-orange-500 flex items-center gap-2"
      >
        <img
          src="@/assets/images/zip_logo.png"
          alt="ZIP 로고"
          class="w-14 h-14 object-contain"
        />
        ZIP
      </router-link>
      <div class="flex items-center space-x-6">
        <span v-if="auth.isLoggedIn" class="text-gray-600">{{ greeting }}</span>
        <nav class="flex items-center space-x-4">
          <button
            @click="toggleInterestDrawer"
            class="flex items-center space-x-2 px-3 py-1.5 rounded-full border-2 transition-all duration-200"
            :class="[
              interestDrawer.isVisible
                ? 'bg-orange-50 border-orange-500 text-orange-600'
                : 'border-gray-200 text-gray-600 hover:border-orange-300'
            ]"
          >
            <StarFilled :class="{ 'text-orange-500': interestDrawer.isVisible }" />
            <span class="text-sm font-medium">
              관심 매물
              <span
                v-if="totalInterests"
                class="ml-1 px-1.5 py-0.5 text-xs bg-orange-500 text-white rounded-full"
              >
                {{ totalInterests }}
              </span>
            </span>
          </button>
          <template v-for="link in links" :key="link.name">
            <router-link
              v-if="link.path"
              :to="link.path"
              class="px-3 py-1.5 rounded-full border-2 transition-all duration-200 text-sm font-medium"
              :class="[
                route.path === link.path
                  ? 'bg-orange-50 border-orange-500 text-orange-600'
                  : 'border-gray-200 text-gray-600 hover:border-orange-300'
              ]"
            >
              {{ link.name }}
            </router-link>
            <button
              v-else
              @click="link.action"
              class="px-3 py-1.5 rounded-full border-2 border-gray-200 text-gray-600
                     transition-all duration-200 text-sm font-medium
                     hover:border-orange-300"
            >
              {{ link.name }}
            </button>
          </template>
        </nav>
      </div>
    </div>
  </header>
</template>

