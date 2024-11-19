import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import MyPageView from '@/views/MyPageView.vue'
import QnAView from '@/views/QnAView.vue'
import SignupView from '@/views/SignupView.vue'
import { createRouter, createWebHistory } from 'vue-router'
const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPageView
  },
  {
    path: '/qna',
    name: 'QnA',
    component: QnAView
  },
  {
    path:'/signup',
    name:'SignUp',
    component: SignupView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router