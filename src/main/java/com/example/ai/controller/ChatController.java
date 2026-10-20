package com.example.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

/**
 * 聊天接口，演示「流式输出 + 对话记忆」两个实用能力。
 *
 * <p>通过 <code>conversationId</code> 区分不同会话，同一 id 下的多轮对话会带上历史上下文。
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ChatController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    /**
     * 同步对话（带记忆）。
     *
     * <pre>GET /chat/sync?message=你好&conversationId=user-001</pre>
     */
    @GetMapping("/sync")
    public String sync(@RequestParam String message, @RequestParam String conversationId) {
        return chatClient.prompt()
                .user(message)
                // 让记忆 advisor 知道当前属于哪个会话
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    /**
     * 流式对话（带记忆），以 SSE (text/event-stream) 增量返回。
     *
     * <pre>GET /chat/stream?message=写一首诗&conversationId=user-001</pre>
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message, @RequestParam String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    /**
     * 清空指定会话的记忆。
     *
     * <pre>DELETE /chat/memory/user-001</pre>
     */
    @DeleteMapping("/memory/{conversationId}")
    public void clearMemory(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
    }
}
