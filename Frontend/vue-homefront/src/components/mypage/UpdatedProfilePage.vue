<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="w-full max-w-2xl mx-auto p-4 pt-20">
      <div class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden border border-gray-100">
        <!-- 헤더 -->
        <header class="bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white py-3 px-4 relative overflow-hidden mt-1">
          <button
            @click="router.push({ name: 'MyPage' })"
            class="absolute left-4 top-1/2 transform -translate-y-1/2 text-white/90 hover:text-white transition-colors z-10 text-sm"
          >
            ← 뒤로
          </button>
          <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/5 to-transparent"></div>
          <h1 class="text-lg font-bold relative z-10 text-center">회원정보 수정</h1>
          <p class="text-orange-100 text-xs text-center mt-0.5">프로필 정보를 수정할 수 있습니다</p>
        </header>

        <div class="p-4">
          <!-- 프로필 사진 섹션 -->
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">📸</span> 프로필 사진
            </h2>
            <div class="flex items-center space-x-3">
              <div class="w-16 h-16 rounded-xl shadow-md overflow-hidden bg-white">
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
                <div v-else class="h-full w-full flex items-center justify-center bg-gradient-to-br from-orange-100 to-orange-50">
                  <span class="text-orange-500 text-xl font-semibold">{{ user.userName?.charAt(0) }}</span>
                </div>
              </div>
              <label class="px-3 py-2 bg-white border border-gray-200 text-gray-700 rounded-xl hover:bg-gray-50 transition-colors cursor-pointer shadow-sm hover:border-orange-200 text-sm">
                🖼️ 파일 선택
                <input type="file" @change="onFileChange" accept="image/*" class="hidden" />
              </label>
            </div>
          </section>

          <!-- 프로필 정보 섹션 -->
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">👤</span> 프로필 정보
            </h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
              <!-- 이름 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">이름</label>
                <input
                  v-model="user.userName"
                  type="text"
                  class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                />
              </div>

              <!-- 이메일 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">이메일</label>
                <div class="flex gap-2">
                  <input
                    v-model="user.emailId"
                    type="text"
                    class="w-1/2 px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                  />
                  <span class="px-2 py-2 text-gray-400">@</span>
                  <input
                    v-model="user.emailDomain"
                    type="text"
                    class="w-1/2 px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                  />
                </div>
              </div>

              <!-- 성별 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">성별</label>
                <select
                  v-model="user.gender"
                  class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                >
                  <option value="">선택하세요</option>
                  <option value="M">남성</option>
                  <option value="F">여성</option>
                  <option value="other">기타</option>
                </select>
              </div>

              <!-- 나이 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">나이</label>
                <input
                  v-model="user.age"
                  type="number"
                  class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                />
              </div>

              <!-- 전화번호 -->
              <div class="col-span-full bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">전화번호</label>
                <input
                  v-model="user.phoneNumber"
                  type="tel"
                  class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                  placeholder="예: 010-1234-5678"
                />
              </div>

              <!-- 비밀번호 -->
              <div class="col-span-full bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <label class="block text-xs font-medium text-gray-500 mb-1">비밀번호</label>
                <input
                  v-model="user.userPwd"
                  type="password"
                  class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm transition-all"
                />
              </div>
            </div>
          </section>

          <!-- 알림 설정 섹션 -->
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">🔔</span> 알림 설정
            </h2>
            <div class="space-y-2">
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50 flex items-center justify-between group cursor-pointer hover:border-orange-200 transition-colors">
                <div class="flex items-center">
                  <span class="text-lg mr-2">📧</span>
                  <span class="text-sm text-gray-700">이메일 알림 받기</span>
                </div>
                <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-colors duration-200"
                     :class="{ 'bg-orange-500': notifications.email }">
                  <input
                    v-model="notifications.email"
                    type="checkbox"
                    class="sr-only"
                  />
                  <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-transform duration-200"
                       :class="{ 'translate-x-6': notifications.email }"></div>
                </div>
              </div>

              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50 flex items-center justify-between group cursor-pointer hover:border-orange-200 transition-colors">
                <div class="flex items-center">
                  <span class="text-lg mr-2">📱</span>
                  <span class="text-sm text-gray-700">SMS 알림 받기</span>
                </div>
                <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-colors duration-200"
                     :class="{ 'bg-orange-500': notifications.sms }">
                  <input
                    v-model="notifications.sms"
                    type="checkbox"
                    class="sr-only"
                  />
                  <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-transform duration-200"
                       :class="{ 'translate-x-6': notifications.sms }"></div>
                </div>
              </div>
            </div>
          </section>

          <!-- 저장 버튼 -->
          <div class="text-center mt-4">
            <button
              @click="saveChanges"
              class="bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white px-5 py-2.5 rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden group text-sm"
            >
              <span class="relative z-10 flex items-center justify-center">
                <span class="text-lg mr-2">💾</span>
                변경사항 저장
              </span>
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-200"></div>
            </button>
          </div>
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
