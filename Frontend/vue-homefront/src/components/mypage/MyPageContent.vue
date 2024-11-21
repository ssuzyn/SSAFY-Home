<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
    <div class="w-full max-w-lg bg-white rounded-xl shadow-md overflow-hidden border border-gray-200">
      <header class="bg-orange-200 text-gray-800 text-center py-4">
        <h1 class="text-2xl font-bold">마이페이지</h1>
      </header>
      <div class="p-6">
        <section class="mb-6">
          <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
            <span class="mr-2">📸</span> 프로필 사진
          </h2>
          <div class="flex items-center space-x-3">
            <div class="w-20 h-20 rounded-full border-2 border-gray-300 overflow-hidden">
              <img
                v-if="user.profileSavePath"
                :src="profileImageUrl"
                alt="프로필 사진"
                @error="handleImageError"
                class="w-full h-full object-cover"
              />
              <div
                v-else
                class="h-full w-full flex items-center justify-center bg-gray-200"
              >
                <span class="text-gray-500 text-xl">{{ user.userName?.charAt(0) }}</span>
              </div>
            </div>
          </div>
        </section>

        <section class="mb-6">
          <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
            <span class="mr-2">👤</span> 프로필 정보
          </h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p class="text-sm font-medium text-gray-600 mb-1">이름</p>
              <p class="text-gray-800">{{ user.userName || "-" }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-600 mb-1">이메일</p>
              <p class="text-gray-800">{{ user.emailId ? `${user.emailId}@${user.emailDomain}` : "-" }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-600 mb-1">성별</p>
              <p class="text-gray-800">{{ formatGender(user.gender) }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-600 mb-1">나이</p>
              <p class="text-gray-800">{{ user.age ? `${user.age}세` : "-" }}</p>
            </div>
            <div class="col-span-1 md:col-span-2">
              <p class="text-sm font-medium text-gray-600 mb-1">전화번호</p>
              <p class="text-gray-800">{{ user.phoneNumber || "-" }}</p>
            </div>
          </div>
        </section>

        <section class="mb-6">
          <h2 class="text-lg font-semibold mb-3 text-gray-700 flex items-center">
            <span class="mr-2">🔔</span> 알림 설정
          </h2>
          <div class="flex flex-col space-y-3">
            <div class="flex items-center space-x-2">
              <svg class="w-4 h-4" :class="notifications.email ? 'text-orange-400' : 'text-gray-400'" fill="currentColor" viewBox="0 0 20 20">
                <path v-if="notifications.email" d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" />
                <path v-else fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
              </svg>
              <span class="text-gray-700">📧 이메일 알림 받기</span>
            </div>
            <div class="flex items-center space-x-2">
              <svg class="w-4 h-4" :class="notifications.sms ? 'text-orange-400' : 'text-gray-400'" fill="currentColor" viewBox="0 0 20 20">
                <path v-if="notifications.sms" d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" />
                <path v-else fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
              </svg>
              <span class="text-gray-700">📱 SMS 알림 받기</span>
            </div>
          </div>
        </section>

        <div class="text-center">
          <button
            @click="handleEdit"
            class="bg-orange-400 text-white px-6 py-2 rounded-lg hover:bg-orange-500 transition transform hover:scale-105 shadow-md"
          >
            ✏️ 프로필 수정
          </button>
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