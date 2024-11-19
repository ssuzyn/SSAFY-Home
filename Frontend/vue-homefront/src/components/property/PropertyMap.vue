<template>
  <div class="relative w-full h-full">
    <div id="map" class="w-full h-full"></div>
    <!-- 카테고리 -->
    <ul id="category" class="absolute top-4 right-4 bg-white rounded-lg shadow-lg overflow-hidden z-10">
      <li 
        v-for="cat in categories" 
        :key="cat.id"
        :id="cat.id"
        :data-order="cat.order"
        class="px-4 py-2 flex items-center cursor-pointer hover:bg-gray-100 transition-colors"
        :class="{ 'bg-blue-50': currCategory === cat.id }"
        @click="onClickCategory(cat.id)"
      >
        <span class="w-6 h-6 mr-2" :class="cat.iconClass"></span>
        {{ cat.name }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";

const props = defineProps({
  properties: {
    type: Array,
    required: true
  },
  selectedProperty: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['select-property']);

let map;
let clusterer;
const markers = ref([]);
const infowindows = ref([]);

let ps = null; // 장소 검색 객체
const currCategory = ref('');
const placeMarkers = ref([]);

const categories = [
  { id: 'BK9', name: '은행', order: 0, iconClass: 'category_bank' },
  { id: 'MT1', name: '마트', order: 1, iconClass: 'category_mart' },
  { id: 'PM9', name: '약국', order: 2, iconClass: 'category_pharmacy' },
  { id: 'CE7', name: '카페', order: 4, iconClass: 'category_cafe' },
  { id: 'CS2', name: '편의점', order: 5, iconClass: 'category_store' }
];


onMounted(() => {
  initializeKakaoMap();
});

// properties가 변경될 때마다 마커 업데이트
watch(() => props.properties, (newProperties) => {
  console.log('Properties changed:', newProperties);
  updateMarkers(newProperties);
}, { deep: true });

// 선택된 property가 변경될 때 지도 중심 이동 및 인포윈도우 표시
watch(() => props.selectedProperty, (newProperty) => {
  if (newProperty && newProperty.latitude && newProperty.longitude) {
    const moveLatLon = new window.kakao.maps.LatLng(newProperty.latitude, newProperty.longitude);
    map.panTo(moveLatLon);
    
    // 기존 인포윈도우 모두 닫기
    infowindows.value.forEach(info => info.close());
    
    // 선택된 마커의 인포윈도우 열기
    const markerIndex = props.properties.findIndex(p => p.aptSeq === newProperty.aptSeq);
    if (markerIndex >= 0 && infowindows.value[markerIndex]) {
      infowindows.value[markerIndex].open(map, markers.value[markerIndex]);
    }
  }
}, { deep: true });

const initializeKakaoMap = () => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&libraries=services,clusterer`;
    script.onload = () => window.kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }
};

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new window.kakao.maps.LatLng(37.5665, 126.9780),
    level: 5
  };
  map = new window.kakao.maps.Map(container, options);
  // map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);    


  // 클러스터러 초기화
  clusterer = new window.kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 5
  });

  // 장소 검색 객체 생성
  ps = new window.kakao.maps.services.Places();
};

// 카테고리 관련 함수들 추가
const onClickCategory = (categoryId) => {
  if (currCategory.value === categoryId) {
    currCategory.value = '';
    removePlaceMarkers();
  } else {
    currCategory.value = categoryId;
    searchPlaces();
  }
};

const searchPlaces = () => {
  if (!currCategory.value) return;

  removePlaceMarkers();

  ps.categorySearch(
    currCategory.value, 
    (data, status) => {
      if (status === window.kakao.maps.services.Status.OK) {
        displayPlaces(data);
      }
    },
    { 
      bounds: map.getBounds(),
      useMapBounds: true
    }
  );
};

const displayPlaces = (places) => {
  const order = categories.find(cat => cat.id === currCategory.value)?.order || 0;

  places.forEach(place => {
    const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png';
    const imageSize = new window.kakao.maps.Size(27, 28);
    const imgOptions = {
      spriteSize: new window.kakao.maps.Size(72, 208),
      spriteOrigin: new window.kakao.maps.Point(46, (order * 36)),
      offset: new window.kakao.maps.Point(11, 28)
    };
    
    const markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
    const position = new window.kakao.maps.LatLng(place.y, place.x);

    const marker = new window.kakao.maps.Marker({
      map: map,
      position: position,
      image: markerImage
    });

    window.kakao.maps.event.addListener(marker, 'click', () => {
      const content = `
        <div class="placeinfo p-3 bg-white rounded-lg shadow-lg">
          <a href="${place.place_url}" target="_blank" class="title text-lg font-bold">${place.place_name}</a>
          <div class="mt-1 text-sm text-gray-600">
            ${place.road_address_name ? 
              `<div>${place.road_address_name}</div>
               <div class="text-gray-400">(지번: ${place.address_name})</div>` :
              `<div>${place.address_name}</div>`
            }
            ${place.phone ? `<div class="mt-1">${place.phone}</div>` : ''}
          </div>
        </div>
      `;

      const infoWindow = new window.kakao.maps.InfoWindow({
        content: content,
        removable: true
      });

      infowindows.value.forEach(info => info.close());
      infoWindow.open(map, marker);
      infowindows.value = [infoWindow];
    });

    placeMarkers.value.push(marker);
  });
};

const removePlaceMarkers = () => {
  placeMarkers.value.forEach(marker => marker.setMap(null));
  placeMarkers.value = [];
  infowindows.value.forEach(info => info.close());
};

const createInfoWindow = (property) => {
 // 같은 위치의 거래들 찾기
 const sameLocationDeals = props.properties.filter(p => 
   p.latitude === property.latitude && 
   p.longitude === property.longitude
 );

 // 최신 거래 찾기
 const latestDeal = sameLocationDeals.reduce((latest, current) => {
   const currentDate = new Date(current.dealYear, current.dealMonth - 1, current.dealDay);
   const latestDate = new Date(latest.dealYear, latest.dealMonth - 1, latest.dealDay);
   return currentDate > latestDate ? current : latest;
 }, sameLocationDeals[0]);

 const content = `
   <div class="bg-white p-2 rounded-lg shadow-lg min-w-[200px]">
     <div class="font-medium text-gray-900">${latestDeal.aptNm}</div>
     <div class="text-sm text-gray-600">
       ${latestDeal.floor}층 | ${Math.round(latestDeal.excluUseAr)}㎡
     </div>
     <div class="text-lg font-bold text-gray-900">
       ${formatPrice(parseAmount(latestDeal.dealAmount))}만
     </div>
     <div class="text-xs text-gray-500">
       ${latestDeal.dealYear}.${String(latestDeal.dealMonth).padStart(2, '0')}.${String(latestDeal.dealDay).padStart(2, '0')}
     </div>
     <div class="text-xs text-gray-500 mt-1">
       총 ${sameLocationDeals.length}건의 거래
     </div>
   </div>
 `;

 return new window.kakao.maps.InfoWindow({
   content: content,
   removable: true
 });
};

const updateMarkers = (properties) => {
  // 기존 마커 제거 로직...
  clusterer.clear();
  markers.value.forEach(marker => marker.setMap(null));
  infowindows.value.forEach(info => info.close());
  markers.value = [];
  infowindows.value = [];

  if (properties.length === 0) return;

  // 마커 생성 및 밀도 계산을 위한 그리드 맵
  const grid = {};
  let maxDensity = 0;
  let densestArea = null;

  // 새 마커 생성 및 밀도 계산
  properties.forEach(property => {
    if (property.latitude && property.longitude) {
      // 위치를 그리드 좌표로 변환 (0.01 단위로 구역 나누기)
      const gridX = Math.floor(Number(property.longitude) * 100);
      const gridY = Math.floor(Number(property.latitude) * 100);
      const gridKey = `${gridX},${gridY}`;

      // 그리드 영역의 마커 수 카운트
      if (!grid[gridKey]) {
        grid[gridKey] = {
          count: 0,
          lat: Number(property.latitude),
          lng: Number(property.longitude)
        };
      }
      grid[gridKey].count++;

      // 가장 밀집된 영역 갱신
      if (grid[gridKey].count > maxDensity) {
        maxDensity = grid[gridKey].count;
        densestArea = {
          lat: grid[gridKey].lat,
          lng: grid[gridKey].lng
        };
      }

      // 마커 생성
      const position = new window.kakao.maps.LatLng(
        Number(property.latitude),
        Number(property.longitude)
      );
      
      const marker = new window.kakao.maps.Marker({
        position: position
      });

      const infowindow = createInfoWindow(property);

      window.kakao.maps.event.addListener(marker, 'click', () => {
        infowindows.value.forEach(info => info.close());
        infowindow.open(map, marker);
        emit('select-property', property);
      });

      markers.value.push(marker);
      infowindows.value.push(infowindow);

      return marker;
    }
  });

  // 클러스터러에 마커 추가
  clusterer.addMarkers(markers.value);

  // 가장 밀집된 영역으로 지도 이동
  if (densestArea) {
    const center = new window.kakao.maps.LatLng(densestArea.lat, densestArea.lng);
    map.setCenter(center);
    map.setLevel(5);
  }
};

const formatPrice = (price) => {
  const billion = Math.floor(price / 100000000);
  const million = Math.floor((price % 100000000) / 10000);
  
  let result = '';
  if (billion > 0) result += `${billion}억 `;
  if (million > 0) result += `${million}`;
  if (billion === 0 && million === 0) result = '0';
  return result.trim();
};

const parseAmount = (amount) => {
  if (!amount) return 0;
  return parseInt(amount.replace(/,/g, '')) * 10000;
};
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

#category {
 background: white;
 border-radius: 0.5rem;
 box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

#category li {
 border-bottom: 1px solid #eeeeee;
}

#category li:last-child {
 border-bottom: none;
}

#category li:hover {
 background: #f8f9fa;
}

#category li.on {
 background: #eef6fd;
}
</style>