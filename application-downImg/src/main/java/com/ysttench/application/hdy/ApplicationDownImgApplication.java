package com.ysttench.application.hdy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.ysttench.application.hdy.mapper")
public class ApplicationDownImgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationDownImgApplication.class, args);
	}

}
