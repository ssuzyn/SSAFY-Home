<template>
  <Modal :open="open" @close="closeModal">
    <PropertyDetailContent 
      :property-detail="propertyDetail"
      @toggle-favorite="handleFavoriteToggle"
    />
  </Modal>
</template>

<script setup>
import Modal from '@/components/common/BaseModal.vue';
import PropertyDetailContent from '@/components/property/PropertyDetailContent.vue';

const props = defineProps({
  open: {
    type: Boolean,
    required: true,
  },
  propertyDetail: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['close', 'toggle-favorite']);

const closeModal = () => {
  emit('close');
};

const handleFavoriteToggle = async (propertyId) => {
  await interestStore.toggleFavorite(propertyId);
};
</script>