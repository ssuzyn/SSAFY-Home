import { localAxios } from "@/util/http-commons";

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

export { getHouseDeals };