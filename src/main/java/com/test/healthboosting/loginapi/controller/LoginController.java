package com.test.healthboosting.loginapi.controller;

import com.test.healthboosting.loginapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login/*")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService ls;



}
