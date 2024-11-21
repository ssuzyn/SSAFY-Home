<script setup>
import { ref, watch, onMounted } from 'vue';
import { listSido, listGugun, listDong } from '@/api/map';
import { getHouseDeals } from '@/api/house';
import BaseSelect from '@/components/common/BaseSelect.vue';
import SearchInput from '@/components/common/SearchInput.vue';

const emit = defineEmits(['search-result']);

const sidoList = ref([]);
const gugunList = ref([]);
const dongList = ref([]);

const selectedSido = ref('');
const selectedGugun = ref('');
const selectedDong = ref('');
const searchQuery = ref('');

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

watch(selectedSido, (newSido) => {
  if (newSido?.code) {
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

watch(selectedGugun, (newGugun) => {
  if (newGugun?.code && selectedSido.value?.code) {
    selectedDong.value = '';
    
    listDong(
      { gugun: newGugun.code },
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
  // 선택된 가장 하위 지역 코드를 사용
  let searchCode = '';
  if (selectedDong.value?.code) {
    searchCode = selectedDong.value.code;  // 동이 선택된 경우
  } else if (selectedGugun.value?.code) {
    searchCode = selectedGugun.value.code;  // 구/군까지만 선택된 경우
  } else if (selectedSido.value?.code) {
    searchCode = selectedSido.value.code;   // 시/도까지만 선택된 경우
  }

  getHouseDeals(
    {
      dongCode: searchCode,
      aptNm: searchQuery.value || '',
      searchLevel: selectedDong.value ? 'dong' : 
                  selectedGugun.value ? 'gugun' : 
                  selectedSido.value ? 'sido' : 'all'
    },
    (response) => {
      const results = response.data?.data || [];
      console.log('Search results:', results);
      emit('search-result', results);
    },
    (error) => {
      console.error('검색 실패:', error);
      emit('search-result', []);
    }
  );
};

const resetAll = () => {
  // 선택된 값들 초기화
  selectedSido.value = null;  // 또는 undefined
  selectedGugun.value = null; // 또는 undefined
  selectedDong.value = null;  // 또는 undefined
  searchQuery.value = '';
  
  // 리스트 초기화
  gugunList.value = [];
  dongList.value = [];
  
  // 검색 결과 초기화
  emit('search-result', []);
};
</script>

<template>
  <div class="bg-transparent">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3">
      <div class="flex flex-col sm:flex-row items-center justify-center space-y-4 sm:space-y-0 sm:space-x-4">
        <BaseSelect
          v-model="selectedSido"
          :options="sidoList"
          placeholder="시/도"
          value-key="sidoCode"
          label-key="sidoName"
          class="bg-white"
        />

        <BaseSelect
          v-model="selectedGugun"
          :options="gugunList"
          :disabled="!selectedSido"
          placeholder="구/군"
          value-key="gugunCode"
          label-key="gugunName"
          class="bg-white"
        />

        <BaseSelect
          v-model="selectedDong"
          :options="dongList"
          :disabled="!selectedGugun"
          placeholder="동"
          value-key="dongCode"
          label-key="dongName"
          class="bg-white"
        />

        <SearchInput
          v-model="searchQuery"
          placeholder="아파트명을 검색하세요"
          @search="handleSearch"
        />

        <button
          class="px-4 py-2 text-sm text-gray-600 bg-gray-100 rounded-md hover:bg-gray-200 transition-colors duration-200"
          @click="resetAll"
        >
          초기화
        </button>
      </div>
    </div>
  </div>
</template>