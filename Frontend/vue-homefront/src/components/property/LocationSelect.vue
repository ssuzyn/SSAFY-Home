<template>
  <div class="bg-white border-b">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3">
      <div class="flex items-center space-x-4">
        <!-- 시/도 Select -->
        <div class="relative">
          <select
            v-model="selectedSido"
            class="appearance-none bg-white border border-gray-300 rounded-md text-black pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
          >
            <option value="">시/도</option>
            <option 
              v-for="sido in sidoList" 
              :key="sido.sidoCode" 
              :value="{ code: sido.sidoCode, name: sido.sidoName }"
            >
              {{ sido.sidoName }}
            </option>
          </select>
          <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-400">
            <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>

        <!-- 구/군 Select -->
        <div class="relative">
          <select
            v-model="selectedGugun"
            :disabled="!selectedSido"
            class="appearance-none bg-white border text-black border-gray-300 rounded-md pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
          >
            <option value="">구/군</option>
            <option 
              v-for="gugun in gugunList" 
              :key="gugun.gugunCode" 
              :value="{ code: gugun.gugunCode, name: gugun.gugunName }"
            >
              {{ gugun.gugunName }}
            </option>
          </select>
          <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-400">
            <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>

        <!-- 동 Select -->
        <div class="relative">
          <select
            v-model="selectedDong"
            :disabled="!selectedGugun"
            class="appearance-none bg-white border text-black border-gray-300 rounded-md pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
          >
            <option value="">동</option>
            <option 
              v-for="dong in dongList" 
              :key="dong.dongCode" 
              :value="{ code: dong.dongCode, name: dong.dongName }"
            >
              {{ dong.dongName }}
            </option>
          </select>
          <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-400">
            <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>

        <!-- Search Input -->
        <div class="relative flex-1">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="아파트명을 검색하세요"
            class="w-full px-4 py-2 border border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm"
            @keyup.enter="handleSearch"
          >
          <button
            @click="handleSearch"
            class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-orange-500"
          >
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { listSido, listGugun, listDong } from '@/api/map';

const emit = defineEmits(['search']);

const sidoList = ref([]);
const gugunList = ref([]);
const dongList = ref([]);

const selectedSido = ref('');
const selectedGugun = ref('');
const selectedDong = ref('');
const searchQuery = ref('');

// 시/도 목록 로드
onMounted(() => {
  listSido(
    ({ data }) => {
      sidoList.value = data;
    },
    (error) => {
      console.error('시/도 목록 로드 실패:', error);
    }
  );
});

// 시/도 선택시 구/군 목록 로드
watch(selectedSido, (newSido) => {
  if (newSido) {
    selectedGugun.value = '';
    selectedDong.value = '';
    
    listGugun(
      { sido: newSido.code },
      ({ data }) => {
        gugunList.value = data;
      },
      (error) => {
        console.error('구/군 목록 로드 실패:', error);
        gugunList.value = [];
      }
    );
  } else {
    gugunList.value = [];
    dongList.value = [];
  }
});

// 구/군 선택시 동 목록 로드
watch(selectedGugun, (newGugun) => {
  if (newGugun && selectedSido.value) {
    selectedDong.value = '';
    
    listDong(
      { 
        sido: selectedSido.value.code,
        gugun: newGugun.code
      },
      ({ data }) => {
        dongList.value = data;
      },
      (error) => {
        console.error('동 목록 로드 실패:', error);
        dongList.value = [];
      }
    );
  } else {
    dongList.value = [];
  }
});

const handleSearch = () => {
  emit('search', {
    sido: selectedSido.value?.name || '',
    sidoCode: selectedSido.value?.code || '',
    gugun: selectedGugun.value?.name || '',
    gugunCode: selectedGugun.value?.code || '',
    dong: selectedDong.value?.name || '',
    dongCode: selectedDong.value?.code || '',
    query: searchQuery.value
  });
};
</script>