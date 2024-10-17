package top.sanjeev.card.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.sanjeev.card.domain.CardInfo;
import top.sanjeev.card.service.CardInfoService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Sanjeev
 * @version 1.0.0
 * @since 2024/10/17 11:07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {

    private final RestTemplate restTemplate;

    /**
     * 构建请求URL
     *
     * @param cardNo 银行卡号
     * @return 构造好的请求 URL 字符串
     */
    private static String buildRequestUrl(String cardNo) {
        // 基于支付宝的 CCDC API 构建请求URL
        return "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?" +
                "_input_charset=utf-8&" + // 指定请求和响应的字符集为UTF-8，确保字符编码的正确处理
                "cardBinCheck=true&" + // 启用卡BIN检查，用于验证卡号前缀是否有效
                "cardNo=" + cardNo; // 卡号
    }

    @Override
    @Cacheable(value = "card-infos", key = "#cardNo")
    public CardInfo getByCardNo(String cardNo) {
        String requestUrl = buildRequestUrl(cardNo);
        CompletableFuture<CardInfo> future = CompletableFuture.supplyAsync(() -> {
            log.info("发起异步请求 URL [{}]", requestUrl);
            try {
                return restTemplate.getForObject(requestUrl, CardInfo.class);
            } catch (Exception e) {
                log.warn("请求远程 API 失败 [{}] [ex={}]", requestUrl, e.getLocalizedMessage());
                return null;
            }
        });

        try {
            // 同步等待异步结果，并设置超时时间为 5 秒
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("获取异步结果失败或超时 [{}] [ex={}]", requestUrl, e.getLocalizedMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
            return null;
        }
    }

}
