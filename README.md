## 企业快速开发平台

#### 项目简介
基于 spring boot 2.0.4，mybatis，vue，element-ui 的快速开发平台，采用前后端分离的架构，项目集成了日常开发中常用的一些功能；项目支持多终端管理，PC网站，小程序，APP等，目前仅完成后台管理系统部分功能，其余各端功能后期会持续更新；基于此项目，您可以快速构建基础架构，并进行业务功能开发；

#### 项目源码
后台管理：

|        | 后台接口                             | 前端                                            |
| ------ | ------------------------------------ | ----------------------------------------------- |
| Gitee  | https://gitee.com/zbcbbs/quick-work  | https://gitee.com/zbcbbs/quick-work-backend-ui  |
| GitHub | https://github.com/zbcbbs/quick-work | https://github.com/zbcbbs/quick-work-backend-ui |

技术文档：

地址：http://zbcbbs.gitee.io/quick-work-docs

#### 主要功能

- 权限认证 基于RBAC权限模型，支持用户、角色、菜单管理，支持多级菜单和动态路由
- 部门管理 可以指定用户所属部门，进一步细化管理
- 字典数据 统一维护系统中字典数据 例如：账号状态，性别等
- 定时任务 动态创建定时任务，并控制定时任务状态，统一管理后台定义的定时任务
- 邮件工具 提供邮件发送功能，支持html格式的邮件
- 存储管理 文件上传功能，本地存储和腾讯云上传
- 代码生成 动态拉取表格信息，快速生成mybatis标准代码块
- 接口文档 集成了Knife4j在线文档，动态更新项目接口
- 数据库管理 远程连接数据库并上传执行sql脚本
- 系统日志 记录在线用户的一些敏感和关键操作
- SQL监控 开启Druid数据源监控功能，监控sql执行效率
- 登录列表 显示当前在线的用户，可以强制用户下线
- 服务监控 动态监控当前服务器的状态 磁盘，内存，CPU使用率等
- 组件管理 提供Web开发中常用的vue插件和组件

#### 项目结构

```yaml
# maven 聚合项目
- quick-work
--- quick-common 公共通用代码封装
--- quick-generator 代码自动生成
--- quick-logging 日志记录相关工具
--- quick-tools 系统工具，上传，邮件，短信等
--- quick-security 认证授权相关 用户，角色，资源等
--- quick-application 业务应用  最终打包部署的
```

#### 特别鸣谢

- 感谢 [PanJiaChen](https://github.com/PanJiaChen/vue-element-admin) 大佬提供的前端模板
- 感谢 [elunez](https://github.com/elunez/eladmin) 大佬提供的前端后台功能设计思想


#### 反馈交流

问题反馈QQ：2317739191

技术交流群：862939491
