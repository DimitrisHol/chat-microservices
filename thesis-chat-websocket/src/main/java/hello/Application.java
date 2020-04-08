package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}


@Configuration
class RestTemplateConfig {

	// Create a bean for the restTemplate to call services

	@Bean
	@LoadBalanced // Instances of 1 service across different ports
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
