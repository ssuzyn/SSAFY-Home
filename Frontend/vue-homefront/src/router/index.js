import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import QnAView from '@/views/QnAView.vue';
import SignupView from '@/views/SignupView.vue';
import MyPageContent from '@/components/mypage/MyPageContent.vue';
import UpdatedProfilePage from '@/components/mypage/UpdatedProfilePage.vue';
import RealEstateNewsPage from '@/components/news/RealEstateNewsPage.vue';
import FindIdPage from '@/components/auth/FindIdPage.vue';
import ResetPasswordPage from '@/components/auth/ResetPasswordPage.vue';
import { createRouter, createWebHistory } from 'vue-router';
import { useAuth } from '@/stores/auth'; // Pinia store 가져오기

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPageContent,
  },
  {
    path: '/mypage/edit',
    name: 'UpdatedProfile',
    component: UpdatedProfilePage,
  },
  {
    path: '/qna',
    name: 'QnA',
    component: QnAView,
  },
  {
    path: '/signup',
    name: 'SignUp',
    component: SignupView,
  },
  {
    path: '/findId',
    name: 'FindId',
    component: FindIdPage,
  },
  {
    path: '/resetPwd',
    name: 'ResetPwd',
    component: ResetPasswordPage,
  },
  {
    path: '/real-estate-news',
    name: 'RealEstateNews',
    component: RealEstateNewsPage,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 전역 가드 수정
router.beforeEach((to, from, next) => {
  const auth = useAuth(); // Pinia의 auth store 사용

  // 로그인 상태에서 /login으로 접근 시 홈으로 리디렉션
  if (to.path === '/login' && auth.isLoggedIn) {
    next('/');
  } else {
    next(); // 그 외 경로는 그대로 진행
  }
});

export default router;

