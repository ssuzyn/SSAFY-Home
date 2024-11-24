<template>
  <div class="image-container" :class="containerClass">
    <!-- 로딩 중 표시 -->
    <div v-if="loading" class="loading-placeholder">
      <div class="animate-pulse bg-gray-200 w-full h-full rounded-full"></div>
    </div>

    <!-- 에러 시 표시할 기본 이미지 또는 실제 이미지 -->
    <img
      :src="imageUrl"
      :alt="alt"
      class="actual-image"
      :class="[imageClass, { 'hidden': loading }]"
      loading="lazy"
      @load="handleLoad"
      @error="handleError"
    />
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import { useAxiosStore } from '@/stores/axiosStore'
import defaultProfile from '@/assets/images/default_profile.png'

const axiosStore = useAxiosStore()

const props = defineProps({
  path: {
    type: String,
    required: true
  },
  alt: {
    type: String,
    default: '프로필 이미지'
  },
  containerClass: {
    type: String,
    default: ''
  },
  imageClass: {
    type: String,
    default: ''
  }
})

const loading = ref(true)
const error = ref(false)
const imageBlob = ref(null)

const imageUrl = computed(() => {
  if (!props.path || error.value) return defaultProfile
  if (imageBlob.value) return URL.createObjectURL(imageBlob.value)
  return defaultProfile
})

const loadImage = async () => {
  if (!props.path) {
    loading.value = false
    error.value = true
    return
  }

  try {
    const response = await axiosStore.get(`/image/${props.path}`, {
      responseType: 'blob'
    })
    imageBlob.value = response.data
    loading.value = false
  } catch (err) {
    console.error('이미지 로드 실패:', err)
    error.value = true
    loading.value = false
  }
}

const handleLoad = () => {
  loading.value = false
}

const handleError = () => {
  loading.value = false
  error.value = true
}

// 컴포넌트가 마운트되면 이미지 로드 시작
loadImage()

// 컴포넌트가 언마운트될 때 Blob URL 정리
onBeforeUnmount(() => {
  if (imageBlob.value) {
    URL.revokeObjectURL(imageUrl.value)
  }
})
</script>

<style scoped>
.image-container {
  position: relative;
  overflow: hidden;
}

.loading-placeholder,
.actual-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.loading-placeholder {
  position: absolute;
  top: 0;
  left: 0;
}

.hidden {
  display: none;
}
</style>
