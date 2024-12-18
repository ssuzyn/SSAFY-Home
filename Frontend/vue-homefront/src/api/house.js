import { localAxios } from "@/util/http-commons";
import { useAxiosStore } from '@/stores/axiosStore';


const local = localAxios();

// api/house.js
function getHouseDeals(param, success, fail) {
  console.log('API request params:', param);
  return local.post(`/house/getDeals`, {
    dongCode: param.dongCode || '',
    aptNm: param.aptNm || ''
  })
  .then(response => {
    console.log('API response:', response);
    success(response);
  })
  .catch(error => {
    console.error('API error:', error);
    fail(error);
  });
}

export async function getHouseDetail(aptSeq) {
  try {
    const axiosStore = useAxiosStore();
    const response = await axiosStore.get(`/house/get/${aptSeq}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching house detail:', error);
    throw error;
  }
}


export { getHouseDeals };