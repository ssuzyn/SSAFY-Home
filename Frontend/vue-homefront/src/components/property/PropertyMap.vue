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
  addMarkers();
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

watch(() => props.properties, addMarkers, { deep: true });

watch(
  () => props.selectedProperty,
  (newVal) => {
    if (newVal && map.value) {
      const latlng = new kakao.maps.LatLng(
        newVal.location.lat,
        newVal.location.lng
      );
      map.value.setCenter(latlng);
      map.value.setLevel(3);
    }
  }
);

// Load Kakao Maps API
const loadKakaoMapsScript = () => {
  return new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) {
      resolve();
      return;
    }

    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&autoload=false`;
    
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