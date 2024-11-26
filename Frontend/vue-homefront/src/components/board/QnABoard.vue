<template>
  <div class="fixed inset-0 min-h-screen w-full bg-gradient-to-br from-orange-50/80 via-gray-50/60 to-white backdrop-blur-sm overflow-auto">
    <div class="w-full max-w-7xl mx-auto px-4 md:px-6 py-8 pt-24">
      <!-- 상단 제목과 버튼 영역 -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-4xl text-gray-900 font-bold tracking-tight">🗣️ 부동산 Q&A</h1>
          <p class="mt-2 text-gray-600">부동산 궁금증, 사용자들과 함께 해결하세요!</p>
        </div>
        <div class="flex gap-3">
          <!-- GPT 버튼 -->
          <button
            @click="openGptDialog"
            class="w-36 bg-emerald-500 hover:bg-emerald-600 text-white px-5 py-2.5 rounded-xl transition-all duration-200 flex items-center justify-center shadow-md hover:shadow-lg transform hover:scale-[1.02] active:scale-[0.98]"
          >
            <span class="text-lg mr-1.5">🤖</span>
            <span class="text-base font-medium">용어 도우미</span>
          </button>
          <!-- 기존 질문하기 버튼 -->
          <button
            @click="openNewQuestionDialog"
            class="w-36 bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white px-5 py-2.5 rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 flex items-center justify-center shadow-lg hover:shadow-orange-200 transform hover:scale-[1.02] active:scale-[0.98]"
          >
            <Plus class="text-lg mr-2 h-5 w-5" />
            <span class="text-base font-medium">질문하기</span>
          </button>
        </div>
      </div>

      <!-- 메인 콘텐츠 영역 -->
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- 왼쪽 질문 목록 -->
        <div class="flex-1">
          <!-- 검색 영역 -->
          <div class="mb-6 flex gap-3 bg-white p-4 rounded-2xl shadow-md">
            <select
              v-model="searchType"
              class="p-3 border border-gray-200 text-gray-700 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 bg-gray-50"
            >
              <option value="title_content">제목 + 내용</option>
              <option value="title">제목</option>
              <option value="content">내용</option>
              <option value="nickname">닉네임</option>
            </select>
            <div class="relative flex-1">
              <Search class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 h-5 w-5" />
              <input
                v-model="searchQuery"
                :placeholder="searchType === 'nickname' ? '닉네임을 입력하세요' : '질문 검색하기'"
                type="search"
                class="w-full pl-11 pr-4 py-3 border border-gray-200 text-gray-900 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 bg-gray-50"
                @input="handleSearch"
              />
            </div>
          </div>

          <!-- 질문 목록 -->
          <div class="space-y-4 overflow-y-auto pr-2" style="max-height: calc(100vh - 12rem);">
            <div v-for="question in board.questions"
                 :key="question.articleNo"
                 @click="openQuestionDetailDialog(question.articleNo)"
                 class="group bg-white border border-gray-100 rounded-2xl p-6 shadow-md cursor-pointer">
              <!-- 상단 메타 정보 -->
              <div class="flex items-center gap-3 mb-3">
                <span class="bg-gradient-to-r from-orange-500 to-orange-600 text-white text-xs px-3 py-1 rounded-full font-medium">Q&A</span>
                <span class="text-xs text-gray-500 flex items-center">
                  <Clock class="w-3 h-3 mr-1" />
                  {{ formatDate(question.registerTime) }}
                </span>
              </div>

              <!-- 제목 영역 -->
              <h3 class="text-xl text-gray-900 font-semibold mb-4 group-hover:text-orange-500 transition-colors line-clamp-2">
                {{ question.subject }}
              </h3>

              <!-- 하단 메타 정보 및 버튼 -->
              <div class="flex items-center justify-between mt-4 pt-4 border-t border-gray-100">
                <div class="flex items-center gap-6">
                  <!-- 프로필 이미지와 사용자 정보 -->
                  <div class="flex items-center">
                    <LazyImage
                      :path="question.userProfile"
                      :alt="`${question.userId}의 프로필`"
                      container-class="w-10 h-10"
                      image-class="rounded-full"
                    />
                    <span class="text-sm font-medium text-gray-700 ml-2">{{ question.userId }}</span>
                  </div>
                  <!-- 조회수 -->
                  <div class="flex items-center px-3 py-1 bg-gray-50 rounded-full">
                    <Eye class="w-4 h-4 text-gray-400 mr-1.5" />
                    <span class="text-sm text-gray-600">{{ question.hit }}</span>
                  </div>
                </div>

                <button
                  @click="openQuestionDetailDialog(question.articleNo)"
                  class="text-orange-500 hover:text-orange-600 flex items-center font-medium text-sm bg-orange-50 px-4 py-2 rounded-xl hover:bg-orange-100 transition-all duration-200 group"
                >
                  자세히 보기
                  <ChevronRight class="ml-1 h-4 w-4 group-hover:translate-x-1 transition-transform" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 오른쪽 사이드바 -->
        <div class="lg:w-80">
          <div class="bg-white border border-gray-100 rounded-2xl p-6 shadow-md sticky top-24">
            <h2 class="text-lg text-gray-900 font-semibold mb-6 flex items-center">
              <TrendingUp class="w-5 h-5 mr-2 text-orange-500" />
              인기 질문
            </h2>
            <ul class="space-y-4">
              <li v-for="(question, index) in topQuestions"
                  :key="question.articleNo"
                  class="flex items-start group cursor-pointer"
                  @click="openQuestionDetailDialog(question.articleNo)">
                <span class="flex items-center justify-center w-6 h-6 bg-orange-100 text-orange-500 font-bold rounded-full mr-3 group-hover:bg-orange-500 group-hover:text-white transition-colors">
                  {{ index + 1 }}
                </span>
                <p class="text-sm text-gray-700 group-hover:text-orange-500 transition-colors line-clamp-2 flex-1">
                  {{ question.subject }}
                </p>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- 새 질문 다이얼로그 -->
    <div v-if="showNewQuestionDialog"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl max-w-2xl w-full mx-4 shadow-2xl transform transition-all duration-300"
           @click.stop>
        <!-- 헤더 영역 -->
        <div class="p-6 border-b border-gray-100">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-2xl font-bold bg-gradient-to-r from-orange-500 to-orange-600 bg-clip-text text-transparent">
                새로운 질문
              </h2>
              <p class="text-gray-500 mt-1.5 text-sm">
                부동산에 관한 궁금한 점을 전문가에게 물어보세요
              </p>
            </div>
            <button @click="closeNewQuestionDialog"
                    class="text-gray-400 hover:text-gray-600 p-2 hover:bg-gray-100 rounded-xl transition-all duration-200">
              <XCircle class="h-6 w-6" />
            </button>
          </div>
        </div>

        <!-- 폼 영역 -->
        <form @submit.prevent="handleNewQuestion" class="p-8">
          <div class="space-y-6">
            <!-- 제목 입력 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">제목</label>
              <div class="relative">
                <input
                  v-model="newQuestion.subject"
                  placeholder="질문의 핵심을 간단히 작성해주세요"
                  required
                  class="w-full pl-4 pr-12 py-4 bg-gray-50 text-gray-900 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 placeholder-gray-400"
                />
                <div class="absolute right-4 top-1/2 -translate-y-1/2">
                  <HelpCircle class="h-5 w-5 text-gray-400" />
                </div>
              </div>
            </div>

            <!-- 내용 입력 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">내용</label>
              <div class="relative">
                <textarea
                  v-model="newQuestion.content"
                  placeholder="질문 내용을 자세히 작성해주세요. 구체적인 내용을 포함면 더 정확한 답변을 받을 수 있습니다."
                  required
                  rows="8"
                  class="w-full p-4 bg-gray-50 text-gray-900 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 placeholder-gray-400"
                ></textarea>
                <div class="absolute right-4 top-4">
                  <MessageSquare class="h-5 w-5 text-gray-400" />
                </div>
              </div>
            </div>

            <!-- 작성 가이드 -->
            <div class="bg-white rounded-xl p-5 border border-gray-200 shadow-sm">
              <h4 class="text-base font-semibold text-gray-800 flex items-center mb-3.5">
                <Lightbulb class="h-5 w-5 mr-2 text-orange-500" />
                작성 팁
              </h4>
              <ul class="space-y-3">
                <li class="flex items-start gap-3 group">
                  <div class="flex-shrink-0 w-6 h-6 rounded-full bg-orange-50 flex items-center justify-center">
                    <span class="text-orange-500 text-sm font-medium">1</span>
                  </div>
                  <p class="text-sm text-gray-600 leading-relaxed pt-0.5 group-hover:text-gray-900 transition-colors">
                    구체적인 상황과 조건을 설명해주세요
                  </p>
                </li>
                <li class="flex items-start gap-3 group">
                  <div class="flex-shrink-0 w-6 h-6 rounded-full bg-orange-50 flex items-center justify-center">
                    <span class="text-orange-500 text-sm font-medium">2</span>
                  </div>
                  <p class="text-sm text-gray-600 leading-relaxed pt-0.5 group-hover:text-gray-900 transition-colors">
                    관련된 배경 정보를 포함하면 좋습니다
                  </p>
                </li>
                <li class="flex items-start gap-3 group">
                  <div class="flex-shrink-0 w-6 h-6 rounded-full bg-orange-50 flex items-center justify-center">
                    <span class="text-orange-500 text-sm font-medium">3</span>
                  </div>
                  <p class="text-sm text-gray-600 leading-relaxed pt-0.5 group-hover:text-gray-900 transition-colors">
                    명확한 질문을 통해 원하는 답변을 받으세요
                  </p>
                </li>
              </ul>
            </div>
          </div>

          <!-- 버튼 영역 -->
          <div class="flex justify-end gap-3 mt-8">
            <button
              type="button"
              @click="closeNewQuestionDialog"
              class="px-6 py-3 text-gray-700 hover:bg-gray-100 rounded-xl transition-all duration-200 font-medium"
            >
              취소
            </button>
            <button
              type="submit"
              class="px-6 py-3 bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 shadow-md hover:shadow-lg transform hover:scale-[1.02] active:scale-[0.98] font-medium flex items-center group"
            >
              <PlusCircle class="w-5 h-5 mr-2" />
              질문 등록하기
              <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-200"></div>
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 질문 상세 다이얼로그 -->
    <div v-if="board.selectedQuestion"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl max-w-3xl w-full mx-4 max-h-[80vh] overflow-hidden shadow-2xl flex flex-col"
           @click.stop>
        <!-- 헤더 영역 -->
        <div class="p-6 border-b border-gray-100 flex-shrink-0">
          <!-- 제목 영역 -->
          <div class="flex items-center justify-between mb-4">
            <!-- 수정 모드일 때 -->
            <div v-if="isEditingQuestion" class="w-full">
              <input
                v-model="editQuestionForm.subject"
                type="text"
                class="w-full p-3 text-2xl font-bold bg-white border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500"
                placeholder="제목을 입력하세요"
              />
            </div>
            <!-- 일반 모드일 때 -->
            <template v-else>
              <h2 class="text-2xl font-bold text-gray-900">{{ board.selectedQuestion.subject }}</h2>
              <div class="flex items-center gap-2">
                <!-- 작성자인 경우에만 수정/삭제 버튼 표시 -->
                <div v-if="auth.user?.userId === board.selectedQuestion.userId" class="flex gap-2">
                  <button @click="handleEditQuestion"
                          class="text-gray-600 hover:text-blue-600 p-2 rounded-lg hover:bg-blue-50">
                    <Edit2 class="h-5 w-5" />
                  </button>
                  <button @click="handleDeleteQuestion"
                          class="text-gray-600 hover:text-red-600 p-2 rounded-lg hover:bg-red-50">
                    <Trash2 class="h-5 w-5" />
                  </button>
                </div>
                <button @click="closeQuestionDetailDialog" class="text-gray-400 hover:text-gray-600 p-1">
                  <XCircle class="h-6 w-6" />
                </button>
              </div>
            </template>
          </div>

          <!-- 작성자 정보 및 메타데이터 -->
          <div class="flex items-center justify-between mb-6 pb-4 border-b border-gray-100">
            <div class="flex items-center gap-4">
              <div class="flex items-center">
                <LazyImage
                  :path="board.selectedQuestion.userProfile"
                  :alt="`${board.selectedQuestion.userId}의 프로필`"
                  container-class="w-8 h-8"
                  image-class="rounded-full"
                />
                <span class="ml-2 font-medium text-gray-900">{{ board.selectedQuestion.userId }}</span>
              </div>
              <div class="flex items-center text-gray-500">
                <Clock class="w-4 h-4 mr-1.5" />
                {{ formatDate(board.selectedQuestion.registerTime) }}
              </div>
              <div v-if="board.selectedQuestion.updateTime" class="text-gray-400">
                <span class="text-xs">(수정됨: {{ formatDate(board.selectedQuestion.updateTime) }})</span>
              </div>
            </div>
            <div class="flex items-center gap-2">
              <Eye class="w-4 h-4 text-gray-400" />
              <span class="text-gray-600">{{ board.selectedQuestion.hit }}</span>
            </div>
          </div>

          <!-- 질문 내용 -->
          <div v-if="isEditingQuestion">
            <textarea
              v-model="editQuestionForm.content"
              rows="8"
              class="w-full p-4 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-orange-500"
              placeholder="내용을 입력하세요"
            ></textarea>
            <div class="flex justify-end gap-2 mt-4">
              <button
                @click="saveEditedQuestion"
                class="px-4 py-2 bg-gradient-to-r from-orange-400 to-orange-500 text-white text-sm rounded-xl hover:from-orange-500 hover:to-orange-600 transition-all duration-200"
              >
                저장
              </button>
              <button
                @click="cancelEditQuestion"
                class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
              >
                취소
              </button>
            </div>
          </div>
          <div v-else class="prose prose-orange max-w-none">
            <p class="text-gray-800 whitespace-pre-wrap">{{ board.selectedQuestion.content }}</p>
          </div>
        </div>

        <!-- 답변 섹션 -->
        <div class="overflow-y-auto p-6" style="max-height: 40vh;">
          <!-- 답변 목록 헤더 -->
          <div class="mb-6">
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <h3 class="text-xl font-bold text-gray-900">답변 목록</h3>
                <span class="bg-orange-100 text-orange-600 text-sm px-2.5 py-0.5 rounded-full">
                  {{ board.comments.length }}
                </span>
              </div>
              <div class="text-sm text-gray-500">
                최신순
              </div>
            </div>
          </div>

          <!-- 답변 목록 -->
          <div class="space-y-4">
            <div v-for="comment in board.comments"
                 :key="comment.commentId"
                 class="bg-gray-50 rounded-xl p-5">
              <!-- 댓글 수정 모드 -->
              <div v-if="editingComment?.commentId === comment.commentId" class="space-y-3">
                <textarea
                  v-model="editCommentContent"
                  rows="3"
                  class="w-full p-4 bg-white text-gray-900 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 shadow-sm"
                ></textarea>
                <div class="flex justify-end gap-2">
                  <button
                    @click="saveEditedComment"
                    class="px-4 py-2 bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white text-sm rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 shadow-md hover:shadow-lg transform hover:scale-[1.02] active:scale-[0.98] font-medium relative overflow-hidden group"
                  >
                    <span class="relative z-10">저장</span>
                    <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-200"></div>
                  </button>
                  <button
                    @click="cancelEditComment"
                    class="px-4 py-2 text-gray-600 text-sm hover:bg-gray-100 rounded-xl transition-all duration-200 font-medium"
                  >
                    취소
                  </button>
                </div>
              </div>
              <!-- 댓글 일반 모드 -->
              <template v-else>
                <div class="flex justify-between items-start">
                  <p class="text-gray-700">{{ comment.content }}</p>
                  <div v-if="auth.user?.userId === comment.userId" class="flex gap-1">
                    <button @click="handleEditComment(comment)"
                            class="text-gray-400 hover:text-blue-600 p-1">
                      <Edit2 class="h-4 w-4" />
                    </button>
                    <button @click="handleDeleteComment(comment)"
                            class="text-gray-400 hover:text-red-600 p-1">
                      <Trash2 class="h-4 w-4" />
                    </button>
                  </div>
                </div>
                <!-- 댓글 작성자 정보 및 시간 -->
                <div class="mt-3 flex items-center gap-4 text-sm text-gray-600">
                  <div class="flex items-center">
                    <LazyImage
                      :path="comment.userProfile"
                      :alt="`${comment.userId}의 프로필`"
                      container-class="w-6 h-6"
                      image-class="rounded-full"
                    />
                    <span class="ml-2">{{ comment.userId }}</span>
                  </div>
                  <div class="flex items-center">
                    <Clock class="w-4 h-4 mr-1.5 text-gray-400" />
                    {{ formatDate(comment.registerTime) }}
                  </div>
                  <div v-if="comment.updateTime" class="flex items-center text-gray-500">
                    <span class="text-xs">(수정됨: {{ formatDate(comment.updateTime) }})</span>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>

        <!-- 답변 작성 영역 -->
        <div class="border-t border-gray-100 p-6 bg-gray-50/80 flex-shrink-0">
          <form @submit.prevent="handleNewAnswer" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">답변 작성</label>
              <textarea v-model="newAnswer"
                        placeholder="답변을 작성해주세요"
                        required
                        rows="3"
                        class="w-full p-4 bg-white text-gray-900 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all duration-200 placeholder-gray-400 shadow-sm"></textarea>
            </div>
            <div class="flex justify-end">
              <button type="submit"
                      class="px-6 py-3 bg-gradient-to-r from-orange-400 via-orange-500 to-orange-400 text-white rounded-xl hover:from-orange-500 hover:via-orange-600 hover:to-orange-500 transition-all duration-200 shadow-md hover:shadow-lg transform hover:scale-[1.02] active:scale-[0.98] font-medium flex items-center relative overflow-hidden group"
              >
                <span class="relative z-10 flex items-center">
                  <SendHorizontal class="w-5 h-5 mr-2" />
                  답변 등록
                </span>
                <div class="absolute inset-0 bg-gradient-to-t from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-200"></div>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- GPT 다이얼로그 추가 -->
    <Teleport to="body">
      <RealEstateGPT
        v-if="showGptDialog"
        @close="closeGptDialog"
      />
    </Teleport>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, h, computed } from 'vue'
