package com.sobrinho.java_trello_mcp;

import com.sobrinho.java_trello_mcp.infra.config.BaseContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BaseContext.class)
public class JavaTrelloMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaTrelloMcpApplication.class, args);
	}

}
