import instance from "@/js/AxiosConfig.js";

/**
 * 获取书架列表
 * @returns
 */
async function getShelvesList(floorNum) {
  const response = await instance.get('/inventory/getShelvesList?floorNum='+floorNum);
  return response.data;
}

/**
 * 获取当前楼层 某个书架的盘点情况
 * @returns
 */
async function getFloorShelfInventoryStatus(floorNum, shelfNum) {
  const response = await instance.get('/inventory/shelf?floorNum='+floorNum+'&shelfNum='+shelfNum);
  return response.data;
}

export {getShelvesList, getFloorShelfInventoryStatus}
