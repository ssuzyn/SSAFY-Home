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
          <button
            @click="showGPT = true"
            class="group flex items-center gap-2 px-5 py-2.5 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl transition-all duration-300 shadow-md hover:shadow-lg hover:-translate-y-0.5">
            <span class="text-2xl group-hover:scale-110 transition-transform">🤖</span>
            <span class="text-base font-medium">용어 도우미</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>

      <!-- 카테고리 탭 -->
      <div class="mb-6">
        <div class="flex space-x-2 overflow-x-auto pb-2">
          <button
            v-for="cat in categories"
            :key="cat.value"
            @click="() => {
              currentCategory = cat.value;
              console.log('Selected category:', cat.value);
              console.log('Filtered news count:', filteredNews.length);
            }"
            :class="[
              'px-4 py-2 rounded-lg font-medium transition-all duration-200',
              currentCategory === cat.value
                ? 'bg-orange-500 text-white shadow-md'
                : 'bg-white text-gray-600 hover:bg-orange-100'
            ]"
          >
            {{ cat.label }}
            <span class="ml-2 px-2 py-0.5 text-sm rounded-full" 
                  :class="currentCategory === cat.value ? 'bg-orange-400' : 'bg-gray-100'">
              {{ getNewsCounts(cat.value) }}
            </span>
          </button>
        </div>
      </div>

      <!-- GPT 모달 -->
      <RealEstateGPT v-if="showGPT" @close="showGPT = false" />

      <!-- 필터링된 뉴스 목록 -->
      <div v-infinite-scroll="loadMore" 
           :infinite-scroll-disabled="loading" 
           :infinite-scroll-distance="10">
        <template v-if="filteredNews.length > 0">
          <a v-for="item in filteredNews"
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
                  {{ formatDate(item.date) }}
                </div>
              </div>
            </div>
          </a>
        </template>
        
        <!-- 데이터가 없을 때 표시할 메시지 -->
        <div v-else-if="!loading" class="text-center py-10">
          <p class="text-gray-500">해당 카테고리의 뉴스가 없습니다.</p>
        </div>

        <!-- 로딩 스피너 -->
        <div v-if="loading" class="flex justify-center p-6">
          <div class="w-8 h-8 border-4 border-orange-500/30 border-t-orange-500 rounded-full animate-spin"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import InfiniteScroll from 'vue-infinite-scroll'
import { message } from 'ant-design-vue'
import RealEstateGPT from './RealEstateGPT.vue'

const news = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const hasMore = ref(true)
const showGPT = ref(false)
const currentCategory = ref('all')

const categories = [
  { label: '전체', value: 'all' },
  { label: '아파트', value: 'apartment' },
  { label: '분양', value: 'sale' },
  { label: '재건축', value: 'reconstruction' }
]

// 카테고리별 뉴스 필터링
const filteredNews = computed(() => {
  console.log('Filtering news for category:', currentCategory.value);
  console.log('Total news items:', news.value.length);
  
  if (currentCategory.value === 'all') {
    return news.value;
  }
  
  const filtered = news.value.filter(item => item.category === currentCategory.value);
  console.log('Filtered news items:', filtered.length);
  return filtered;
})

// 카테고리별 뉴스 수 계산
const getNewsCounts = (category) => {
  if (category === 'all') {
    return news.value.length;
  }
  return news.value.filter(item => item.category === category).length;
}

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
      console.log('Fetched news:', data.data);
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
</script>

<style scoped>
/* 스크롤바 스타일링 */
.overflow-auto {
  scrollbar-width: thin;
  scrollbar-color: #e2e8f0 #f8fafc;  /* 연한 회색으로 변경 */
}

.overflow-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-auto::-webkit-scrollbar-track {
  background: #f8fafc;
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb {
  background: #e2e8f0;  /* 연한 회색으로 변경 */
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb:hover {
  background: #cbd5e1;  /* hover 시 약간 진한 회색 */
}
</style>