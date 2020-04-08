package hello.datasource;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresDatasource {

    // Create the configuration through this bean
    // configuration : data such as username and password.

    @Bean
    @ConfigurationProperties("app.datasource")
    public HikariDataSource hikariDataSource(){

        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
    }
}
