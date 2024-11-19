// HomeView.vue
<script setup>
import { ref } from "vue";
import LocationSelect from "@/components/property/LocationSelect.vue";
import PropertyList from "@/components/property/PropertyList.vue";
import PropertyMap from "@/components/property/PropertyMap.vue";
import PropertyDetailModal from "@/components/property/PropertyDetailModal.vue";

const searchResults = ref([]);
const selectedProperty = ref(null);

const handleSearchResult = (response) => {
  console.log('Search results:', response);
  // response가 이미 배열이므로 직접 할당
  searchResults.value = Array.isArray(response) ? response : [];
  console.log('Updated searchResults:', searchResults.value);
  selectedProperty.value = null;
};

const handlePropertySelect = (property) => {
  console.log('Selected property:', property);
  selectedProperty.value = property;
};
</script>

<template>
  <div class="flex flex-col h-screen pt-16">
    <LocationSelect @search-result="handleSearchResult" />
    <div class="flex flex-1 overflow-hidden">
      <!-- 검색 결과 개수 표시 -->
      <div v-if="searchResults.length" class="absolute top-20 left-4 text-sm text-gray-500">
        검색 결과: {{ searchResults.length }}건
      </div>
      
      <PropertyList
        :properties="searchResults"
        @select-property="handlePropertySelect"
        class="w-[400px] border-r overflow-y-auto"
      />
      <PropertyMap
        :properties="searchResults"
        :selected-property="selectedProperty"
        @select-property="handlePropertySelect"
        class="flex-1"
      />
    </div>

    <PropertyDetailModal
      v-if="selectedProperty"
      :property="selectedProperty"
      @close="selectedProperty = null"
    />
  </div>
</template>