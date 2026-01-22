package com.l7bug.system.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

/**
 * WorkWxBotApi
 *
 * @author Administrator
 * @since 2026/1/19 13:47
 */
@HttpExchange
public interface WorkWxBotApi {
	@PostExchange("/cgi-bin/webhook/send")
	Map<String, Object> send(@RequestBody Map<String, Object> jsonObject, @RequestParam("key") String key);
}
