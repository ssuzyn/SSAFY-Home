<template>
  <div class="relative w-full h-full">
    <div id="map" class="w-full h-full"></div>
    <!-- 카테고리 -->
    <ul id="category" class="absolute top-20 right-4 bg-white rounded-lg shadow-lg overflow-hidden z-10">
      <li
        v-for="cat in categories"
        :key="cat.id"
        :id="cat.id"
        :data-order="cat.order"
        class="text-black flex items-center cursor-pointer hover:bg-gray-100 transition-colors"
        :class="{ 'bg-blue-50': currCategory === cat.id }"
        @click="onClickCategory(cat.id)"
      >
        <div class="w-6 flex items-center justify-center">
          <img :src="cat.iconUrl" alt="Category Icon" class="category-icon">
        </div>
        <span class="text-sm font-medium">{{ cat.name }}</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { useInterestStore } from '@/stores/interest';

const props = defineProps({
  properties: {
    type: Array,
    required: true
  },
  selectedProperty: {
    type: Object,
    default: null
  },
  selectedMarkerInfo: {
    type: Object,
    default: null
  },
  centerPosition: {
    type: Object,
    default: null
  },
  zoomLevel: {
    type: Number,
    default: 15
  }
});

const emit = defineEmits(['select-property']);

let map;
let clusterer;
const markers = ref([]);
const infowindows = ref([]);
let tileLoadedListener = null;

let ps = null; // 장소 검색 객체
const currCategory = ref('');
const placeMarkers = ref([]);

let markerImage;

const categoryImages = {
  'SW8': 'src/assets/images/subway-blue.png',    // 지하철역
  'SC4': 'src/assets/images/school-blue.png',    // 학교
  'BK9': 'src/assets/images/bank-blue.png',      // 은행
  'MT1': 'src/assets/images/mart-blue.png',      // 마트
  'HP8': 'src/assets/images/hospital-blue.png',  // 병원
  'CE7': 'src/assets/images/cafe-blue.png',      // 카페
  'FD6': 'src/assets/images/food-blue.png'       // 음식점
};

const categories = [
  {
    id: 'SW8',
    name: '지하철',
    order: 0,
    iconUrl: categoryImages['SW8']
  },
  {
    id: 'SC4',
    name: '학교',
    order: 1,
    iconUrl: categoryImages['SC4']
  },
  {
    id: 'BK9',
    name: '은행',
    order: 2,
    iconUrl: categoryImages['BK9']
  },
  {
    id: 'MT1',
    name: '마트',
    order: 3,
    iconUrl: categoryImages['MT1']
  },
  {
    id: 'HP8',
    name: '병원',
    order: 4,
    iconUrl: categoryImages['HP8']
  },
  {
    id: 'CE7',
    name: '카페',
    order: 5,
    iconUrl: categoryImages['CE7']
  },
  {
    id: 'FD6',
    name: '음식점',
    order: 6,
    iconUrl: categoryImages['FD6']
  }
];

const interestStore = useInterestStore();

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
    // 기존 인포윈도우 모두 닫기
    infowindows.value.forEach(info => info.close());

    // 선택된 마커의 위치 (살짝 위로 조정)
    const lat = Number(newProperty.latitude);
    const lng = Number(newProperty.longitude);
    const moveLatLon = new window.kakao.maps.LatLng(
      lat + 0.0002, // 위도를 살짝 위로 조정
      lng
    );

    // 이전 이벤트 리스너 제거
    if (tileLoadedListener) {
      kakao.maps.event.removeListener(map, 'tilesloaded', tileLoadedListener);
    }

    // 현재 지도 레벨이 4보다 크면 더 가깝게 조정
    const currentLevel = map.getLevel();
    if (currentLevel > 4) {
      map.setLevel(4, {animate: true});
    }

    // 부드럽게 이동
    map.panTo(moveLatLon);

    // 이동 완료 후 인포윈도우 표시
    tileLoadedListener = function() {
      setTimeout(() => {
        const markerIndex = markers.value.findIndex(marker =>
          marker.propertyData && marker.propertyData.aptSeq === newProperty.aptSeq
        );

        if (markerIndex >= 0) {
          infowindows.value[markerIndex].open(map, markers.value[markerIndex]);
        }
      }, 100);

      kakao.maps.event.removeListener(map, 'tilesloaded', tileLoadedListener);
    };

    kakao.maps.event.addListener(map, 'tilesloaded', tileLoadedListener);
  }
}, { deep: true });



