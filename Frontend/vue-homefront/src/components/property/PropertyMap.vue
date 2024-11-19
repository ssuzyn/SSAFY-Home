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

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(37.5665, 126.978),
    level: 7,
  };
  
  map.value = new kakao.maps.Map(container, options);
  
  // 지도가 생성된 후에 마커를 추가
  if (props.properties && props.properties.length > 0) {
    addMarkers();
    
    // 초기 바운드 설정
    const bounds = new kakao.maps.LatLngBounds();
    props.properties.forEach((property) => {
      bounds.extend(new kakao.maps.LatLng(
        property.location.lat,
        property.location.lng
      ));
    });
    map.value.setBounds(bounds);
  }
};

const addMarkers = () => {
  clearMarkers();
  if (!props.properties || !map.value) return;

  props.properties.forEach((property) => {
    const latlng = new kakao.maps.LatLng(
      property.location.lat,
      property.location.lng
    );
    const marker = new kakao.maps.Marker({
      map: map.value,
      position: latlng,
      title: property.name,
      clickable: true, // 클릭 이벤트 활성화
    });

    markers.value.push(marker);

    kakao.maps.event.addListener(marker, "click", () => {
      emit("selectProperty", property);
    });
  });
};

const clearMarkers = () => {
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value = [];
};

// properties가 변경될 때마다 마커 다시 생성
watch(
  () => props.properties,
  (newVal) => {
    if (newVal && map.value) {
      addMarkers();
      
      // 모든 마커가 보이도록 바운드 조정
      if (newVal.length > 0) {
        const bounds = new kakao.maps.LatLngBounds();
        newVal.forEach((property) => {
          bounds.extend(new kakao.maps.LatLng(
            property.location.lat,
            property.location.lng
          ));
        });
        map.value.setBounds(bounds);
      }
    }
  },
  { deep: true }
);

// 선택된 속성이 변경될 때 지도 중심 이동
watch(
  () => props.selectedProperty,
  (newVal) => {
    if (newVal && map.value) {
      const latlng = new kakao.maps.LatLng(
        newVal.location.lat,
        newVal.location.lng
      );
      map.value.panTo(latlng); // setCenter 대신 panTo 사용하여 부드러운 이동
      map.value.setLevel(3);
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
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&autoload=false&libraries=services,clusterer`; // libraries 추가
    
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