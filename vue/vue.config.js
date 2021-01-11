module.exports = {
  //axios域代理，解决axios跨域问题   https://www.cnblogs.com/eye-like/p/13305801.html
  baseUrl: "/",
  devServer: {
    proxy: {
      "": {
        target: "http://localhost:8888",
        changeOrigin: true,
        ws: true,
        pathRewrite: {}
      }
    }
  }
};
