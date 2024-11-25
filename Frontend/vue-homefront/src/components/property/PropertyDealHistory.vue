<template>
    <div class="mt-6">
      <h4 class="text-lg font-bold mb-3">거래 내역</h4>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                거래일
              </th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                층
              </th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                면적
              </th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                거래금액
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr
              v-for="deal in displayedDeals"
              :key="deal.dealDate + deal.floor"
              class="hover:bg-gray-50"
            >
              <td class="px-3 py-2 text-sm">
                {{ formatDate(deal.dealDate) }}
              </td>
              <td class="px-3 py-2 text-sm">{{ deal.floor }}층</td>
              <td class="px-3 py-2 text-sm">
                {{ deal.excluUseAr }}㎡
              </td>
              <td class="px-3 py-2 text-sm">
                {{ formatPrice(deal.dealAmount) }}
              </td>
            </tr>
          </tbody>
        </table>
        
        <div v-if="hasMoreDeals" class="text-center mt-4">
          <button 
            @click="showMore" 
            class="px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-md text-sm"
          >
            더보기
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';

  const props = defineProps({
    deals: {
      type: Array,
      required: true,
    },
    formatDate: {
      type: Function,
      required: true,
    },
    formatPrice: {
      type: Function,
      required: true,
    },
  });

  const itemsPerPage = ref(5);
  const currentPage = ref(1);

  const displayedDeals = computed(() => {
    return props.deals.slice(0, currentPage.value * itemsPerPage.value);
  });

  const hasMoreDeals = computed(() => {
    return displayedDeals.value.length < props.deals.length;
  });

  const showMore = () => {
    currentPage.value++;
  };
  </script>