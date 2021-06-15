# springboot-vue-module
使用本仓库的vuebest和spring-boot-api-quick-core进行前端后分离开发，之后会逐步添加开发及部署说明，初衷是以个人为主体进行全栈开发，并逐步添加目前流行的技术，以个人需求及技术学习为驱使


## 技术栈
 springboot+vue+mybatis+redis+nginx+maven+jdk1.8+oracle+dubbo+zookeeper+kafka


## 简略描述
 ### spring-boot-api-quick-core
 主核心服务(主消费者)

 ### vue
 前端代码


### spring-boot-api-dubbo-service
dubbo服务下的服务提供者



### 补充说明
(1)接口测试可以直接用 swagger-ui.html  
(2) CodeGenerator.java生成基础代码



### 启动步骤
(#)https.js: 如果不使用nginx，直接使用RUNTYPE = "DEV"  
(#) vue 下启动前端:先安装 yarn install  再启动  yarn run dev     //前端启动完毕  这里推荐使用yarn(安装npm,使用npm安装yarn 命令：npm install -g yarn)  
(#)启动redis,zookeeper,kafka  
(#)启动 spring-boot-api-quick-core   http://localhost:8085/swagger-ui.html  
(#)启动 spring-boot-api-dubbo-service   (可以不启动，主要是作为dubbo的服务提供者)


### 项目部署到nginx
(1)前端 yarn run build 打出dist文件夹  
(2)
nginx中：
nginx.config的http{}的 server{}里修改端口号为8888

nginx.config的http{}里添加
# 新加的
        location /api/ {
           proxy_pass   http://127.0.0.1:8085/; # 后端接口 IP:port
	         add_header Access-Control-Allow-Origin *;
           add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
           add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
        }  
(3)将打出的dist文件放到nginx的html文件夹下  
(4)重启nginx,访问 http://localhost:8888/  

