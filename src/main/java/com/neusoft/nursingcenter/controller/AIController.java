package com.neusoft.nursingcenter.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
//import org.springframework.ai.model.openai.autoconfigure.OpenAIAutoConfigurationUtil;

@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class AIController {
    //	加载客户端聊天模型
    private final ChatModel chatModel;

    public AIController(ChatModel chatModel) {
        super();
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public String chat(String msg) {
        Prompt prompt=new Prompt(msg);
        ChatResponse response = chatModel.call(prompt);
        System.out.println(response.toString());
        String result = response.getResult().getOutput().getText();
        System.out.println("模型响应输出："+result);
        String model = response.getMetadata().getModel();
        System.out.println("模型信息："+model);
        int tokens = response.getMetadata().getUsage().getTotalTokens();
        System.out.println("消耗总token数："+tokens);
        return result;
    }

    @GetMapping(value = "/chatStream",produces = "text/event-stream;charset=utf-8")
    public Flux<String> chatStream(String msg) {

        return chatModel.stream(msg);
    }

}
