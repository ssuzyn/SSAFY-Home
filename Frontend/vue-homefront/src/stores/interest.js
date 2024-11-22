import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getHouseDetail } from '@/api/house';

export const useInterestStore = defineStore('interest', () => {
  const selectedProperty = ref(null);
  const shouldShowDetail = ref(false);

  // 관심 매물 선택 시 상세 정보 조회
  const selectProperty = async (property) => {
    try {
      const detail = await getHouseDetail(property.aptSeq);
      selectedProperty.value = {
        ...property,
        ...detail,
        deals: detail.deals.map(deal => ({
          ...deal,
          dealAmount: parseAmount(deal.dealAmount)
        }))
      };
      shouldShowDetail.value = true;
    } catch (error) {
      console.error('Failed to fetch interest property details:', error);
      selectedProperty.value = null;
      shouldShowDetail.value = false;
    }
  };

  const clearSelection = () => {
    selectedProperty.value = null;
    shouldShowDetail.value = false;
  };

  // 금액 파싱 유틸리티 함수
  const parseAmount = (amount) => {
    if (!amount) return 0;
    return parseInt(amount.replace(/,/g, '')) * 10000;
  };

  return {
    selectedProperty,
    shouldShowDetail,
    selectProperty,
    clearSelection
  };
});