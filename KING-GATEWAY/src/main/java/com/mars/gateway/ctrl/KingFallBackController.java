package com.mars.gateway.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author King
 * @create 2020/5/12 9:09
 */
@RestController
@RequestMapping(value = "/fallback")
public class KingFallBackController {

    @RequestMapping(value = "web")
    public String fallbackWeb() {
        return "errorWeb";
    }


    @RequestMapping(value = "app")
    public String fallbackApp() {
        return "errorApp";
    }

}