// centerPosition이나 zoomLevel이 변경될 때마다 지도 업데이트
watch([() => props.centerPosition, () => props.zoomLevel], ([newCenter, newZoom]) => {
  if (map.value && newCenter) {
    // 지도 중심과 줌 레벨 변경
    const moveLatLng = new kakao.maps.LatLng(newCenter.lat, newCenter.lng);
    map.value.setCenter(moveLatLng);
    map.value.setLevel(newZoom); // 카카오맵은 레벨이 반대로 동작하므로 변환
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
    level: 3
  };
  map = new window.kakao.maps.Map(container, options);

  // 마커 이미지 초기화를 여기로 이동
  markerImage = new window.kakao.maps.MarkerImage(
    'src/assets/images/marker.png',
    new window.kakao.maps.Size(35, 45),
    { offset: new window.kakao.maps.Point(18, 36) }
  );

  // 클러스터러 초기화
  clusterer = new window.kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 3,
    texts: (size) => `${size}건`
  });

  // Places 서비스 초기화
  ps = new window.kakao.maps.services.Places(map);
};

// 카테고리 관련 함수들 추가
const onClickCategory = (categoryId) => {
  console.log('Clicked category:', categoryId);
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
      } else {
        console.log('카테고리 검색 실패:', status);
      }
    },
    {
      location: map.getCenter(),
      radius: 2000,
      sort: kakao.maps.services.SortBy.DISTANCE
    }
  );
};

const displayPlaces = (places) => {
  // 기존 마커와 인포윈도우 제거
  removePlaceMarkers();

  places.forEach(place => {
    const position = new window.kakao.maps.LatLng(place.y, place.x);

    // DOM 엘리먼트 생성
    const content = document.createElement('div');
    content.innerHTML = `
      <div style="
        width: 32px;
        height: 32px;
        background: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        border: 3px solid #3091FF;
        cursor: pointer;
      ">
        <img
          src="${categoryImages[currCategory.value]}"
          style="width: 18px; height: 18px;"
          alt="Category marker"
        />
      </div>
    `;

    // 커스텀 오버레이 생성
    const marker = new window.kakao.maps.CustomOverlay({
      position: position,
      content: content,
      yAnchor: 1
    });

    // 인포윈도우 생성
    const infoWindow = new window.kakao.maps.InfoWindow({
      position: position,
      content: `
        <div style="
          padding: 12px 16px;
          background: white;
          border-radius: 8px;
          border-bottom: 3px solid #3091FF;
          min-width: 150px;
          box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        ">
          <div style="
            font-size: 15px;
            font-weight: 600;
            color: #3091FF;
            margin-bottom: 6px;
          ">${place.place_name}</div>
          <div style="
            font-size: 12px;
            color: #666;
            margin-bottom: ${place.phone ? '4px' : '0'};
          ">${place.road_address_name || place.address_name}</div>
          ${place.phone ? `
            <div style="
              font-size: 12px;
              color: #888;
            ">${place.phone}</div>
          ` : ''}
        </div>
      `,
      removable: true
    });

    // 클릭 이벤트 처리
    content.onclick = function() {
      // 기존 인포윈도우 모두 닫기
      infowindows.value.forEach(info => info.close());
      infowindows.value = [];

      // 새 인포윈도우 열기
      infoWindow.open(map);
      infowindows.value.push(infoWindow);
    };

    // 마커를 지도에 표시
    marker.setMap(map);

    // 배열에 저장
    placeMarkers.value.push(marker);
  });
};

// 마커 제거 함수 수정
const removePlaceMarkers = () => {
  // 기존 인포윈도우 닫기
  infowindows.value.forEach(info => info.close());
  infowindows.value = [];

  // 기존 마커 제거
  placeMarkers.value.forEach(marker => {
    marker.setMap(null);
  });
  placeMarkers.value = [];
};

const createInfoWindow = (property) => {
  const content = `
    <div style="
      background: white;
      padding: 20px;
      min-width: 280px;
      border-radius: 16px;
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
      border: 1px solid rgba(255, 107, 0, 0.1);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    ">
      <div style="
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 12px;
      ">
        <div style="
          font-size: 18px;
          font-weight: 600;
          color: #1a1a1a;
          line-height: 1.4;
        ">${property.aptNm}</div>
      </div>

      <div style="
        display: flex;
        gap: 6px;
        margin-bottom: 16px;
      ">
        <div style="
          display: inline-flex;
          align-items: center;
          gap: 4px;
          background: #fff5eb;
          padding: 6px 10px;
          border-radius: 8px;
          color: #ff6b00;
          font-size: 13px;
          font-weight: 500;
        ">
          <span style="color: #ff8534;">⬆</span> ${property.floor}층
        </div>
        <div style="
          display: inline-flex;
          align-items: center;
          gap: 4px;
          background: #fff5eb;
          padding: 6px 10px;
          border-radius: 8px;
          color: #ff6b00;
          font-size: 13px;
          font-weight: 500;
        ">
          <span style="color: #ff8534;">◼</span> ${Math.round(property.excluUseAr)}㎡
        </div>
      </div>

      <div style="
        font-size: 24px;
        font-weight: 700;
        color: #ff6b00;
        margin-bottom: 16px;
        letter-spacing: -0.5px;
      ">${interestStore.formatPrice(interestStore.parseAmount(property.dealAmount))}</div>

      <div style="
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 12px;
        border-top: 1px solid #ffe4cc;
      ">
        <div style="
          display: flex;
          align-items: center;
          gap: 8px;
        ">
          <span style="
            color: #666;
            font-size: 13px;
          ">거래일</span>
          <span style="
            color: #1a1a1a;
            font-weight: 500;
            font-size: 14px;
          ">${property.dealYear}.${String(property.dealMonth).padStart(2, '0')}.${String(property.dealDay).padStart(2, '0')}</span>
        </div>
        <div style="
          background: #f8f9fa;
          color: #666;
          padding: 4px 10px;
          border-radius: 6px;
          font-size: 13px;
          display: flex;
          align-items: center;
          gap: 4px;
        ">
          <span style="color: #ff6b00; font-weight: 600;">${property.dealCount}</span>건의 거래
        </div>
      </div>
    </div>
  `;

  return new window.kakao.maps.InfoWindow({
    content: content,
    removable: true
  });
};

