import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useAxiosStore } from './axiosStore'
import { useAuth } from './auth'

const API_URL = '/board'

export const useBoard = defineStore('board', () => {
  const auth = useAuth()
  const axiosStore = useAxiosStore()
  const questions = ref([])
  const selectedQuestion = ref(null)
  const comments = ref([])

  const fetchAllQuestions = async () => {
    try {
      const response = await axiosStore.get(`${API_URL}/list`)
      questions.value = response.data
      console.log(questions.value)
    } catch (error) {
      console.error('Failed to fetch all questions:', error)
    }
  }

  const fetchQuestions = async (type = '', keyword = '') => {
    try {
      const response = await axiosStore.get(`${API_URL}/search`, {
        params: { type, keyword }
      })
      questions.value = response.data
    } catch (error) {
      console.error('Failed to fetch questions:', error)
    }
  }

  const createQuestion = async (boardDto) => {
    try {
      await axiosStore.post(API_URL, boardDto)
      await fetchAllQuestions()
    } catch (error) {
      console.error('Failed to create question:', error)
    }
  }

  const getQuestionDetails = async (articleNo) => {
    try {
      const response = await axiosStore.get(`${API_URL}/${articleNo}`)
      console.log(response);
      selectedQuestion.value = response.data
      console.log('Question details:', selectedQuestion.value)
    } catch (error) {
      console.error('Failed to get question details:', error)
    }
  }

  const updateQuestion = async (boardDto) => {
    try {
      await axiosStore.put(API_URL, boardDto)
      await getQuestionDetails(boardDto.articleNo)
    } catch (error) {
      console.error('Failed to update question:', error)
    }
  }

  const deleteQuestion = async (articleNo) => {
    try {
      await axiosStore.delete(`${API_URL}/${articleNo}`)
      await fetchAllQuestions()
    } catch (error) {
      console.error('Failed to delete question:', error)
    }
  }

  const fetchComments = async (articleNo) => {
    try {
      const response = await axiosStore.get(`${API_URL}/${articleNo}/comments`)
      comments.value = response.data
      console.log('Comments:', comments.value)
    } catch (error) {
      console.error('Failed to fetch comments:', error)
    }
  }

  const createComment = async (commentDto) => {
    try {
      await axiosStore.post(`${API_URL}/comments`, commentDto)
      await fetchComments(commentDto.articleNo)
    } catch (error) {
      console.error('Failed to create comment:', error)
    }
  }

  const deleteComment = async (commentId, articleNo) => {
    try {
      await axiosStore.delete(`${API_URL}/comments/${commentId}`)
      await fetchComments(articleNo)
    } catch (error) {
      console.error('Failed to delete comment:', error)
    }
  }

  return {
    questions,
    selectedQuestion,
    comments,
    fetchAllQuestions,
    fetchQuestions,
    createQuestion,
    getQuestionDetails,
    updateQuestion,
    deleteQuestion,
    fetchComments,
    createComment,
    deleteComment
  }
})