<script setup>
import { ref } from "vue";
import SearchNavBar from "@/components/property/search/SearchNavBar.vue";
import SearchFilter from "@/components/property/search/SearchFilter.vue";
import PropertyList from "@/components/property/PropertyList.vue";
import PropertyMap from "@/components/property/PropertyMap.vue";
import PropertyDetailModal from "@/components/property/PropertyDetailModal.vue";

// ... existing properties data ...

const selectedProperty = ref(null);

const handlePropertySelect = (property) => {
  selectedProperty.value = property;
};

const handleNavBarSearch = (query) => {
  console.log("Nav bar search:", query);
  // Implement nav bar search logic here
};

const handleFilterSearch = (filters) => {
  console.log("Filter search:", filters);
  // Implement filter search logic here
  // Example:
  // const filteredProperties = properties.value.filter(property => {
  //   return (
  //     (!filters.city || property.location.city === filters.city) &&
  //     (!filters.district || property.location.district === filters.district) &&
  //     (!filters.dong || property.location.dong === filters.dong) &&
  //     (!filters.query || property.name.includes(filters.query))
  //   )
  // })
};
</script>

<template>
  <div class="flex flex-col h-screen">
    <SearchNavBar @search="handleNavBarSearch" />
    <SearchFilter @search="handleFilterSearch" />
    <div class="flex flex-1 overflow-hidden">
      <PropertyList
        :properties="properties"
        @select-property="handlePropertySelect"
        class="w-[400px] border-r"
      />
      <PropertyMap
        :properties="properties"
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
