import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useInterestDrawer = defineStore('interestDrawer', () => {
  const isVisible = ref(false);
  const favorites = ref([
    {
      id: 1,
      location: '압구정동',
      name: '한양5차',
      change: 7.1,
      size: '84.55m²',
      price: '5억 9000만',
      dong: '1동',
      date: '2024.08.24'
    },
    {
      id: 2,
      location: '거제동',
      name: '거제1차현대홀타운',
      change: 2.3,
      size: '112.66m²',
      price: '7억 7700만',
      dong: '2동',
      date: '2024.08.23'
    },
    {
      id: 3,
      location: '효복동',
      name: '동대구푸르지오브리센트',
      change: -4.1,
      size: '84.55m²',
      price: '6억 2300만',
      dong: '3동',
      date: '2024.08.22'
    }
  ]);

  const toggle = () => {
    isVisible.value = !isVisible.value;
  };

  const removeFavorite = (id) => {
    favorites.value = favorites.value.filter(item => item.id !== id);
  };

  const close = () => {
    isVisible.value = false;
  };
  
  return {
    isVisible,
    favorites,
    toggle,
    removeFavorite,
    close
  };
});
