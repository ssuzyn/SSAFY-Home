<script setup>
import { StarFilled, CloseOutlined } from "@ant-design/icons-vue";
import { useInterestDrawer } from "@/stores/interestDrawer";
import { storeToRefs } from "pinia";
import { useAuth } from "@/stores/auth";
import { onMounted, watch } from "vue";
import InterestButton from "./InterestButton.vue";
import { useInterestStore } from '@/stores/interest';
import { getHouseDetail } from '@/api/house';

const store = useInterestDrawer();
const { favorites } = storeToRefs(store);
const auth = useAuth();
const interestStore = useInterestStore();

// closeDrawer 함수 추가
const closeDrawer = () => {
  store.isVisible = false;
};

// 아파트 클릭 핸들러
const handlePropertyClick = async (property) => {
  try {
    // 1. 먼저 백엔드에서 아파트 상세 정보를 가져옴
    const detail = await getHouseDetail(property.aptSeq);

    // 2. 상세 정보에서 위도/경도를 포함한 정보를 store에 저장
    interestStore.selectedProperty = {
      ...property,
      ...detail  // 여기에 위도/경도 등 상세 정보가 포함됨
    };
  } catch (error) {
    console.error('Failed to fetch property details:', error);
  }
};

onMounted(() => {
  if (auth.isLoggedIn) {
    store.initialize();
  }
});

watch(() => store.isVisible, (newValue) => {
  if (newValue && auth.isLoggedIn) {
    store.initialize();
  }
});
</script>

<template>
  <div
    class="fixed right-0 top-0 h-full bg-white z-30 transform transition-transform duration-300 ease-in-out shadow-lg"
    :class="{
      'translate-x-0 w-[360px]': store.isVisible,
      'translate-x-full': !store.isVisible,
    }"
    style="top: 55px"
  >
    <div class="h-full overflow-y-auto">
      <div class="relative">
        <button
          @click="closeDrawer"
          class="absolute left-2 top-4 p-2 hover:bg-gray-100 rounded-full"
        >
          <close-outlined />
        </button>
      </div>

      <div class="drawer-header">
        <div class="header-title">
          <span v-if="auth.isLoggedIn"
            >총 {{ store.totalCount() }}개의 관심 매물이 있어요!</span
          >
          <span v-else>로그인이 필요한 서비스입니다!</span>
        </div>
      </div>

      <div v-if="!auth.isLoggedIn" class="p-4 text-center">
        <div class="text-gray-600 mb-4">
          <p>관심 매물 서비스를 이용하시려면</p>
          <p>로그인해주세요.</p>
        </div>

        <router-link
          to="/login"
          class="inline-block px-4 py-2.5 text-lg bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 text-white font-semibold rounded-xl transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden"
          @click="closeDrawer"
        >
          <span class="relative z-10 text-white">로그인하기</span>
          <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-50"></div>
          <div class="login-button-shine"></div>
        </router-link>
      </div>

      <div v-else class="properties-list">
        <div v-if="store.loading" class="text-center py-4">로딩 중...</div>
        <template v-else>
          <div
            v-for="property in favorites"
            :key="property.aptSeq"
            class="property-item cursor-pointer"
            @click="handlePropertyClick(property)"
          >
            <div class="mb-1">
              <span class="text-gray-600 text-sm">{{ property.dongNm }}</span>
            </div>
            <div class="mb-1">
              <span class="text-gray-600 text-medium">{{
                property.aptNm
              }}</span>
            </div>
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <h3 class="text-lg font-medium">
                  {{ interestStore.formatPrice(interestStore.parseAmount(property.latestPrice)) }}
                </h3>
              </div>
              <div class="flex items-center space-x-3">
                <div
                  class="price-change"
                  :class="{
                    'bg-red-50 text-red-500': property.change > 0,
                    'bg-blue-50 text-blue-500': property.change < 0,
                  }"
                >
                  <span v-if="property.change > 0">▲</span>
                  <span v-else>▼</span>
                  {{ Math.abs(property.change) }}%
                </div>
                <InterestButton
                  :apt-seq="property.aptSeq"
                  :disabled="store.loading"
                  class="interest-btn text-blue-500"
                  @click.stop
                  />
              </div>
            </div>
            <div class="mt-2 border-b pb-4"></div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<style scoped>
.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  margin-top: 40px;
  border-bottom: 1px solid #f0f0f0;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
}

.interest-btn {
  font-size: 24px;
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.properties-list {
  height: calc(100vh - 104px);
  overflow-y: auto;
  padding: 16px;
}

.property-card {
  margin-bottom: 16px;
}

.location {
  color: #8c8c8c;
  font-size: 12px;
}

.property-name {
  font-size: 16px;
  font-weight: 500;
  margin: 4px 0;
}

.price-change {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.price-change.increase {
  background-color: #fff1f0;
  color: #ff4d4f;
}

.price-change.decrease {
  background-color: #f0f5ff;
  color: #1890ff;
}
</style>
