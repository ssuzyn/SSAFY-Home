import { defineStore } from "pinia";
import { ref } from "vue";
import { message } from "ant-design-vue";
import { useAxiosStore } from "./axiosStore";

export const useInterestDrawer = defineStore("interestDrawer", () => {
  const isVisible = ref(false);
  const favorites = ref([]);
  const loading = ref(false);

  // axiosStore는 메서드 내부에서 호출
  const getAxiosStore = () => {
    const axiosStore = useAxiosStore();
    if (!axiosStore) {
      throw new Error("AxiosStore not initialized");
    }
    return axiosStore;
  };

  const isFavorite = (aptSeq) => {
    return favorites.value.some((item) => item.aptSeq === aptSeq);
  };

  const totalCount = () => favorites.value.length;

  const initialize = async () => {
    console.log("initialize called");
    if (loading.value) return;

    try {
      loading.value = true;
      console.log("Making API request...");
      const axiosStore = getAxiosStore();
      const response = await axiosStore.get("/interest-apt/list");

      console.log("API Response:", response.data);
      if (response.data.data) {
        favorites.value = response.data.data.map((item) => ({
          aptSeq: item.aptSeq,
          aptName: item.aptName,
          dongName: item.dongName,
          latestPrice: item.latestDealAmount,
          prevPrice: item.prevDealAmount,
          change: item.priceChangeRate,
        }));
      }
    } catch (error) {
      console.error("API Error:", error);
      message.error(
        error.response?.data?.message ||
          "관심 매물 목록을 불러오는데 실패했습니다"
      );
    } finally {
      loading.value = false;
    }
  };

  const toggleFavorite = async (aptSeq) => {
    if (!aptSeq) return;

    try {
      loading.value = true;
      const axiosStore = useAxiosStore();
      const isCurrentlyFavorite = isFavorite(aptSeq);

      if (isCurrentlyFavorite) {
        // 관심 매물 삭제 요청
        await axiosStore.delete(`/interest-apt/delete/${aptSeq}`);
        favorites.value = favorites.value.filter(
          (item) => item.aptSeq !== aptSeq
        );
        message.success("관심 매물이 삭제되었습니다");
      } else {
        // 관심 매물 추가 요청
        const response = await axiosStore.post(`/interest-apt/add/${aptSeq}`);
        const newFavorite = response.data; // 백엔드에서 반환된 데이터 사용
        favorites.value = [...favorites.value, newFavorite]; // 상태 업데이트
        message.success("관심 매물이 추가되었습니다");
      }
    } catch (error) {
      console.error("API Error:", error);
      message.error(
        error.response?.data?.message || "처리 중 오류가 발생했습니다"
      );
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
    closeDrawer,
  };
});
