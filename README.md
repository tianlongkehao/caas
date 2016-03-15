#EPM_PAAS_CLOUD

# 任务分工

- 构建 － 杨见
- 服务 － 冯涛
- 镜像 － 尚文
- 登录认证 & 用户管理 － 冯涛
- Docker & DockerFile  － 尚文
- 前端布局  － 徐沛蓝
- 租户
- 集群

### 第一次演示：12月4号 亦庄演示

# 项目运行说明
- 1.获取代码
- 2.修改项目配置(非必须)：修改配置文件
- 3.运行:mvn spring-boot:run -Drun.profiles=dev（开发）或者yq（叶青配置）或者profile（正式）
- 4.默认访问地址：http//:localhost:8082/

# 代码使用说明
- 1.获取代码
- 2.构建项目：mvn eclipse:eclipsec
- 3.编译打包：mvn clean package -P dev（开发）或者yq（叶青配置）或者profile（正式）
