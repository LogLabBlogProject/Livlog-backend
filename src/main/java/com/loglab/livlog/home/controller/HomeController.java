package com.loglab.livlog.home.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<CommonResponse<?>> home1() {
        String responseData = "Hello World from /";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseData));
    }

    @GetMapping("/home")
    public ResponseEntity<CommonResponse<?>> home2() {
        String responseData = "Hello World from /home";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseData));
    }

    @GetMapping("/index")
    public ResponseEntity<CommonResponse<?>> index() {
        String responseData = "Hello World from /index";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(responseData));
    }
}
