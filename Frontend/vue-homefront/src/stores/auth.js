import { ref } from 'vue'

export const useAuth = () => {
  const isLoggedIn = ref(false)

  const login = () => {
    isLoggedIn.value = true
  }

  const logout = () => {
    isLoggedIn.value = false
  }

  return { isLoggedIn, login, logout }
}