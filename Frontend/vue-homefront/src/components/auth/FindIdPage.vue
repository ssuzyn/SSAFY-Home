<template>
    <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div class="w-full max-w-lg">
        <div class="text-center mb-8">
          <h1 class="text-5xl font-bold text-orange-500">HOUSE-TOSS</h1>
        </div>
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-2xl font-bold mb-6 text-center">아이디 찾기</h2>
          <form @submit.prevent="handleFindId" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">이름</label>
              <input
                v-model="formData.name"
                type="text"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="이름 입력"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
              <input
                v-model="formData.email"
                type="email"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="이메일 입력"
                required
              />
            </div>
            <button
              type="submit"
              :disabled="isLoading"
              class="w-full py-3 px-4 text-lg bg-orange-500 hover:bg-orange-600 text-white font-medium rounded-md transition-colors disabled:opacity-50"
            >
              {{ isLoading ? '처리 중...' : '아이디 찾기' }}
            </button>
          </form>
          <div v-if="result" class="mt-4 text-center" :class="{ 'text-green-600': result.success, 'text-red-600': !result.success }">
            {{ result.success ? `찾은 아이디: ${result.userId}` : result.error }}
          </div>
          <div class="mt-6 text-center">
            <router-link to="/login" class="text-orange-500 hover:text-orange-600">로그인으로 돌아가기</router-link>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import { useAuth } from '@/stores/auth'
  
  const auth = useAuth()
  const isLoading = ref(false)
  const result = ref(null)
  
  const formData = reactive({
    name: '',
    email: ''
  })
  
  const handleFindId = async () => {
    isLoading.value = true
    result.value = null
  
    try {
      result.value = await auth.findUserId(formData.name, formData.email)
    } catch (error) {
      console.error('Error finding user ID:', error)
      result.value = { success: false, error: '아이디를 찾는 중 오류가 발생했습니다.' }
    } finally {
      isLoading.value = false
    }
  }
  </script>