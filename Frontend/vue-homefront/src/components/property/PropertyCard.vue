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
          {{ interestStore.formatPrice(property.price) }}
          <span class="text-sm"></span>
        </div>

        <div class="flex items-center gap-2 text-xs text-gray-600">
          <span>{{ property.type }}</span>
          <span class="text-gray-300 mx-1">·</span>
          <span>{{ property.size }}m²</span>
          <span class="text-gray-300 mx-1">·</span>
          <span>{{ property.floor }}층</span>
        </div>
      </div>

      <div class="mt-2 text-2xs text-gray-500">
        <div class="text-xs mb-0.5">
          총 거래 {{ property.dealCount }}건
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
import { useInterestStore } from '@/stores/interest';

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
      dealCount: 0,
      dealPercent: 0,
      date: new Date(),
    })
  }
});

defineEmits(['click']);

const interestStore = useInterestStore();

const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
};
</script>
