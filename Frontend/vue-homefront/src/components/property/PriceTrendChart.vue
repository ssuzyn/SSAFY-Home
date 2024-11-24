<script setup>
import { ref, computed } from 'vue';
import { Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const props = defineProps({
  deals: {
    type: Array,
    default: () => []
  }
});

const selectedPeriod = ref('전체');

const periods = [
  { label: '3년', value: '3년' },
  { label: '5년', value: '5년' },
  { label: '전체', value: '전체' }
];

const filteredDeals = computed(() => {
  if (!props.deals || props.deals.length === 0) return [];
  
  const sortedDeals = [...props.deals].sort((a, b) => 
    a.dealDate.localeCompare(b.dealDate)
  );

  if (selectedPeriod.value === '전체') return sortedDeals;

  const years = parseInt(selectedPeriod.value);
  const cutoffDate = new Date();
  cutoffDate.setFullYear(cutoffDate.getFullYear() - years);
  
  return sortedDeals.filter(deal => 
    new Date(deal.dealDate) >= cutoffDate
  );
});

const priceDifference = computed(() => {
  if (filteredDeals.value.length < 2) return null;

  const latestDeal = filteredDeals.value[filteredDeals.value.length - 1];
  const previousDeal = filteredDeals.value[filteredDeals.value.length - 2];

  const difference = latestDeal.dealAmount - previousDeal.dealAmount;
  const percentage = ((difference / previousDeal.dealAmount) * 100).toFixed(2);

  return {
    difference,
    percentage
  };
});

const formatPriceForChart = (amount) => {
  // 차트에서는 만원 단위로 받으므로 다시 원 단위로 변환
  const won = amount * 10000;
  
  if (won >= 100000000) {
    const uk = Math.floor(won / 100000000);
    const man = Math.floor((won % 100000000) / 10000);
    if (man > 0) {
      return `${uk}억 ${man}만원`;
    }
    return `${uk}억원`;
  } else {
    return `${amount}만원`;
  }
};


const chartData = computed(() => {
  if (!filteredDeals.value.length) {
    console.log('No deals available');
    return {
      labels: [],
      datasets: []
    };
  }

  console.log('Filtered Deals:', filteredDeals.value);

  return {
    labels: filteredDeals.value.map(deal => 
      deal.dealDate.replace(/(\d{4})-(\d{2})-(\d{2})/, '$1년 $2월')
    ),
    datasets: [
      {
        type: 'line',
        label: '거래가격',
        data: filteredDeals.value.map(deal => deal.dealAmount / 10000), // 억원 단위로 변환
        borderColor: '#4A8CFF',
        backgroundColor: '#4A8CFF',
        borderWidth: 2,
        tension: 0.4,
        pointRadius: 0,
        pointHoverRadius: 6,
        pointBackgroundColor: '#4A8CFF',
        pointHoverBackgroundColor: '#4A8CFF',
        pointBorderColor: '#fff',
        pointHoverBorderColor: '#fff',
        pointBorderWidth: 2,
        pointHoverBorderWidth: 2,
        fill: false,
        yAxisID: 'y1', // 가격을 위한 y축
        order: 0
      },
      {
        type: 'bar',
        label: '거래량',
        data: filteredDeals.value.map(deal => {
          console.log('Volume:', deal.excluUseAr);
          return deal.excluUseAr;
        }), // 거래량 데이터로 변경
        backgroundColor: '#E8F1FF',
        yAxisID: 'y2', // 거래량을 위한 y축
        order: 1,
        barPercentage: 0.8, // 막대 너비 조정
        categoryPercentage: 0.8 // 카테고리 너비 조정
      }
    ]
  };
});

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      mode: 'index',
      intersect: false,
      callbacks: {
        label: (context) => {
          const value = context.parsed.y;
          if (context.dataset.type === 'line') {
            return formatPriceForChart(value);
          }
          return `${value}건`; // 거래량에 대한 툴팁
        }
      },
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      titleColor: '#333',
      bodyColor: '#666',
      borderColor: '#ddd',
      borderWidth: 1,
      padding: 10,
      displayColors: false
    }
  },
  scales: {
    y1: { // 가격을 위한 y축
      position: 'right',
      beginAtZero: true,
      grid: {
        drawBorder: false,
        color: '#f0f0f0'
      },
      ticks: {
        callback: (value) => {
          if (value >= 10000) {
            return `${Math.floor(value / 10000)}억`;
          }
          return `${value}만`;
        },
        color: '#999',
        font: {
          size: 11
        }
      }
    },
    y2: { // 거래량을 위한 y축
      position: 'left',
      beginAtZero: true, // 거래량이 0부터 시작
      grid: {
        drawBorder: false,
        color: '#f0f0f0'
      },
      ticks: {
        stepSize: 1, // 자연수로 표시
        color: '#999',
        font: {
          size: 11
        }
      }
    },
    x: {
      grid: {
        display: false
      },
      ticks: {
        maxRotation: 0,
        color: '#999',
        font: {
          size: 11
        }
      }
    }
  },
  interaction: {
    intersect: false,
    mode: 'index'
  }
};
</script>

<template>
  <div>
    <!-- 차트 위 정보 표시 영역 -->
    <div class="mb-4 p-4 bg-gray-100 rounded-lg flex justify-between items-center">
      <div class="text-center">
        <div class="text-lg font-bold">최근 실거래가</div>
        <div class="text-2xl font-bold">24억 3,000</div>
        <div class="text-sm">최고 대비 -1억 9,000만 (7.25%)</div>
      </div>
      <div class="text-center">
        <div class="text-lg font-bold">매물 최저가</div>
        <div class="text-2xl font-bold text-red-500">22억 9,000</div>
        <div class="text-sm text-red-500">4주 전 대비 1,000만</div>
      </div>
    </div>

    <!-- 차트 헤더 -->
    <div class="flex items-center justify-between mb-6">
      <h4 class="text-lg font-bold">실거래가 추이</h4>
      <div class="inline-flex rounded-lg bg-gray-100 p-1">
        <button
          v-for="period in periods"
          :key="period.value"
          :class="[
            'px-4 py-1 text-sm rounded-md transition-all',
            selectedPeriod === period.value
              ? 'bg-white text-blue-600 shadow'
              : 'text-gray-600 hover:text-gray-900'
          ]"
          @click="selectedPeriod = period.value"
        >
          {{ period.label }}
        </button>
      </div>
    </div>
    
    <!-- 차트 영역 -->
    <div class="h-64">
      <template v-if="!filteredDeals.length">
        <div class="h-full flex items-center justify-center text-gray-500 bg-gray-50 rounded-lg">
          거래 내역이 없습니다
        </div>
      </template>
      <template v-else>
        <Line
          :data="chartData"
          :options="chartOptions"
          class="h-full"
        />
      </template>
    </div>
  </div>
</template>

<style scoped>
.relative {
  position: relative;
}
.absolute {
  position: absolute;
}
</style>