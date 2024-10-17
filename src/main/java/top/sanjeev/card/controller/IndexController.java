package top.sanjeev.card.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sanjeev
 * @version 1.0.0
 * @since 2024/10/16 17:03
 */
@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("index", true);
        return "index";
    }

}
