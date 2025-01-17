package com.lantu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;

//@SpringBootApplication
@SpringBootApplication
//@MapperScan("com.lantu.mapper")
public class VueAdminTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueAdminTemplateApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            FileWatcherService watcherService = ctx.getBean(FileWatcherService.class);
            watcherService.setDirectoryPath("C:\\Users\\XuShiPing\\Desktop\\test");
            new Thread(() -> {
                watcherService.start();
            }).start();
        };
    }
}
