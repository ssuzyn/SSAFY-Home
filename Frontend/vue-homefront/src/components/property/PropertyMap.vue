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
  map.value = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(37.5665, 126.978), // Seoul coordinates
    level: 7,
  });
  addMarkers();
};

const addMarkers = () => {
  clearMarkers();
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

watch(() => props.properties, addMarkers);

watch(
  () => props.selectedProperty,
  (newVal) => {
    if (newVal) {
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
  return new Promise((resolve) => {
    const script = document.createElement("script");
    script.onload = () => {
      kakao.maps.load(resolve);
    };
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=$${
      import.meta.env.KAKAO_MAP_SERVICE_KEY
    }&autoload=false`;
    document.head.appendChild(script);
  });
};

onMounted(async () => {
  await loadKakaoMapsScript();
  initMap();
});
</script>

<template>
  <div id="map" class="h-full w-full"></div>
</template>
