package top.sanjeev.card.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.sanjeev.card.domain.CardInfo;
import top.sanjeev.card.service.CardInfoService;

@RequestMapping("/card-info")
@Controller
@RequiredArgsConstructor
public class CardInfoController {

    private final IndexController indexController;

    private final CardInfoService cardInfoService;

    @GetMapping
    public String getCardInfo(@RequestParam(name = "cardNo", required = false) String cardNo, Model model) {
        if (!StringUtils.hasText(cardNo)) {
            return indexController.index(model);
        }
        CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);
        if (cardInfo == null) {
            return indexController.index(model);
        }
        model.addAttribute("validated", cardInfo.getValidated());
        model.addAttribute("cardType", cardInfo.getCardType());
        model.addAttribute("bank", cardInfo.getBank());
        model.addAttribute("key", cardInfo.getKey());
        model.addAttribute("messages", cardInfo.getMessages());
        return "index";
    }
}
