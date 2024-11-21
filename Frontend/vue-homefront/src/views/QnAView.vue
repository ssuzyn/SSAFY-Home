<script setup>
import QnABoard from "@/components/board/QnABoard.vue";
</script>

<template>
  <div class="w-full min-h-[calc(100vh-4rem)] flex items-center justify-center px-4 lg:px-72">
    <!-- px-4는 양쪽에 기본 여백, lg:px-8은 큰 화면에서 더 큰 여백 추가 -->
    <QnABoard />
  </div>
</template>
