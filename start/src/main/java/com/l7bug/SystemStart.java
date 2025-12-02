package com.l7bug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SystemStart
 *
 * @author Administrator
 * @since 2025/11/14 10:08
 */
@SpringBootApplication
public class SystemStart {
	public static void main(String[] args) {
		long currentTimeMillis = System.currentTimeMillis();
		SpringApplication.run(SystemStart.class, args);
		System.err.println(System.currentTimeMillis() - currentTimeMillis + "/ms");
	}
}
