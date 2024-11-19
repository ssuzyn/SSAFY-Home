<script setup>
import { ref } from "vue";

const searchQuery = ref("");

const handleSearch = () => {
  console.log("Searching for:", searchQuery.value);
  // Emit search event to parent component
  emit("search", searchQuery.value);
};

const emit = defineEmits(["search"]);
</script>

<template>
  <nav class="bg-white shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <img class="h-8 w-8" src="/houselogo.ico" alt="Logo" />
          </div>
          <div class="ml-4 flex items-baseline space-x-4">
            <a
              href="#"
              class="text-gray-700 hover:text-orange-500 px-3 py-2 rounded-md text-sm font-medium"
              >홈</a
            >
            <a
              href="#"
              class="text-gray-700 hover:text-orange-500 px-3 py-2 rounded-md text-sm font-medium"
              >매물</a
            >
            <a
              href="#"
              class="text-gray-700 hover:text-orange-500 px-3 py-2 rounded-md text-sm font-medium"
              >뉴스</a
            >
          </div>
        </div>
        <div class="flex items-center">
          <div class="relative">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="아파트명을 검색하세요"
              class="w-64 px-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500"
              @keyup.enter="handleSearch"
            />
            <button
              @click="handleSearch"
              class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-orange-500"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fill-rule="evenodd"
                  d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>
