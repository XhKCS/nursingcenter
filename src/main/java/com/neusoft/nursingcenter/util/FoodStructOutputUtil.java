package com.neusoft.nursingcenter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nursingcenter.entity.Food;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
public class FoodStructOutputUtil {
    //	Spring AI聊天客户端
    private final ChatClient chatClient;
    //	List转换器，需留意该转换器仅支持List<String>处理
    private final ListOutputConverter listConverter;
    //	实体类转换器
    private final BeanOutputConverter<Food> converter;
    //	Map类型转换器
    private final MapOutputConverter mapConverter;

    //	Jackson框架
    @Autowired
    private ObjectMapper om;

    // 注入ChatClient的Builder
    public FoodStructOutputUtil(ChatClient.Builder builder) {
//		利用ChatClient的Builder创建ChatClient对象
        this.chatClient = builder.build();
//		完成三种转换器初始化
        this.listConverter = new ListOutputConverter(new DefaultConversionService());
        this.converter = new BeanOutputConverter<>(Food.class);
        this.mapConverter=new MapOutputConverter();
    }

    public String chatObj(String query) throws JsonMappingException, JsonProcessingException {
        Prompt prompt = new Prompt(query);
//			promptUserSpec是用户要求AI提供输出格式的提示词
//			本例在输出格式上，给予程序员自定义输出json格式的权利，采用本做法情况下不要在调用param方法了，否则会报异常
//			异常信息为：The template string is not valid
        String promptUserSpec = """
                format: 根据用户的要求以纯文本输出一个食品的 json，包括6个字段：id, name（名称）, type（品类）, description（描述）, price（单价）, imageUrl（图片链接）；
                其中 id 固定为 0；type为字符串数组类型，取值可以是 早餐/午餐/晚餐/甜品 这4种取值；
                imageUrl 请从必应图片上按食品名称获取真实图片的url：https://cn.bing.com/images/search?q={name}，其中{name}是提取出的食品名称；
                请不要包含任何多余的文字——包括 markdown 格式;
                以下是输入和输出示例：
                inputExample:给我加一个食品：小米粥;
                outputExample: {
                     "id": 0,
                     "name": "小米粥",
                     "type": ["早餐"],
                     "description": "软糯小米熬制，富含膳食纤维",
                     "price": 5.00,
                     "imageUrl": "https://tse4-mm.cn.bing.net/th/id/OIP-C.EOnFQ2Vwd4VTaD_ASFTNCAHaEW?w=306&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3"
                };
        """;
//		获得最终答案
        String str = chatClient.prompt(prompt).user(u -> u.text(promptUserSpec)).call().content();
        System.out.println("输出文本：");
        System.out.println(str);
//		 利用BeanOutputConverter完成转换
//        Food food = converter.convert(str);
        return str;
    }
}
