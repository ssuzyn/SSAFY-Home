<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import IconSearch from '@/components/icons/IconSearch.vue';

defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
});

defineEmits(["update:modelValue", "search"]);
</script>

<template>
  <div class="relative w-full sm:w-80">
    <input
      type="text"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :placeholder="placeholder"
      class="w-full px-4 py-2 border border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm"
      @keyup.enter="$emit('search')"
    />
    <BaseButton position="absolute" @click="$emit('search')">
      <IconSearch />
    </BaseButton>
  </div>
</template>