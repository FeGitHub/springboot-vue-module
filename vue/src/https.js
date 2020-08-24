import axios from "axios";
import qs from "qs";
let RUNTYPE = "DEV";//DEV PROD TEST
let baseUrl = RUNTYPE == "DEV" ? "http://localhost:8085" : "http://localhost:8888/api";
axios.defaults.timeout = 5000; //响应时间
axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded;charset=UTF-8"; //配置请求头
axios.defaults.baseURL = baseUrl; //配置接口地址


//POST传参序列化(添加请求拦截器)`
axios.interceptors.request.use(
  config => {
    //在发送请求之前做某件事
    if (config.method === "post") {
      config.data = qs.stringify(config.data);
    }
    if (localStorage.getItem('token')) {
      config.headers.token = localStorage.getItem('token');
    }
    if (RUNTYPE == "DEV") {
      config.headers.token = "a38b4b83d97cac745529ea3dbb587b68";
    }
    return config;
  },
  error => {
    console.log("错误的传参");
    return Promise.reject(error);
  }
);

//返回状态判断(添加响应拦截器)
axios.interceptors.response.use(
  res => {
    //对响应数据做些事
    if (!res.data.success) {
      return Promise.resolve(res);
    }
    return res;
  },
  error => {
    console.log("网络异常");
    return Promise.reject(error);
  }
);

//返回一个Promise(发送post请求)
export function Post (url, params) {
  return new Promise((resolve, reject) => {
    axios
      .post(baseUrl + url, params)
      .then(
        response => {
          resolve(response);
        },
        err => {
          reject(err);
        }
      )
      .catch(error => {
        reject(error);
      });
  });
}
//返回一个Promise(发送get请求)
export function Get (url, param) {
  return new Promise((resolve, reject) => {
    axios
      .get(baseUrl + url, { params: param })
      .then(
        response => {
          resolve(response);
        },
        err => {
          reject(err);
        }
      )
      .catch(error => {
        reject(error);
      });
  });
}
export default {
  Post,
  Get
};
