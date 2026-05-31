# GitHub 上传指南

由于系统未安装 Git 命令行工具，请使用以下方式之一将代码上传到 GitHub。

## 方式一：GitHub 网页上传（推荐，最简单）

1. **解压文件**
   - 将 `sulianda-release.zip` 解压

2. **创建 GitHub 仓库**
   - 访问 https://github.com/new
   - 仓库名称: `sulianda`
   - 设为 Public 或 Private
   - **不要**勾选 "Add a README file"
   - 点击 "Create repository"

3. **上传文件**
   - 在仓库页面点击 "uploading an existing file"
   - 将解压后的所有文件拖拽上传
   - 点击 "Commit changes"

## 方式二：安装 Git 后使用命令行

### 安装 Git
- 下载: https://git-scm.com/download/win
- 安装后重新打开 PowerShell

### 上传命令
```powershell
cd G:\temp\sulianda-release
git init
git add .
git commit -m "Initial commit: 速联达物流仓储管理系统"
git remote add origin https://github.com/liuzhen2017/sulianda.git
git branch -M main
git push -u origin main
```

## 方式三：使用 GitHub Desktop

1. 下载安装 GitHub Desktop
2. File > Add local repository > 选择 `G:\temp\sulianda-release`
3. Repository > Repository settings > 设置远程仓库为 `https://github.com/liuzhen2017/sulianda.git`
4. 点击 "Publish repository"

## 仓库结构说明

上传后仓库应包含以下结构：
```
sulianda/
├── .gitignore          # Git 忽略文件配置
├── README.md           # 项目说明文档
├── backend/            # 后端 Java 项目
│   ├── pom.xml
│   ├── src/
│   └── ...
└── frontend/           # 前端 Vue 项目
    ├── package.json
    ├── src/
    └── ...
```

## 验证上传成功

访问 https://github.com/liuzhen2017/sulianda 检查：
- backend/ 目录存在
- frontend/ 目录存在
- README.md 显示正常
