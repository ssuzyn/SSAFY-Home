<script setup>
import { StarFilled, CloseOutlined } from '@ant-design/icons-vue';
import { useInterestDrawer } from '@/stores/interestDrawer';

const store = useInterestDrawer();

const closeDrawer = () => {
 store.isVisible = false;
};
</script>

<template>
 <div 
   class="fixed right-0 top-0 h-full bg-white z-30 transform transition-transform duration-300 ease-in-out shadow-lg"
   :class="{
     'translate-x-0 w-[320px]': store.isVisible,
     'translate-x-full': !store.isVisible
   }"
   style="top: 120px;"
 >
   <div class="h-full overflow-y-auto">
     <div class="relative">
       <button 
         @click="closeDrawer"
         class="absolute left-2 top-3 p-2 hover:bg-gray-100 rounded-full "
       >
         <close-outlined />
       </button>
     </div>

     <div class="drawer-header">
       <div class="header-title">
         <span>총 {{ store.favorites.length }}개의 관심 매물이 있어요!</span>
       </div>
     </div>

     <div class="properties-list">
      <div v-for="property in store.favorites" :key="property.id" class="property-item">
        <!-- 위치 + 아파트명 -->
        <div class="mb-1">
          <span class="text-gray-600 text-sm">{{ property.location }}</span>
        </div>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <h3 class="text-lg font-medium">{{ property.name }}</h3>
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
            <button class="interest-btn text-blue-500">
              <StarFilled />
            </button>
          </div>
        </div>
        <div class="mt-2 border-b pb-4"></div>
      </div>
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