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
- 3.编译代码：mvn clean compile install
- 4.运行方式:
- 1）运行打包出来的jar:java -jar 包名
- 2）如果使用开发工具，可以直接运行com.bonc.epm.paas.WebAppConfig的main方法
- 3)如果使用开发工具，可以maven运行：mvn spring-boot:run
- 5.默认访问地址:http//:localhost:8082/