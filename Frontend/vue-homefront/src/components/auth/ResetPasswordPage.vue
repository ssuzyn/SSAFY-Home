<template>
  <div class="absolute inset-0 bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="min-h-screen flex flex-col items-center justify-center p-4">
      <div class="w-full max-w-lg">
        <div class="text-center mb-10">
          <h1 class="text-5xl font-bold text-orange-500 tracking-tight">ZIP</h1>
          <p class="mt-2 text-gray-600">안전하고 편리한 주거공간 플랫폼</p>
        </div>
        <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl p-8 border border-gray-100">
          <h2 class="text-2xl font-bold mb-6 text-center">비밀번호 재설정</h2>
          <form @submit.prevent="handleResetPassword" class="space-y-6">
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">이름</label>
              <div class="relative">
                <input
                  v-model="formData.name"
                  type="text"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="이름을 입력해주세요"
                  required
                />
                <button
                  v-if="formData.name"
                  @click="formData.name = ''"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                  type="button"
                >
                  <XCircle class="w-5 h-5" />
                </button>
              </div>
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">아이디</label>
              <div class="relative">
                <input
                  v-model="formData.userId"
                  type="text"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="아이디를 입력해주세요"
                  required
                />
                <button
                  v-if="formData.userId"
                  @click="formData.userId = ''"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                  type="button"
                >
                  <XCircle class="w-5 h-5" />
                </button>
              </div>
            </div>
            <div v-if="result" class="text-center" :class="{ 'text-green-600': result.success, 'text-red-600': !result.success }">
              {{ result.success ? result.message : result.error }}
            </div>
            <button
              type="submit"
              :disabled="isLoading"
              class="w-full py-3.5 px-4 text-lg bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 text-white font-semibold rounded-xl transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden disabled:opacity-50"
            >
              <span class="relative z-10">{{ isLoading ? '처리 중...' : '비밀번호 재설정' }}</span>
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-50"></div>
              <div class="login-button-shine"></div>
            </button>
          </form>
          <div class="mt-8 flex items-center justify-center space-x-6 text-sm text-gray-600">
            <router-link to="/login" class="hover:text-orange-500 transition-colors">로그인으로 돌아가기</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useAuth } from '@/stores/auth'
import { XCircle } from 'lucide-vue-next'

const auth = useAuth()
const isLoading = ref(false)
const result = ref(null)

const formData = reactive({
  name: '',
  userId: ''
})

const handleResetPassword = async () => {
  isLoading.value = true
  result.value = null

  try {
    result.value = await auth.resetPassword(formData.name, formData.userId)
  } catch (error) {
    console.error('Error resetting password:', error)
    result.value = { success: false, error: '비밀번호 재설정 중 오류가 발생했습니다.' }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.login-button-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    120deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  animation: shine 2s infinite;
}

@keyframes shine {
  0% { left: -100%; }
  100% { left: 100%; }
}
</style>