import { Plus, ChevronRight, UserCircle, Eye, XCircle, PlusCircle, Clock, MessageCircle, SendHorizontal, Edit2, Trash2, Search, TrendingUp, HelpCircle, Lightbulb, MessageSquare } from 'lucide-vue-next'
import { useBoard } from '@/stores/board'
import { useAuth } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { message } from "ant-design-vue";
import LazyImage from '@/components/common/LazyImage.vue'
import RealEstateGPT from '@/components/news/RealEstateGPT.vue'


const board = useBoard()
const auth = useAuth()
const router = useRouter()

const searchQuery = ref('')
const searchType = ref('title_content')
const showNewQuestionDialog = ref(false)
const newQuestion = reactive({ subject: '', content: '' })
const newAnswer = ref('')
const editingComment = ref(null)
const editCommentContent = ref('')
const isEditingQuestion = ref(false)
const editQuestionForm = ref({
  subject: '',
  content: '',
  articleNo: null
})

const topQuestions = computed(() => {
  return [...board.questions]
    .sort((a, b) => b.hit - a.hit)
    .slice(0, 3);
})

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
    board.fetchQuestions(searchType.value, searchQuery.value)
  }
}

const openNewQuestionDialog = () => {
  if (!auth.isLoggedIn) {
    message.info({
      content: '로그인이 필요 서비스입니다.',
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
  isEditingQuestion.value = false
  editQuestionForm.value = {
    subject: '',
    content: '',
    articleNo: null
  }
  editingComment.value = null
  editCommentContent.value = ''
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

watch(() => board.selectedQuestion, (newVal) => {
  if (newVal) {
    console.log('Selected question updated:', newVal)
  }
}, { deep: true })

watch(() => board.comments, (newVal) => {
  console.log('Comments updated:', newVal)
}, { deep: true })

const handleEditQuestion = () => {
  editQuestionForm.value = {
    subject: board.selectedQuestion.subject,
    content: board.selectedQuestion.content,
    articleNo: board.selectedQuestion.articleNo
  }
  isEditingQuestion.value = true
}

const saveEditedQuestion = async () => {
  if (!editQuestionForm.value.subject.trim() || !editQuestionForm.value.content.trim()) {
    message.error('제목과 내용을 모두 입력해주세요.')
    return
  }

  try {
    await board.updateQuestion({
      ...editQuestionForm.value,
      userId: auth.user.userId
    })
    isEditingQuestion.value = false
    message.success('게시글이 수정되었습니다.')
  } catch (error) {
    message.error('게시글 수정에 실패했습니다.')
    console.error('Failed to update question:', error)
  }
}

const cancelEditQuestion = () => {
  isEditingQuestion.value = false
  editQuestionForm.value = {
    subject: '',
    content: '',
    articleNo: null
  }
}

const handleDeleteQuestion = async () => {
  if (confirm('정말 이 게시글을 삭제하시겠습니까?')) {
    try {
      await board.deleteQuestion(board.selectedQuestion.articleNo)
      closeQuestionDetailDialog()
      message.success('게시글이 삭제되었습니다.')
    } catch (error) {
      message.error('게시글 삭제에 실패했습니다.')
    }
  }
}

const handleEditComment = (comment) => {
  editingComment.value = comment
  editCommentContent.value = comment.content
}

const saveEditedComment = async () => {
  if (editingComment.value && editCommentContent.value.trim()) {
    try {
      await board.updateComment({
        commentId: editingComment.value.commentId,
        articleNo: board.selectedQuestion.articleNo,
        content: editCommentContent.value
      })
      await board.fetchComments(board.selectedQuestion.articleNo)
      editingComment.value = null
      editCommentContent.value = ''
      message.success('댓글이 수정되었습니다.')
    } catch (error) {
      message.error('댓글 수정에 실패했습니다.')
    }
  }
}

const cancelEditComment = () => {
  editingComment.value = null
  editCommentContent.value = ''
}

const handleDeleteComment = async (comment) => {
  if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
    try {
      await board.deleteComment(comment.commentId, board.selectedQuestion.articleNo)
      message.success('댓글이 삭제되었습니다.')
    } catch (error) {
      message.error('댓글 삭제에 실패했습니다.')
    }
  }
}

// GPT 다이얼로그 상태 관리
const showGptDialog = ref(false)

const openGptDialog = () => {
  showGptDialog.value = true
}

const closeGptDialog = () => {
  showGptDialog.value = false
}
</script>

<style scoped>
/* 전역 배경 스타일 추가 */
:root {
  background-color: #fff7ed;
}

/* 스크롤바 스타일링 */
.overflow-auto {
  scrollbar-width: thin;
  scrollbar-color: #f97316 #f1f1f1;
}

.overflow-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb {
  background: #f97316;
  border-radius: 10px;
}

.overflow-auto::-webkit-scrollbar-thumb:hover {
  background: #ea580c;
}

/* 기존 애니메이션 스타일 유지 */
.login-button-shine {
  /* ... */
}

@keyframes shine {
  /* ... */
}
</style>

