<template>
  <div class="w-full bg-gray-50 min-h-[calc(100vh-4rem)] pt-16">
    <div class="w-full max-w-7xl mx-auto px-4 md:px-6 py-8">
      <!-- 상단 제목과 버튼 영역 -->
      <div class="flex items-center justify-between mb-8">
        <h1 class="text-3xl text-gray-900 font-bold">부동산 QnA</h1>
        <button
          @click="openNewQuestionDialog"
          class="bg-orange-500 text-white px-6 py-2.5 rounded-lg hover:bg-orange-600 transition-colors duration-200 flex items-center shadow-sm"
        >
          <Plus class="mr-2 h-5 w-5" />
          질문하기
        </button>
      </div>

      <!-- 메인 콘텐츠 영역 -->
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- 왼쪽 질문 목록 -->
        <div class="flex-1">
          <div class="mb-6">
            <input
              v-model="searchQuery"
              placeholder="질문 검색하기"
              type="search"
              class="w-full p-3 border border-gray-200 text-gray-900 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200"
              @input="handleSearch"
            />
          </div>

          <div class="space-y-4">
            <div v-for="question in board.questions"
                 :key="question.articleNo"
                 class="bg-white border border-gray-100 rounded-xl p-6 shadow-sm hover:shadow-md transition-all duration-200 hover:border-orange-100">
              <!-- 상단 메타 정보 -->
              <div class="flex items-center gap-3 mb-3">
                <span class="bg-orange-100 text-orange-600 text-xs px-2.5 py-1 rounded-full font-medium">Q&A</span>
                <span class="text-xs text-gray-500">{{ formatDate(question.registerTime) }}</span>
              </div>

              <!-- 제목 영역 -->
              <h3 class="text-lg text-gray-900 font-semibold mb-3 hover:text-orange-500 transition-colors cursor-pointer">
                {{ question.subject }}
              </h3>

              <!-- 하단 메타 정보 및 버튼 -->
              <div class="flex items-center justify-between mt-4 pt-4 border-t border-gray-100">
                <div class="flex items-center gap-4">
                  <div class="flex items-center">
                    <LazyImage
                      :path="question.userProfile"
                      :alt="`${question.userId}의 프로필`"
                      container-class="w-8 h-8"
                      image-class="rounded-full"
                    />
                    <span class="text-sm text-gray-700 ml-2">{{ question.userId }}</span>
                  </div>
                  <div class="flex items-center">
                    <Eye class="w-4 h-4 text-gray-400 mr-1.5" />
                    <span class="text-sm text-gray-600">{{ question.hit }}</span>
                  </div>
                </div>

                <button
                  @click="openQuestionDetailDialog(question.articleNo)"
                  class="text-orange-500 hover:text-orange-600 flex items-center font-medium text-sm bg-orange-50 px-4 py-2 rounded-lg hover:bg-orange-100 transition-colors"
                >
                  자세히 보기
                  <ChevronRight class="ml-1 h-4 w-4" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 오른쪽 사이드바 -->
        <div class="lg:w-80 bg-white border border-gray-100 rounded-xl p-6 h-fit shadow-sm">
          <h2 class="text-lg text-gray-900 font-semibold mb-4">인기 질문</h2>
          <ul class="space-y-4">
            <li v-for="(question, index) in popularQuestions" :key="index">
              <a class="text-sm hover:text-orange-600 text-gray-700 hover:underline transition-colors duration-200 block" href="#">
                {{ question }}
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 새 질문 다이얼로그 -->
    <div v-if="showNewQuestionDialog"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl max-w-xl w-full mx-4 shadow-2xl"
           @click.stop>
        <!-- 헤더 영역 -->
        <div class="p-6 border-b border-gray-100">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-2xl text-gray-900 font-bold">새 질문 작성</h2>
              <p class="text-gray-500 mt-1">부동산에 관한 질문을 작성해주세요.</p>
            </div>
            <button @click="closeNewQuestionDialog"
                    class="text-gray-400 hover:text-gray-600 p-1">
              <XCircle class="h-6 w-6" />
            </button>
          </div>
        </div>

        <!-- 폼 영역 -->
        <form @submit.prevent="handleNewQuestion" class="p-6">
          <div class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">제목</label>
              <input v-model="newQuestion.subject"
                     placeholder="질문의 제목을 입력해주세요"
                     required
                     class="w-full p-3 bg-gray-50 text-gray-900 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">내용</label>
              <textarea v-model="newQuestion.content"
                        placeholder="질문 내용을 자세히 적어주세요"
                        required
                        rows="5"
                        class="w-full p-3 bg-gray-50 text-gray-900 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
          </div>

          <!-- 버튼 영역 -->
          <div class="flex justify-end gap-3 mt-6">
            <button type="button"
                    @click="closeNewQuestionDialog"
                    class="px-5 py-2.5 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors duration-200">
              취소
            </button>
            <button type="submit"
                    class="px-5 py-2.5 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition-colors duration-200 shadow-sm flex items-center">
              <PlusCircle class="w-5 h-5 mr-2" />
              질문 등록
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 질문 상세 다이얼로그 -->
    <div v-if="board.selectedQuestion"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl max-w-3xl w-full mx-4 max-h-[90vh] shadow-2xl flex flex-col"
           @click.stop>
        <!-- 헤더 영역 -->
        <div class="p-6 border-b border-gray-100">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-2xl font-bold text-gray-900">{{ board.selectedQuestion.subject }}</h2>
            <button @click="closeQuestionDetailDialog"
                    class="text-gray-400 hover:text-gray-600 p-1">
              <XCircle class="h-6 w-6" />
            </button>
          </div>
          <div class="flex items-center gap-4 text-sm text-gray-600">
            <div class="flex items-center">
              <LazyImage
                :path="board.selectedQuestion.userProfile"
                :alt="`${board.selectedQuestion.userId}의 프로필`"
                container-class="w-8 h-8"
                image-class="rounded-full"
              />
              <span class="text-sm ml-2">{{ board.selectedQuestion.userId }}</span>
            </div>
            <div class="flex items-center">
              <Clock class="w-4 h-4 mr-1.5 text-gray-400" />
              {{ formatDate(board.selectedQuestion.registerTime) }}
            </div>
          </div>
        </div>

        <!-- 스크롤 가능한 컨텐츠 영역 -->
        <div class="flex-1 overflow-y-auto p-6">
          <!-- 질문 내용 -->
          <div class="bg-gray-50 rounded-xl p-5 mb-8">
            <h3 class="font-semibold text-gray-900 mb-3">질문 내용</h3>
            <p class="text-gray-700">{{ board.selectedQuestion.content }}</p>
          </div>

          <!-- 답변 목록 -->
          <div>
            <h3 class="font-semibold text-gray-900 mb-4 flex items-center">
              <MessageCircle class="w-5 h-5 mr-2" />
              답변 목록
            </h3>
            <div class="space-y-4">
              <div v-for="comment in board.comments"
                   :key="comment.commentId"
                   class="bg-gray-50 rounded-xl p-5">
                <p class="text-gray-700">{{ comment.content }}</p>
                <div class="mt-3 flex items-center gap-4 text-sm text-gray-600">
                  <div class="flex items-center">
                    <LazyImage
                      :path="comment.userProfile"
                      :alt="`${comment.userId}의 프로필`"
                      container-class="w-6 h-6"
                      image-class="rounded-full"
                    />
                    <span class="text-sm ml-2">{{ comment.userId }}</span>
                  </div>
                  <div class="flex items-center">
                    <Clock class="w-4 h-4 mr-1.5 text-gray-400" />
                    {{ formatDate(comment.registerTime) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 답변 작성 영역 -->
        <div class="border-t border-gray-100 p-6">
          <form @submit.prevent="handleNewAnswer">
            <textarea v-model="newAnswer"
                      placeholder="답변을 작성해주세요"
                      required
                      rows="3"
                      class="w-full p-3 bg-gray-50 text-gray-900 border border-gray-200 rounded-lg mb-4 focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200"></textarea>
            <div class="flex justify-end gap-3">
              <button type="submit"
                      class="px-5 py-2.5 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition-colors duration-200 shadow-sm flex items-center">
                <SendHorizontal class="w-5 h-5 mr-2" />
                답변 등록
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, h } from 'vue'
import { Plus, ChevronRight, UserCircle, Eye, XCircle, PlusCircle, Clock, MessageCircle, SendHorizontal } from 'lucide-vue-next'
import { useBoard } from '@/stores/board'
import { useAuth } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { message } from "ant-design-vue";
import LazyImage from '@/components/common/LazyImage.vue'


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
    message.info({
      content: '로그인이 필요한 서비스입니다.',
      class: 'custom-message',
      duration: 2,
      btn: h(
        'button',
        {
          type: 'button',
          class: 'ml-4 text-orange-500 hover:text-orange-600',
          onClick: () => router.push('/login')
        },
        '로그인하기'
      )
    });
    return;
  }
  showNewQuestionDialog.value = true;
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
    message.info({
      content: '로그인이 필요한 서비스입니다.',
      class: 'custom-message',
      duration: 2,
      btn: h(
        'button',
        {
          type: 'button',
          class: 'ml-4 text-orange-500 hover:text-orange-600',
          onClick: () => router.push('/login')
        },
        '로그인하기'
      )
    });
    return;
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

