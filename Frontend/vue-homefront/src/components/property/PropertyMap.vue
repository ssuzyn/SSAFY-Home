<script setup>
import { onMounted, watch, ref } from "vue";

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

const emit = defineEmits(["selectProperty"]);

const map = ref(null);
const markers = ref([]);
const geocoder = ref(null);

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(37.5665, 126.978),
    level: 7,
  };
  
  map.value = new kakao.maps.Map(container, options);
  geocoder.value = new kakao.maps.services.Geocoder();

  if (props.properties && props.properties.length > 0) {
    addMarkers();
  }
};

const addMarkers = () => {
  clearMarkers();
  if (!props.properties || !map.value) return;

  const bounds = new kakao.maps.LatLngBounds();
  let processedCount = 0;
  const totalProperties = props.properties.length;

  props.properties.forEach((property) => {
    const address = property.roadNmAddr || property.jibunAddr; // 도로명 주소나 지번 주소 사용
    if (!address) return;

    geocoder.value.addressSearch(address, (result, status) => {
      processedCount++;
      
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        
        const marker = new kakao.maps.Marker({
          map: map.value,
          position: coords,
          title: property.aptNm,
          clickable: true,
        });

        bounds.extend(coords);
        markers.value.push(marker);

        kakao.maps.event.addListener(marker, "click", () => {
          emit("selectProperty", property);
        });

        // 모든 마커가 처리되었을 때 지도 범위 조정
        if (processedCount === totalProperties) {
          map.value.setBounds(bounds);
        }
      }
    });
  });
};

const clearMarkers = () => {
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value = [];
};

watch(
  () => props.properties,
  (newVal) => {
    if (newVal && map.value) {
      addMarkers();
    }
  },
  { deep: true }
);

watch(
  () => props.selectedProperty,
  (newVal) => {
    if (newVal && map.value) {
      const address = newVal.roadNmAddr || newVal.jibunAddr;
      if (!address) return;

      geocoder.value.addressSearch(address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          map.value.panTo(coords);
          map.value.setLevel(3);
        }
      });
    }
  }
);

const loadKakaoMapsScript = () => {
  return new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) {
      resolve();
      return;
    }

    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&autoload=false&libraries=services`;
    
    script.onload = () => {
      kakao.maps.load(() => {
        resolve();
      });
    };
    
    script.onerror = (error) => {
      reject(error);
    };

    document.head.appendChild(script);
  });
};

onMounted(async () => {
  try {
    await loadKakaoMapsScript();
    initMap();
  } catch (error) {
    console.error('카카오 맵 로드 실패:', error);
  }
});
</script>

<template>
  <div id="map" class="h-full w-full min-h-[400px]"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 100%;
  min-height: 400px;
}
</style>