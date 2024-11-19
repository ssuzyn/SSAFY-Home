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

// components/property/LocationSelect.vue
const handleSearch = () => {
  getHouseDeals(
    {
      dongCode: selectedDong.value?.code || '',
      aptNm: searchQuery.value || ''
    },
    (response) => {
      // response.data가 배열인지 확인
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
</script>

<template>
  <div class="bg-white border-b">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3">
      <div class="flex flex-col sm:flex-row items-center justify-center space-y-4 sm:space-y-0 sm:space-x-4">
        <BaseSelect
          v-model="selectedSido"
          :options="sidoList"
          placeholder="시/도"
          value-key="sidoCode"
          label-key="sidoName"
        />

        <BaseSelect
          v-model="selectedGugun"
          :options="gugunList"
          :disabled="!selectedSido"
          placeholder="구/군"
          value-key="gugunCode"
          label-key="gugunName"
        />

        <BaseSelect
          v-model="selectedDong"
          :options="dongList"
          :disabled="!selectedGugun"
          placeholder="동"
          value-key="dongCode"
          label-key="dongName"
        />

        <SearchInput
          v-model="searchQuery"
          placeholder="아파트명을 검색하세요"
          @search="handleSearch"
        />
      </div>
    </div>
  </div>
</template>