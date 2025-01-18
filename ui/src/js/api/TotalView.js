import instance from "@/js/AxiosConfig.js";


async function getTotalView() {
  const response = await instance.get('/totalView');
  return response.data;
}

export {getTotalView}
