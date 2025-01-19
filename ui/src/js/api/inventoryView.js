import instance from "@/js/AxiosConfig.js";

/**
 * 获取书架列表
 * @returns
 */
async function getShelvesList(floorNum) {
  const response = await instance.get('/inventory/getShelvesList',{
    params:{
      floorNum:floorNum
    }
  });
  return response.data;
}

/**
 * 获取当前楼层 某个书架的盘点情况
 * @returns
 */
async function getFloorShelfInventoryStatus(floorNum, shelfNum) {
  const response = await instance.get('/inventory/shelf',{
    params:{
      floorNum:floorNum,
      shelfNum:shelfNum

    }
  });
  return response.data;
}

/**
 * 获取当前楼层 所有书架的盘点情况总览
 * @param floor
 * @returns {Promise<any>}
 */
async function getInventoryFloorView(floor) {
  const response = await instance.get('/inventory/floor', {
    params: {
      floorNum: floor
    }
  });
  return response.data;
}

/**
 * 获取存在所有楼层
 * @returns {Promise<any>}
 */
async function getFloors() {
  const response = await instance.get('/inventory/floors')
  return response.data;
}

/**
 *
 * @returns {Promise<any>}
 */
async function getInventoryBooksByFrame(floorNum, shelfNum, rowNum, colNum) {
  const response = await instance.get('/inventory/bookFrame', {
    params: {
      floorNum: floorNum,
      shelfNum: shelfNum,
      rowNum: rowNum,
      colNum: colNum
    }
  })
  return response.data;
}

/**
 * 获取书本详情
 * @param bookId
 * @returns
 */
async function getBookDetailInfo(bookId) {
  const response = await instance.get('/inventory/bookDetailInfo', {
    params: {
      newBarcodeId: bookId
    }
  })
  return response.data;
}

export {getShelvesList, getFloorShelfInventoryStatus, getInventoryFloorView, getFloors, getInventoryBooksByFrame, getBookDetailInfo}



