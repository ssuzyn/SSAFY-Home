<script setup>
const props = defineProps({
  properties: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["selectProperty"]);

const selectProperty = (property) => {
  emit("selectProperty", property);
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
};

const formatPrice = (price) => {
  return new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: "KRW",
  }).format(price);
};
</script>

<template>
  <div class="bg-white h-full overflow-auto">
    <div class="px-4 py-5 sm:px-6">
      <h3 class="text-lg leading-6 font-medium text-gray-900">매물 목록</h3>
    </div>
    <ul class="divide-y divide-gray-200">
      <li
        v-for="property in properties"
        :key="property.id"
        class="px-4 py-4 sm:px-6 hover:bg-gray-50 cursor-pointer"
        @click="selectProperty(property)"
      >
        <div class="flex items-center justify-between">
          <p class="text-sm font-medium text-orange-600 truncate">
            {{ property.name }}
          </p>
          <div class="ml-2 flex-shrink-0 flex">
            <p
              class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800"
            >
              {{ formatDate(property.date) }}
            </p>
          </div>
        </div>
        <div class="mt-2 sm:flex sm:justify-between">
          <div class="sm:flex">
            <p class="flex items-center text-sm text-gray-500">
              {{ property.size }}m² · {{ property.floor }}/{{
                property.totalFloors
              }}층
            </p>
          </div>
          <div class="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
            <p>
              {{ formatPrice(property.price) }}
            </p>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>
