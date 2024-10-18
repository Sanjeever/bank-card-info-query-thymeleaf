package top.sanjeev.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.sanjeev.card.service.ImgService;

import java.time.Duration;

/**
 * @author SeanRon.Wong
 * @version 1.0.0
 * @since 2024/10/18 10:34
 */
@RequiredArgsConstructor
@Service
public class ImgServiceImpl implements ImgService {

    private final RestTemplate restTemplate;

    /**
     * 构建请求URL
     *
     * @param bank 行代码
     * @return 构造好的请求 URL 字符串
     */
    private static String buildRequestUrl(String bank) {
        return "https://apimg.alipay.com/combo.png?d=cashier&t=" + bank;
    }

    @Override
    @Cacheable(value = "imgs", key = "#bank")
    public ResponseEntity<byte[]> getByBank(String bank) {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(buildRequestUrl(bank), byte[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

}
