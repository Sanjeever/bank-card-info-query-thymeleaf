package top.sanjeev.card.service;

import top.sanjeev.card.domain.CardInfo;

/**
 * @author Sanjeev
 * @version 1.0.0
 * @since 2024/10/17 11:05
 */
public interface CardInfoService {

    CardInfo getByCardNo(String cardNo);
}
