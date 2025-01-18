import axios from "axios";
import CustomError from "@/js/error/CustomError";


const instance = axios.create({
  baseURL: "/api",
  timeout: 10000
});

// 请求拦截器（例如添加 token）
instance.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
}, error => Promise.reject(error));

// 响应拦截器（统一处理响应和错误）
instance.interceptors.response.use(
  response => {
    console.log(response)
    // 如果接口有标准的成功标识，例如 `code: 200`
    if (response.data.code === 20000) {
      return response.data;
    } else {
      console.log(response.data.msg)
      return Promise.reject(new CustomError(response.data.code,(response.data.msg || "未知错误")));
    }
  },
  error => {
    console.log(error)
    switch (error.response.status) {
      case 500:
        console.log("服务器故障!")
    }
    //为防止和业务异常冲突，改为501
    return Promise.reject(new CustomError(501,"服务器繁忙 ! 请稍后再试 !"));
  }
);
export default instance;
