<template>
    <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <div class="w-full max-w-lg bg-white rounded-xl shadow-md overflow-hidden border border-gray-200">
        <header class="bg-orange-200 text-gray-800 text-center py-4 relative">
          <button
            @click="router.push({ name: 'MyPage' })"
            class="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-600 hover:text-gray-800"
          >
            ← 뒤로
          </button>
          <h1 class="text-2xl font-bold">회원정보 수정</h1>
        </header>
        <div class="p-6">
          <section class="mb-6">
            <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
              <span class="mr-2">📸</span> 프로필 사진
            </h2>
            <div class="flex items-center space-x-3">
              <div class="w-20 h-20 rounded-full border-2 border-gray-300 overflow-hidden">
                <img
                  v-if="imagePreview"
                  :src="imagePreview"
                  alt="프로필 사진 미리보기"
                  class="w-full h-full object-cover"
                />
                <img
                  v-else-if="user.profileSavePath"
                  :src="profileImageUrl"
                  alt="현재 프로필 사진"
                  class="w-full h-full object-cover"
                  @error="handleImageError"
                />
                <div
                  v-else
                  class="h-full w-full flex items-center justify-center bg-gray-200"
                >
                  <span class="text-gray-500 text-xl">{{ user.userName?.charAt(0) }}</span>
                </div>
              </div>
              <label class="bg-gray-100 text-gray-600 px-4 py-2 rounded-full hover:bg-gray-200 transition cursor-pointer">
                🖼️ 파일 선택
                <input type="file" @change="onFileChange" accept="image/*" class="hidden" />
              </label>
            </div>
          </section>
  
          <section class="mb-6">
            <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
              <span class="mr-2">👤</span> 프로필 정보
            </h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-600 mb-1">이름</label>
                <input
                  v-model="user.userName"
                  type="text"
                  class="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                />
              </div>
  
              <div>
                <label class="block text-sm font-medium text-gray-600 mb-1">이메일</label>
                <div class="flex">
                  <input
                    v-model="user.emailId"
                    type="text"
                    class="w-1/2 px-3 py-2 border border-gray-200 rounded-l-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                  />
                  <span class="px-2 py-2 bg-gray-50 border border-gray-200">@</span>
                  <input
                    v-model="user.emailDomain"
                    type="text"
                    class="w-1/2 px-3 py-2 border border-gray-200 rounded-r-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                  />
                </div>
              </div>
  
              <div>
                <label class="block text-sm font-medium text-gray-600 mb-1">성별</label>
                <select
                  v-model="user.gender"
                  class="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                >
                  <option value="">선택하세요</option>
                  <option value="M">남성</option>
                  <option value="F">여성</option>
                  <option value="other">기타</option>
                </select>
              </div>
  
              <div>
                <label class="block text-sm font-medium text-gray-600 mb-1">나이</label>
                <input
                  v-model="user.age"
                  type="number"
                  class="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                />
              </div>
  
              <div class="col-span-2">
                <label class="block text-sm font-medium text-gray-600 mb-1">전화번호</label>
                <input
                  v-model="user.phoneNumber"
                  type="tel"
                  class="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                  placeholder="예: 010-1234-5678"
                />
              </div>
  
              <div class="col-span-2">
                <label class="block text-sm font-medium text-gray-600 mb-1">비밀번호</label>
                <input
                  v-model="user.userPwd"
                  type="password"
                  class="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-200 focus:border-orange-300"
                />
              </div>
            </div>
          </section>
  
          <section class="mb-6">
            <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
              <span class="mr-2">🔔</span> 알림 설정
            </h2>
            <div class="flex flex-col space-y-3">
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="notifications.email"
                  type="checkbox"
                  class="h-4 w-4 text-orange-400 rounded border-gray-300"
                />
                <span class="text-gray-700">📧 이메일 알림 받기</span>
              </label>
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="notifications.sms"
                  type="checkbox"
                  class="h-4 w-4 text-orange-400 rounded border-gray-300"
                />
                <span class="text-gray-700">📱 SMS 알림 받기</span>
              </label>
            </div>
          </section>
  
          <div class="text-center">
            <button
              @click="saveChanges"
              class="bg-orange-400 text-white px-6 py-2 rounded-lg hover:bg-orange-500 transition transform hover:scale-105 shadow-md"
            >
              💾 변경사항 저장
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref, onMounted, computed } from 'vue';
  import { useAuth } from "@/stores/auth";
  import { useAxiosStore } from "@/stores/axiosStore";
  import { useRouter } from "vue-router";
  import { message } from "ant-design-vue";

  
  const router = useRouter();
  const auth = useAuth();
  const axiosStore = useAxiosStore();
  
  const user = reactive({
    userName: "",
    emailId: "",
    emailDomain: "",
    userPwd: "",
    gender: "",
    age: "",
    phoneNumber: "",
    profileSavePath: ""
  });
  
  const notifications = reactive({
    email: true,
    sms: false
  });
  
  const imagePreview = ref(null);
  const selectedFile = ref(null);
  
  const profileImageUrl = computed(() => {
    if (!user.profileSavePath) return null;
    return `http://localhost:8080/image/${user.profileSavePath}`;
  });
  
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
  
  const handleImageError = async (e) => {
    try {
      const response = await axiosStore.get(`/image/${user.profileSavePath}`, {
        responseType: 'blob'
      });
      
      const blob = response.data;
      const url = URL.createObjectURL(blob);
      e.target.src = url;
    } catch (error) {
      console.error('이미지 로드 실패:', error);
      e.target.style.display = "none";
      const parent = e.target.parentElement;
      parent.innerHTML = `<div class="h-full w-full flex items-center justify-center bg-gray-200">
        <span class="text-gray-500 text-xl">${user.userName?.charAt(0) || "?"}</span>
      </div>`;
    }
  };
  
  const saveChanges = async () => {
    try {
      const updateData = {
        userName: user.userName,
        emailId: user.emailId,
        emailDomain: user.emailDomain,
        userPwd: user.userPwd,
        gender: user.gender,
        age: user.age,
        phoneNumber: user.phoneNumber
      };
  
      const { success, error } = await auth.updateUserInfo(updateData);
      if (!success) throw new Error(error);
  
      if (selectedFile.value) {
        const { success: uploadSuccess, error: uploadError } = 
          await auth.uploadProfileImage(selectedFile.value);
        if (!uploadSuccess) throw new Error(uploadError);
        await auth.fetchUserInfo();
        Object.assign(user, auth.user);
        imagePreview.value = null;
        selectedFile.value = null;
      }
  
      message.success("변경사항이 성공적으로 저장되었습니다.");
      router.push({ name: 'MyPage' });
    } catch (error) {
      console.error("변경사항 저장 중 오류 발생:", error);
      message.error("변경사항 저장 중 오류가 발생했습니다:"|| error.response?.data?.message);
    }
  };
  </script>