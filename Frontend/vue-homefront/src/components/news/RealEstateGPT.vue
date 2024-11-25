<template>
  <div class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl w-full max-w-2xl">
      <!-- 헤더 -->
      <div class="p-6 border-b flex items-center justify-between">
        <h2 class="text-xl font-bold flex items-center gap-2">
          <span>🤖</span>
          부동산 용어 도우미
        </h2>
        <button
          @click="$emit('close')"
          class="text-gray-500 hover:text-gray-700">
          <CloseOutlined class="text-xl" />
        </button>
      </div>

      <!-- 메인 컨텐츠 -->
      <div class="p-6 space-y-6">
        <!-- 작성 팁 -->
        <div class="bg-[#f7f7f8] rounded-xl p-4 border border-[#00A67E]/20">
          <h4 class="text-lg font-semibold text-[#00A67E] flex items-center gap-2 mb-4">
            <span class="text-2xl">💡</span>
            뉴스에서 본 생소한 용어를 물어보세요
          </h4>
          <ul class="text-sm text-gray-600 space-y-2 ml-4 list-disc">
            <li>"총부채원리금상환비율(DSR)이 무엇인가요?"</li>
            <li>"기준금리 인상이 부동산 시장에 미치는 영향은?"</li>
            <li>"무순위 청약이란 무엇인가요?"</li>
          </ul>
        </div>

        <!-- 입력창 -->
        <AInputSearch
          v-model:value="question"
          placeholder="부동산 뉴스에서 궁금한 용어나 개념을 입력해주세요"
          enter-button="질문하기"
          size="large"
          :loading="loading"
          @search="askGPT"
          class="!rounded-xl"
        />

        <!-- GPT 답변 -->
        <div v-if="gptAnswer" class="mt-6 p-4 bg-[#f7f7f8] rounded-xl border border-[#00A67E]/20">
          <div class="flex items-start gap-3">
            <span class="text-2xl">🤖</span>
            <p class="whitespace-pre-line text-gray-700">{{ gptAnswer }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { message, Input } from 'ant-design-vue'
import { CloseOutlined } from '@ant-design/icons-vue'
import { useAxiosStore } from '@/stores/axiosStore'

const { Search: AInputSearch } = Input

const axiosStore = useAxiosStore()
const question = ref('')
const gptAnswer = ref('')
const loading = ref(false)

const askGPT = async () => {
  if (!question.value) {
    message.warning('질문을 입력해주세요')
    return
  }

  loading.value = true
  try {
    const response = await axiosStore.get(`/gpt/ask?question=${encodeURIComponent(question.value)}`)
    if (response.data.success) {
      gptAnswer.value = response.data.data
    }
  } catch (error) {
    message.error('질문 처리 중 오류가 발생했습니다.')
    console.error('GPT 요청 오류:', error)
  } finally {
    loading.value = false
  }
}

defineEmits(['close'])
</script>

<style scoped>
:deep(.ant-input-search-button) {
  background-color: #00A67E !important;
  border-color: #00A67E !important;
}

:deep(.ant-input-search-button:hover) {
  background-color: #008F6B !important;
  border-color: #008F6B !important;
}

:deep(.ant-input) {
  border-color: #E5E7EB !important;
}

:deep(.ant-input:focus) {
  border-color: #00A67E !important;
  box-shadow: 0 0 0 2px rgba(0, 166, 126, 0.2) !important;
}
</style>
