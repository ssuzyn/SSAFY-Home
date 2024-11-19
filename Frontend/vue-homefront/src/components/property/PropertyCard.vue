<template>
  <div 
    class="bg-gray-100 rounded-lg shadow-sm hover:shadow-md
    transition-shadow duration-200 cursor-pointer overflow-hidden"
    @click="$emit('click')"
  >
    <div class="p-3 flex flex-col">
      <div class="text-medium font-medium text-gray-900 mb-2">{{ property.name }}</div>
      
      <div class="flex flex-col">
        <div class="text-xl font-bold text-gray-900 mb-2">
          {{ formatPrice(property.price) }}
          <span class="text-sm">만</span>
        </div>
        
        <div class="flex items-center gap-2 text-xs text-gray-600">
          <span>{{ property.size }}m²</span>
          <span class="text-gray-300 mx-1">·</span>
          <span>{{ property.type }}</span>
          <span class="text-gray-300 mx-1">·</span>
          <span>{{ property.floor }}층</span>
        </div>
      </div>

      <div class="mt-2 text-2xs text-gray-500">
        <div v-if="property.recentDeals" class="mb-0.5">
          최근 6개월 거래 {{ property.recentDeals }}건
        </div>
        <div v-if="property.dealPercent">
          최고 대비 실거래 {{ property.dealPercent }}%
        </div>
      </div>

      <div class="mt-2 text-xs text-gray-500">
        {{ formatDate(property.date) }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  property: {
    type: Object,
    required: true,
    default: () => ({
      name: '',
      price: 0,
      size: 0,
      type: '아파트',
      floor: 0,
      recentDeals: 0,
      dealPercent: 0,
      date: new Date(),
    })
  }
});

defineEmits(['click']);

const formatPrice = (price) => {
  const billion = Math.floor(price / 100000000);
  const million = Math.floor((price % 100000000) / 10000);
  
  let result = '';
  if (billion > 0) {
    result += `${billion}억 `;
  }
  if (million > 0) {
    result += `${million}`;
  }
  if (billion === 0 && million === 0) {
    result = '0';
  }
  return result.trim();
};

const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
};
</script>