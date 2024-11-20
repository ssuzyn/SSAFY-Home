// stores/favorites.js
import { defineStore } from 'pinia'
import { message } from 'ant-design-vue'

export const useFavoritesStore = defineStore('favorites', {
  state: () => ({
    items: [],
    loading: false
  }),

  getters: {
    isFavorite: (state) => (propertyId) => {
      return state.items.some(item => item.propertyId === propertyId)
    },
    totalCount: (state) => state.items.length
  },

  actions: {
    async initialize() {
      if (this.loading || this.items.length) return
      
      try {
        this.loading = true
        const { data } = await fetch('/api/favorites').then(r => r.json())
        this.items = data
      } catch (error) {
        message.error('관심 매물 목록을 불러오는데 실패했습니다')
      } finally {
        this.loading = false
      }
    },

    async toggle(property) {
      const isFavorite = this.isFavorite(property.id)
      
      try {
        if (isFavorite) {
          await fetch(`/api/favorites/${property.id}`, { method: 'DELETE' })
          this.items = this.items.filter(item => item.propertyId !== property.id)
        } else {
          await fetch('/api/favorites', {
            method: 'POST',
            body: JSON.stringify(property)
          })
          this.items.push(property)
        }
      } catch (error) {
        message.error('처리 중 오류가 발생했습니다')
      }
    }
  }
})