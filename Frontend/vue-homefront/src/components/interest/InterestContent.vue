<script setup>
import { StarFilled, CloseOutlined } from '@ant-design/icons-vue';
import { useInterestDrawer } from '@/stores/interestDrawer';
import { useAuth } from "@/stores/auth";
import { onMounted, watch } from 'vue';

const store = useInterestDrawer();
const { isLoggedIn } = useAuth();



watch(() => store.isVisible, (newVal) => {
  console.log('drawer visibility changed:', newVal);
  if (newVal && isLoggedIn) {
    store.initialize();
  }
});

const closeDrawer = () => {
  store.isVisible = false;
};
</script>

<template>
  <div 
    class="fixed right-0 top-0 h-full bg-white z-30 transform transition-transform duration-300 ease-in-out shadow-lg"
    :class="{
      'translate-x-0 w-[360px]': store.isVisible,
      'translate-x-full': !store.isVisible
    }"
    style="top: 130px;"
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
          <span v-if="isLoggedIn">총 {{ store.totalCount() }}개의 관심 매물이 있어요!</span>
          <span v-else>로그인이 필요한 서비스입니다!</span>
        </div>
      </div>

      <div v-if="!isLoggedIn" class="p-4 text-center">
        <div class="text-gray-600 mb-4">
          <p >관심 매물 서비스를 이용하시려면</p>
          <p>로그인해주세요.</p>
        </div>
        
        <router-link 
          to="/login" 
          class="inline-block px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition-colors"
          @click="closeDrawer"
        >
          로그인하기
        </router-link>
      </div>

      <div v-else class="properties-list">
        <div v-if="store.loading" class="text-center py-4">
          로딩 중...
        </div>
        <template v-else>
          <div v-for="property in store.favorites" :key="property.aptSeq" class="property-item">
            <div class="mb-1">
              <span class="text-gray-600 text-sm">{{ property.dongName }}</span>
            </div>
            <div class="mb-1">
              <span class="text-gray-600 text-medium">{{ property.aptName }}</span>
            </div>
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <h3 class="text-lg font-medium">{{ property.latestPrice }}만원</h3>
              </div>
              <div class="flex items-center space-x-3">
                <div 
                  class="price-change"
                  :class="{ 
                    'bg-red-50 text-red-500': property.change > 0,
                    'bg-blue-50 text-blue-500': property.change < 0
                  }"
                >
                  <span v-if="property.change > 0">▲</span>
                  <span v-else>▼</span>
                  {{ Math.abs(property.change) }}%
                </div>
                <button 
                  class="interest-btn text-blue-500"
                  @click="store.toggleFavorite(property.aptSeq)"
                >
                  <StarFilled />
                </button>
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