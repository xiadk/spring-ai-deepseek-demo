package com.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI 客户端装配。
 *
 * <p>将 {@link MessageChatMemoryAdvisor} 挂到 ChatClient 上，从而使每个请求在调用
 * DeepSeek 前后自动读写会话记忆。记忆的存储由 {@link ChatMemory}（starter 自动装配
 * 提供内存实现）负责；具体会话靠请求时传入的 {@link ChatMemory#CONVERSATION_ID} 区分。
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                // 启用对话记忆（Spring AI 自动装配了内存版 ChatMemory）
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                // 全局系统提示词
                .defaultSystem("你是一名乐于助人的中文智能助手，回答请准确、简洁、友好。")
                .build();
    }
}
