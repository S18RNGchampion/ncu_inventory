package com.lantu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

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
//            watcherService.setDirectoryPath("C:\\Users\\86182\\Desktop\\newBarcode");
            Executors.newSingleThreadExecutor().execute(watcherService::start);
        };
    }
}
