<script setup>
const props = defineProps({
  open: {
    type: Boolean,
    required: true,
  },
  propertyDetail: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["close"]);

const closeModal = () => {
  emit("close");
};

const formatDate = (dateString) => {
  return dateString.replace(/(\d{4})-(\d{2})-(\d{2})/, '$1년 $2월 $3일');
};

const formatPrice = (amount) => {
  return new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: "KRW",
    maximumFractionDigits: 0
  }).format(amount);
};

const getRecentPrice = () => {
  if (props.propertyDetail.deals && props.propertyDetail.deals.length > 0) {
    return formatPrice(props.propertyDetail.deals[0].dealAmount);
  }
  return '가격정보 없음';
};

const getRecentSize = () => {
  if (props.propertyDetail.deals && props.propertyDetail.deals.length > 0) {
    return props.propertyDetail.deals[0].excluUseAr;
  }
  return '-';
};
</script>

<template>
  <Transition appear>
    <div v-if="open" class="fixed inset-0 z-[100]">
      <!-- Backdrop -->
      <div class="fixed inset-0 bg-black/30" aria-hidden="true" @click="closeModal" />
      
      <!-- Modal -->
      <div class="fixed inset-0 flex items-center justify-center p-4">
        <div class="relative bg-white rounded-lg max-w-lg w-full max-h-[90vh] overflow-y-auto">
          <!-- Header -->
          <div class="flex items-center justify-between p-4 border-b">
            <h3 class="text-xl font-semibold text-gray-900">
              {{ propertyDetail.aptNm }}
            </h3>
            <button 
              @click="closeModal"
              class="text-gray-400 hover:text-gray-500 focus:outline-none"
            >
              <span class="sr-only">Close</span>
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Content -->
          <div class="p-6">
            <div class="space-y-4">
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <p class="text-sm text-gray-500">지역</p>
                  <p class="font-medium">{{ propertyDetail.dongNm }}</p>
                </div>
                <div>
                  <p class="text-sm text-gray-500">전용면적</p>
                  <p class="font-medium">{{ getRecentSize() }}㎡</p>
                </div>
                <div>
                  <p class="text-sm text-gray-500">최근 거래가</p>
                  <p class="font-medium">{{ getRecentPrice() }}</p>
                </div>
                <div>
                  <p class="text-sm text-gray-500">총 거래건수</p>
                  <p class="font-medium">{{ propertyDetail.dealCount }}건</p>
                </div>
              </div>

              <!-- 거래 내역 -->
              <div class="mt-6">
                <h4 class="text-lg font-medium mb-3">거래 내역</h4>
                <div class="overflow-x-auto">
                  <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                      <tr>
                        <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">거래일</th>
                        <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">층</th>
                        <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">면적</th>
                        <th class="px-3 py-2 text-left text-xs font-medium text-gray-500">거래금액</th>
                      </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                      <tr v-for="deal in propertyDetail.deals" 
                          :key="deal.dealDate + deal.floor" 
                          class="hover:bg-gray-50">
                        <td class="px-3 py-2 text-sm">{{ formatDate(deal.dealDate) }}</td>
                        <td class="px-3 py-2 text-sm">{{ deal.floor }}층</td>
                        <td class="px-3 py-2 text-sm">{{ deal.excluUseAr }}㎡</td>
                        <td class="px-3 py-2 text-sm">{{ formatPrice(deal.dealAmount) }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.2s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>