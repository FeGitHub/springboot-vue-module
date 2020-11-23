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
(1)https.js: 如果不使用nginx，直接使用RUNTYPE = "DEV"
(2)这里推荐使用yarn(安装npm,使用npm安装yarn 命令：npm install -g yarn)  vue 下启动前端   yarn run dev     //前端启动完毕
(3)启动redis,zookeeper,kafka 
(4)启动 spring-boot-api-quick-core   http://localhost:8085/swagger-ui.html
(5)启动 spring-boot-api-dubbo-service   (可以不启动，主要是作为dubbo的服务提供者)
