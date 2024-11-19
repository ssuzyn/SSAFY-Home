<!-- src/components/common/VSelect.vue -->
<script setup>
import { ref } from "vue";

defineProps({
  selectOption: Array,
  disabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(["onKeySelect"]);
const key = ref("");

const onSelect = () => {
  emit("onKeySelect", key.value);
};
</script>

<template>
  <select
    v-model="key"
    class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
    :disabled="disabled"
    @change="onSelect"
  >
    <option
      v-for="option in selectOption"
      :key="option.value"
      :value="option.value"
      :disabled="option.value === ''"
    >
      {{ option.text }}
    </option>
  </select>
</template>