package dimitrisHol.eurekaclientzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy        // Enable Zuul
public class EurekaClientZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientZuulApplication.class, args);
	}

}
