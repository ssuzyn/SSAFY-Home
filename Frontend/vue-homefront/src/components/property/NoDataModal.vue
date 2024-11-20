<template>
    <a-modal
      v-model:visible="visible"
      :footer="null"
      :closable="false"
      :width="320"
      class="search-history-modal"
    >
      <div class="modal-content">
        <a-icon-component class="modal-icon">
          <HistoryOutlined />
        </a-icon-component>
        <h2 class="modal-title">검색 내역 없음</h2>
        <p class="modal-description">아직 검색 내역이 없습니다.</p>
      </div>
    </a-modal>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { Modal, Button } from 'ant-design-vue';
  import { HistoryOutlined } from '@ant-design/icons-vue';
  
  const visible = ref(true);
  
  const closeModal = () => {
    visible.value = false;
  };
  </script>
  
  <style scoped>
  .search-history-modal :deep(.ant-modal-content) {
    background-color: #fff;
    border-radius: 16px;
    text-align: center;
    padding: 32px;
  }
  
  .modal-content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .modal-icon {
    font-size: 48px;
    color: #F97316; /* text-orange-500 색상 */
    margin-bottom: 16px;
  }
  
  .modal-title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
  }
  
  .modal-description {
    font-size: 16px;
    color: #666;
    margin-bottom: 24px;
  }
  
  .close-button {
    background-color: #F97316; /* text-orange-500 색상 */
    border-color: #F97316;
    font-weight: bold;
    height: 40px;
    padding: 0 24px;
  }
  
  .close-button:hover,
  .close-button:focus {
    background-color: #EA580C; /* text-orange-600 색상, 더 어두운 버전 */
    border-color: #EA580C;
  }
  
  /* Ant Design Vue의 기본 버튼 스타일 오버라이드 */
  :deep(.ant-btn-primary) {
    background-color: #F97316;
    border-color: #F97316;
  }
  
  :deep(.ant-btn-primary:hover),
  :deep(.ant-btn-primary:focus) {
    background-color: #EA580C;
    border-color: #EA580C;
  }
  </style>