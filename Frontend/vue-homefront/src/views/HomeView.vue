<template>
  <div class="flex flex-col h-screen">
    <!-- 헤더 영역 -->
    <div class="h-16">
      <!-- AppHeader 컴포넌트 -->
    </div>

    <!-- 메인 콘텐츠 영역 -->
    <div class="flex-1 relative">
      <!-- 검색 영역 - 상단에 고정 -->
      <div class="absolute top-0 left-0 right-0 z-10">
        <LocationSelect @search-result="handleSearchResult" />
      </div>

      <!-- 지도 영역 -->
      <LocationMap
        :properties="searchResults"
        :selected-property="selectedProperty"
        :center-position="mapCenter" 
        :zoom-level="mapZoom"
        @select-property="handleMarkerClick"
        class="h-full w-full"
      />

      <!-- 슬라이딩 PropertyList -->
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

      <!-- 오른쪽 InterestContent 슬라이딩 - top 위치 조정 -->
      <div 
        class="absolute top-[55px] right-0 h-[calc(100vh-55px)] bg-white z-20 transform transition-transform duration-300 ease-in-out shadow-lg"
        :class="{
          'translate-x-0 w-[400px]': isInterestOpen,
          'translate-x-full': !isInterestOpen
        }"
      >
        <!-- overflow-y-auto를 InterestContent에 직접 적용하도록 수정 -->
        <InterestContent 
          v-show="isInterestOpen" 
          class="h-full overflow-y-auto"
        />
      </div>
    </div>

    <PropertyDetailModal
      v-if="isDetailModalOpen"
      :open="isDetailModalOpen"
      :property-detail="detailData"
      @close="closeDetailModal"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from 'axios';
import LocationSelect from "@/components/property/LocationSelect.vue";
import PropertyList from "@/components/property/PropertyList.vue";
import LocationMap from "@/components/map/LocationMap.vue";
import PropertyDetailModal from "@/components/property/PropertyDetailModal.vue";

const searchResults = ref([]);
const selectedProperty = ref(null);
const isSidebarOpen = ref(false);
const isDetailModalOpen = ref(false);
const detailData = ref(null);
const mapCenter = ref(null);
const mapZoom = ref(15);

const DETAIL_ZOOM_LEVEL = 3; // 상세 보기 시 줌 레벨
const selectedMarkerInfo = ref(null); // 선��된 마커의 인포윈도우 상태

const fetchPropertyDetail = async (aptSeq) => {
  try {
    const response = await axios.get(`http://localhost:8080/house/get/${aptSeq}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching property detail:', error);
    throw error;
  }
};

const parseAmount = (amount) => {
  if (!amount) return 0;
  return parseInt(amount.replace(/,/g, '')) * 10000;
};

// 리스트 클릭 처리 - 지도 이동 & 인포윈도우 표시
const handleListClick = (property) => {
  selectedProperty.value = property;
  
  // 선택된 property의 위도/경도 있는 경우에만 지도 이동
  if (property.latitude && property.longitude) {
    mapCenter.value = {
      lat: parseFloat(property.latitude),
      lng: parseFloat(property.longitude)
    };
    mapZoom.value = 3; // 마커가 잘 보일 정도의 줌 레벨
  }
};

// 마커 클릭 처리
const handleMarkerClick = async (property) => {
  try {
    selectedProperty.value = property;
    selectedMarkerInfo.value = null; // 마커 클릭 시는 인포윈도우 닫기
    
    if (property.latitude && property.longitude) {
      mapCenter.value = {
        lat: parseFloat(property.latitude),
        lng: parseFloat(property.longitude)
      };
      mapZoom.value = DETAIL_ZOOM_LEVEL;
    }
    
    const detail = await fetchPropertyDetail(property.aptSeq);
    detailData.value = {
      ...detail,
      deals: detail.deals.map(deal => ({
        ...deal,
        dealAmount: parseAmount(deal.dealAmount)
      }))
    };
    isDetailModalOpen.value = true;
  } catch (error) {
    console.error('Failed to fetch property details:', error);
  }
};

// 검색 결과 처리시 상태 초기화
const handleSearchResult = (response) => {
  searchResults.value = Array.isArray(response) ? response : [];
  selectedProperty.value = null;
  selectedMarkerInfo.value = null; // 인포윈도우 상태 초기화
  mapCenter.value = null;
  mapZoom.value = 7;
  isSidebarOpen.value = true;
};

const closeDetailModal = () => {
  isDetailModalOpen.value = false;
  detailData.value = null;
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