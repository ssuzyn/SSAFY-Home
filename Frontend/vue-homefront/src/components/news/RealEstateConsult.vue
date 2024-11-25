<template>
  <div class="p-4">
    <a-row :gutter="[16, 16]">
      <a-col :span="24">
        <a-card title="부동산 상담">
          <a-input-search
            v-model:value="question"
            placeholder="부동산 관련 질문을 입력하세요"
            enter-button="질문하기"
            size="large"
            @search="askGPT"
          />
          <div v-if="gptAnswer" class="mt-4 p-4 bg-gray-50 rounded-lg">
            <p class="whitespace-pre-line">{{ gptAnswer }}</p>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { message } from 'ant-design-vue'

const question = ref('')
const gptAnswer = ref('')

const getToken = () => localStorage.getItem('token')

const askGPT = async () => {
  if (!question.value) {
    message.warning('질문을 입력해주세요')
    return
  }

  try {
    const response = await fetch(`http://localhost:8080/gpt/ask?question=${encodeURIComponent(question.value)}`, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    })
    const data = await response.json()
    if (data.success) {
      gptAnswer.value = data.data
    }
  } catch (error) {
    message.error('질문 처리 중 오류가 발생했습니다.')
  }
}
</script>
