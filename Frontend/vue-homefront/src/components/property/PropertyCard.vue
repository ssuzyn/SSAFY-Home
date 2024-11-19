<template>
  <div 
    class="bg-white rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow duration-200 cursor-pointer"
    @click="$emit('click')"
  >
    <div class="flex justify-between items-start">
      <div class="space-y-1">
        <h3 class="text-lg font-semibold text-gray-900">{{ property.name }}</h3>
        <p class="text-xl font-bold text-blue-600">
          {{ formatPrice(property.price) }}
        </p>
        <div class="flex items-center gap-2 text-sm text-gray-600">
          <span>{{ property.size }}m²</span>
          <span class="text-gray-300">·</span>
          <span>{{ property.type }}</span>
          <span class="text-gray-300">·</span>
          <span>{{ property.floor }}층</span>
        </div>
        <div v-if="property.address" class="text-sm text-gray-500">
          {{ property.address }}
        </div>
        <div class="flex items-center gap-2 text-xs text-gray-400 mt-2">
          <template v-if="property.maintenanceFee">
            <span>관리비 {{ formatPrice(property.maintenanceFee) }}/월</span>
            <span class="text-gray-300">·</span>
          </template>
          <template v-if="property.parking">
            <span>주차 {{ property.parking }}대</span>
          </template>
        </div>
      </div>
      <span class="text-sm text-gray-500">{{ formatDate(property.date) }}</span>
    </div>
    <template v-if="property.description">
      <div class="mt-3 pt-3 border-t border-gray-200">
        <p class="text-sm text-gray-600">
          {{ property.description }}
        </p>
      </div>
    </template>
    <template v-if="property.agency || property.registrationNumber">
      <div class="mt-3 flex justify-between items-center text-xs text-gray-500">
        <span v-if="property.agency">중개사무소: {{ property.agency }}</span>
        <span v-if="property.registrationNumber">등록번호: {{ property.registrationNumber }}</span>
      </div>
    </template>
  </div>
</template>

<script setup>
const props = defineProps({
  property: {
    type: Object,
    required: true,
    default: () => ({
      name: '',
      saleType: '',
      price: 0,
      size: 0,
      type: '',
      floor: 0,
      totalFloors: 0,
      address: '',
      maintenanceFee: 0,
      parking: 0,
      date: new Date(),
      description: '',
      agency: '',
      registrationNumber: ''
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
    result += `${million}만`;
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