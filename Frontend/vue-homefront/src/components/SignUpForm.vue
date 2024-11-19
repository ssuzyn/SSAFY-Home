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
              <input
                v-model="formData.user_id"
                type="text"
                maxlength="50"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="아이디 입력"
                required
              />
            </div>
  
            <!-- 비밀번호 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">비밀번호</label>
              <div class="relative">
                <input
                  v-model="formData.user_pwd"
                  :type="showPassword ? 'text' : 'password'"
                  maxlength="255"
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
                type="password"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="비밀번호 재입력"
                required
              />
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
              <input
                v-model="formData.email"
                type="email"
                class="w-full px-4 py-3 text-lg border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-gray-900 placeholder-gray-400"
                placeholder="example@example.com"
                required
              />
            </div>
  
            <!-- 회원가입 버튼 -->
            <button
              type="submit"
              class="w-full py-3 px-4 text-lg bg-orange-500 hover:bg-orange-600 text-white font-medium rounded-md transition-colors"
            >
              회원가입
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
  
  const showPassword = ref(false)
  
  const formData = reactive({
    user_id: '',
    user_pwd: '',
    confirmPassword: '',
    user_name: '',
    email: '',
    agreeTerms: false
  })
  
  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(email)
  }
  
  const handleSignUp = () => {
    if (formData.user_pwd !== formData.confirmPassword) {
      alert('비밀번호가 일치하지 않습니다.')
      return
    }
  
    if (!validateEmail(formData.email)) {
      alert('올바른 이메일 형식이 아닙니다.')
      return
    }
  
    const [email_id, email_domain] = formData.email.split('@')
  
    const signUpData = {
      user_id: formData.user_id,
      user_pwd: formData.user_pwd,
      user_name: formData.user_name,
      email_id,
      email_domain,
      join_date: new Date().toISOString()
    }
  
    console.log('회원가입 시도:', signUpData)
    // 여기에 회원가입 API 호출 로직 구현
  }
  </script>