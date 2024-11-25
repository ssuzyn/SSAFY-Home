<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="w-full max-w-2xl mx-auto p-4 pt-24">
      <div class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden border border-gray-100">
        <!-- 헤더 -->
        <header class="bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white py-3 px-4 relative overflow-hidden mt-1">
          <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/5 to-transparent"></div>
          <h1 class="text-lg font-bold relative z-10 text-center">마이페이지</h1>
          <p class="text-orange-100 text-xs text-center mt-0.5">개인정보 관리</p>
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
                  v-if="user.profileSavePath"
                  :src="profileImageUrl"
                  alt="프로필 사진"
                  @error="handleImageError"
                  class="w-full h-full object-cover"
                />
                <div
                  v-else
                  class="h-full w-full flex items-center justify-center bg-gradient-to-br from-orange-100 to-orange-50"
                >
                  <span class="text-orange-500 text-2xl font-semibold">{{ user.userName?.charAt(0) }}</span>
                </div>
              </div>
            </div>
          </section>

          <!-- 프로필 정보 섹션 -->
          <section class="mb-4">
            <h2 class="text-base font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-lg">👤</span> 프로필 정보
            </h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <p class="text-xs font-medium text-gray-500 mb-1">이름</p>
                <p class="text-sm text-gray-900">{{ user.userName || "-" }}</p>
              </div>
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <p class="text-xs font-medium text-gray-500 mb-1">이메일</p>
                <p class="text-sm text-gray-900">{{ user.emailId ? `${user.emailId}@${user.emailDomain}` : "-" }}</p>
              </div>
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <p class="text-xs font-medium text-gray-500 mb-1">성별</p>
                <p class="text-sm text-gray-900">{{ formatGender(user.gender) }}</p>
              </div>
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <p class="text-xs font-medium text-gray-500 mb-1">나이</p>
                <p class="text-sm text-gray-900">{{ user.age ? `${user.age}세` : "-" }}</p>
              </div>
              <div class="col-span-1 md:col-span-2 bg-white p-3 rounded-xl shadow-sm border border-gray-50">
                <p class="text-xs font-medium text-gray-500 mb-1">전화번호</p>
                <p class="text-sm text-gray-900">{{ user.phoneNumber || "-" }}</p>
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
                  <span class="text-lg mr-3">📧</span>
                  <span class="text-gray-700">이메일 알림 받기</span>
                </div>
                <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-colors duration-200"
                     :class="{ 'bg-orange-500': notifications.email }">
                  <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-transform duration-200"
                       :class="{ 'translate-x-6': notifications.email }"></div>
                </div>
              </div>
              <div class="bg-white p-3 rounded-xl shadow-sm border border-gray-50 flex items-center justify-between group cursor-pointer hover:border-orange-200 transition-colors">
                <div class="flex items-center">
                  <span class="text-lg mr-3">📱</span>
                  <span class="text-gray-700">SMS 알림 받기</span>
                </div>
                <div class="w-12 h-6 bg-gray-200 rounded-full relative transition-colors duration-200"
                     :class="{ 'bg-orange-500': notifications.sms }">
                  <div class="absolute w-5 h-5 bg-white rounded-full top-0.5 left-0.5 transition-transform duration-200"
                       :class="{ 'translate-x-6': notifications.sms }"></div>
                </div>
              </div>
            </div>
          </section>

          <!-- 프로필 수정 버튼 -->
          <div class="text-center mt-4">
            <button
              @click="handleEdit"
              class="bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white px-5 py-2.5 rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-md hover:shadow-lg relative overflow-hidden group text-sm"
            >
              <span class="relative z-10 flex items-center justify-center">
                <span class="text-lg mr-2">✏️</span>
                프로필 수정
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
