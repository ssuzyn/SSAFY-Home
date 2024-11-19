<template>
  <div class="relative w-full h-full">
    <div id="map" class="w-full h-full"></div>
    <ul id="category" class="absolute top-4 right-4 bg-white rounded-lg shadow-lg overflow-hidden z-10">
      <li
        v-for="cat in categories"
        :key="cat.id"
        class="px-4 py-2 flex items-center cursor-pointer hover:bg-gray-100 transition-colors"
        :class="{ 'bg-blue-50': mapStore.currCategory === cat.id }"
        @click="mapStore.onClickCategory(cat.id)"
      >
        <span class="w-6 h-6 mr-2" :class="cat.iconClass"></span>
        {{ cat.name }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { watch, onMounted } from "vue";
import { useMapStore } from "@/stores/mapStore";

const mapStore = useMapStore();

const props = defineProps({
  properties: {
    type: Array,
    required: true,
  },
  selectedProperty: {
    type: Object,
    default: null,
  },
});


const categories = [
    { id: 'BK9', name: '은행', order: 0, iconClass: 'category_bank' },
    { id: 'MT1', name: '마트', order: 1, iconClass: 'category_mart' },
    { id: 'PM9', name: '약국', order: 2, iconClass: 'category_pharmacy' },
    { id: 'CE7', name: '카페', order: 4, iconClass: 'category_cafe' },
    { id: 'CS2', name: '편의점', order: 5, iconClass: 'category_store' }
  ];

const initializeKakaoMap = () => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`;
    script.onload = () => window.kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }
};

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new window.kakao.maps.LatLng(37.5665, 126.9780),
    level: 5,
  };

  const map = new window.kakao.maps.Map(container, options);
  const clusterer = new window.kakao.maps.MarkerClusterer({
    map,
    averageCenter: true,
    minLevel: 5,
  });
  const ps = new window.kakao.maps.services.Places();

  mapStore.setMapInstance(map);
  mapStore.setClusterer(map);
  mapStore.setPsInstance(ps);
};

onMounted(() => {
  initializeKakaoMap();
  watch(
    () => props.properties,
    (newProperties) => {
      console.log("아파트 데이터 변경:", newProperties);
      mapStore.updateApartmentMarkers(newProperties);
    },
    { deep: true }
  );
});
</script>

<style scoped>
.category-icon {
  display: inline-block;
  width: 27px;
  height: 28px;
}

.category_bank {
  background: url(@/assets/images/bank.png) no-repeat center;
  background-size: contain;
}

.category_mart {
  background: url(@/assets/images/mart.png) no-repeat center;
  background-size: contain;
}

.category_pharmacy {
  background: url(@/assets/images/pharmacy.png) no-repeat center;
  background-size: contain;
}

.category_cafe {
  background: url(@/assets/images/cafe.png) no-repeat center;
  background-size: contain;
}

.category_store {
  background: url(@/assets/images/store.png) no-repeat center;
  background-size: contain;
}
</style>
