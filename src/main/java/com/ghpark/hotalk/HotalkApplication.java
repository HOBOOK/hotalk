package com.ghpark.hotalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EntityScan("com.ghpark.hotalk.*")
@EnableMongoRepositories("com.ghpark.hotalk.*")
@SpringBootApplication
public class HotalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotalkApplication.class, args);
    }

}
