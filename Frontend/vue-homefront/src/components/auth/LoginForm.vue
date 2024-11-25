<template>
  <div class="absolute inset-0 bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="min-h-screen flex flex-col items-center justify-center p-4">
      <div class="w-full max-w-lg">
        <!-- 로고 -->
        <div class="text-center mb-10">
          <h1 class="text-5xl font-bold text-orange-500 tracking-tight">ZIP</h1>
          <p class="mt-2 text-gray-600">안전하고 편리한 주거공간 플랫폼</p>
        </div>

        <!-- 로그인 폼 -->
        <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl p-8 border border-gray-100">
          <form @submit.prevent="handleLogin" class="space-y-6">
            <!-- 아이디 입력 -->
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">아이디</label>
              <div class="relative">
                <input
                  v-model="formData.userId"
                  type="text"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="아이디를 입력해주세요"
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

            <!-- 비밀번호 입력 -->
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">비밀번호</label>
              <div class="relative">
                <input
                  v-model="formData.userPwd"
                  :type="showPassword ? 'text' : 'password'"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="비밀번호를 입력해주세요"
                />
                <button
                  @click="showPassword = !showPassword"
                  type="button"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                >
                  <component :is="showPassword ? Eye : EyeOff" class="w-5 h-5" />
                </button>
              </div>
            </div>

            <!-- 로그인 상태 유지 체크박스 -->
            <div class="flex items-center">
              <label class="flex items-center group cursor-pointer">
                <input
                  v-model="formData.rememberUserId"
                  type="checkbox"
                  class="rounded-md border-gray-300 text-orange-500 focus:ring-orange-500 transition-colors"
                />
                <span class="ml-2 text-sm text-gray-600 group-hover:text-gray-900 transition-colors">
                  아이디 기억하기
                </span>
              </label>
            </div>

            <!-- 에러 메시지 -->
            <div v-if="errorMessage" class="text-red-500 text-sm mt-2">
              {{ errorMessage }}
            </div>

            <!-- 로그인 버튼 -->
            <button
              type="submit"
              :disabled="isLoading"
              class="w-full py-3.5 px-4 text-lg bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 text-white font-semibold rounded-xl transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden disabled:opacity-50"
            >
              <span class="relative z-10">{{ isLoading ? '로그인 중...' : '로그인' }}</span>
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-50"></div>
              <div class="login-button-shine"></div>
            </button>
          </form>

          <!-- 링크 네비게이션 -->
          <div class="mt-8 flex items-center justify-center space-x-6 text-sm text-gray-600">
            <router-link
              to="/resetPwd"
              class="hover:text-orange-500 transition-colors"
            >
              비밀번호 찾기
            </router-link>
            <span class="text-gray-300">|</span>
            <router-link
              to="/findId"
              class="hover:text-orange-500 transition-colors"
            >
              아이디 찾기
            </router-link>
            <span class="text-gray-300">|</span>
            <router-link
              to="/signup"
              class="hover:text-orange-500 transition-colors"
            >
              회원가입
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { XCircle, Eye, EyeOff } from 'lucide-vue-next'
import { useAuth } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const auth = useAuth()
const router = useRouter()

const showPassword = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')

const formData = reactive({
  userId: '',
  userPwd: '',
  rememberUserId: false
})

onMounted(() => {
  formData.userId = auth.rememberedUserId
  formData.rememberUserId = !!auth.rememberedUserId
})

const handleLogin = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const { success, error } = await auth.login(
      formData.userId,
      formData.userPwd,
      formData.rememberUserId
    )

    if (success) {
      console.log('로그인 성공')
      router.push('/')
      message.success("환영합니다!")
    } else {
      console.error('로그인 실패:', error)
      errorMessage.value = error || '로그인에 실패했습니다. 다시 시도해주세요.'
      message.error(error || "로그인에 실패했습니다. 아이디나 비밀번호를 확인해주세요.")
    }
  } catch (error) {
    console.error('로그인 오류:', error)
    errorMessage.value = '로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.'
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
