<template>
  <div class="w-full bg-white min-h-[calc(100vh-4rem)] pt-16">
    <div class="w-full px-4 md:px-6 py-6">
      <!-- 상단 제목과 버튼 영역 -->
      <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl text-black font-bold">부동산 QnA</h1>
        <button 
          @click="openNewQuestionDialog" 
          class="bg-orange-500 text-white px-4 py-2 rounded-md hover:bg-orange-600 flex items-center"
        >
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
              class="w-full p-2 border text-black rounded-md"
              @input="handleSearch"
            />
          </div>
          
          <div class="space-y-4">
            <div v-for="question in board.questions" :key="question.articleNo" class="bg-white border rounded-lg p-4 shadow-sm">
              <h3 class="text-lg text-black font-semibold">{{ question.subject }}</h3>
              <p class="text-sm text-black mt-1">
                작성자: {{ question.userId }} | 작성일: {{ formatDate(question.registerTime) }}
              </p>
              <div class="mt-3 flex justify-between items-center">
                <div class="flex items-center gap-4">
                  <span class="text-sm text-black">조회수: {{ question.hit }}</span>
                </div>
                <button 
                  @click="openQuestionDetailDialog(question.articleNo)" 
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
            v-model="newQuestion.subject" 
            placeholder="제목" 
            required 
            class="w-full p-2 text-black border rounded-md mb-4"
          />
          <textarea 
            v-model="newQuestion.content" 
            placeholder="질문 내용을 자세히 적어주세요." 
            required 
            class="w-full p-2 text-black border rounded-md mb-4" 
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
    <div v-if="board.selectedQuestion" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg max-w-3xl w-full mx-4 max-h-[90vh] overflow-auto">
        <h2 class="text-xl font-bold mb-2 text-black">{{ board.selectedQuestion.subject }}</h2>
        <p class="text-sm text-black mb-4">
          작성자: {{ board.selectedQuestion.userId }} | 작성일: {{ formatDate(board.selectedQuestion.registerTime) }}
        </p>
        <div class="mb-4">
          <h3 class="font-semibold text-black">질문 내용</h3>
          <p class="mt-2 text-black">{{ board.selectedQuestion.content }}</p>
        </div>
        <hr class="my-4" />
        <div>
          <h3 class="font-semibold mb-2 text-black">답변</h3>
          <div class="space-y-4">
            <div v-for="comment in board.comments" :key="comment.commentId" class="border rounded-md p-4 text-black">
              <p>{{ comment.content }}</p>
              <p class="mt-2 text-sm text-black">
                답변자: {{ comment.userId }} | 작성일: {{ formatDate(comment.registerTime) }}
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
import { ref, reactive, onMounted, watch } from 'vue'
import { Plus, ChevronRight } from 'lucide-vue-next'
import { useBoard } from '@/stores/board'
import { useAuth } from '@/stores/auth'
import { useRouter } from 'vue-router'

const board = useBoard()
const auth = useAuth()
const router = useRouter()

const popularQuestions = [
  "전세 사기 예방하는 방법",
  "부동산 세금 계산하는 법",
  "신축 아파트 vs 구축 아파트",
]

const searchQuery = ref('')
const showNewQuestionDialog = ref(false)
const newQuestion = reactive({ subject: '', content: '' })
const newAnswer = ref('')

onMounted(async () => {
  try {
    await board.fetchAllQuestions()
  } catch (error) {
    console.error('Failed to fetch questions:', error)
  }
})

const handleSearch = () => {
  if (searchQuery.value.trim() === '') {
    board.fetchAllQuestions()
  } else {
    board.fetchQuestions('title_content', searchQuery.value)
  }
}

const openNewQuestionDialog = () => {
  if (!auth.isLoggedIn) {
    if (confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?')) {
      router.push('/login')
    }
    return
  }
  
  showNewQuestionDialog.value = true
}

const closeNewQuestionDialog = () => {
  showNewQuestionDialog.value = false
  newQuestion.subject = ''
  newQuestion.content = ''
}

const handleNewQuestion = async () => {
  await board.createQuestion({
    userId: auth.user.userId,
    subject: newQuestion.subject,
    content: newQuestion.content,
    hit: 0
  })
  closeNewQuestionDialog()
}

const openQuestionDetailDialog = async (articleNo) => {
  if (!auth.isLoggedIn) {
    if (confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?')) {
      router.push('/login')
    }
    return
  }
  
  await board.getQuestionDetails(articleNo)
  await board.fetchComments(articleNo)
}

const closeQuestionDetailDialog = () => {
  board.selectedQuestion = null
  newAnswer.value = ''
}

const handleNewAnswer = async () => {
  if (board.selectedQuestion) {
    await board.createComment({
      articleNo: board.selectedQuestion.articleNo,
      userId: auth.user.userId,
      content: newAnswer.value
    })
    newAnswer.value = ''
    await board.fetchComments(board.selectedQuestion.articleNo)
  }
}

const formatDate = (dateArray) => {
  if (!dateArray || !Array.isArray(dateArray) || dateArray.length !== 6) {
    console.error('Invalid date array:', dateArray);
    return 'Invalid Date';
  }
  const [year, month, day, hour, minute, second] = dateArray;
  const date = new Date(year, month - 1, day, hour, minute, second);
  if (isNaN(date.getTime())) {
    console.error('Invalid date:', dateArray);
    return 'Invalid Date';
  }
  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  });
}

// Watch for changes in selectedQuestion and comments
watch(() => board.selectedQuestion, (newVal) => {
  if (newVal) {
    console.log('Selected question updated:', newVal)
  }
}, { deep: true })

watch(() => board.comments, (newVal) => {
  console.log('Comments updated:', newVal)
}, { deep: true })
</script>