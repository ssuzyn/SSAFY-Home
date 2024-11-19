<template>
  <div class="flex flex-col h-screen pt-16">
    <!-- 상단 영역 -->
    <div class="relative z-50">
      <LocationSelect @search-result="handleSearchResult" />
    </div>
    
    <!-- 메인 콘텐츠 영역 -->
    <div class="flex-1 relative">
      <!-- 지도 영역 (항상 전체 영역 차지) -->
      <div class="absolute inset-0">
        <LocationMap
          :properties="searchResults"
          :selected-property="selectedProperty"
          @select-property="handlePropertySelect"
          class="h-full w-full"
        />
      </div>

      <!-- 왼쪽 사이드바 (PropertyList) -->
      <div 
        class="absolute h-full bg-white z-30 transform transition-transform duration-300 ease-in-out shadow-lg"
        :class="{
          'translate-x-0 w-[400px]': isSidebarOpen,
          '-translate-x-full': !isSidebarOpen
        }"
      >
        <!-- PropertyList -->
        <div class="h-full overflow-y-auto">
          <PropertyList
            v-show="isSidebarOpen"
            :properties="searchResults"
            @select-property="handlePropertySelect"
          />
        </div>
      </div>

      <!-- 토글 버튼 -->
      <button 
        @click="toggleSidebar"
        class="absolute top-4 z-50 bg-white w-8 h-8 rounded-lg shadow-lg hover:bg-gray-50 flex items-center justify-center transition-all duration-300 ease-in-out"
        :class="[
          isSidebarOpen 
            ? 'left-[400px]' 
            : 'left-4'
        ]"
      >
        <svg 
          class="w-5 h-5 text-gray-600"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path 
            v-if="isSidebarOpen"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M11 19l-7-7 7-7m8 14l-7-7 7-7"
          /> 
          <path 
            v-else
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M13 5l7 7-7 7M5 5l7 7-7 7"
          /> 
        </svg>
      </button>
    </div>

    <PropertyDetailModal
      v-if="selectedProperty"
      :property="selectedProperty"
      @close="selectedProperty = null"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import LocationSelect from "@/components/property/LocationSelect.vue";
import PropertyList from "@/components/property/PropertyList.vue";
import LocationMap from "@/components/map/LocationMap.vue";
import PropertyDetailModal from "@/components/property/PropertyDetailModal.vue";

const searchResults = ref([]);
const selectedProperty = ref(null);
const isSidebarOpen = ref(false);

const handleSearchResult = (response) => {
  searchResults.value = Array.isArray(response) ? response : [];
  selectedProperty.value = null;
  isSidebarOpen.value = true;
};

const handlePropertySelect = (property) => {
  selectedProperty.value = property;
};

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};
</script>

<style scoped>
.overflow-y-auto {
  -ms-overflow-style: none;
  scrollbar-width: thin;
}

.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>