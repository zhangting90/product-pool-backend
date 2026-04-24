package com.productpool.backend.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Jackson JSON 序列化配置类 配置日期时间格式化规则和 Long 类型序列化为 String */
@Configuration
public class JacksonConfig {

  /**
   * 通过 Jackson2ObjectMapperBuilderCustomizer 定制 Spring Boot 自动配置的 ObjectMapper， 确保 Long 转 String
   * 的配置在 Spring MVC 的消息转换器中生效。
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      // 注册 Java 8 日期时间模块
      builder.modules(new JavaTimeModule());
      // Long 包装类型序列化为 String，避免前端 JavaScript 大数字精度丢失
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      // Long 基本类型序列化为 String
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }
}
