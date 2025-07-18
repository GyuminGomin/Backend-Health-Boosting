package com.test.healthboosting.common.service;

import com.test.healthboosting.common.enums.LoginType;
import com.test.healthboosting.common.enums.Role;
import com.test.healthboosting.common.mapper.CustomOAuth2UserMapper;
import com.test.healthboosting.loginapi.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {

    private final CustomOAuth2UserMapper customOAuth2UserMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        // provider : google, kakao, naver
        String providerId = oAuth2User.getName(); // 보통 sub 값
        // providerId = sub
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profileImg = oAuth2User.getAttribute("picture");

        // 회원 조회
        int userCnt = customOAuth2UserMapper.isExistUser(providerId);


        if (userCnt == 0) {
            MemberDTO loginDTO = MemberDTO.builder()
                    .loginType(LoginType.OAUTH)
                    .oauthProvider(provider)
                    .oauthProviderId(providerId)
                    .email(email)
                    .name(name)
                    .profileImageUrl(profileImg)
                    .role(Role.USER)
                    .build();
            customOAuth2UserMapper.insertUser();
        }



        return null;
    }
}
