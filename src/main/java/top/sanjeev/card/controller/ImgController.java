package top.sanjeev.card.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sanjeev.card.service.ImgService;

/**
 * @author SeanRon.Wong
 * @version 1.0.0
 * @since 2024/10/18 10:39
 */
@RequestMapping("/img")
@RequiredArgsConstructor
@RestController
public class ImgController {


    private final ImgService imgService;

    @RequestMapping("/{bank}")
    public ResponseEntity<byte[]> getByBank(@PathVariable String bank) {
        return imgService.getByBank(bank);
    }

}
