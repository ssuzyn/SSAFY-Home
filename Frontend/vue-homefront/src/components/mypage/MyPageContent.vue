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
                  <div v-else-if="user.profileSavePath" class="h-16 w-16 rounded-full bg-gray-200 flex items-center justify-center">
                    <img :src="user.profileSavePath" class="h-16 w-16 object-cover rounded-full" alt="프로필 사진" />
                  </div>
                  <div v-else class="h-16 w-16 rounded-full bg-gray-200 flex items-center justify-center">
                    <span class="text-gray-500 text-2xl">{{ user.userName?.charAt(0) }}</span>
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
                    v-model="user.userName"
                    type="text"
                    class="w-full px-3 text-black py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
                  <div class="flex">
                    <input
                      v-model="user.emailId"
                      type="text"
                      class="w-1/2 px-3 py-2 border text-black border-gray-300 rounded-l-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                    <span class="px-3 py-2 bg-gray-100 border text-black border-gray-300">@</span>
                    <input
                      v-model="user.emailDomain"
                      type="text"
                      class="w-1/2 px-3 py-2 border text-black border-gray-300 rounded-r-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                    />
                  </div>
                </div>
                <div>
                  <label class="block text-sm font-medium  text-gray-700 mb-1">비밀번호</label>
                  <input
                    v-model="user.userPwd"
                    type="password"
                    class="w-full px-3 py-2 border border-gray-300 text-black rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
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
import { ref, reactive, onMounted } from 'vue'
import { useAuth } from '@/stores/auth'

const auth = useAuth()

const user = reactive({
  userName: '',
  emailId: '',
  emailDomain: '',
  userPwd: '',
  profileSavePath: ''
})

const notifications = reactive({
  email: true,
  sms: false
})

const imagePreview = ref(null)
const selectedFile = ref(null)

onMounted(async () => {
  await auth.fetchUserInfo()
  Object.assign(user, auth.user)
})

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

const saveChanges = async () => {
  try {
    // 사용자 정보 업데이트
    const updateData = {
      userName: user.userName,
      emailId: user.emailId,
      emailDomain: user.emailDomain,
      userPwd: user.userPwd
    }
    const { success, error } = await auth.updateUserInfo(updateData)
    if (!success) {
      throw new Error(error)
    }

    // 프로필 이미지 업로드
    if (selectedFile.value) {
      const { success: uploadSuccess, error: uploadError } = await auth.uploadProfileImage(selectedFile.value)
      if (!uploadSuccess) {
        throw new Error(uploadError)
      }
    }

    // 알림 설정은 백엔드 API가 없으므로 로컬에서만 처리
    console.log('알림 설정:', notifications)

    alert('변경사항이 성공적으로 저장되었습니다.')
  } catch (error) {
    console.error('변경사항 저장 중 오류 발생:', error)
    alert('변경사항 저장 중 오류가 발생했습니다: ' + error.message)
  }
}
</script>