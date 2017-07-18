#EPM_PAAS_CLOUD

# 项目运行说明
- 1.获取代码
- 2.修改项目配置(非必须)：修改配置文件
- 3.运行:mvn spring-boot:run -Drun.profiles=dev（开发）或者yq（叶青配置）或者profile（正式）
- 4.默认访问地址：http//:localhost:8082/

# 代码使用说明
- 1.获取代码
- 2.构建项目：mvn eclipse:eclipse
- 3.编译打包：mvn clean package -P dev（开发）或者yq（叶青配置）或者profile（正式）
