<template>
  <div class="grid grid-cols-2 gap-3 p-4">
    <div v-if="!properties || properties.length === 0" class="col-span-2 text-center text-gray-500 py-8">
      검색 결과가 없습니다.
    </div>
    <div v-else class="col-span-2">
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <PropertyCard
          v-for="property in properties"
          :key="property.aptSeq + '-' + property.floor"
          :property="transformProperty(property)"
          @click="$emit('select-property', property)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import PropertyCard from './PropertyCard.vue';
import { onMounted, watch } from 'vue';

const props = defineProps({
  properties: {
    type: Array,
    required: true
  }
});

// props 변경 감시
watch(() => props.properties, (newProps) => {
  console.log('PropertyList received properties:', newProps);
}, { deep: true });

const transformProperty = (deal) => {
  console.log('Transforming property:', deal);
  const transformed = {
    name: deal.aptNm,
    saleType: '매매',
    price: parseAmount(deal.dealAmount),
    size: Math.round(deal.excluUseAr),
    type: '아파트',
    floor: deal.floor,
    date: new Date(deal.dealYear, deal.dealMonth - 1, deal.dealDay),
    description: `전용 ${deal.excluUseAr}㎡, ${deal.floor}층`,
    registrationNumber: deal.aptSeq
  };
  console.log('Transformed property:', transformed);
  return transformed;
};

const parseAmount = (amount) => {
  if (!amount) return 0;
  return parseInt(amount.replace(/,/g, '')) * 10000;
};

defineEmits(['select-property']);
</script>