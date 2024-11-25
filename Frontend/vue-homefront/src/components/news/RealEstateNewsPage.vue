<template>
  <div class="fixed inset-0 min-h-screen w-full bg-gradient-to-br from-orange-50/80 via-gray-50/60 to-white backdrop-blur-sm overflow-auto">
    <div class="w-full max-w-7xl mx-auto px-4 md:px-6 py-8 pt-24">
      <!-- 상단 제목 영역 -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-4xl text-gray-900 font-bold tracking-tight">📰 부동산 News</h1>
          <p class="mt-2 text-gray-600">최신 부동산 뉴스를 한눈에 확인하세요</p>
        </div>
        <div class="flex items-center gap-4">
          <Clock />
          <button
            @click="showGPT = true"
            class="group flex items-center gap-2 px-5 py-2.5 bg-[#00A67E] hover:bg-[#008F6B] text-white rounded-xl transition-all duration-300 shadow-md hover:shadow-lg hover:-translate-y-0.5">
            <span class="text-2xl group-hover:scale-110 transition-transform">🤖</span>
            <span class="font-medium">용어 도우미</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>

      <!-- GPT 모달 추가 -->
      <RealEstateGPT
        v-if="showGPT"
        @close="showGPT = false"
      />

      <!-- 뉴스 목록 -->
      <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
        <a v-for="item in news"
           :key="item.id"
           :href="item.link"
           target="_blank"
           class="group block bg-white border border-gray-100 rounded-2xl p-6 shadow-md hover:shadow-lg transition-all duration-300 hover:border-orange-200 transform hover:-translate-y-1 mb-4 cursor-pointer">
          <div class="flex gap-6">
            <!-- 썸네일 -->
            <div v-if="item.thumbnail" class="flex-shrink-0">
              <img :src="item.thumbnail"
                   :alt="item.title"
                   class="w-32 h-24 object-cover rounded-xl"/>
            </div>
            <!-- 뉴스 내용 -->
            <div class="flex-1">
              <h3 class="text-xl font-semibold text-gray-900 group-hover:text-orange-500 transition-colors line-clamp-2 mb-3">
                {{ item.title }}
              </h3>
              <div class="flex items-center text-gray-500 text-sm">
                <Clock class="w-4 h-4 mr-1.5" />
                {{ formatDate(item.date) }}
              </div>
            </div>
          </div>
        </a>

        <!-- 로딩 스피너 -->
        <div v-if="loading" class="flex justify-center p-6">
          <div class="w-8 h-8 border-4 border-orange-500/30 border-t-orange-500 rounded-full animate-spin"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import InfiniteScroll from 'vue-infinite-scroll'
import { message, Spin } from 'ant-design-vue'
import {
  Row,
  Col,
  List,
  ListItem,
} from 'ant-design-vue'
import RealEstateGPT from './RealEstateGPT.vue'

const news = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const hasMore = ref(true)
const showGPT = ref(false)

const getToken = () => localStorage.getItem('token')

const fetchNews = async () => {
  if (!hasMore.value || loading.value) return

  loading.value = true
  try {
    const response = await fetch(`http://localhost:8080/news/all?page=${page.value}&pageSize=${pageSize}`, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    })
    const data = await response.json()
    if (data.success) {
      if (data.data.length < pageSize) {
        hasMore.value = false
      }
      news.value = [...news.value, ...data.data]
      page.value += 1
    }
  } catch (error) {
    message.error('뉴스를 불러오는데 실패했습니다.')
  } finally {
    loading.value = false
  }
}

const loadMore = () => {
  if (hasMore.value && !loading.value) {
    fetchNews()
  }
}

const formatDate = (dateArr) => {
  if (!dateArr || dateArr.length !== 3) return ''
  return `${dateArr[0]}-${String(dateArr[1]).padStart(2, '0')}-${String(dateArr[2]).padStart(2, '0')}`
}

onMounted(() => {
  fetchNews()
})

// directives 등록
const vInfiniteScroll = InfiniteScroll.directive
</script>

<style scoped>
/* 스크롤바 스타일링 */
.overflow-auto {
  scrollbar-width: thin;
  scrollbar-color: #f97316 #f1f1f1;
}

.overflow-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb {
  background: #f97316;
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb:hover {
  background: #ea580c;
}
</style>
