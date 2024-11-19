<template>
    <div class="w-full bg-white min-h-[calc(100vh-4rem)] pt-16">
      <div class="w-full px-4 md:px-6 py-6">
        <!-- 상단 제목과 버튼 영역 -->
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl text-black font-bold">부동산 QnA</h1>
          <button @click="openNewQuestionDialog" class="bg-orange-500 text-white px-4 py-2 rounded-md hover:bg-orange-600 flex items-center">
            <Plus class="mr-2 h-4 w-4" />
            질문하기
          </button>
        </div>
  
        <!-- 메인 콘텐츠 영역 -->
        <div class="flex flex-col lg:flex-row gap-6">
          <!-- 왼쪽 질문 목록 -->
          <div class="flex-1">
            <div class="mb-4">
              <input 
                v-model="searchQuery" 
                placeholder="질문 검색" 
                type="search" 
                class="w-full p-2 border rounded-md"
              />
            </div>
            
            <div class="space-y-4">
              <div v-for="question in questions" :key="question.id" class="bg-white border rounded-lg p-4 shadow-sm">
                <h3 class="text-lg text-black font-semibold">{{ question.title }}</h3>
                <p class="text-sm text-black mt-1">
                  작성자: {{ question.author }} | 작성일: {{ question.date }}
                </p>
                <div class="mt-3 flex justify-between items-center">
                  <div class="flex items-center gap-4">
                    <button class="text-black hover:text-gray-700 flex items-center">
                      <ThumbsUp class="mr-2 h-4 w-4" />
                      {{ question.likes }}
                    </button>
                    <span class="text-sm text-black">답변 {{ question.answers }}개</span>
                  </div>
                  <button 
                    @click="openQuestionDetailDialog(question)" 
                    class="text-orange-500 hover:text-orange-600 flex items-center"
                  >
                    자세히 보기
                    <ChevronRight class="ml-2 h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
          </div>
  
          <!-- 오른쪽 사이드바 -->
          <div class="lg:w-80 bg-white border rounded-lg p-4 h-fit">
            <h2 class="text-lg text-black font-semibold mb-4">인기 질문</h2>
            <ul class="space-y-3">
              <li v-for="(question, index) in popularQuestions" :key="index">
                <a class="text-sm hover:underline text-orange-500" href="#">
                  {{ question }}
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
  
      <!-- 새 질문 다이얼로그 -->
      <div v-if="showNewQuestionDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg max-w-md w-full mx-4">
          <h2 class="text-xl text-black font-bold mb-4">새 질문 작성</h2>
          <p class="text-black mb-4">부동산에 관한 질문을 작성해주세요.</p>
          <form @submit.prevent="handleNewQuestion">
            <input 
              v-model="newQuestion.title" 
              placeholder="제목" 
              required 
              class="w-full p-2 border rounded-md mb-4"
            />
            <textarea 
              v-model="newQuestion.content" 
              placeholder="질문 내용을 자세히 적어주세요." 
              required 
              class="w-full p-2 border rounded-md mb-4" 
              rows="4"
            ></textarea>
            <div class="flex justify-end gap-2">
              <button 
                type="button" 
                @click="closeNewQuestionDialog" 
                class="px-4 py-2 text-black hover:text-gray-800"
              >
                취소
              </button>
              <button 
                type="submit" 
                class="px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600"
              >
                질문 등록
              </button>
            </div>
          </form>
        </div>
      </div>
  
      <!-- 질문 상세 다이얼로그 -->
      <div v-if="selectedQuestion" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg max-w-3xl w-full mx-4 max-h-[90vh] overflow-auto">
          <h2 class="text-xl font-bold mb-2 text-black">{{ selectedQuestion.title }}</h2>
          <p class="text-sm text-black mb-4">
            작성자: {{ selectedQuestion.author }} | 작성일: {{ selectedQuestion.date }}
          </p>
          <div class="mb-4">
            <h3 class="font-semibold text-black">질문 내용</h3>
            <p class="mt-2 text-black">이 부분에 실제 질문 내용이 들어갑니다.</p>
          </div>
          <hr class="my-4" />
          <div>
            <h3 class="font-semibold mb-2 text-black">답변</h3>
            <div class="space-y-4">
              <div class="border rounded-md p-4 text-black">
                <p>이 부분에 답변 내용이 들어갑니다.</p>
                <p class="mt-2 text-sm text-black">
                  답변자: 홍길동 | 작성일: 2024-01-16
                </p>
              </div>
            </div>
          </div>
          <form @submit.prevent="handleNewAnswer" class="mt-4">
            <textarea 
              v-model="newAnswer" 
              placeholder="답변을 작성해주세요." 
              class="w-full p-2 border rounded-md mb-2 text-black" 
              rows="4"
            ></textarea>
            <div class="flex justify-between">
              <button 
                @click="closeQuestionDetailDialog" 
                type="button"
                class="px-4 py-2 text-black hover:text-gray-800"
              >
                닫기
              </button>
              <button 
                type="submit" 
                class="px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600"
              >
                답변 등록
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import { Plus, ChevronRight, ThumbsUp } from 'lucide-vue-next'
  
  const questions = ref([
    { id: 1, title: "전세 계약시 주의할 점이 무엇인가요?", author: "김부동", date: "2024-01-15", likes: 5, answers: 2 },
    { id: 2, title: "아파트 시세는 어떻게 알아볼 수 있나요?", author: "이매매", date: "2024-01-14", likes: 3, answers: 1 },
    { id: 3, title: "재개발 지역 투자, 어떻게 생각하시나요?", author: "박투자", date: "2024-01-13", likes: 7, answers: 4 },
  ])
  
  const popularQuestions = [
    "전세 사기 예방하는 방법",
    "부동산 세금 계산하는 법",
    "신축 아파트 vs 구축 아파트",
  ]
  
  const searchQuery = ref('')
  const showNewQuestionDialog = ref(false)
  const selectedQuestion = ref(null)
  const newQuestion = reactive({ title: '', content: '' })
  const newAnswer = ref('')
  
  const openNewQuestionDialog = () => {
    showNewQuestionDialog.value = true
  }
  
  const closeNewQuestionDialog = () => {
    showNewQuestionDialog.value = false
    newQuestion.title = ''
    newQuestion.content = ''
  }
  
  const handleNewQuestion = () => {
    const question = {
      id: questions.value.length + 1,
      title: newQuestion.title,
      author: "현재 사용자",
      date: new Date().toISOString().split('T')[0],
      likes: 0,
      answers: 0,
    }
    questions.value.unshift(question)
    closeNewQuestionDialog()
  }
  
  const openQuestionDetailDialog = (question) => {
    selectedQuestion.value = question
  }
  
  const closeQuestionDetailDialog = () => {
    selectedQuestion.value = null
    newAnswer.value = ''
  }
  
  const handleNewAnswer = () => {
    if (selectedQuestion.value) {
      selectedQuestion.value.answers++
    }
    newAnswer.value = ''
  }
  </script>
  