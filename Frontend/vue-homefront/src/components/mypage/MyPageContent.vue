<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-6">
    <div class="w-full max-w-2xl bg-white rounded-2xl shadow-lg overflow-hidden border-2 border-gray-200">
      <header class="bg-orange-200 text-gray-800 text-center py-6">
        <h1 class="text-3xl font-bold">마이페이지</h1>
      </header>
      <div class="p-8">
        <!-- 프로필 사진 섹션 -->
        <section class="mb-8">
          <h2 class="text-xl font-semibold mb-4 text-gray-700 flex items-center">
            <span class="mr-2">📸</span> 프로필 사진
          </h2>
          <div class="flex items-center space-x-4">
            <div class="w-24 h-24 rounded-full border-2 border-gray-300 overflow-hidden">
              <img v-if="imagePreview" :src="imagePreview" alt="프로필 사진" class="w-full h-full object-cover" />
              <div v-else class="h-full w-full flex items-center justify-center bg-gray-200">
                <span class="text-gray-500 text-2xl">{{ user.userName?.charAt(0) }}</span>
              </div>
            </div>
            <label class="bg-gray-100 text-gray-600 px-4 py-2 rounded-full hover:bg-gray-200 transition transform hover:scale-105 cursor-pointer">
              🖼️ 파일 선택
              <input type="file" @change="onFileChange" accept="image/*" class="hidden" />
            </label>
          </div>
        </section>

        <!-- 프로필 정보 섹션 -->
        <section class="mb-8">
          <h2 class="text-xl font-semibold mb-4 text-gray-700 flex items-center">
            <span class="mr-2">👤</span> 프로필 정보
          </h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-600 mb-1">이름</label>
              <input
                v-model="user.userName"
                type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-600 mb-1">이메일</label>
              <div class="flex">
                <input
                  v-model="user.emailId"
                  type="text"
                  class="w-1/2 px-4 py-2 border border-gray-300 rounded-l-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                />
                <span class="px-3 py-2 bg-gray-100 border border-gray-300">@</span>
                <input
                  v-model="user.emailDomain"
                  type="text"
                  class="w-1/2 px-4 py-2 border border-gray-300 rounded-r-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                />
              </div>
            </div>
            <div class="col-span-1 md:col-span-2">
              <label class="block text-sm font-medium text-gray-600 mb-1">비밀번호</label>
              <input
                v-model="user.userPwd"
                type="password"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
              />
            </div>
          </div>
        </section>

        <!-- 알림 설정 -->
        <section class="mb-8">
          <h2 class="text-xl font-semibold mb-4 text-gray-700 flex items-center">
            <span class="mr-2">🔔</span> 알림 설정
          </h2>
          <div class="flex flex-col space-y-4">
            <label class="flex items-center space-x-3 cursor-pointer">
              <input
                v-model="notifications.email"
                type="checkbox"
                class="form-checkbox text-orange-400 rounded h-5 w-5"
              />
              <span class="text-gray-700">📧 이메일 알림 받기</span>
            </label>
            <label class="flex items-center space-x-3 cursor-pointer">
              <input
                v-model="notifications.sms"
                type="checkbox"
                class="form-checkbox text-orange-400 rounded h-5 w-5"
              />
              <span class="text-gray-700">📱 SMS 알림 받기</span>
            </label>
          </div>
        </section>

        <!-- 저장 버튼 -->
        <div class="text-center">
          <button
            @click="saveChanges"
            class="bg-orange-400 text-white px-8 py-3 rounded-lg hover:bg-orange-500 transition transform hover:scale-105 shadow-md"
          >
            💾 변경사항 저장
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuth } from '@/stores/auth';

const auth = useAuth();

const user = reactive({
  userName: '',
  emailId: '',
  emailDomain: '',
  userPwd: '',
  profileSavePath: ''
});

const notifications = reactive({
  email: true,
  sms: false
});

const imagePreview = ref(null);
const selectedFile = ref(null);

onMounted(async () => {
  await auth.fetchUserInfo();
  Object.assign(user, auth.user);
});

const onFileChange = (e) => {
  const file = e.target.files[0];
  selectedFile.value = file;
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const saveChanges = async () => {
  try {
    const updateData = {
      userName: user.userName,
      emailId: user.emailId,
      emailDomain: user.emailDomain,
      userPwd: user.userPwd
    };
    const { success, error } = await auth.updateUserInfo(updateData);
    if (!success) throw new Error(error);

    if (selectedFile.value) {
      const { success: uploadSuccess, error: uploadError } = await auth.uploadProfileImage(selectedFile.value);
      if (!uploadSuccess) throw new Error(uploadError);
    }

    console.log('알림 설정:', notifications);
    alert('변경사항이 성공적으로 저장되었습니다.');
  } catch (error) {
    console.error('변경사항 저장 중 오류 발생:', error);
    alert('변경사항 저장 중 오류가 발생했습니다: ' + error.message);
  }
};
</script>
