<template>
    <div class="min-h-screen bg-gray-50 pt-16">
      <div class="max-w-7xl mx-auto px-4 py-8">
        <div class="bg-white rounded-lg shadow">
          <div class="p-6">
            <h2 class="text-2xl font-bold text-gray-900 mb-6">마이페이지</h2>
            
            <div class="space-y-6">
              <!-- 프로필 사진 섹션 -->
              <div class="border-b pb-6">
                <h3 class="text-lg font-medium text-gray-900 mb-4">프로필 사진</h3>
                <div class="flex items-center space-x-6">
                  <div class="shrink-0">
                    <img v-if="imagePreview" :src="imagePreview" class="h-16 w-16 object-cover rounded-full" alt="프로필 사진" />
                    <div v-else class="h-16 w-16 rounded-full bg-gray-200 flex items-center justify-center">
                      <span class="text-gray-500 text-2xl">{{ user.name.charAt(0) }}</span>
                    </div>
                  </div>
                  <label class="block">
                    <span class="sr-only">프로필 사진 선택</span>
                    <input type="file" @change="onFileChange" accept="image/*" class="block w-full text-sm text-gray-500
                      file:mr-4 file:py-2 file:px-4
                      file:rounded-full file:border-0
                      file:text-sm file:font-semibold
                      file:bg-orange-50 file:text-orange-700
                      hover:file:bg-orange-100
                    "/>
                  </label>
                </div>
              </div>
  
              <!-- 프로필 섹션 -->
              <div class="border-b pb-6">
                <h3 class="text-lg font-medium text-gray-900 mb-4">프로필 정보</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">이름</label>
                    <input
                      v-model="user.name"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
                    <input
                      v-model="user.email"
                      type="email"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">전화번호</label>
                    <input
                      v-model="user.phone"
                      type="tel"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">생년월일</label>
                    <input
                      v-model="user.birthdate"
                      type="date"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                  </div>
                </div>
              </div>
  
              <!-- 알림 설정 -->
              <div class="border-b pb-6">
                <h3 class="text-lg font-medium text-gray-900 mb-4">알림 설정</h3>
                <div class="space-y-4">
                  <label class="flex items-center">
                    <input
                      v-model="notifications.email"
                      type="checkbox"
                      class="rounded border-gray-300 text-orange-500 focus:ring-orange-500"
                    />
                    <span class="ml-2 text-sm text-gray-600">이메일 알림 받기</span>
                  </label>
                  <label class="flex items-center">
                    <input
                      v-model="notifications.sms"
                      type="checkbox"
                      class="rounded border-gray-300 text-orange-500 focus:ring-orange-500"
                    />
                    <span class="ml-2 text-sm text-gray-600">SMS 알림 받기</span>
                  </label>
                </div>
              </div>
  
              <!-- 저장 버튼 -->
              <div class="flex justify-end">
                <button
                  @click="saveChanges"
                  class="px-6 py-2 bg-orange-500 text-white font-medium rounded-md hover:bg-orange-600 transition-colors"
                >
                  변경사항 저장
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  
  const user = reactive({
    name: '홍길동',
    email: 'hong@example.com',
    phone: '010-1234-5678',
    birthdate: '1990-01-01'
  })
  
  const notifications = reactive({
    email: true,
    sms: false
  })
  
  const imagePreview = ref(null)
  const selectedFile = ref(null)
  
  const onFileChange = (e) => {
    const file = e.target.files[0]
    selectedFile.value = file
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        imagePreview.value = e.target.result
      }
      reader.readAsDataURL(file)
    }
  }
  
  const saveChanges = () => {
    console.log('변경사항 저장:', { user, notifications, profilePicture: selectedFile.value })
    // 여기에 저장 로직 구현
    // 예: API 호출을 통한 서버 저장
    // 프로필 사진 업로드를 위해 FormData를 사용할 수 있습니다
    /*
    const formData = new FormData()
    formData.append('name', user.name)
    formData.append('email', user.email)
    formData.append('phone', user.phone)
    formData.append('birthdate', user.birthdate)
    formData.append('notifications', JSON.stringify(notifications))
    if (selectedFile.value) {
      formData.append('profilePicture', selectedFile.value)
    }
    // API 호출 예시
    // await axios.post('/api/update-profile', formData)
    */
  }
  </script>