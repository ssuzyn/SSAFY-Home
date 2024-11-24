import { defineStore } from 'pinia';
import axios from 'axios';
import { ref, computed } from 'vue';

export const useAxiosStore = defineStore('axios', () => {
  const token = ref(localStorage.getItem('token') || null);
  
  // axios 인스턴스를 store 내부로 이동
  const axiosInstance = axios.create({
    baseURL: 'http://127.0.0.1:8080',
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
    }
  });

  // 토큰 설정 함수
  const setToken = (newToken) => {
    token.value = newToken;
    if (newToken) {
      localStorage.setItem('token', newToken);
      // 토큰이 설정될 때 axios 기본 헤더도 업데이트
      axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
    } else {
      localStorage.removeItem('token');
      delete axiosInstance.defaults.headers.common['Authorization'];
    }
  };

  // 초기 토큰이 있다면 설정
  if (token.value) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
  }

  // 인터셉터 설정
  axiosInstance.interceptors.request.use(
    (config) => {
      // 현재 토큰 값을 매 요청마다 확인
      if (token.value && !config.url.endsWith('/user/login')) {
        config.headers['Authorization'] = `Bearer ${token.value}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (error.response?.status === 401) {
        setToken(null);
        // location.reload();
      }
      return Promise.reject(error);
    }
  );

  // axios 메서드들을 함수로 래핑하여 반환
  const get = (url, config) => axiosInstance.get(url, config);
  const post = (url, data, config) => axiosInstance.post(url, data, config);
  const put = (url, data, config) => axiosInstance.put(url, data, config);
  const del = (url, config) => axiosInstance.delete(url, config);

  return {
    token,
    setToken,
    get,
    post,
    put,
    delete: del
  }
});