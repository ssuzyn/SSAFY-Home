<template>
  <div class="flex flex-col h-screen">
    <!-- 헤더 영역 -->
    <div class="h-16">
      <!-- AppHeader 컴포넌트 -->
    </div>

    <!-- 메인 콘텐츠 영역 -->
    <div class="flex-1 relative">
      <!-- 검색 영역 -->
      <div class="absolute top-0 left-0 right-0 z-10">
        <LocationSelect @search-result="handleSearchResult" />
      </div>

      <!-- 지도 영역 -->
      <LocationMap
        :properties="displayProperties"
        :selected-property="selectedProperty"
        :center-position="mapCenter" 
        :zoom-level="mapZoom"
        @select-property="handleMarkerClick"
        class="h-full w-full relative z-0"
      />

      <!-- 검색결과 PropertyList -->
      <div 
        class="absolute top-0 h-full bg-white z-20 transform transition-transform duration-300 ease-in-out shadow-lg"
        :class="{
          'translate-x-0 w-[400px]': isSidebarOpen,
          '-translate-x-full': !isSidebarOpen
        }"
      >
        <div class="h-full overflow-y-auto">
          <PropertyList
            v-show="isSidebarOpen"
            :properties="searchResults"
            @select-property="handleListClick"
          />
        </div>
      </div>

      <!-- 토글 버튼 -->
      <button 
        @click="toggleSidebar"
        class="absolute top-2 z-30 bg-white w-8 h-8 rounded-lg shadow-lg hover:bg-gray-50 flex items-center justify-center transition-all duration-300 ease-in-out"
        :class="[isSidebarOpen ? 'left-[400px]' : 'left-2']"
      >
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
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

    <!-- 상세 정보 모달 -->
    <PropertyDetailModal
      v-if="isDetailModalOpen"
      :open="isDetailModalOpen"
      :property-detail="detailData"
      @close="closeDetailModal"
      @toggle-favorite="handleFavoriteToggle"
      class="z-[60]"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import { getHouseDetail } from '@/api/house';
import { useRoute } from 'vue-router';
import { useAxiosStore } from '@/stores/axiosStore';
import { useInterestStore } from '@/stores/interest';
import LocationSelect from "@/components/property/LocationSelect.vue";
import PropertyList from "@/components/property/PropertyList.vue";
import LocationMap from "@/components/map/LocationMap.vue";
import PropertyDetailModal from "@/components/property/PropertyDetailModal.vue";
import { useInterestDrawer } from "@/stores/interestDrawer";

const interestStore = useInterestStore();
const interestDrawerStore = useInterestDrawer();

// 상태 관리
const searchResults = ref([]);
const selectedProperty = ref(null);
const isSidebarOpen = ref(false);
const isDetailModalOpen = ref(false);
const detailData = ref(null);
const mapCenter = ref(null);
const mapZoom = ref(15);

const DETAIL_ZOOM_LEVEL = 3;

const axiosStore = useAxiosStore();
const route = useRoute();

const displayProperties = computed(() => {
  if (interestStore.selectedProperty) {
    return [...searchResults.value, interestStore.selectedProperty];
  }
  return searchResults.value;
});

watch(
  () => route.query,
  async (newQuery) => {
    if (newQuery.aptSeq && newQuery.lat && newQuery.lng) {
      try {
        const detail = await fetchPropertyDetail(newQuery.aptSeq);
        
        // 지도 중심 이동
        mapCenter.value = {
          lat: parseFloat(newQuery.lat),
          lng: parseFloat(newQuery.lng)
        };
        mapZoom.value = DETAIL_ZOOM_LEVEL;
        
        // 상세 정보 모달 표시
        detailData.value = detail;
        isDetailModalOpen.value = true;
      } catch (error) {
        console.error('Failed to fetch property details:', error);
      }
    }
  },
  { immediate: true }  // 컴포넌트 마운트 시에도 실행
);

watch(() => interestStore.shouldShowDetail, (newValue) => {
  if (newValue && interestStore.selectedProperty) {
    // 선택된 관심 매물이 있으면 지도 중심 이동 및 모달 표시
    const property = interestStore.selectedProperty;
    if (property.latitude && property.longitude) {
      mapCenter.value = {
        lat: parseFloat(property.latitude),
        lng: parseFloat(property.longitude)
      };
      mapZoom.value = DETAIL_ZOOM_LEVEL;
    }
    
    // 상세 정보 모달 표시
    detailData.value = property;
    isDetailModalOpen.value = true;
  }
});

// API 호출
const fetchPropertyDetail = async (aptSeq) => {
  try {
    const detail = await getHouseDetail(aptSeq);
    // 받아온 데이터 구조 로깅
    console.log('Received detail:', detail);
    
    return {
      ...detail,
      deals: detail.deals.map(deal => {
        // 개별 deal 데이터 로깅
        console.log('Processing deal:', deal);
        return {
          ...deal,
          dealAmount: parseAmount(deal.dealAmount)
        };
      })
    };
  } catch (error) {
    console.error('Error fetching property detail:', error);
    throw error;
  }
};

// 유틸리티 함수
const parseAmount = (amount) => {
  if (!amount) return 0;
  
  // 이미 숫자인 경우
  if (typeof amount === 'number') {
    return amount * 10000;
  }
  
  // 문자열인 경우 콤마 제거 후 변환
  if (typeof amount === 'string') {
    return parseInt(amount.replace(/,/g, '')) * 10000;
  }
  
  return 0;
};

// 이벤트 핸들러
const handleSearchResult = (response) => {
  searchResults.value = Array.isArray(response) ? response : [];
  selectedProperty.value = null;
  mapCenter.value = null;
  mapZoom.value = 7;
  isSidebarOpen.value = true;
};

const handleListClick = (property) => {
  selectedProperty.value = property;
  if (property.latitude && property.longitude) {
    mapCenter.value = {
      lat: parseFloat(property.latitude),
      lng: parseFloat(property.longitude)
    };
    mapZoom.value = DETAIL_ZOOM_LEVEL;
  }
};

const handleMarkerClick = async (property) => {
  try {
    selectedProperty.value = property;
    if (property.latitude && property.longitude) {
      mapCenter.value = {
        lat: parseFloat(property.latitude),
        lng: parseFloat(property.longitude)
      };
      mapZoom.value = DETAIL_ZOOM_LEVEL;
    }
    
    const detail = await fetchPropertyDetail(property.aptSeq);
    // 변환된 데이터 로깅
    console.log('Processed detail:', detail);
    detailData.value = detail;
    isDetailModalOpen.value = true;
  } catch (error) {
    console.error('Failed to fetch property details:', error);
  }
};

const handleInterestPropertySelect = (property) => {
  selectedProperty.value = property;
  if (property.latitude && property.longitude) {
    mapCenter.value = {
      lat: parseFloat(property.latitude),
      lng: parseFloat(property.longitude)
    };
    mapZoom.value = DETAIL_ZOOM_LEVEL;
  }
};

const handleFavoriteToggle = async (propertyId) => {
  try {
    await interestDrawerStore.toggleFavorite(propertyId);
  } catch (error) {
    console.error('Failed to toggle favorite:', error);
  }
};

const closeDetailModal = () => {
  isDetailModalOpen.value = false;
  detailData.value = null;
  selectedProperty.value = null;
  interestStore.clearSelection();
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