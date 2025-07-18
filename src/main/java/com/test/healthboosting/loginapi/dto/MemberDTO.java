package com.test.healthboosting.loginapi.dto;

import com.test.healthboosting.common.enums.LoginType;
import com.test.healthboosting.common.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {

    private LoginType loginType; // OAUTH, NORMAL
    private String oauthProvider; // "google", "naver" 등
    private String oauthProviderId; // 구글 sub 값

    private String userId; // 일반 로그인 ID
    private String password; // 일반 로그인 password

    private String phoneNumber;
    private String gender;
    private String email;
    private String name;
    private String profileImageUrl;
    private Role role; // USER, ADMIN
    private String birthday;
}
