package com.productpool.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Jackson JSON 序列化配置类 配置日期时间格式化规则，统一 LocalDateTime 的序列化方式 */
@Configuration
public class JacksonConfig {

  /** 自定义 ObjectMapper，注册 Java 8 日期时间模块 禁用时间戳格式，使用 ISO-8601 标准格式输出日期时间 */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Long 类型序列化为 String，避免前端 JavaScript 大数字精度丢失
    SimpleModule longModule = new SimpleModule();
    longModule.addSerializer(
        Long.class, new com.fasterxml.jackson.databind.ser.std.ToStringSerializer());
    longModule.addSerializer(
        Long.TYPE, new com.fasterxml.jackson.databind.ser.std.ToStringSerializer());
    longModule.addDeserializer(
        Long.class,
        new com.fasterxml.jackson.databind.deser.std.StdDeserializer<Long>(Long.class) {
          @Override
          public Long deserialize(
              com.fasterxml.jackson.core.JsonParser p,
              com.fasterxml.jackson.databind.DeserializationContext ctxt)
              throws java.io.IOException {
            return p.getValueAsString() != null && !p.getValueAsString().isEmpty()
                ? Long.parseLong(p.getValueAsString())
                : null;
          }
        });
    longModule.addDeserializer(
        Long.TYPE,
        new com.fasterxml.jackson.databind.deser.std.StdDeserializer<Long>(Long.TYPE) {
          @Override
          public Long deserialize(
              com.fasterxml.jackson.core.JsonParser p,
              com.fasterxml.jackson.databind.DeserializationContext ctxt)
              throws java.io.IOException {
            return Long.parseLong(p.getValueAsString());
          }
        });
    mapper.registerModule(longModule);

    return mapper;
  }
}
