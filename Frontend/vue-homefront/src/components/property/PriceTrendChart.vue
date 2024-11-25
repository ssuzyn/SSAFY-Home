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
    difference: formatPriceForChart(Math.abs(difference/10000)),
    percentage: Math.abs(percentage),
    isDecrease: difference < 0
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

const groupDealsByMonth = computed(() => {
  if (!filteredDeals.value.length) return [];
  
  const monthlyDeals = {};
  
  filteredDeals.value.forEach(deal => {
    const monthKey = deal.dealDate.substring(0, 7); // YYYY-MM 형식으로 추출
    if (!monthlyDeals[monthKey]) {
      monthlyDeals[monthKey] = {
        count: 0,
        totalAmount: 0,
        date: monthKey
      };
    }
    monthlyDeals[monthKey].count += 1;
    monthlyDeals[monthKey].totalAmount += deal.dealAmount;
  });

  return Object.values(monthlyDeals).sort((a, b) => 
    a.date.localeCompare(b.date)
  );
});

const chartData = computed(() => {
  if (!filteredDeals.value.length) {
    console.log('No deals available');
    return {
      labels: [],
      datasets: []
    };
  }

  const monthlyData = groupDealsByMonth.value;

  return {
    labels: monthlyData.map(data => 
      data.date.replace(/(\d{4})-(\d{2})/, '$1년 $2월')
    ),
    datasets: [
      {
        type: 'line',
        label: '거래가격',
        data: filteredDeals.value.map(deal => deal.dealAmount / 10000),
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
        yAxisID: 'y1',
        order: 0
      },
      {
        type: 'bar',
        label: '거래량',
        data: monthlyData.map(data => data.count),
        backgroundColor: '#E8F1FF',
        yAxisID: 'y2',
        order: 1,
        barPercentage: 0.5,
        categoryPercentage: 0.7
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
    y1: {
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
    y2: {
      position: 'left',
      beginAtZero: true,
      grid: {
        display: false
      },
      ticks: {
        stepSize: 5,
        callback: (value) => `${value}건`,
        color: '#999',
        font: {
          size: 11
        },
        padding: 10
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
        },
        autoSkip: true,
        maxTicksLimit: 12
      }
    }
  },
  interaction: {
    intersect: false,
    mode: 'index'
  }
};

const latestPrice = computed(() => {
  if (!filteredDeals.value.length) return null;
  return formatPriceForChart(filteredDeals.value[filteredDeals.value.length - 1].dealAmount/10000);
});

const highestPrice = computed(() => {
  if (!filteredDeals.value.length) return null;
  const maxDeal = Math.max(...filteredDeals.value.map(deal => deal.dealAmount));
  return maxDeal;
});

const lowestPrice = computed(() => {
  if (!filteredDeals.value.length) return null;
  const minDeal = Math.min(...filteredDeals.value.map(deal => deal.dealAmount));
  return minDeal;
});



const priceComparison = computed(() => {
  if (!filteredDeals.value.length) return null;
  
  const latest = filteredDeals.value[filteredDeals.value.length - 1].dealAmount;
  const highest = highestPrice.value;
  
  const difference = latest - highest;
  const percentage = ((difference / highest) * 100).toFixed(2);
  
  return {
    difference: formatPriceForChart(Math.abs(difference/10000)),
    percentage: Math.abs(percentage),
    isDecrease: difference < 0
  };
});

const lowpriceComparison = computed(() => {
  if (!filteredDeals.value.length) return null;
  
  const latest = filteredDeals.value[filteredDeals.value.length - 1].dealAmount;
  const lowest = lowestPrice.value;
  
  const difference = lowest - latest;
  const percentage = ((difference / lowest) * 100).toFixed(2);
  
  return {
    difference: formatPriceForChart(Math.abs(difference/10000)),
    percentage: Math.abs(percentage),
    isDecrease: difference < 0
  };
});


</script>

<template>
  <div>
    <!-- 차트 위 정보 표시 영역 -->
    <div class="mb-4 p-4 bg-gray-100 rounded-lg flex justify-between items-center">
      <div class="text-center">
        <div class="text-lg font-bold">최근 실거래가</div>
        <div class="text-2xl font-bold">{{ latestPrice ? latestPrice : '데이터 없음' }}</div>
        <div class="text-sm" v-if="priceDifference">
          직전 거래 대비 
          <span :class="priceDifference.isDecrease ? 'text-blue-500' : 'text-red-500'">
            {{ priceDifference.isDecrease ? '-' : '+' }}{{ priceDifference.difference }}
            ({{ priceDifference.percentage }}%)
          </span>
        </div>
        <div class="text-sm" v-else>비교 데이터 없음</div>
      </div>
      <div class="text-center">
        <div class="text-lg font-bold">최저 실거래가</div>
        <div class="text-2xl font-bold">
          {{ lowestPrice ? formatPriceForChart(lowestPrice/10000) : '데이터 없음' }}
        </div>
        <div class="text-sm" v-if="priceComparison">
          최근가 대비 
          <span :class="lowpriceComparison.isDecrease ? 'text-blue-500' : 'text-red-500'">
            {{ lowpriceComparison.isDecrease ? '-' : '+' }}{{ lowpriceComparison.difference }}
            ({{ lowpriceComparison.percentage }}%)
          </span>
        </div>
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