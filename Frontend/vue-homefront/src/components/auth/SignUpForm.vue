<template>
  <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4 pt-16">
    <div class="w-full max-w-lg">
      <!-- 로고 -->
      <div class="text-center mb-8">
        <h1 class="text-5xl font-bold text-orange-500">HOUSE-TOSS</h1>
      </div>

      <!-- 회원가입 폼 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold text-gray-900 mb-6">회원가입</h2>
        <form @submit.prevent="handleSignUp" class="space-y-4">
          <!-- 아이디 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
            <div class="relative">
              <input
                v-model="formData.user_id"
                @input="checkIdAvailability"
                type="text"
                maxlength="50"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                :class="{'border-red-500': idCheckMessage && !idAvailable, 'border-green-500': idCheckMessage && idAvailable}"
                placeholder="아이디 입력"
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

          <!-- 비밀번호 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
            <div class="relative">
              <input
                v-model="formData.user_pwd"
                :type="showPassword ? 'text' : 'password'"
                maxlength="255"
                @input="checkPasswordMatch"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="비밀번호 입력"
                required
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

          <!-- 비밀번호 확인 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호 확인</label>
            <input
              v-model="formData.confirmPassword"
              :type="showPassword ? 'text' : 'password'"
              @input="checkPasswordMatch"
              class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
              placeholder="비밀번호 재입력"
              required
            />
            <div v-if="passwordMismatchMessage" class="text-red-500 text-sm mt-1">
              {{ passwordMismatchMessage }}
            </div>
          </div>

          <!-- 이름 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">이름</label>
            <input
              v-model="formData.user_name"
              type="text"
              maxlength="100"
              class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
              placeholder="이름 입력"
              required
            />
          </div>

          <!-- 이메일 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
            <div class="flex">
              <input
                v-model="formData.email_id"
                type="text"
                class="w-1/2 px-4 py-3 text-lg border border-gray-300 rounded-l-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="이메일 아이디"
                required
              />
              <span class="flex items-center justify-center px-4 bg-gray-100 border-t border-b border-gray-300">@</span>
              <input
                v-model="formData.email_domain"
                type="text"
                class="w-1/2 px-4 py-3 text-lg border border-gray-300 rounded-r-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="도메인"
                required
              />
            </div>
          </div>

          <!-- 성별 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">성별</label>
            <div class="flex space-x-4">
              <label class="inline-flex items-center">
                <input type="radio" v-model="formData.gender" value="M" class="form-radio text-orange-500" required>
                <span class="ml-2">남성</span>
              </label>
              <label class="inline-flex items-center">
                <input type="radio" v-model="formData.gender" value="F" class="form-radio text-orange-500" required>
                <span class="ml-2">여성</span>
              </label>
            </div>
          </div>

          <!-- 나이 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">나이</label>
            <input
              v-model="formData.age"
              type="number"
              min="1"
              max="150"
              class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
              placeholder="나이 입력"
              required
            />
          </div>

          <!-- 전화번호 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">전화번호</label>
            <input
              v-model="formData.phone_number"
              type="tel"
              maxlength="15"
              class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
              placeholder="전화번호 입력 (예: 010-1234-5678)"
              required
            />
          </div>

          <!-- 회원가입 버튼 -->
          <button
            type="submit"
            :disabled="!idAvailable || isSubmitting || formData.user_pwd !== formData.confirmPassword"
            class="w-full py-3 px-4 text-lg bg-orange-500 hover:bg-orange-600 text-white font-medium rounded-md transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed mt-6"
          >
            {{ isSubmitting ? '처리중...' : '회원가입' }}
          </button>
        </form>

        <!-- 로그인 링크 -->
        <div class="mt-6 text-center">
          <p class="text-sm text-gray-600">
            이미 계정이 있으신가요?
            <router-link to="/login" class="text-orange-500 hover:text-orange-600">로그인</router-link>
          </p>
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

