<template>
  <div class="mt-6">
    <h4 class="text-lg font-bold mb-4">거래 내역</h4>
    <div class="bg-white rounded-xl border border-gray-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <th class="px-6 py-3.5 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                거래일
              </th>
              <th class="px-6 py-3.5 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                층
              </th>
              <th class="px-6 py-3.5 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                면적
              </th>
              <th class="px-6 py-3.5 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                거래금액
              </th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr
              v-for="(deal, index) in displayedDeals"
              :key="deal.dealDate + deal.floor"
              class="hover:bg-gray-50 transition-colors duration-150"
            >
              <td class="px-6 py-4 text-sm text-gray-600">
                {{ formatDate(deal.dealDate) }}
              </td>
              <td class="px-6 py-4 text-sm text-gray-600">
                {{ deal.floor }}층
              </td>
              <td class="px-6 py-4 text-sm text-gray-600">
                {{ deal.excluUseAr }}㎡
              </td>
              <td class="px-6 py-4 text-sm font-medium text-gray-900">
                {{ formatPrice(deal.dealAmount) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 더보기 버튼 -->
      <div v-if="hasMoreDeals"
           class="px-6 py-3 bg-gray-50 border-t border-gray-200">
        <button
          @click="showMore"
          class="w-full py-2 text-sm text-gray-600 hover:text-gray-900 transition-colors flex items-center justify-center gap-1 group"
        >
          더보기
          <svg
            class="w-4 h-4 transition-transform group-hover:translate-y-0.5"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M6 9l6 6 6-6" />
          </svg>
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
