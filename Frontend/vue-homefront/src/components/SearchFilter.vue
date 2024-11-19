<template>
    <div class="bg-white border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3">
        <div class="flex items-center space-x-4">
          <!-- 시/도 Select -->
          <div class="relative">
            <select
              v-model="selectedCity"
              class="appearance-none bg-white border border-gray-300 rounded-md text-black pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
            >
              <option value="">시/도</option>
              <option v-for="city in cities" :key="city" :value="city">{{ city }}</option>
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
              v-model="selectedDistrict"
              :disabled="!selectedCity"
              class="appearance-none bg-white border text-black border-gray-300 rounded-md pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
            >
              <option value="">구/군</option>
              <option v-for="district in districts" :key="district" :value="district">{{ district }}</option>
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
              :disabled="!selectedDistrict"
              class="appearance-none bg-white border text-black border-gray-300 rounded-md pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer min-w-[100px]"
            >
              <option value="">동</option>
              <option v-for="dong in dongs" :key="dong" :value="dong">{{ dong }}</option>
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
  import { ref, watch } from 'vue';
  
  const emit = defineEmits(['search']);
  
  // Sample data - replace with actual data from your backend
  const cities = ['서울특별시', '경기도', '인천광역시'];
  const districtsByCity = {
    '서울특별시': ['강남구', '서초구', '송파구', '강동구'],
    '경기도': ['성남시', '수원시', '용인시'],
    '인천광역시': ['연수구', '남동구', '서구']
  };
  const dongsByDistrict = {
    '강남구': ['논현동', '삼성동', '청담동'],
    '서초구': ['서초동', '반포동', '방배동'],
    '송파구': ['잠실동', '문정동', '가락동']
  };
  
  const selectedCity = ref('');
  const selectedDistrict = ref('');
  const selectedDong = ref('');
  const searchQuery = ref('');
  
  const districts = ref([]);
  const dongs = ref([]);
  
  watch(selectedCity, (newCity) => {
    selectedDistrict.value = '';
    selectedDong.value = '';
    districts.value = newCity ? districtsByCity[newCity] || [] : [];
  });
  
  watch(selectedDistrict, (newDistrict) => {
    selectedDong.value = '';
    dongs.value = newDistrict ? dongsByDistrict[newDistrict] || [] : [];
  });
  
  const handleSearch = () => {
    emit('search', {
      city: selectedCity.value,
      district: selectedDistrict.value,
      dong: selectedDong.value,
      query: searchQuery.value
    });
  };
  </script>