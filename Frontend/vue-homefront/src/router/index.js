import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MyPageView from '../views/MyPageView.vue'
import QnAView from '../views/QnAView.vue'
import HomeView from '../views/HomeView.vue'
import SignUpForm from '@/components/SignUpForm.vue'

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
    component: SignUpForm
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router