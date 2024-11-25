<template>
  <div class="absolute inset-0 bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="min-h-screen flex flex-col items-center justify-center p-4 pt-16">
      <div class="w-full max-w-2xl">
        <!-- 로고 -->
        <div class="text-center mb-10">
          <h1 class="text-5xl font-bold text-orange-500 tracking-tight">HOUSE-TOSS</h1>
          <p class="mt-2 text-gray-600">안전하고 편리한 주거공간 플랫폼</p>
        </div>

        <!-- 회원가입 폼 -->
        <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl p-8 border border-gray-100">
          <h2 class="text-2xl font-bold text-gray-900 mb-8 text-center">회원가입</h2>
          <form @submit.prevent="handleSignUp" class="space-y-6">
            <!-- 아이디 -->
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">아이디</label>
              <div class="relative">
                <input
                  v-model="formData.user_id"
                  @input="checkIdAvailability"
                  type="text"
                  maxlength="50"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  :class="{'border-red-500': idCheckMessage && !idAvailable, 'border-green-500': idCheckMessage && idAvailable}"
                  placeholder="아이디를 입력해주세요"
                  required
                />
                <div
                  v-if="idCheckMessage"
                  class="absolute right-0 top-full mt-1 text-sm"
                  :class="{'text-green-500': idAvailable, 'text-red-500': !idAvailable}"
                >
                  {{ idCheckMessage }}
                </div>
              </div>
            </div>

            <!-- 비밀번호 그룹 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 비밀번호 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">비밀번호</label>
                <div class="relative">
                  <input
                    v-model="formData.user_pwd"
                    :type="showPassword ? 'text' : 'password'"
                    maxlength="255"
                    @input="checkPasswordMatch"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    placeholder="비밀번호를 입력해주세요"
                    required
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

              <!-- 비밀번호 확인 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">비밀번호 확인</label>
                <div class="relative">
                  <input
                    v-model="formData.confirmPassword"
                    :type="showPassword ? 'text' : 'password'"
                    @input="checkPasswordMatch"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    placeholder="비밀번호를 다시 입력해주세요"
                    required
                  />
                  <div v-if="passwordMismatchMessage" class="absolute right-0 top-full mt-1 text-sm text-red-500">
                    {{ passwordMismatchMessage }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 개인정보 그룹 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 이름 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">이름</label>
                <input
                  v-model="formData.user_name"
                  type="text"
                  maxlength="100"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="이름을 입력해주세요"
                  required
                />
              </div>

              <!-- 나이 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">나이</label>
                <input
                  v-model="formData.age"
                  type="number"
                  min="1"
                  max="150"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="나이를 입력해주세요"
                  required
                />
              </div>
            </div>

            <!-- 이메일 -->
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">이메일</label>
              <div class="flex space-x-2">
                <input
                  v-model="formData.email_id"
                  type="text"
                  class="w-1/2 px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="이메일 아이디"
                  required
                />
                <span class="flex items-center justify-center px-4 text-gray-500">@</span>
                <input
                  v-model="formData.email_domain"
                  type="text"
                  class="w-1/2 px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="도메인"
                  required
                />
              </div>
            </div>

            <!-- 성별 & 전화번호 그룹 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 성별 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">성별</label>
                <div class="flex space-x-4">
                  <label class="flex items-center space-x-2 cursor-pointer group">
                    <input type="radio" v-model="formData.gender" value="M" class="form-radio text-orange-500 focus:ring-orange-500" required>
                    <span class="group-hover:text-orange-500 transition-colors">남성</span>
                  </label>
                  <label class="flex items-center space-x-2 cursor-pointer group">
                    <input type="radio" v-model="formData.gender" value="F" class="form-radio text-orange-500 focus:ring-orange-500" required>
                    <span class="group-hover:text-orange-500 transition-colors">여성</span>
                  </label>
                </div>
              </div>

              <!-- 전화번호 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">전화번호</label>
                <input
                  v-model="formData.phone_number"
                  type="tel"
                  maxlength="15"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  placeholder="010-0000-0000"
                  required
                />
              </div>
            </div>

            <!-- 회원가입 버튼 -->
            <button
              type="submit"
              :disabled="!idAvailable || isSubmitting || formData.user_pwd !== formData.confirmPassword"
              class="w-full py-3.5 px-4 text-lg bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 text-white font-semibold rounded-xl transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden disabled:opacity-50 mt-8"
            >
              <span class="relative z-10">{{ isSubmitting ? '처리중...' : '회원가입' }}</span>
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-50"></div>
              <div class="login-button-shine"></div>
            </button>
          </form>

          <!-- 로그인 링크 -->
          <div class="mt-8 text-center">
            <p class="text-sm text-gray-600">
              이미 계정이 있으신가요?
              <router-link to="/login" class="text-orange-500 hover:text-orange-600 font-semibold transition-colors">로그인</router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Eye, EyeOff } from 'lucide-vue-next'
import { useAxiosStore } from '@/stores/axiosStore'
import { useRouter } from 'vue-router'

const axiosStore = useAxiosStore()
const router = useRouter()
const showPassword = ref(false)
const isSubmitting = ref(false)
const idAvailable = ref(false)
const idCheckMessage = ref('')
const passwordMismatchMessage = ref('')

let idCheckTimeout = null

const formData = reactive({
  user_id: '',
  user_pwd: '',
  confirmPassword: '',
  user_name: '',
  email_id: '',
  email_domain: '',
  gender: '',
  age: '',
  phone_number: ''
})

const checkIdAvailability = async () => {
  if (idCheckTimeout) clearTimeout(idCheckTimeout)

  idAvailable.value = false

  if (formData.user_id.length < 4) {
    idCheckMessage.value = '아이디는 4자 이상이어야 합니다'
    return
  }

  idCheckTimeout = setTimeout(async () => {
    try {
      const response = await axiosStore.get(`/user/check-id/${formData.user_id}`)
      idAvailable.value = response.data.available
      idCheckMessage.value = response.data.message
    } catch (error) {
      console.error('아이디 중복 확인 중 오류 발생:', error)
      idCheckMessage.value = '아이디 확인 중 오류가 발생했습니다'
      idAvailable.value = false
    }
  }, 500)
}

const checkPasswordMatch = () => {
  if (formData.user_pwd === formData.confirmPassword) {
    passwordMismatchMessage.value = ''
  } else {
    passwordMismatchMessage.value = '비밀번호가 일치하지 않습니다.'
  }
}

const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

const handleSignUp = async () => {
  if (passwordMismatchMessage.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  if (!validateEmail(`${formData.email_id}@${formData.email_domain}`)) {
    alert('올바른 이메일 형식이 아닙니다.')
    return
  }

  const signUpData = {
    userId: formData.user_id,
    userPwd: formData.user_pwd,
    userName: formData.user_name,
    emailId: formData.email_id,
    emailDomain: formData.email_domain,
    gender: formData.gender,
    age: parseInt(formData.age),
    phoneNumber: formData.phone_number
  }

  isSubmitting.value = true

  try {
    await axiosStore.post('/user/register', signUpData)
    alert('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (error) {
    console.error('회원가입 실패:', error)
    alert('회원가입에 실패했습니다. 다시 시도해주세요.')
  } finally {
    isSubmitting.value = false
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

