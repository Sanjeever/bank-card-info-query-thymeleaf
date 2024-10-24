package top.sanjeev.card.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/lib/**") // 静态资源路径
            .addResourceLocations("classpath:/static/lib/") // 资源文件夹位置
            .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic()); // 缓存30天
    }

}