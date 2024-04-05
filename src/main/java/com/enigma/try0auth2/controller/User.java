package com.enigma.try0auth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {
    //    @GetMapping("/user")
    //    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    //        return Collections.singletonMap("name", principal.getAttribute("name"));
    //    }

    @GetMapping("/")
    public Object loginGoogle(Authentication authentication) {
        System.out.println(authentication.getPrincipal());
        return authentication.getPrincipal();
    }
}
