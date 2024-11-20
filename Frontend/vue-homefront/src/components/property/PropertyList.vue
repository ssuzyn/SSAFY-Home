<template>
  <div class="grid grid-cols-2 gap-3 p-4">
    <div v-if="!properties || properties.length === 0" class="col-span-2 text-center text-gray-500 py-8">
      <NoDataModal 
        :open="showNoDataModal" 
        @close="closeModal" 
      />
    </div>
    <div v-else class="col-span-2">
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <div 
          v-for="property in properties"
          :key="property.aptSeq + '-' + property.floor"
          class="cursor-pointer"
          @click="handlePropertyClick(property)"
        >
          <PropertyCard
            :property="transformProperty(property)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import PropertyCard from '@/components/property/PropertyCard.vue';
import NoDataModal from '@/components/property/NoDataModal.vue';
import { ref, watch } from 'vue';

const props = defineProps({
  properties: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['select-property']);

watch(() => props.properties, (newProps) => {
  console.log('PropertyList received properties:', newProps);
}, { deep: true });

const transformProperty = (deal) => {
  return {
    aptSeq: deal.aptSeq,
    name: deal.aptNm,
    saleType: '매매',
    price: parseAmount(deal.dealAmount),
    size: Math.round(deal.excluUseAr),
    type: '아파트',
    floor: deal.floor,
    date: new Date(deal.dealYear, deal.dealMonth - 1, deal.dealDay),
    description: `전용 ${deal.excluUseAr}㎡, ${deal.floor}층`,
    registrationNumber: deal.aptSeq,
    dealCount: deal.dealCount
  };
};

const parseAmount = (amount) => {
  if (!amount) return 0;
  return parseInt(amount.replace(/,/g, '')) * 10000;
};

const handlePropertyClick = (property) => {
  console.log('Property clicked:', property);
  emit('select-property', property);
};

</script>