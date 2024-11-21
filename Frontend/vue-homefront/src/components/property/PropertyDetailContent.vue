<!-- components/property/PropertyDetailContent.vue -->
<template>
  <div>
    <!-- Header with heart button -->
    <div class="flex items-center justify-between p-4 border-b">
      <h3 class="text-xl font-semibold text-gray-900">
        {{ propertyDetail.aptNm }}
      </h3>
      <InterestButton :apt-seq="propertyDetail.aptSeq" />
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
import { onMounted } from 'vue';
import { useInterestDrawer } from '@/stores/interestDrawer';
import InterestButton from '@/components/interest/InterestButton.vue';
import PriceTrendChart from "@/components/property/PriceTrendChart.vue";
import PropertyDetailInfo from "@/components/property/PropertyDetailInfo.vue";
import PropertyDealHistory from "@/components/property/PropertyDealHistory.vue";

const props = defineProps({
  propertyDetail: {
    type: Object,
    required: true,
  },
});

const interestStore = useInterestDrawer();

onMounted(() => {
  interestStore.initialize();
});

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