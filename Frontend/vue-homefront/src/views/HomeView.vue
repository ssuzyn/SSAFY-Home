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
const axiosStore = useAxiosStore();
const route = useRoute();

// 상태 관리
const searchResults = ref([]); // 검색 결과용
const interestProperty = ref(null); // 관심 매물용
const selectedProperty = ref(null);
const isSidebarOpen = ref(false);
const isDetailModalOpen = ref(false);
const detailData = ref(null);
const mapCenter = ref(null);
const mapZoom = ref(15);

const DETAIL_ZOOM_LEVEL = 3;

watch(() => interestStore.selectedProperty, (newProperty) => {
  if (newProperty) {
    // PropertyList의 transformProperty 함수에 맞는 데이터 구조로 변환

    console.log('관심매물 property for PropertyList:', newProperty);

    interestProperty.value = newProperty;
    searchResults.value = [newProperty]; // 배열로 래핑
    isSidebarOpen.value = true;

    mapCenter.value = {
      lat: parseFloat(newProperty.latitude),
      lng: parseFloat(newProperty.longitude)
    };
    mapZoom.value = DETAIL_ZOOM_LEVEL;
  } else {
    interestProperty.value = null;
    searchResults.value = [];
  }
});

// 디버깅을 위한 추가 watch
watch(() => searchResults.value, (newResults) => {
  console.log('Search results updated:', newResults);
  if (newResults.length > 0) {
    console.log('First property in results:', newResults[0]);
  }
}, { deep: true });

// searchResults가 변경될 때마다 로그 출력
// watch(() => searchResults.value, (newResults) => {
//   console.log('Search results updated:', newResults);
// }, { deep: true });

// LocationMap에 전달할 properties 계산
const displayProperties = computed(() => {
  if (interestProperty.value) {
    return [interestProperty.value];
  }
  return searchResults.value;
});

// API 호출
const fetchPropertyDetail = async (aptSeq) => {
  try {
    const detail = await getHouseDetail(aptSeq);
    return {
      ...detail,
      deals: detail.deals.map(deal => ({
        ...deal,
        dealAmount: parseAmount(deal.dealAmount)
      }))
    };
  } catch (error) {
    console.error('Error fetching property detail:', error);
    throw error;
  }
};

// 유틸리티 함수
const parseAmount = (amount) => {
  if (!amount) return 0;
  if (typeof amount === 'number') return amount * 10000;
  if (typeof amount === 'string') return parseInt(amount.replace(/,/g, '')) * 10000;
  return 0;
};

// 이벤트 핸들러
const handleSearchResult = (response) => {
  clearAllProperties();
  searchResults.value = Array.isArray(response) ? response : [];
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
    detailData.value = detail;
    isDetailModalOpen.value = true;
  } catch (error) {
    console.error('Failed to fetch property details:', error);
  }
};

const handleFavoriteToggle = async (propertyId) => {
  try {
    await interestDrawerStore.toggleFavorite(propertyId);
  } catch (error) {
    console.error('Failed to toggle favorite:', error);
  }
};

const clearAllProperties = () => {
  selectedProperty.value = null;
  interestProperty.value = null;
  searchResults.value = [];
  interestStore.clearSelection();
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