<template>
    <div>
      <!-- Header with heart button -->
      <div class="flex items-center justify-between p-4 border-b">
        <h3 class="text-xl font-semibold text-gray-900">
          {{ propertyDetail.aptNm }}
        </h3>
        <button 
          class="p-2 rounded-full hover:bg-gray-100 transition-colors"
          @click.stop="toggleFavorite"
        >
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            class="h-6 w-6"
            :class="{ 
              'text-red-500 fill-red-500': isFavorite,
              'text-gray-400': !isFavorite 
            }"
            viewBox="0 0 24 24" 
            stroke="currentColor" 
            stroke-width="2"
          >
            <path 
              stroke-linecap="round" 
              stroke-linejoin="round" 
              d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
            />
          </svg>
        </button>
      </div>
  
      <!-- Content -->
      <div class="p-6">
        <div class="space-y-4">
          <PropertyDetailInfo 
            :property-detail="propertyDetail" 
            :recent-size="getRecentSize()"
            :recent-price="getRecentPrice()"
          />
          
          <!-- 차트 섹션 -->
          <div class="my-10 border-b pb-8">
            <PriceTrendChart :deals="propertyDetail.deals" />
          </div>
  
          <PropertyDealHistory 
            :deals="propertyDetail.deals"
            :format-date="formatDate"
            :format-price="formatPrice"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import PriceTrendChart from "@/components/property/PriceTrendChart.vue";
  import PropertyDetailInfo from "@/components/property/PropertyDetailInfo.vue";
  import PropertyDealHistory from "@/components/property/PropertyDealHistory.vue";
  
  const props = defineProps({
    propertyDetail: {
      type: Object,
      required: true,
    },
  });
  
  const emit = defineEmits(["toggle-favorite"]);
  
  // 찜하기 상태 관리
  const isFavorite = ref(false);
  
  onMounted(() => {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '{}');
    isFavorite.value = favorites[props.propertyDetail.aptSeq] || false;
  });
  
  const toggleFavorite = () => {
    isFavorite.value = !isFavorite.value;
    const favorites = JSON.parse(localStorage.getItem('favorites') || '{}');
    favorites[props.propertyDetail.aptSeq] = isFavorite.value;
    localStorage.setItem('favorites', JSON.stringify(favorites));
    emit('toggle-favorite', {
      propertyId: props.propertyDetail.aptSeq,
      isFavorite: isFavorite.value
    });
  };
  
  const formatDate = (dateString) => {
    return dateString.replace(/(\d{4})-(\d{2})-(\d{2})/, "$1년 $2월 $3일");
  };
  
  const formatPrice = (amount) => {
    if (amount >= 100000000) {
      const uk = Math.floor(amount / 100000000);
      const man = Math.floor((amount % 100000000) / 10000);
      if (man > 0) {
        return `${uk}억 ${man}만원`;
      }
      return `${uk}억원`;
    }
    const man = Math.floor(amount / 10000);
    return `${man}만원`;
  };
  
  const getRecentPrice = () => {
    if (props.propertyDetail.deals?.length > 0) {
      return formatPrice(props.propertyDetail.deals[0].dealAmount);
    }
    return "가격정보 없음";
  };
  
  const getRecentSize = () => {
    if (props.propertyDetail.deals?.length > 0) {
      return props.propertyDetail.deals[0].excluUseAr;
    }
    return "-";
  };
  </script>