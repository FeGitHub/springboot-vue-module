import axios from "axios";
import qs from "qs";

//正式环境的话使用nginx(端口：8888)去反向请求后端接口 8085 (项目部署到nginx的html文件夹下)
//let baseUrl =  "http://localhost:8085";  //本地开启环境直接使用chrome的关闭跨越限制 直接请求后端接口 8085
let baseUrl =  "http://192.168.186.129:8888/api";//通过nginx去处理跨越问题，用nginx（启动端口8888）去代理前端和后端
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
  //  if (RUNTYPE == "DEV") {
      //用于测试的token
      config.headers.token = "a38b4b83d97cac745529ea3dbb587b68";
  //  }
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
    console.log(error);
    return Promise.reject("网络异常");
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
