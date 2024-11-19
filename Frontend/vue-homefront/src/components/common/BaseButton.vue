<script setup>
defineProps({
  position: {
    type: String,
    default: "relative",
  },
});

defineEmits(["click"]);
</script>

<template>
  <button
    @click="$emit('click')"
    :class="[
      'text-gray-400 hover:text-orange-500',
      position === 'absolute'
        ? 'absolute right-2 top-1/2 transform -translate-y-1/2'
        : '',
    ]"
  >
    <slot />
  </button>
</template>
