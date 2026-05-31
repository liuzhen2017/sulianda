# 速联达物流仓储管理系统

一套基于 Spring Boot + Vue 的前后端分离物流仓储管理系统。

## 项目结构

```
sulianda/
├── backend/          # 后端 Java 项目 (Spring Boot 2.5 + Java 8)
└── frontend/         # 前端 Vue 项目 (Vue 2 + Element UI)
```

## 技术栈

### 后端技术
- Spring Boot 2.5.x
- Spring Security + JWT
- MyBatis
- MySQL
- Redis

### 前端技术
- Vue 2.x
- Element UI
- Vue CLI 4
- Axios

## 快速开始

### 后端启动

```bash
cd backend
mvn clean package
java -jar target/ruoyi.jar
```

后端默认运行在 `http://localhost:8080`

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端默认运行在 `http://localhost:80`

## 生产部署

### 后端打包部署

```bash
cd backend
mvn -DskipTests clean package
```

生成的 jar 文件位于 `target/ruoyi.jar`

使用以下命令运行：

```bash
java -jar ruoyi.jar
```

### 前端构建部署

```bash
cd frontend
npm run build:prod
```

构建产物位于 `dist/` 目录，可将整个目录部署到 nginx 静态服务器。

## 环境配置

### 前端环境配置

前端项目包含三个环境配置文件：

- `.env.development` - 开发环境
- `.env.staging` - 测试环境
- `.env.production` - 生产环境

主要配置后端 API 地址：

```env
VUE_APP_BASE_API = '/prod-api'
```

### 后端配置

后端配置文件位于 `backend/src/main/resources/application.yml`

主要配置数据库和 Redis 连接信息。

## 项目特性

- 订单管理（FBN/FBA）
- 仓储管理
- 快递渠道管理
- 支付充值管理
- 数据统计报表
- 权限管理系统

## 远程部署说明

### 服务器信息

- 前端域名: `https://www.sulianda.com.cn/`
- 后端 API: `https://www.sulianda.com.cn/prod-api`
- SSH 主机: `114.67.220.40`

### 部署路径

- 后端 jar: `/www/wwwroot/warehouse/sz-sulianda.jar`
- 上传文件根目录: `/www/wwwroot/warehouse/file`
- 前端静态资源: `/www/wwwroot/warehouse/dist`
- 日志文件: `/www/wwwroot/warehouse/logs/sz-sulianda.log`

### 部署步骤

1. **上传后端 jar**
   ```bash
   scp target/ruoyi.jar root@114.67.220.40:/www/wwwroot/warehouse/sz-sulianda.jar
   ```

2. **上传前端 dist**
   ```bash
   scp -r frontend/dist/* root@114.67.220.40:/www/wwwroot/warehouse/dist/
   ```

3. **重启后端服务**
   ```bash
   ssh root@114.67.220.40
   cd /www/wwwroot/warehouse
   ./restart.sh
   ```

## 开发说明

详细的项目说明和架构文档请参考：

- `backend/README.md` - 后端详细说明
- `frontend/README.md` - 前端详细说明

## License

本项目基于 RuoYi-Vue 框架开发，遵循原项目 License。
