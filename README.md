# Text2SQL - 自然语言转SQL查询系统

一个基于 AI 的自然语言转 SQL 查询系统，支持使用中文或英文自然语言描述查询需求，自动生成可执行的 SQL 语句。

## 🌟 功能特性

- **自然语言转SQL**：使用阿里云 DashScope（通义千问）AI 模型，支持中英文自然语言输入
- **多数据库支持**：支持 MySQL、PostgreSQL 等主流关系型数据库
- **可视化界面**：基于 Vue 3 + Element Plus 的现代化 Web 界面
- **数据库管理**：支持动态添加、测试、管理多个数据库连接
- **Schema 浏览器**：可视化查看数据库表结构和字段信息
- **查询历史**：保存查询历史，支持重新执行和历史搜索
- **安全验证**：只允许 SELECT 查询，防止危险操作
- **结果导出**：支持将查询结果导出为 CSV 文件
- **深色模式**：支持亮色/深色主题切换

## 🏗️ 技术栈

### 后端
- **框架**：Spring Boot 3.2.2
- **Java版本**：Java 17
- **AI服务**：Spring AI Alibaba + DashScope（通义千问）
- **数据库连接**：HikariCP 连接池
- **数据库支持**：MySQL 8.0+, PostgreSQL 12+
- **缓存**：Spring Cache

### 前端
- **框架**：Vue 3.4 + TypeScript
- **构建工具**：Vite 5
- **UI库**：Element Plus
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **HTTP客户端**：Axios

## 📋 系统要求

- Java 17+
- Node.js 18+
- MySQL 8.0+ 或 PostgreSQL 12+
- Maven 3.6+

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd test2sql
```

### 2. 配置 DashScope API Key

在 `backend/src/main/resources/application.yml` 中配置：

```yaml
ai:
  alibaba:
    dashscope:
      api-key: your-api-key-here
```

或设置环境变量：

```bash
export DASHSCOPE_API_KEY=your-api-key-here
```

### 3. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

### 5. 访问应用

打开浏览器访问 `http://localhost:5173`

## 📖 使用指南

### 添加数据库连接

1. 点击左侧菜单"数据库管理"
2. 点击"添加连接"按钮
3. 填写连接信息（名称、类型、主机、端口、数据库名、用户名、密码）
4. 点击"测试连接"验证配置
5. 点击"保存"

### 执行自然语言查询

1. 在顶部选择数据库连接
2. 在查询输入框输入自然语言，例如：
   - "查询所有员工信息"
   - "查找年龄大于30岁的员工"
   - "统计各部门的员工数量"
3. 点击"生成SQL"按钮
4. 查看生成的SQL语句（可编辑）
5. 点击"执行查询"按钮
6. 在结果表格中查看查询结果

### 查看表结构

在查询页面右侧的"数据库表结构"面板中：
- 展开表查看字段信息
- 点击字段名可复制到剪贴板
- 使用搜索框快速定位表

## 🔧 配置说明

### 后端配置

主要配置文件：`backend/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/text2sql_app
    username: root
    password: root

ai:
  alibaba:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      model: qwen-plus
      temperature: 0.7

query:
  execution:
    timeout: 30
    max-rows: 10000
```

### 前端配置

主要配置文件：`frontend/.env`

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Text2SQL
```

## 📊 测试数据

系统提供了完整的测试数据集，包括：

- **10个部门**
- **50名员工**
- **8个产品类别**
- **30个产品**
- **25个客户**
- **100个订单**

### 初始化测试数据

1. 创建数据库：
```sql
CREATE DATABASE text2sql_app CHARACTER SET utf8mb4;
```

2. 调用API初始化测试数据：
```bash
curl -X POST http://localhost:8080/api/test-data/initialize
```

### 查看示例查询

参考 `backend/src/main/resources/db/sample-queries.md` 文件

## 🔒 安全特性

- **SQL注入防护**：使用参数化查询
- **危险操作过滤**：只允许 SELECT 查询
- **查询超时控制**：默认30秒超时
- **结果集限制**：默认最多返回10,000行
- **CORS配置**：支持跨域请求
- **输入验证**：所有用户输入都经过验证

## 📝 API文档

### 数据库连接管理

- `GET /api/connections` - 获取所有连接
- `GET /api/connections/{id}` - 获取指定连接
- `POST /api/connections` - 创建新连接
- `PUT /api/connections/{id}` - 更新连接
- `DELETE /api/connections/{id}` - 删除连接
- `POST /api/connections/{id}/test` - 测试连接

### Schema管理

- `GET /api/schema/{connectionId}/tables` - 获取表列表
- `GET /api/schema/{connectionId}/tables/{tableName}` - 获取表结构
- `GET /api/schema/{connectionId}/schemas` - 获取所有表结构
- `DELETE /api/schema/{connectionId}/cache` - 清除缓存

### 自然语言转SQL

- `POST /api/nl2sql/convert` - 转换自然语言为SQL
- `POST /api/nl2sql/explain` - 解释SQL语句
- `GET /api/nl2sql/suggestions` - 获取查询建议

### 查询执行

- `POST /api/query/execute` - 执行SQL查询
- `POST /api/query/execute/export` - 导出查询结果为CSV

### 查询历史

- `GET /api/history` - 获取查询历史
- `GET /api/history/recent` - 获取最近查询
- `GET /api/history/search` - 搜索历史
- `DELETE /api/history` - 清空历史

### 测试数据

- `POST /api/test-data/initialize` - 初始化测试数据
- `POST /api/test-data/reset` - 重置测试数据

## 🐳 Docker部署

### 构建镜像

```bash
# 后端
cd backend
docker build -t text2sql-backend .

# 前端
cd frontend
docker build -t text2sql-frontend .
```

### 使用Docker Compose

```bash
docker-compose up -d
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

MIT License

## 👥 联系方式

如有问题或建议，请提交 Issue。

---

**Made with ❤️ by Text2SQL Team**
