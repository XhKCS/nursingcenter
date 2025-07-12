package com.neusoft.nursingcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nursingcenter.entity.NursingProgram;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
public class NursingProgramStructOutputUtil {
    //	Spring AI聊天客户端
    private final ChatClient chatClient;
    //	List转换器，需留意该转换器仅支持List<String>处理
    private final ListOutputConverter listConverter;
    //	实体类转换器
    private final BeanOutputConverter<NursingProgram> converter;
    //	Map类型转换器
    private final MapOutputConverter mapConverter;

    //	Jackson框架
    @Autowired
    private ObjectMapper om;

    // 注入ChatClient的Builder
    public NursingProgramStructOutputUtil(ChatClient.Builder builder) {
//		利用ChatClient的Builder创建ChatClient对象
        this.chatClient = builder.build();
//		完成三种转换器初始化
        this.listConverter = new ListOutputConverter(new DefaultConversionService());
        this.converter = new BeanOutputConverter<>(NursingProgram.class);
        this.mapConverter=new MapOutputConverter();
    }

    public NursingProgram chatObj(String query) throws JsonMappingException, JsonProcessingException {
        Prompt prompt = new Prompt(query);
//			promptUserSpec是用户要求AI提供输出格式的提示词
        String promptUserSpec = """
                format: 根据用户的要求以纯文本输出一个护理项目的 json，包含9个字段：
                id（固定为0）, programCode（编号，不能与已有的重复）, name（名称）, price（单价）, status（默认为1）, executionPeriod（执行周期，例如：每天/三天一次/每周/每月/...）, executionTimes（每个周期的执行次数，数字）, description（描述）, isDeleted（默认为0）；
                请不要包含任何多余的文字——包括 markdown 格式;
                以下是输入和输出示例：
                inputExample: 给我添加一个护理项目：按摩;
                outputExample: {
                     "id": 0,
                     "programCode": "HLXM0006",
                     "name": "按摩",
                     "price": 50.00,
                     "status": 1,
                     "executionPeriod": "每周",
                     "executionTimes": 5,
                     "description": "帮老人按摩，缓解腰部和背部的酸痛",
                     "isDeleted": 0
                };
        """;
//		获得最终答案
        String str = chatClient.prompt(prompt).user(u -> u.text(promptUserSpec)).call().content();
        System.out.println("输出文本：");
        System.out.println(str);
//		 利用BeanOutputConverter完成转换
        NursingProgram nursingProgram = converter.convert(str);
        return nursingProgram;
    }
}
