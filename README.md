#EPM_PAAS_CLOUD

# 任务分工

- 构建 － 杨见
- 服务 － 冯涛
- 镜像 － 尚文
- 登录认证 & 用户管理 － 李祝平
- Docker & DockerFile  － 尚文
- 前端布局  － 徐沛蓝


### 第一次演示：12月4号 亦庄演示

# 代码使用说明
- 1.获取代码
- 2.构建项目：mvn eclipse:eclipse
- 3.修改项目配置：修改application-dev.properties中对应本地的mysql地址及账号密码
- 4.编译代码：mvn clean compile install
- 5.运行方式:
- 1）部署打包出来的war包，放入web容器启动
- 2）如果使用开发工具，可以直接运行com.bonc.epm.paas.WebAppConfig的main方法
- 3)如果使用开发工具，可以maven运行：mvn spring-boot:run
- 6.默认访问地址:http//:localhost:8082/