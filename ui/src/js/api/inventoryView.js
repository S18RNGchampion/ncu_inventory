import instance from "@/js/AxiosConfig.js";

export async function getInventoryFloorView(floor) {
  const response = await instance.get('/inventory/floor', {
    params: {
      floorNum: floor
    }
  });
  return response.data;
}

export async function getFloors() {
  const response = await instance.get('/inventory/floors')
  return response.data;
}

