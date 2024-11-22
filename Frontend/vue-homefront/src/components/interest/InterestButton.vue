<template>
  <button
    class="p-2 rounded-full hover:bg-gray-100 transition-colors"
    @click="handleFavoriteClick"
  >
    <StarFilled
      v-if="isInterested || localInterested"
      class="text-yellow-400 w-6 h-6 transform scale-125 transition-all"
    />
    <StarOutlined
      v-else
      class="text-gray-400 w-6 h-6 transform scale-125 transition-all"
    />
  </button>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { StarOutlined, StarFilled } from '@ant-design/icons-vue';
import { useInterestDrawer } from '@/stores/interestDrawer';

const props = defineProps({
  aptSeq: {
    type: String,
    required: true
  }
});

const interestStore = useInterestDrawer();
const localInterested = ref(false);
const isInterested = computed(() => interestStore.isFavorite(props.aptSeq));

const handleFavoriteClick = async () => {
  localInterested.value = !isInterested.value;
  try {
    await interestStore.toggleFavorite(props.aptSeq);
    // 관심 매물 목록 즉시 새로고침
    interestStore.initialize();
  } catch (error) {
    localInterested.value = isInterested.value;
  }
};
</script>

<style scoped>
.anticon {
  font-size: 24px; /* 아이콘 기본 크기 증가 */
}
</style>
