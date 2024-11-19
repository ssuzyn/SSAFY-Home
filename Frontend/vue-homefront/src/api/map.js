// src/api/map.js
import { localAxios } from "@/util/http-commons";

const local = localAxios();

function listSido(success, fail) {
  local.get(`/map/sido`).then(success).catch(fail);
}

function listGugun(param, success, fail) {  // params -> param 수정
  local.get(`/map/gugun?sido=${param.sido}`).then(success).catch(fail);
}

function listDong(param, success, fail) {  // params -> param 수정
  local.get(`/map/dong?gugun=${param.gugun}`).then(success).catch(fail);
}

export { listSido, listGugun, listDong };