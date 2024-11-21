import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

// axios 인스턴스 생성
export const axiosInstance = axios.create({
  baseURL: 'http://127.0.0.1:8080',
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
      if (currentToken && !config.url.endsWith('/user/login')) {
        config.headers['Authorization'] = `Bearer ${currentToken}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (error.response?.status === 401) {
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
        const response = await axiosInstance.get('/user/info');  // 경로 수정
        user.value = response.data['userInfo'];
        localStorage.setItem('user', JSON.stringify(user.value));
      } catch (error) {
        console.error('User info 요청 실패:', error);
        user.value = null;
        localStorage.removeItem('user');
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
      const response = await axiosInstance.get('/user/info');  // 경로 수정
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
      await axiosInstance.put('/user/update', updateData);  // 경로 수정
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
      await axiosInstance.post('/user/uploadProfile', formData, {  // 경로 수정
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
      const response = await axiosInstance.post('/user/login', {  // 이미 올바른 경로
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

  const validateToken = async () => {
    if (token.value) {
      try {
        const response = await axiosInstance.get('/user/info');  // 경로 수정
        user.value = response.data['userInfo'];
        localStorage.setItem('user', JSON.stringify(user.value));
      } catch (error) {
        console.error('토큰 검증 실패:', error);
        logout();
      }
    }
  };

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