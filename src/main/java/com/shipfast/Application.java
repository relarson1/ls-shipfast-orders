package com.shipfast;

import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.shipfast")
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Configuration
	static class LocalConfig {
		public @Bean MongoDbFactory mongoDbFactory() throws Exception {
			return new SimpleMongoDbFactory(new MongoClient(), "inventory");
		}

		public @Bean MongoTemplate mongoTemplate() throws Exception {
			return new MongoTemplate(mongoDbFactory());
		}
	}

	@Configuration
	@Profile("docker")
	static class DockerConfig {
		public @Bean MongoDbFactory mongoDbFactory() throws Exception {
			return new SimpleMongoDbFactory(new MongoClient("inventory-db"), "inventory");
		}

		public @Bean MongoTemplate mongoTemplate() throws Exception {
			return new MongoTemplate(mongoDbFactory());
		}
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

	@Bean
	public InventoryService inventoryService() {
		return new InventoryService();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
