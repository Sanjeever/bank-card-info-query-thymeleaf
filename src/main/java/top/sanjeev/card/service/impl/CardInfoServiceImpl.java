package top.sanjeev.card.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.sanjeev.card.domain.CardInfo;
import top.sanjeev.card.service.CardInfoService;

import java.util.concurrent.*;

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

    private final ConcurrentHashMap<String, CompletableFuture<CardInfo>> inFlightRequests = new ConcurrentHashMap<>();

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
        return getCardInfoWithDeduplication(cardNo);
    }

    private CardInfo getCardInfoWithDeduplication(String cardNo) {
        CompletableFuture<CardInfo> future = inFlightRequests.computeIfAbsent(cardNo, this::createCardInfoFuture);

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("获取卡信息失败 [cardNo={}] [ex={}]", cardNo, e.getLocalizedMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
            return null;
        } finally {
            inFlightRequests.remove(cardNo, future);
        }
    }

    private CompletableFuture<CardInfo> createCardInfoFuture(String cardNo) {
        return CompletableFuture.supplyAsync(() -> {
            String requestUrl = buildRequestUrl(cardNo);
            log.info("发起API请求 [cardNo={}] [url={}]", cardNo, requestUrl);
            try {
                return restTemplate.getForObject(requestUrl, CardInfo.class);
            } catch (Exception e) {
                log.warn("API请求失败 [cardNo={}] [ex={}]", cardNo, e.getLocalizedMessage());
                return null;
            }
        });
    }

}
