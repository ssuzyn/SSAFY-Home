<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="w-full max-w-2xl mx-auto p-4 pt-36">
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden border border-gray-100">
        <!-- 헤더 -->
        <header class="bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] text-white py-4 px-6 relative overflow-hidden">
          <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent"></div>
          <h1 class="text-xl font-bold relative z-10 text-center">프로필 수정</h1>
          <p class="text-orange-50 text-xs text-center mt-1">개인정보 수정</p>
        </header>

        <div class="p-6">
          <!-- 프로필 사진과 정보를 감싸는 컨테이너 -->
          <div class="flex gap-6 mb-6">
            <!-- 왼쪽: 프로필 사진 섹션 -->
            <div class="w-1/3">
              <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
                <span class="mr-2 text-lg">👤</span> 사용자 프로필
              </h2>
              <!-- 프로필 사진 -->
              <div class="w-full aspect-square rounded-xl shadow-md overflow-hidden bg-white mb-3 relative group">
                <img
                  v-if="imagePreview"
                  :src="imagePreview"
                  alt="프로필 미리보기"
                  class="w-full h-full object-cover"
                />
                <img
                  v-else-if="user.profileSavePath"
                  :src="profileImageUrl"
                  alt="프로필 사진"
                  @error="handleImageError"
                  class="w-full h-full object-cover"
                />
                <div
                  v-else
                  class="h-full w-full flex items-center justify-center bg-gradient-to-br from-orange-50 to-white"
                >
                  <span class="text-[#FF965B] text-3xl font-semibold">{{ user.userName?.charAt(0) }}</span>
                </div>
                <!-- 이미지 업로드 오버레이 -->
                <label class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center cursor-pointer">
                  <span class="text-white text-sm">🖼️ 사진 변경</span>
                  <input type="file" @change="onFileChange" accept="image/*" class="hidden" />
                </label>
              </div>
              <!-- 이름과 아이디 입력 -->
              <div class="space-y-2">
                <p class="text-sm text-gray-500">{{ user.emailId ? `@${user.emailId}` : "-" }}</p>
                <input
                  v-model="user.userName"
                  type="text"
                  class="w-full text-center px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                  placeholder="이름을 입력하세요"
                />
              </div>
            </div>

            <!-- 오른쪽: 프로필 정보 섹션 -->
            <div class="w-2/3">
              <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
                <span class="mr-2 text-lg">📋</span> 프로필 정보
              </h2>
              <div class="space-y-2">
                <!-- 이메일 -->
                <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100">
                  <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                    <span class="mr-1.5">📧</span> 이메일
                  </p>
                  <div class="flex items-center gap-1">
                    <input
                      v-model="user.emailId"
                      type="text"
                      class="w-[45%] px-2 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                      placeholder="이메일"
                    />
                    <span class="text-gray-400 px-0.5">@</span>
                    <input
                      v-model="user.emailDomain"
                      type="text"
                      class="w-[45%] px-2 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                      placeholder="도메인"
                    />
                  </div>
                </div>

                <!-- 전화번호 -->
                <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100">
                  <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                    <span class="mr-1.5">📱</span> 전화번호
                  </p>
                  <input
                    v-model="user.phoneNumber"
                    type="tel"
                    class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                    placeholder="전화번호를 입력하세요"
                  />
                </div>

                <!-- 성별과 나이를 같은 행에 배치 -->
                <div class="grid grid-cols-2 gap-2">
                  <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100">
                    <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                      <span class="mr-1.5">👥</span> 성별
                    </p>
                    <select
                      v-model="user.gender"
                      class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                    >
                      <option value="">선택하세요</option>
                      <option value="M">남성</option>
                      <option value="F">여성</option>
                    </select>
                  </div>
                  <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100">
                    <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                      <span class="mr-1.5">🎂</span> 나이
                    </p>
                    <input
                      v-model="user.age"
                      type="number"
                      class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
                      placeholder="나이를 입력하세요"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 알림 설정 섹션 -->
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">🔔</span> 알림 설정
            </h2>
            <div class="space-y-2">
              <!-- 이메일 알림 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border transition-all duration-300"
                   :class="{ 'border-orange-200': notifications.email, 'border-gray-100': !notifications.email }">
                <div class="flex items-center justify-between">
                  <div class="flex items-center">
                    <span class="text-lg mr-3">📧</span>
                    <span class="text-sm" :class="{ 'text-gray-700': !notifications.email, 'text-orange-600': notifications.email }">
                      이메일 알림 받기
                    </span>
                  </div>
                  <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-all duration-300 cursor-pointer"
                       :class="{ 'bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] shadow-inner shadow-orange-400/30': notifications.email }"
                       @click="notifications.email = !notifications.email">
                    <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-all duration-300 shadow-md"
                         :class="{
                           'translate-x-6 shadow-orange-300': notifications.email,
                           'shadow-gray-300': !notifications.email
                         }">
                    </div>
                  </div>
                </div>
              </div>

              <!-- SMS 알림 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border transition-all duration-300"
                   :class="{ 'border-orange-200': notifications.sms, 'border-gray-100': !notifications.sms }">
                <div class="flex items-center justify-between">
                  <div class="flex items-center">
                    <span class="text-lg mr-3">💌</span>
                    <span class="text-sm" :class="{ 'text-gray-700': !notifications.sms, 'text-orange-600': notifications.sms }">
                      SMS 알림 받기
                    </span>
                  </div>
                  <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-all duration-300 cursor-pointer"
                       :class="{ 'bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] shadow-inner shadow-orange-400/30': notifications.sms }"
                       @click="notifications.sms = !notifications.sms">
                    <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-all duration-300 shadow-md"
                         :class="{
                           'translate-x-6 shadow-orange-300': notifications.sms,
                           'shadow-gray-300': !notifications.sms
                         }">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 저장 및 뒤로가기 버튼 -->
          <div class="flex justify-center gap-3">
            <!-- 뒤로가기 버튼 -->
            <button
              @click="$router.go(-1)"
              class="px-5 py-2.5 bg-gray-100 text-gray-700 rounded-xl hover:bg-gray-200 transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] text-sm font-medium flex items-center"
            >
              <span class="text-lg mr-1.5">←</span>
              뒤로가기
            </button>

            <!-- 저장 버튼 -->
            <button
              @click="handleSave"
              class="px-5 py-2.5 bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] text-white rounded-xl hover:from-[#FF8A3D] hover:to-[#FF7A2D] transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-orange-200/50 relative overflow-hidden group text-sm font-medium"
            >
              <span class="relative z-10 flex items-center justify-center">
                <span class="text-lg mr-1.5">💾</span>
                변경사항 저장
              </span>
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-white/20 opacity-0 group-hover:opacity-100 transition-opacity duration-200"></div>
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

const handleSave = async () => {
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
