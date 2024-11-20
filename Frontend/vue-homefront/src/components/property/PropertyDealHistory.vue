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
              v-for="deal in deals"
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
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
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
  </script>