const updateMarkers = (properties) => {
  // 기존 인포윈도우 먼저 닫기
  infowindows.value.forEach(info => info.close());
  infowindows.value = [];

  // 클러스터러 초기화
  clusterer.clear();

  // 기존 마커 제거
  markers.value.forEach(marker => {
    marker.setMap(null);
  });
  markers.value = [];

  if (properties.length === 0) return;

  // 마커 생성 및 밀도 계산을 위한 그리드 맵
  const grid = {};
  let maxDensity = 0;
  let densestArea = null;

  // 새 마커 생성 및 밀도 계산
  const markersToAdd = properties.map(property => {
    // 위도, 경도 유효성 검사 강화
    const lat = Number(property.latitude);
    const lng = Number(property.longitude);

    if (isNaN(lat) || isNaN(lng) || !lat || !lng) {
      console.warn('Invalid coordinates for property:', property.aptSeq, property.aptNm);
      return null;
    }

    // 위도, 경도 범위 체크 (대한민국 기준)
    if (lat < 33 || lat > 39 || lng < 124 || lng > 132) {
      console.warn('Coordinates out of Korea range:', property.aptSeq, property.aptNm);
      return null;
    }

    try {
      // 위치를 그리드 좌표로 변환 (0.01 단위로 구역 나누기)
      const gridX = Math.floor(lng * 100);
      const gridY = Math.floor(lat * 100);
      const gridKey = `${gridX},${gridY}`;

      // 그리드 영역의 마�� 수 카운트
      if (!grid[gridKey]) {
        grid[gridKey] = {
          count: 0,
          lat: lat,
          lng: lng
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
      const position = new window.kakao.maps.LatLng(lat, lng);
      const marker = new window.kakao.maps.Marker({
        position: position,
        image: markerImage
      });

      marker.propertyData = { ...property };
      const infowindow = createInfoWindow(property);

      // 마커 클릭 이벤트
      window.kakao.maps.event.addListener(marker, 'click', () => {
        infowindows.value.forEach(info => info.close());
        infowindow.open(map, marker);
        emit('select-property', property);
      });

      // 마커 마우스오버 이벤트 추가
      window.kakao.maps.event.addListener(marker, 'mouseover', () => {
        infowindow.open(map, marker);
      });

      // 마커 마우스아웃 이벤트 추가
      window.kakao.maps.event.addListener(marker, 'mouseout', () => {
        // 현재 선택된 property가 아닌 경우에만 인포윈도우 닫기
        if (!props.selectedProperty || props.selectedProperty.aptSeq !== property.aptSeq) {
          infowindow.close();
        }
      });

      markers.value.push(marker);
      infowindows.value.push(infowindow);

      return marker;

    } catch (error) {
      console.error('Error creating marker for property:', property.aptSeq, error);
      return null;
    }
  }).filter(marker => marker !== null);

  // 유효한 마커만 클러스터러에 추가
  if (markersToAdd.length > 0) {
    try {
      clusterer.addMarkers(markersToAdd);

      if (densestArea) {
        const center = new window.kakao.maps.LatLng(densestArea.lat, densestArea.lng);
        map.setCenter(center);
        map.setLevel(5);
      }
    } catch (error) {
      console.error('Error adding markers to clusterer:', error);
    }
  }
};

</script>

<style scoped>
.category-icon {
  display: inline-block;
  width: 27px;
  height: 28px;
}

.w-6 {
  width: 36px !important;
  height: 36px !important;
  background: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.category_bank {
  background: url(@/assets/images/bank.png) no-repeat center;
  background-size: 24px;
}

.category_mart {
  background: url(@/assets/images/mart.png) no-repeat center;
  background-size: 24px;
}

.category_hospital {
  background: url(@/assets/images/pharmacy.png) no-repeat center;
  background-size: 24px;
}

.category_cafe {
  background: url(@/assets/images/cafe.png) no-repeat center;
  background-size: 24px;
}

.category_store {
  background: url(@/assets/images/store.png) no-repeat center;
  background-size: 24px;
}

#category {
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

#category li {
  border-bottom: 1px solid #eeeeee;
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 12px;
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
