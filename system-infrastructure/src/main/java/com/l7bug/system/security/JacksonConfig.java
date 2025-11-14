package com.l7bug.system.security;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * JacksonConfig
 *
 * @author Administrator
 * @since 2025/8/25 11:28
 */
@Configuration
public class JacksonConfig {
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			// 注册 JavaTimeModule 并配置序列化/反序列化器
			JavaTimeModule module = new JavaTimeModule();
			module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
			module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
			module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
			module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
			builder.modules(module, simpleModule);
			builder.timeZone(TimeZone.getTimeZone("GMT+8")); // 设置中国时区
			builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
		};
	}
}
