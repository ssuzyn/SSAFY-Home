// stores/interestDrawer.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { message } from 'ant-design-vue';
import { axiosInstance } from '@/stores/auth';  // auth.js의 axiosInstance를 import

export const useInterestDrawer = defineStore('interestDrawer', () => {
  const isVisible = ref(false);
  const favorites = ref([]);
  const loading = ref(false);

  const isFavorite = (aptSeq) => {
    return favorites.value.some(item => item.aptSeq === aptSeq);
  };

  const totalCount = () => favorites.value.length;

  const initialize = async () => {
    console.log('initialize called');
    if (loading.value) return;
    
    try {
      loading.value = true;
      console.log('Making API request...');
      const response = await axiosInstance.get('/interest-apt/list');
      console.log('API Response:', response.data);
      if (response.data.data) {
        favorites.value = response.data.data.map(item => ({
          aptSeq: item.aptSeq,
          name: item.aptName,
          latestPrice: item.latestDealAmount,
          prevPrice: item.prevDealAmount,
          change: item.priceChangeRate
        }));
      }
    } catch (error) {
      console.error('API Error:', error);
      message.error(error.response?.data?.message || '관심 매물 목록을 불러오는데 실패했습니다');
    } finally {
      loading.value = false;
    }
  };

  const toggleFavorite = async (aptSeq) => {
    if (!aptSeq) return;
    
    try {
      loading.value = true;
      const isCurrentlyFavorite = isFavorite(aptSeq);
      
      if (isCurrentlyFavorite) {
        await axiosInstance.delete(`/interest-apt/delete/${aptSeq}`);
        favorites.value = favorites.value.filter(item => item.aptSeq !== aptSeq);
        message.success('관심 매물이 삭제되었습니다');
      } else {
        await axiosInstance.post(`/interest-apt/add/${aptSeq}`);
        await initialize();
        message.success('관심 매물이 추가되었습니다');
      }
    } catch (error) {
      console.error('API Error:', error);
      message.error(error.response?.data?.message || '처리 중 오류가 발생했습니다');
    } finally {
      loading.value = false;
    }
  };

  const toggleDrawer = () => {
    isVisible.value = !isVisible.value;
  };

  const closeDrawer = () => {
    isVisible.value = false;
  };

  return {
    isVisible,
    favorites,
    loading,
    isFavorite,
    totalCount,
    initialize,
    toggleFavorite,
    toggleDrawer,
    closeDrawer
  };
});