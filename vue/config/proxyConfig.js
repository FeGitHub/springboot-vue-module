module.exports = {
  dev: {
    proxyTable: {
      '/api/': {
        target: 'http://localhost:8085', // 开发环境下要跨域访问的接口域名
        changeOrigin: true,  //是否跨域
        pathRewrite: {
          '^/api/': ''   //表示访问接口时/api/会被重写成''
        }
      },
    },
  }
}
