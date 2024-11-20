<template>
    <Transition appear>
      <div v-if="open" class="fixed inset-0 z-[100]">
        <!-- Backdrop -->
        <div
          class="fixed inset-0 bg-black/30"
          aria-hidden="true"
          @click="closeModal"
        />
  
        <!-- Modal -->
        <div class="fixed inset-0 flex items-center justify-center p-4">
          <!-- Modal Container -->
          <div class="relative max-w-2xl w-full">
            <!-- Close button positioned outside -->
            <button
              @click="closeModal"
              class="absolute -right-12 top-2 text-white hover:text-gray-200 focus:outline-none"
            >
              <span class="sr-only">Close</span>
              <svg
                class="h-8 w-8"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
  
            <!-- Modal Content -->
            <div class="bg-white rounded-lg max-h-[90vh] overflow-y-auto">
              <slot></slot>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </template>
  
  <script setup>
  defineProps({
    open: {
      type: Boolean,
      required: true,
    },
  });
  
  const emit = defineEmits(['close']);
  
  const closeModal = () => {
    emit('close');
  };
  </script>
  
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