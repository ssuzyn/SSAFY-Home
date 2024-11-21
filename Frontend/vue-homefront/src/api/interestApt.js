import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';

export const useInterestStore = defineStore('interest', () => {
  // State
  const isVisible = ref(false);
  const favorites = ref([]);
  const loading = ref(false);

  // Getters
  const isFavorite = (aptSeq) => {
    return favorites.value.some(item => item.aptSeq === aptSeq);
  };

  const totalCount = () => favorites.value.length;

  // Actions
  const initialize = async () => {
    if (loading.value || favorites.value.length) return;
    
    try {
      loading.value = true;
      const response = await axios.get('/interest-apt/list');
      favorites.value = response.data.data;
    } catch (error) {
      message.error(error.response?.data?.message || '관심 매물 목록을 불러오는데 실패했습니다');
      console.error('Failed to initialize favorites:', error);
    } finally {
      loading.value = false;
    }
  };

  const toggle = async (property) => {
    const isCurrentlyFavorite = isFavorite(property.aptSeq);
    
    try {
      loading.value = true;
      
      if (isCurrentlyFavorite) {
        await axios.delete(`/interest-apt/delete/${property.aptSeq}`);
        favorites.value = favorites.value.filter(item => item.aptSeq !== property.aptSeq);
        message.success('관심 매물이 삭제되었습니다');
      } else {
        await axios.post(`/interest-apt/add/${property.aptSeq}`);
        // API 응답에 따라 전체 목록을 다시 불러오거나 또는 property를 직접 추가
        await initialize();  // 전체 목록 갱신
        message.success('관심 매물이 추가되었습니다');
      }
    } catch (error) {
      message.error(error.response?.data?.message || '처리 중 오류가 발생했습니다');
      console.error('Failed to toggle favorite:', error);
    } finally {
      loading.value = false;
    }
  };

  // Drawer controls
  const toggleDrawer = () => {
    isVisible.value = !isVisible.value;
  };

  const closeDrawer = () => {
    isVisible.value = false;
  };

  return {
    // State
    isVisible,
    favorites,
    loading,
    
    // Getters
    isFavorite,
    totalCount,
    
    // Actions
    initialize,
    toggle,
    toggleDrawer,
    closeDrawer
  };
});