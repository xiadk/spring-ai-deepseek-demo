# Spring AI + DeepSeek 演示工程

一个实用的 Spring Boot 3 + Spring AI 1.0.0 示例，基于官方 `spring-ai-starter-model-deepseek` 对接
**DeepSeek**，演示两个生产中高频的能力：

- **流式输出**（SSE 增量返回，打字机效果）
- **对话记忆**（基于 Spring AI 的 `ChatMemory`，按 `conversationId` 保留上下文）

## 环境要求

| 依赖 | 版本 |
|------|------|
| JDK | 17+ |
| Maven | 3.6+ |
| DeepSeek API Key | 在 [platform.deepseek.com](https://platform.deepseek.com/api_keys) 申请 |

## 快速开始

```bash
# 1. 设置 API Key（Windows PowerShell / CMD）
set DEEPSEEK_API_KEY=sk-xxxxxx
# 或在项目根目录新建 .env 参考 env.example

# 2. 打包
mvn -q clean package

# 3. 启动（默认端口 8080）
mvn spring-boot:run
```

## 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/chat/sync?message=你好&conversationId=user-001` | 同步对话，带记忆 |
| GET | `/chat/stream?message=写一首诗&conversationId=user-001` | 流式对话（SSE），带记忆 |
| DELETE | `/chat/memory/{conversationId}` | 清空某会话的记忆 |

## 记忆是如何工作的

`AiConfig` 中把 `MessageChatMemoryAdvisor` 挂到 `ChatClient`：

```java
.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
```

每次请求通过 `conversationId`（`ChatMemory.CONVERSATION_ID`）指定会话。advisor 在调用 DeepSeek **之前**
取出该会话的历史消息拼进 prompt，**之后** 自动把这一次的用户消息与模型回复写回记忆。这样同一
`conversationId` 下的多轮对话就能"记住上文"。

> 默认使用进程内内存存储（`InMemoryChatMemoryRepository`），重启即清空。生产环境可替换为
> JDBC / Redis 等持久化实现（`spring-ai-starter-model-chat-memory-*`）。

## 常用 curl 示例（Windows）

```bash
# 同步
curl "http://localhost:8080/chat/sync?message=%E4%BD%A0%E5%A5%BD&conversationId=user-001"

# 流式
curl -N "http://localhost:8080/chat/stream?message=1%2B1%3D%3F&conversationId=user-001"

# 验证记忆：先告诉它你的名字，再问"我叫什么"
curl "http://localhost:8080/chat/sync?message=%E6%88%91%E5%8F%AB%E9%98%BF%E5%B9%B3&conversationId=user-001"
curl "http://localhost:8080/chat/sync?message=%E6%88%91%E5%8F%AB%E4%BB%80%E4%B9%88&conversationId=user-001"
```

## 自定义模型

修改 `application.yml` 中的 `spring.ai.deepseek.chat.options.model`：
- `deepseek-chat`：通用对话（默认）
- `deepseek-reasoner`：深度思考（推理）模型
