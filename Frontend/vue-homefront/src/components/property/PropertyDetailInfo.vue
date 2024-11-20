<template>
    <div class="grid grid-cols-2 gap-4">
      <div>
        <p class="text-sm text-gray-500">지역</p>
        <p class="font-medium">{{ propertyDetail.dongNm }}</p>
      </div>
      <div>
        <p class="text-sm text-gray-500">전용면적</p>
        <p class="font-medium">{{ recentSize }}㎡</p>
      </div>
      <div>
        <p class="text-sm text-gray-500">최근 거래가</p>
        <p class="font-medium">{{ recentPrice }}</p>
      </div>
      <div>
        <p class="text-sm text-gray-500">총 거래건수</p>
        <p class="font-medium">{{ propertyDetail.dealCount }}건</p>
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