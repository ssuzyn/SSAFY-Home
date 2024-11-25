<template>
  <div class="min-h-screen bg-gradient-to-br from-orange-50 via-gray-50 to-white">
    <div class="w-full max-w-2xl mx-auto p-4 pt-36">
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden border border-gray-100">
        <!-- 헤더 -->
        <header class="bg-gradient-to-r from-[#FF965B] to-[#FF8A3D] text-white py-4 px-6 relative overflow-hidden">
          <div class="flex items-center justify-center">
            <h1 class="text-2xl font-bold text-white">마이페이지</h1>
          </div>
        </header>

        <div class="p-6">
          <!-- 프로필 사진과 정보를 감싸는 컨테이너 -->
          <div class="flex gap-6 mb-6">
            <!-- 왼쪽: 프로필 사진 섹션 -->
            <div class="w-1/3">
              <h2 class="text-lg font-semibold mb-3 text-gray-800 flex items-center">
                <span class="mr-2 text-xl">👤</span> 사용자 프로필
              </h2>
              <!-- 로필 사진 -->
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
              <h2 class="text-lg font-semibold mb-3 text-gray-800 flex items-center">
                <span class="mr-2 text-xl">📋</span> 프로필 정보
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
            <h2 class="text-lg font-semibold mb-3 text-gray-800 flex items-center">
              <span class="mr-2 text-xl">🔔</span> 알림 설정
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

          <!-- 프로필 수정 버튼과 회원탈퇴 버튼을 포함하는 div -->
          <div class="flex justify-center items-center gap-4">
            <!-- 프로필 수정 버튼 -->
            <button
              @click="handleEdit"
              class="flex-1 max-w-[160px] bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white px-6 py-3 rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 flex items-center justify-center shadow-lg hover:shadow-orange-200 transform hover:scale-[1.02] active:scale-[0.98]"
            >
              <span class="text-lg mr-2">✏️</span>
              프로필 수정
            </button>

            <!-- 회원탈퇴 버튼 -->
            <button
              @click="handleDeleteAccount"
              class="flex-1 max-w-[160px] group flex items-center justify-center gap-2 px-6 py-3 rounded-xl border border-orange-200 hover:border-orange-300 hover:bg-orange-50 transition-all duration-200"
            >
            <svg
                class="w-4 h-4 text-orange-400 group-hover:text-orange-500 transition-colors"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                />
              </svg>
              <span class="text-orange-400 group-hover:text-orange-500 text-sm font-medium transition-colors">
                회원탈퇴
              </span>
              
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 회원탈퇴 확인 모달 -->
  <div v-if="showDeleteModal" class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl p-6 max-w-sm w-full shadow-xl transform transition-all">
      <div class="text-center mb-6">
        <div class="mx-auto w-12 h-12 rounded-full bg-red-100 flex items-center justify-center mb-4">
          <svg
            class="w-6 h-6 text-red-500"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
            />
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800">회원탈퇴 확인</h3>
        <p class="text-gray-600 mt-2">정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.</p>
      </div>
      <div class="flex gap-3">
        <button
          @click="showDeleteModal = false"
          class="flex-1 px-4 py-2.5 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-xl font-medium transition-colors"
        >
          취소
        </button>
        <button
          @click="confirmDelete"
          class="flex-1 px-4 py-2.5 bg-red-500 hover:bg-red-600 text-white rounded-xl font-medium transition-colors"
        >
          탈퇴하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
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

const showDeleteModal = ref(false);

onMounted(async () => {
  await auth.fetchUserInfo();
  Object.assign(user, auth.user);
});

const handleEdit = () => {
  router.push({
    name: 'UpdatedProfile'
  });
};

const handleDeleteAccount = () => {
  showDeleteModal.value = true;
};

const confirmDelete = async () => {
  try {
    const response = await axiosStore.delete('/user/delete');
    if (response.status === 200) {
      alert('회원탈퇴가 완료되었습니다.');
      await auth.logout();
      router.push('/');
    }
  } catch (error) {
    console.error('회원탈퇴 실패:', error);
    alert(error.response?.data?.message || '회원탈퇴 처리 중 문제가 발생했습니다.');
  } finally {
    showDeleteModal.value = false;
  }
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
