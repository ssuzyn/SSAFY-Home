import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getHouseDetail } from '@/api/house';

export const useInterestStore = defineStore('interest', () => {
  const selectedProperty = ref(null);
  const shouldShowDetail = ref(false);

  // 날짜 파싱 함수
  const parseDealDate = (dealDate) => {
    if (!dealDate) return { dealYear: '', dealMonth: '', dealDay: '' };
    return {
      dealYear: dealDate.substring(0, 4),
      dealMonth: dealDate.substring(4, 6),
      dealDay: dealDate.substring(6, 8)
    };
  };

  // property 데이터 구조 매핑 함수
  const mapPropertyData = (property, detail) => {
    // 최신 거래 정보 가져오기 (deals 배열의 첫 번째 항목)
    const latestDeal = detail.deals?.[0] || {};
    const { dealYear, dealMonth, dealDay } = parseDealDate(latestDeal.dealDate);

    return {
      // 기본 정보
      aptSeq: detail.aptSeq,
      aptNm: detail.aptNm, // infowindow용
      dongNm: detail.dongNm,
      
      // 위치 정보
      latitude: detail.latitude,
      longitude: detail.longitude,
      
      // 최신 거래 정보
      floor: detail.floor || '',
      excluUseAr: detail.excluUseAr || 0,
      dealAmount: detail.dealAmount || '',
      
      // 날짜 정보
      dealYear,
      dealMonth,
      dealDay,
      
      // 거래 목록
      deals: (detail.deals || []).map(deal => {
        const { dealYear, dealMonth, dealDay } = parseDealDate(deal.dealDate);
        return {
          dealAmount: parseAmount(deal.dealAmount),
          dealYear,
          dealMonth,
          dealDay,
          floor: deal.floor,
          excluUseAr: deal.excluUseAr
        };
      }),
      
      // 거래 건수
      dealCount: detail.dealCount || '0',
      
      // 지도 마커와 인포윈도우에 필요한 계산된 필드들
      price: parseAmount(detail.dealAmount), // 숫자형 가격
      formattedDate: dealYear ? 
        `${dealYear}.${dealMonth}.${dealDay}` : 
        null
    };
  };

  // 관심 매물 선택 시 상세 정보 조회
  const selectProperty = async (property) => {
    try {
      const detail = await getHouseDetail(property.aptSeq);
      selectedProperty.value = mapPropertyData(property, detail);
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

  // 가격 포맷팅 유틸리티 함수
  const formatPrice = (price) => {
    const billion = Math.floor(price / 100000000);
    const million = Math.floor((price % 100000000) / 10000);
    
    let result = '';
    if (billion > 0) result += `${billion}억 `;
    if (million > 0) result += `${million}`;
    if (billion === 0 && million === 0) result = '0';
    return result.trim();
  };

  return {
    selectedProperty,
    shouldShowDetail,
    selectProperty,
    clearSelection,
    formatPrice
  };
});