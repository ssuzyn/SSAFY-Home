import { defineStore } from "pinia";
import { ref } from "vue";

export const useMapStore = defineStore("map", () => {
  const currCategory = ref("");
  const infowindows = ref([]);
  const apartmentMarkers = ref([]);
  const placeMarkers = ref([]);

  let map = null;
  let clusterer = null;
  let ps = null;
  let markers = [];

  const initializeArrays = () => {
    markers = [];
    placeMarkers.value = [];
    apartmentMarkers.value = [];
  };

  // 초기화 함수 추가
  const setMapInstance = (mapInstance) => {
    map = mapInstance;
    initializeArrays();
  };

  // Places 서비스 인스턴스 설정
  const setPsInstance = (psInstance) => {
    ps = psInstance;
  };

  // 클러스터러 설정
  const setClusterer = (mapInstance) => {
    clusterer = new window.kakao.maps.MarkerClusterer({
      map: mapInstance,
      averageCenter: true,
      minClusterSize: 2,
      gridSize: 60,
    });
  };

  // 카테고리 클릭 처리
  const onClickCategory = (categoryId) => {
    if (currCategory.value === categoryId) {
      currCategory.value = "";
      removePlaceMarkers();
    } else {
      currCategory.value = categoryId;
      searchPlaces(); // 선택한 카테고리에 맞는 장소 검색
    }
  };

  // 장소 검색
  const searchPlaces = () => {
    if (!currCategory.value || !ps || !map) return;

    removePlaceMarkers();

    ps.categorySearch(
      currCategory.value,
      (data, status) => {
        if (status === window.kakao.maps.services.Status.OK) {
          displayPlaces(data); // 검색 결과를 화면에 표시
        } else {
          console.error("카테고리 검색 실패:", status);
        }
      },
      { bounds: map.getBounds(), useMapBounds: true }
    );
  };

  // 장소 마커 삭제
  const removePlaceMarkers = () => {
    placeMarkers.value.forEach((marker) => marker.setMap(null));
    placeMarkers.value = [];
    infowindows.value.forEach((info) => info.close());
  };

  const updateApartmentMarkers = (properties) => {
    // 기존 마커/클러스터 제거
    clusterer.clear();
    apartmentMarkers.value.forEach(marker => marker.setMap(null)); 
    infowindows.value.forEach(info => info.close());
    apartmentMarkers.value = [];
    infowindows.value = [];
   
    if (properties.length === 0) return;
   
    // 밀도 계산용 그리드
    const grid = {};
    let maxDensity = 0;
    let densestArea = null;
   
    // 마커 생성 및 밀도 계산
    properties.forEach(property => {
      if (property.latitude && property.longitude) {
        const gridX = Math.floor(Number(property.longitude) * 100);
        const gridY = Math.floor(Number(property.latitude) * 100);
        const gridKey = `${gridX},${gridY}`;
   
        if (!grid[gridKey]) {
          grid[gridKey] = {
            count: 0,
            lat: Number(property.latitude), 
            lng: Number(property.longitude)
          };
        }
        grid[gridKey].count++;
   
        if (grid[gridKey].count > maxDensity) {
          maxDensity = grid[gridKey].count;
          densestArea = {
            lat: grid[gridKey].lat,
            lng: grid[gridKey].lng
          };
        }
   
        const position = new window.kakao.maps.LatLng(
          Number(property.latitude),
          Number(property.longitude)
        );
   
        const marker = new window.kakao.maps.Marker({
          position: position
        });
   
        const infowindow = createApartmentInfoWindow(property);
   
        window.kakao.maps.event.addListener(marker, 'click', () => {
          infowindows.value.forEach(info => info.close());
          infowindow.open(map, marker);
        });
   
        apartmentMarkers.value.push(marker);
        infowindows.value.push(infowindow);
      }
    });
   
    // 클러스터러에 마커 추가
    clusterer.addMarkers(apartmentMarkers.value);
   
    // 가장 밀집된 영역으로 지도 이동
    if (densestArea) {
      const center = new window.kakao.maps.LatLng(densestArea.lat, densestArea.lng);
      map.setCenter(center);
      map.setLevel(5);
    }
   };

  // 아파트 정보창 생성
  const createApartmentInfoWindow = (property) => {
    const content = `
      <div class="info-window p-2 bg-white rounded-lg shadow-md">
        <h3 class="font-bold">${property.name}</h3>
        <p>${property.address}</p>
        <p>${property.price} 만원</p>
      </div>
    `;

    return new window.kakao.maps.InfoWindow({
      content,
      removable: true,
    });
  };

  // 장소 마커 생성 및 표시
  const displayPlaces = (places) => {
    const categoryOrder = {
      BK9: 0, // 은행
      MT1: 1, // 마트
      PM9: 2, // 약국
      CE7: 4, // 카페
      CS2: 5, // 편의점
    };

    const order = categoryOrder[currCategory.value] || 0;

    places.forEach((place) => {
      const position = new window.kakao.maps.LatLng(place.y, place.x);
      const imageSrc =
        "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png";
      const imageSize = new window.kakao.maps.Size(27, 28);
      const imgOptions = {
        spriteSize: new window.kakao.maps.Size(72, 208),
        spriteOrigin: new window.kakao.maps.Point(46, order * 36),
        offset: new window.kakao.maps.Point(11, 28),
      };

      const markerImage = new window.kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imgOptions
      );

      const marker = new window.kakao.maps.Marker({
        position,
        map,
        image: markerImage,
      });

      window.kakao.maps.event.addListener(marker, "click", () => {
        const content = `
          <div class="placeinfo p-3 bg-white rounded-lg shadow-lg">
            <a href="${
              place.place_url
            }" target="_blank" class="title text-lg font-bold">${
          place.place_name
        }</a>
            <div class="mt-1 text-sm text-gray-600">
              ${place.road_address_name || place.address_name}
            </div>
            ${place.phone ? `<div class="mt-1">${place.phone}</div>` : ""}
          </div>
        `;

        const infoWindow = new window.kakao.maps.InfoWindow({
          content,
          removable: true,
        });

        infowindows.value.forEach((info) => info.close());
        infoWindow.open(map, marker);
        infowindows.value = [infoWindow];
      });

      placeMarkers.value.push(marker);
    });
  };

  return {
    currCategory,
    placeMarkers,
    apartmentMarkers,
    infowindows,
    markers,
    setMapInstance,
    setPsInstance,
    setClusterer,
    updateApartmentMarkers,
    onClickCategory,
    searchPlaces,
  };
});
