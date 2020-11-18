package com.template.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/")
    public ResponseEntity<?> handleDemo() {
        return ResponseEntity.ok("You are authenticated");
    }

}
