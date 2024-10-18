package top.sanjeev.card.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.sanjeev.card.service.ImgService;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * @author SeanRon.Wong
 * @version 1.0.0
 * @since 2024/10/18 10:34
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ImgServiceImpl implements ImgService {

    private final RestTemplate restTemplate;

    private final ConcurrentHashMap<String, CompletableFuture<ResponseEntity<byte[]>>> inFlightRequests = new ConcurrentHashMap<>();


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
        ResponseEntity<byte[]> response = getByBankWithDeduplication(bank);
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    private ResponseEntity<byte[]> getByBankWithDeduplication(String bank) {
        CompletableFuture<ResponseEntity<byte[]>> future = inFlightRequests.computeIfAbsent(bank, this::createImgFuture);
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("获取行图标失败 [bank={}] [ex={}]", bank, e.getLocalizedMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            inFlightRequests.remove(bank, future);
        }
    }

    private CompletableFuture<ResponseEntity<byte[]>> createImgFuture(String bank) {
        return CompletableFuture.supplyAsync(() -> {
            String requestUrl = buildRequestUrl(bank);
            log.info("发起API请求 [bank={}] [url={}]", bank, requestUrl);
            try {
                return restTemplate.getForEntity(buildRequestUrl(bank), byte[].class);
            } catch (Exception e) {
                log.warn("API请求失败 [bank={}] [ex={}]", bank, e.getLocalizedMessage());
                return null;
            }
        });
    }

}
