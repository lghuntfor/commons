package cn.lghuntfor.commons.webmvc.serialzation;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * Web的序列化统一配置
 * 默认开启, 不需要时可以配置关闭掉, 或者进行覆盖
 * @author liaogang
 * @date 2020/9/1 14:16
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "spring.webmvc.serialization.enable", havingValue = "true", matchIfMissing = true)
public class SerializationConfig {

    private JsonDateFormat dateFormat = new JsonDateFormat();

    /**
     * jackson序列化配置
     */
    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper objectMapper = JsonMapper.builder()
                /** 解析器支持解析单引号 */
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                /** 允许未转义的控制字符 */
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                /** 允许不带引号的字段名 */
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                /** 忽略实体不存在的字段 */
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        /**
         * 使用字段名进行序列化与反序列化
         * springboot默认使用get set方法进行序列化与反序列化
         */
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        /**
         * 将该标记放在属性上，如果该属性为NULL则不参与序列化
         * 如果放在类上边,那对这个类的全部属性起作用
         * Include.Include.ALWAYS 默认
         * Include.NON_DEFAULT 属性为默认值不序列化
         * Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
         * Include.NON_NULL 属性为NULL 不序列化
         */
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        /** 添加各类型序列化与反序列化的转换 */
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance).addSerializer(Long.TYPE, ToStringSerializer.instance)
                .addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
                    @Override
                    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeNumber(value.stripTrailingZeros().toPlainString());
                    }
                })
                .addSerializer(String.class, new JsonSerializer<String>() {
                    @Override
                    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeString(value.trim());
                    }
                })
                .addDeserializer(String.class, new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                        return jsonParser.getText().trim();
                    }
                })
                .addSerializer(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeNumber(new BigDecimal(value.toString()).stripTrailingZeros().toPlainString());
                    }
                })
                .addSerializer(Float.class, new JsonSerializer<Float>() {
                    @Override
                    public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeNumber(new BigDecimal(value.toString()).stripTrailingZeros().toPlainString());
                    }
                })
                .addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeString(LocalDateTimeUtil.formatNormal(value));
                    }
                })
                .addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeString(LocalDateTimeUtil.formatNormal(value));
                    }
                })
                /**此处增加对@RequestBody中的时间参数反序列化*/
                .addDeserializer(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                        return dateFormat.parse(jsonParser.getText());
                    }
                })
        );
        return objectMapper;
    }

    /**
     * 添加parameter入参日期类型转换, 如@RequestParam相关参数
     */
    @Bean
    public Converter<String, Date> stringDateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                Date date = null;
                try {
                    date = dateFormat.parse(source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }


    /**
     * 添加parameter入参字符串类型转换, 如@RequestParam相关参数
     * 去除首尾空格
     */
    @Bean
    public Converter<String, String> stringStringConverter() {
        return new Converter<String, String>() {
            @Override
            public String convert(String source) {
                return source.trim();
            }
        };
    }
}
