package top.sanjeev.card.service;

import org.springframework.http.ResponseEntity;

/**
 * @author SeanRon.Wong
 * @version 1.0.0
 * @since 2024/10/18 10:34
 */
public interface ImgService {

    ResponseEntity<byte[]> getByBank(String bank);

}
