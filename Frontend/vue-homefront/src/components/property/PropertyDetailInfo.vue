<template>
  <div class="grid grid-cols-2 gap-x-12 gap-y-8">
    <!-- 지역 정보 -->
    <div class="flex items-start gap-4 group">
      <div class="p-2.5 bg-gray-50 rounded-lg group-hover:bg-gray-100 transition-colors">
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
      </div>
      <div>
        <p class="text-sm font-medium text-gray-600 mb-1">지역</p>
        <p class="text-lg font-bold text-gray-900">{{ propertyDetail.dongNm }}</p>
      </div>
    </div>

    <!-- 전용면적 -->
    <div class="flex items-start gap-4 group">
      <div class="p-2.5 bg-gray-50 rounded-lg group-hover:bg-gray-100 transition-colors">
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4" />
        </svg>
      </div>
      <div>
        <p class="text-sm font-medium text-gray-600 mb-1">전용면적</p>
        <p class="text-lg font-bold text-gray-900">{{ recentSize }}㎡</p>
      </div>
    </div>

    <!-- 최근 거래가 -->
    <div class="flex items-start gap-4 group">
      <div class="p-2.5 bg-gray-50 rounded-lg group-hover:bg-gray-100 transition-colors">
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </div>
      <div>
        <p class="text-sm font-medium text-gray-600 mb-1">전용면적</p>
        <p class="text-lg font-bold text-gray-900">{{ recentPrice }}</p>
      </div>
    </div>

    <!-- 총 거래건수 -->
    <div class="flex items-start gap-4 group">
      <div class="p-2.5 bg-gray-50 rounded-lg group-hover:bg-gray-100 transition-colors">
        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
        </svg>
      </div>
      <div>
        <p class="text-sm font-medium text-gray-600 mb-1">총 거래건수</p>
        <p class="text-lg font-bold text-gray-900">{{ propertyDetail.dealCount }}건</p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  propertyDetail: {
    type: Object,
    required: true,
  },
  recentSize: {
    type: [String, Number],
    required: true,
  },
  recentPrice: {
    type: String,
    required: true,
  },
});
</script>
