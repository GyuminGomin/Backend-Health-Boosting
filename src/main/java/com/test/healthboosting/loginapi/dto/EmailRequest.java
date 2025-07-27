package com.test.healthboosting.loginapi.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String email;
    private String code;
}
