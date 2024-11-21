<template>
  <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
    <div class="w-full max-w-lg">
      <!-- 로고 -->
      <div class="text-center mb-8">
        <h1 class="text-5xl font-bold text-orange-500">HOUSE-TOSS</h1>
      </div>

      <!-- 로그인 폼 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
            <div class="relative">
              <input
                v-model="formData.userId"
                type="text"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="아이디 입력"
              />
              <button 
                v-if="formData.userId"
                @click="formData.userId = ''"
                class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
              >
                <XCircle class="w-6 h-6" />
              </button>
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
            <div class="relative">
              <input
                v-model="formData.userPwd"
                :type="showPassword ? 'text' : 'password'"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="비밀번호 입력"
              />
              <button 
                @click="showPassword = !showPassword"
                type="button"
                class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
              >
                <component :is="showPassword ? Eye : EyeOff" class="w-6 h-6" />
              </button>
            </div>
          </div>

          <div class="flex items-center">
            <label class="flex items-center">
              <input
                v-model="formData.rememberMe"
                type="checkbox"
                class="rounded border-gray-300 text-orange-500 focus:ring-orange-500"
              />
              <span class="ml-2 text-sm text-gray-600">로그인 상태 유지</span>
            </label>
          </div>

          <button
            type="submit"
            class="w-full py-3 px-4 text-lg bg-orange-500 hover:bg-orange-600 text-white font-medium rounded-md transition-colors"
          >
            로그인
          </button>
        </form>

        <div class="mt-6 flex items-center justify-center space-x-4 text-base text-gray-600">
          <a href="#" class="hover:text-orange-500">비밀번호 찾기</a>
          <span class="text-gray-300">|</span>
          <a href="#" class="hover:text-orange-500">아이디 찾기</a>
          <span class="text-gray-300">|</span>
          <a href="/signup" class="hover:text-orange-500">회원가입</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { XCircle, Eye, EyeOff } from 'lucide-vue-next'
import { useAuth } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuth()
const router = useRouter()

const showPassword = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')

const formData = reactive({
  userId: '',
  userPwd: '',
rememberMe: false
})

const handleLogin = async () => {
isLoading.value = true
errorMessage.value = ''

try {
  const { success, error } = await auth.login(
    formData.userId,
    formData.userPwd,
    formData.rememberMe
  )

  if (success) {
    console.log('로그인 성공')
    router.push('/')  // 로그인 성공 시 홈페이지로 이동
  } else {
    console.error('로그인 실패:', error)
    errorMessage.value = error || '로그인에 실패했습니다. 다시 시도해주세요.'
  }
} catch (error) {
  console.error('로그인 오류:', error)
  errorMessage.value = '로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.'
} finally {
  isLoading.value = false
}
}
</script>