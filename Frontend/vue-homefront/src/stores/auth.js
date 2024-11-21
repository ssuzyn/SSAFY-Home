import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useAxiosStore } from './axiosStore';

export const useAuth = defineStore('auth', () => {
  const axiosStore = useAxiosStore();
  const user = ref(JSON.parse(localStorage.getItem('user')) || null);
  const isLoggedIn = computed(() => !!axiosStore.token);

  const setToken = async (newToken) => {
    axiosStore.setToken(newToken);
  
    if (newToken) {
      try {
        const response = await axiosStore.get('/user/info');
        user.value = response.data['userInfo'];
        localStorage.setItem('user', JSON.stringify(user.value));
        location.reload();
      } catch (error) {
        console.error('User info 요청 실패:', error);
        user.value = null;
        localStorage.removeItem('user');
      }
    } else {
      user.value = null;
      localStorage.removeItem('user');
      window.location.href = '/';
    }
    
  };

  const fetchUserInfo = async () => {
    try {
      const response = await axiosStore.get('/user/info');
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
      await axiosStore.put('/user/update', updateData);
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
      await axiosStore.post('/user/uploadProfile', formData, {
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
      const response = await axiosStore.post('/user/login', {
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
  };

  const validateToken = async () => {
    if (axiosStore.token) {
      try {
        const response = await axiosStore.get('/user/info');
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
});