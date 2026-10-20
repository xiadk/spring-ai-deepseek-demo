package com.example.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring AI + DeepSeek 演示应用入口。
 *
 * <p>演示两大实用能力：
 * <ul>
 *   <li><b>流式输出</b>：/{@code chat/stream} 以 SSE 增量返回模型回复。</li>
 *   <li><b>对话记忆</b>：基于 Spring AI 的 ChatMemory（内存实现），按 conversationId 保留上下文。</li>
 * </ul>
 */
@SpringBootApplication
public class SpringAiDeepSeekDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiDeepSeekDemoApplication.class, args);
    }
}
