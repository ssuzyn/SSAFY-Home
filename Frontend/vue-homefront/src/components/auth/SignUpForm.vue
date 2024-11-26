<template>
  <div class="absolute inset-0 bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="min-h-screen flex flex-col items-center justify-center p-4 pt-16">
      <div class="w-full max-w-2xl">
        <!-- 로고 -->
        <div class="text-center mb-10">
          <h1 class="text-5xl font-bold text-orange-500 tracking-tight">ZIP</h1>
          <p class="mt-2 text-gray-600">쉽고 똑똑한 부동산 정보 제공 플랫폼</p>
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
                  type="text"
                  class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                  :class="{
                    'border-red-500': validationMessages.userId && !validationMessages.userId.includes('✓'),
                    'border-green-500': validationMessages.userId?.includes('✓')
                  }"
                  placeholder="아이디를 입력해주세요"
                  required
                />
                <div
                  v-if="validationMessages.userId"
                  class="absolute right-0 top-full mt-1 text-sm"
                  :class="{
                    'text-red-500': !validationMessages.userId.includes('✓'),
                    'text-green-500': validationMessages.userId.includes('✓')
                  }"
                >
                  {{ validationMessages.userId }}
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
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    :class="{
                      'border-red-500': validationMessages.passwordMatch && !validationMessages.passwordMatch.includes('✓'),
                      'border-green-500': validationMessages.passwordMatch?.includes('✓')
                    }"
                    placeholder="비밀번호를 다시 입력해주세요"
                    required
                  />
                  <div
                    v-if="validationMessages.passwordMatch"
                    class="absolute right-0 top-full mt-1 text-sm"
                    :class="{
                      'text-red-500': !validationMessages.passwordMatch.includes('✓'),
                      'text-green-500': validationMessages.passwordMatch.includes('✓')
                    }"
                  >
                    {{ validationMessages.passwordMatch }}
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
                <div class="relative">
                  <input
                    v-model="formData.age"
                    type="number"
                    min="1"
                    max="150"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    :class="{
                      'border-red-500': validationMessages.age && !validationMessages.age.includes('✓'),
                      'border-green-500': validationMessages.age?.includes('✓')
                    }"
                    placeholder="나이를 입력해주세요"
                    required
                  />
                  <div
                    v-if="validationMessages.age"
                    class="absolute right-0 top-full mt-1 text-sm"
                    :class="{
                      'text-red-500': !validationMessages.age.includes('✓'),
                      'text-green-500': validationMessages.age.includes('✓')
                    }"
                  >
                    {{ validationMessages.age }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 이메일 -->
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">이메일</label>
              <div class="relative flex space-x-2">
                <input
                  v-model="formData.email_id"
                  type="text"
                  class="w-1/2 px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                  :class="{
                    'border-red-500': validationMessages.email && !validationMessages.email.includes('✓'),
                    'border-green-500': validationMessages.email?.includes('✓')
                  }"
                  placeholder="이메일 아이디"
                  required
                />
                <span class="flex items-center justify-center px-4 text-gray-500">@</span>
                <div class="w-1/2 relative">
                  <select
                    v-if="formData.email_domain !== 'custom'"
                    v-model="formData.email_domain"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70 appearance-none"
                    :class="{
                      'border-red-500': validationMessages.email && !validationMessages.email.includes('✓'),
                      'border-green-500': validationMessages.email?.includes('✓')
                    }"
                    required
                  >
                    <option value="">도메인 선택</option>
                    <option v-for="domain in emailDomains" :key="domain" :value="domain">
                      {{ domain === 'custom' ? '직접 입력' : domain }}
                    </option>
                  </select>
                  <input
                    v-if="formData.email_domain === 'custom'"
                    v-model="formData.email_domain"
                    type="text"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    placeholder="도메인 입력"
                    required
                  />
                  <div class="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
                    <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </div>
                <div
                  v-if="validationMessages.email"
                  class="absolute right-0 top-full mt-1 text-sm"
                  :class="{
                    'text-red-500': !validationMessages.email.includes('✓'),
                    'text-green-500': validationMessages.email.includes('✓')
                  }"
                >
                  {{ validationMessages.email }}
                </div>
              </div>
            </div>

            <!-- 성별 & 전화번호 그룹 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 성별 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">성별</label>
                <div class="flex space-x-4">
                  <label class="flex-1">
                    <input
                      type="radio"
                      v-model="formData.gender"
                      value="M"
                      class="hidden"
                      required
                    >
                    <div
                      class="flex items-center justify-center p-3 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:bg-green-50"
                      :class="formData.gender === 'M' ? 'border-green-500 bg-green-50' : 'border-gray-200'"
                    >
                      <div class="flex flex-col items-center space-y-1">
                        <span class="text-lg font-medium" :class="formData.gender === 'M' ? 'text-green-500' : 'text-gray-600'">
                          👨 남성
                        </span>
                      </div>
                    </div>
                  </label>
                  <label class="flex-1">
                    <input
                      type="radio"
                      v-model="formData.gender"
                      value="F"
                      class="hidden"
                      required
                    >
                    <div
                      class="flex items-center justify-center p-3 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:bg-green-50"
                      :class="formData.gender === 'F' ? 'border-green-500 bg-green-50' : 'border-gray-200'"
                    >
                      <div class="flex flex-col items-center space-y-1">
                        <span class="text-lg font-medium" :class="formData.gender === 'F' ? 'text-green-500' : 'text-gray-600'">
                          👩 여성
                        </span>
                      </div>
                    </div>
                  </label>
                </div>
              </div>

              <!-- 전화번호 -->
              <div>
                <label class="block text-sm font-semibold text-gray-700 mb-2">전화번호</label>
                <div class="relative">
                  <input
                    v-model="formData.phone_number"
                    type="tel"
                    maxlength="13"
                    class="w-full px-4 py-3.5 text-lg border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400 transition-all duration-200 bg-white/70"
                    :class="{
                      'border-red-500': validationMessages.phone && !validationMessages.phone.includes('✓'),
                      'border-green-500': validationMessages.phone?.includes('✓')
                    }"
                    placeholder="010-0000-0000"
                    required
                  />
                  <div
                    v-if="validationMessages.phone"
                    class="absolute right-0 top-full mt-1 text-sm"
                    :class="{
                      'text-red-500': !validationMessages.phone.includes('✓'),
                      'text-green-500': validationMessages.phone.includes('✓')
                    }"
                  >
                    {{ validationMessages.phone }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 회원가입 버튼 -->
            <button
              type="submit"
              :disabled="!isFormValid || isSubmitting"
              class="w-full py-3.5 px-4 text-lg bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white font-semibold rounded-xl transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100 mt-8"
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
import { ref, reactive, watch, computed } from 'vue'
import { Eye, EyeOff } from 'lucide-vue-next'
import { message } from 'ant-design-vue'
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

const validationMessages = reactive({
  userId: '',
  passwordMatch: '',
  email: '',
  age: '',
  phone: ''
})

const emailDomains = [
  'naver.com',
  'gmail.com',
  'daum.net',
  'hanmail.net',
  'nate.com',
  'kakao.com',
  'custom'
]

// 필드 라벨 정의
const fieldLabels = {
  userId: '아이디',
  password: '비밀번호',
  passwordMatch: '비밀번호 확인',
  age: '나이',
  phone: '전화번호',
  email: '이메일'
}

// 필드명과 formData 속성 매핑
const fieldNames = {
  userId: 'user_id',
  password: 'user_pwd',
  passwordMatch: 'confirmPassword',
  age: 'age',
  phone: 'phone_number',
  email: 'email_id'
}

// placeholder 텍스트 정의
const placeholders = {
  userId: '아이디를 입력해주세요',
  password: '비밀번호를 입력해주세요',
  passwordMatch: '비밀번호를 다시 입력해주세요',
  age: '나이를 입력해주세요',
  phone: '010-0000-0000',
  email: '이메일 주소'
}

const checkIdAvailability = async () => {
  if (idCheckTimeout) clearTimeout(idCheckTimeout)
  idAvailable.value = false

  if (!formData.user_id) {
    validationMessages.userId = ''
    return
  }

  if (formData.user_id.length < 4) {
    validationMessages.userId = '아이디는 4자 이상이어야 합니다'
    return
  }

  idCheckTimeout = setTimeout(async () => {
    try {
      const response = await axiosStore.get(`/user/check-id/${formData.user_id}`)
      idAvailable.value = response.data.available
      validationMessages.userId = response.data.available ?
        '✓ 사용 가능한 아이디입니다' :
        '이미 사용 중인 아이디입니다'
    } catch (error) {
      console.error('아이디 중복 확인 중 오류 발생:', error)
      validationMessages.userId = '아이디 확인 중 오류가 발생했습니다'
    }
  }, 500)
}

const checkPasswordMatch = () => {
  if (!formData.user_pwd || !formData.confirmPassword) {
    validationMessages.passwordMatch = ''
  } else if (formData.user_pwd === formData.confirmPassword) {
    validationMessages.passwordMatch = '✓ 비밀번호가 일치합니다'
  } else {
    validationMessages.passwordMatch = '비밀번호가 일치하지 않습니다'
  }
}

const checkAge = () => {
  const age = Number(formData.age)
  if (!formData.age) {
    validationMessages.age = ''
  } else if (isNaN(age) || age < 1 || age > 150) {
    validationMessages.age = '올바른 나이를 입력해주세요'
  } else {
    validationMessages.age = '✓ 올바른 나이입니다'
  }
}

const checkPhoneNumber = () => {
  const phoneRegex = /^010-\d{4}-\d{4}$/
  if (!formData.phone_number) {
    validationMessages.phone = ''
  } else if (!phoneRegex.test(formData.phone_number)) {
    validationMessages.phone = '010-0000-0000 형식으로 입력해주세요'
  } else {
    validationMessages.phone = '✓ 올바른 전화번호입니다'
  }
}

const checkEmail = () => {
  if (!formData.email_id || !formData.email_domain) {
    validationMessages.email = ''
  } else {
    validationMessages.email = '✓ 올바른 이메일입니다'
  }
}

watch(() => formData.user_id, checkIdAvailability)
watch([() => formData.user_pwd, () => formData.confirmPassword], checkPasswordMatch)
watch(() => formData.age, checkAge)
watch(() => formData.phone_number, checkPhoneNumber)
watch([() => formData.email_id, () => formData.email_domain], checkEmail)

watch(() => formData.phone_number, (newValue) => {
  if (!newValue) return
  formData.phone_number = newValue.replace(/[^0-9]/g, '')
    .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
    .replace(/\-{1,2}$/g, "")
})

const isFormValid = computed(() => {
  return (
    formData.user_id &&
    idAvailable.value &&
    formData.user_pwd &&
    formData.confirmPassword &&
    formData.user_pwd === formData.confirmPassword &&
    formData.user_name &&
    formData.age &&
    formData.email_id &&
    formData.email_domain &&
    formData.gender &&
    formData.phone_number &&
    validationMessages.phone?.includes('✓')
  )
})

const handleSignUp = async () => {
  if (!isFormValid.value) {
    message.warning('모든 필드를 올바르게 입력해주세요.')
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
    message.success('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (error) {
    console.error('회원가입 실패:', error)
    message.error('회원가입에 실패했습니다. 다시 시도해주세요.')
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

