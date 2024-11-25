<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="w-full max-w-2xl mx-auto p-4 pt-36">
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden border border-gray-100">
        <!-- 헤더 -->
        <header class="bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] text-white py-4 px-6 relative overflow-hidden">
          <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent"></div>
          <h1 class="text-xl font-bold relative z-10 text-center">마이페이지</h1>
          <p class="text-orange-50 text-xs text-center mt-1">개인정보 관리</p>
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
              <div class="w-full aspect-square rounded-xl shadow-md overflow-hidden bg-white mb-3">
                <img
                  v-if="user.profileSavePath"
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
              </div>
              <!-- 이름과 아이디 -->
              <div class="text-center space-y-1">
                <p class="text-sm text-gray-500">{{ user.emailId ? `@${user.emailId}` : "-" }}</p>
                <p class="font-semibold text-gray-800">{{ user.userName || "-" }}</p>
              </div>
            </div>

            <!-- 오른쪽: 프로필 정보 섹션 -->
            <div class="w-2/3">
              <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
                <span class="mr-2 text-lg">📋</span> 프로필 정보
              </h2>
              <div class="space-y-2">
                <!-- 이메일 -->
                <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                  <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                    <span class="mr-1.5">📧</span> 이메일
                  </p>
                  <p class="text-sm text-gray-700">{{ user.emailId ? `${user.emailId}@${user.emailDomain}` : "-" }}</p>
                </div>

                <!-- 전화번호 -->
                <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                  <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                    <span class="mr-1.5">📱</span> 전화번호
                  </p>
                  <p class="text-sm text-gray-700">{{ user.phoneNumber || "-" }}</p>
                </div>

                <!-- 성별과 나이를 같은 행에 배치 -->
                <div class="grid grid-cols-2 gap-2">
                  <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                    <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                      <span class="mr-1.5">👥</span> 성별
                    </p>
                    <p class="text-sm text-gray-700">{{ formatGender(user.gender) }}</p>
                  </div>
                  <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                    <p class="text-xs font-medium text-gray-500 mb-1 flex items-center">
                      <span class="mr-1.5">🎂</span> 나이
                    </p>
                    <p class="text-sm text-gray-700">{{ user.age ? `${user.age}세` : "-" }}</p>
                  </div>
                </div>


              </div>
            </div>
          </div>

          <!-- 알림 설정 섹션 -->
          <br>
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">🔔</span> 알림 설정
            </h2>
            <div class="space-y-2">
              <!-- 이메일 알림 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                <div class="flex items-center justify-between">
                  <div class="flex items-center">
                    <span class="text-lg mr-3">📧</span>
                    <span class="text-sm text-gray-700">이메일 알림 받기</span>
                  </div>
                  <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-all duration-300 cursor-pointer"
                       :class="{ 'bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] shadow-inner shadow-orange-400/30': notifications.email }">
                    <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-all duration-300 shadow-md"
                         :class="{
                           'translate-x-6 shadow-orange-300': notifications.email,
                           'shadow-gray-300': !notifications.email
                         }"></div>
                  </div>
                </div>
              </div>

              <!-- SMS 알림 -->
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-100 hover:border-orange-200 transition-colors">
                <div class="flex items-center justify-between">
                  <div class="flex items-center">
                    <span class="text-lg mr-3">💌</span>
                    <span class="text-sm text-gray-700">SMS 알림 받기</span>
                  </div>
                  <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-all duration-300 cursor-pointer"
                       :class="{ 'bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] shadow-inner shadow-orange-400/30': notifications.sms }">
                    <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-all duration-300 shadow-md"
                         :class="{
                           'translate-x-6 shadow-orange-300': notifications.sms,
                           'shadow-gray-300': !notifications.sms
                         }"></div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 프로필 수정 버튼 -->
          <div class="text-center">
            <button
              @click="handleEdit"
              class="bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white px-6 py-3 rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 flex items-center justify-center mx-auto shadow-lg hover:shadow-orange-200 transform hover:scale-[1.02] active:scale-[0.98]"
            >
              <span class="text-lg mr-2">✏️</span>
              프로필 수정
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, computed } from "vue";
import { useAuth } from "@/stores/auth";
import { useAxiosStore } from "@/stores/axiosStore";
import { useRouter } from "vue-router";

const router = useRouter();
const auth = useAuth();
const axiosStore = useAxiosStore();

const user = reactive({
  userName: "",
  emailId: "",
  emailDomain: "",
  gender: "",
  age: "",
  phoneNumber: "",
  profileSavePath: "",
});

const notifications = reactive({
  email: true,
  sms: false,
});

onMounted(async () => {
  await auth.fetchUserInfo();
  Object.assign(user, auth.user);
});

const handleEdit = () => {
  router.push({
    name: 'UpdatedProfile'
  });
};

const profileImageUrl = computed(() => {
  if (!user.profileSavePath) return null;
  return `http://localhost:8080/image/${user.profileSavePath}`;
});

const formatGender = (gender) => {
  const genderMap = {
    M: "남성",
    F: "여성",
    other: "기타",
    "": "-",
  };
  return genderMap[gender] || "-";
};

const handleImageError = async (e) => {
  try {
    const response = await axiosStore.get(
      `/image/${user.profileSavePath}`,
      {
        responseType: "blob",
      }
    );

    const blob = response.data;
    const url = URL.createObjectURL(blob);
    e.target.src = url;
  } catch (error) {
    console.error("이미지 로드 실패:", error);
    e.target.style.display = "none";
    const parent = e.target.parentElement;
    parent.innerHTML = `<div class="h-full w-full flex items-center justify-center bg-gray-200">
      <span class="text-gray-500 text-xl">${user.userName?.charAt(0) || "?"}</span>
    </div>`;
  }
};
</script>
