package top.sanjeev.card;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import top.sanjeev.card.utils.SpringUtils;

@Slf4j
@EnableCaching
@SpringBootApplication
public class App implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        String port = SpringUtils.getRequiredProperty("server.port");
        log.info("----------------------------------------------");
        log.info("API 地址：{}", "http://localhost:" + port);
        log.info("----------------------------------------------");
    }
}
