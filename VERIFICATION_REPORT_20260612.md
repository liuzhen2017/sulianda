# 速联达线上测试与修复报告

## 范围
本次针对以下问题进行了线上验证、代码修复与部署：

1. 批量导入后继续人工处理/匹配渠道
2. 运费试算页面空白
3. 普通角色被错误路由到管理员订单页
4. 采购图片 URL 导入保留
5. 佳邮 / 云途 / 华羽通渠道分发识别
6. 杰航 / 捷航名称兼容

## 已验证并修复完成

### 1. 采购图片 URL 导入保留
- 结果：已修复并线上验证通过
- 表现：当模板中的 `采购图片地址` 非空时，导入后保留外部 URL，不再改成后台 `/profile/upload/...`

### 2. 批量导入后继续人工处理
- 结果：已验证可行
- 表现：批量导入后订单进入待处理状态，后续仍可在系统内继续人工处理。

### 3. 运费试算页面空白
- 原因：线上菜单路由指向 `express/express/fee`，但前端缺少对应页面模块，导致运行时报：
  - `Cannot find module './express/express/fee'`
- 修复：新增页面
  - `frontend/src/views/express/express/fee.vue`
- 结果：前端已重新构建并部署到服务器。

### 4. 普通角色被路由到管理员订单页
- 原因：生产菜单项 `menuId=1085`（仓库管理 -> 订单）配置成了 `order/order/fbn_admin_index`，普通角色也拿到了这个组件。
- 修复：后端在下发路由时增加非管理员改写逻辑：
  - admin 仍返回 `order/order/fbn_admin_index`
  - non-admin 自动返回 `order/order/fbn_user_index`
- 文件：
  - `backend/src/main/java/com/ruoyi/project/system/service/impl/SysMenuServiceImpl.java`
- 结果：已用真实临时普通角色账号验证通过。

### 5. 杰航 / 捷航名称兼容
- 修复：在物流渠道分发中同时兼容 `杰航` 和 `捷航`
- 文件：
  - `backend/src/main/java/com/ruoyi/project/warehouse/service/impl/BusiExpressServiceImpl.java`

## 已上线但仍未打通的部分

### 6. 佳邮 / 云途 / 华羽通
- 现状：已新增渠道识别与占位实现，系统不再报“未找到物流服务实现”前置识别问题。
- 当前返回：
  - `佳邮物流渠道已识别，但暂未接入实现`
  - `云途物流渠道已识别，但暂未接入实现`
  - `华羽通渠道已识别，但暂未接入实现`
- 说明：分发层已修通，但真实 API 协议、鉴权和下单/试算/面单实现还未接入。

## 仍需生产配置处理

### 三态 / 递四方 / 杰航
- 当前线上仍返回：
  - `未找到物流渠道配置，expressId=1/2/3`
- 说明：主要问题在生产库 `busi_express` 配置缺失，不是代码无法识别。

### 其他渠道
- 线上测试发现：
  - `id=4/5/6` 也仍然报 `未找到物流渠道配置`
- 说明：这些渠道记录同样缺失或不可用。

## 实际部署情况确认

### README 中的说明与线上实际不一致
文档中写的是：
- jar：`/www/wwwroot/warehouse/sz-sulianda.jar`
- 重启：`./restart.sh`

实际线上运行的是：
- jar：`/www/wwwroot/warehouse/ruoyi.jar`
- 启动命令：
  - `/www/server/java/jdk1.8.0_371/bin/java -Xmx1024M -Xms256M -jar /www/wwwroot/warehouse/ruoyi.jar --server.port=8444`
- 目录中没有可用的 `restart.sh`

### 本次实际部署动作
- 前端：重新构建 `frontend/dist` 并覆盖服务器静态目录
- 后端：重新打包 `backend/target/ruoyi.jar` 并替换服务器正在使用的 `ruoyi.jar`
- 通过进程切换确认新版本已生效

## 本次新增 / 修改的关键文件

### 前端
- `frontend/package.json`
  - 增加 `NODE_OPTIONS=--openssl-legacy-provider`，解决本机构建问题
- `frontend/src/views/express/express/fee.vue`
  - 新增运费试算页面

### 后端
- `backend/src/main/java/com/ruoyi/project/warehouse/service/impl/BusiExpressServiceImpl.java`
  - 增加杰航/捷航兼容
  - 增加佳邮/云途/华羽通识别分支
- `backend/src/main/java/com/ruoyi/project/warehouse/service/impl/Express07JiaYouServiceImpl.java`
- `backend/src/main/java/com/ruoyi/project/warehouse/service/impl/Express08YunTuServiceImpl.java`
- `backend/src/main/java/com/ruoyi/project/warehouse/service/impl/Express09HuaYuTongServiceImpl.java`
- `backend/src/main/java/com/ruoyi/project/system/service/impl/SysMenuServiceImpl.java`
  - 非管理员订单页自动路由到 `fbn_user_index`

## 当前结论

### 已完成并验证
- 采购图片 URL 导入保留
- 批量导入后继续人工处理
- 运费试算页面缺失修复并已部署
- 普通角色错误进入管理员订单页修复并已验证
- 杰航 / 捷航兼容已上线

### 已上线但仍需继续开发
- 佳邮 / 云途 / 华羽通真实 API 接入

### 仍需生产配置
- 三态 / 递四方 / 杰航配置
- id=4/5/6 对应渠道配置
