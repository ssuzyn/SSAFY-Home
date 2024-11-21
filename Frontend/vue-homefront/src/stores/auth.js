import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

// axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://127.0.0.1:8080/user',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  }
});

export const useAuth = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user')) || null)
  const isLoggedIn = computed(() => !!token.value)

  // axios 인터셉터 설정
  axiosInstance.interceptors.request.use(
    (config) => {
      const currentToken = token.value;
      if (currentToken && !config.url.endsWith('/login')) {
        config.headers['Authorization'] = `Bearer ${currentToken}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  // 응답 인터셉터 추가 (토큰 만료 등 처리)
  axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (error.response?.status === 401) {
        // 토큰 만료 등으로 인한 인증 오류
        logout();
      }
      return Promise.reject(error);
    }
  );

  const setToken = async (newToken) => {
    token.value = newToken;
  
    if (newToken) {
      localStorage.setItem('token', newToken);
      try {
        const response = await axiosInstance.get('/info');
        user.value = response.data['userInfo'];
        localStorage.setItem('user', JSON.stringify(user.value)); // 사용자 정보 저장
      } catch (error) {
        console.error('User info 요청 실패:', error);
        user.value = null;
        localStorage.removeItem('user'); // 실패 시 사용자 정보 삭제
      }
    } else {
      localStorage.removeItem('token');
      user.value = null;
      localStorage.removeItem('user');
    }
    location.reload();
  };

  const fetchUserInfo = async () => {
    try {
      const response = await axiosInstance.get('/info');
      user.value = response.data.userInfo;
      localStorage.setItem('user', JSON.stringify(user.value));
    } catch (error) {
      console.error('User info 요청 실패:', error);
      user.value = null;
      localStorage.removeItem('user');
    }
  };
  
  const updateUserInfo = async (updateData) => {
    try {
      await axiosInstance.put('/update', updateData);
      await fetchUserInfo();
      return { success: true };
    } catch (error) {
      console.error('Failed to update user info:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || '정보 업데이트에 실패했습니다.' 
      };
    }
  };

  const uploadProfileImage = async (file) => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      await axiosInstance.post('/uploadProfile', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      await fetchUserInfo();
      return { success: true };
    } catch (error) {
      console.error('Failed to upload profile image:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || '프로필 이미지 업로드에 실패했습니다.' 
      };
    }
  };

  const login = async (userId, userPwd, rememberMe) => {
    try {
      const response = await axiosInstance.post('/login', { 
        userId, 
        userPwd 
      });
      
      const accessToken = response.data['access-token'];
      if (!accessToken) {
        throw new Error('토큰이 없습니다.');
      }

      await setToken(accessToken);

      if (rememberMe) {
        // rememberMe 로직 구현
      }

      return { success: true };
    } catch (error) {
      console.error('Login failed:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || '로그인에 실패했습니다.' 
      };
    }
  };

  const logout = () => {
    setToken(null);
    user.value = null;
  };

  // 초기 로드시 토큰 유효성 검증
  const validateToken = async () => {
    if (token.value) {
      try {
        const response = await axiosInstance.get('/info');
        user.value = response.data['userInfo'];
        localStorage.setItem('user', JSON.stringify(user.value)); // 서버에서 확인 후 다시 저장
      } catch (error) {
        console.error('토큰 검증 실패:', error);
        logout(); // 유효하지 않으면 로그아웃
      }
    }
  };

  // 컴포넌트 마운트시 호출
  validateToken();

  return {
    user,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    updateUserInfo,
    uploadProfileImage
  }
})