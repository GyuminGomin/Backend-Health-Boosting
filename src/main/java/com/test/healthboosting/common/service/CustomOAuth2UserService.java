package com.test.healthboosting.common.service;

import com.test.healthboosting.common.mapper.CustomOAuth2UserMapper;
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
        String providerId = oAuth2User.getName(); // 보통 sub 값


        // 회원 조회
        int userCnt = customOAuth2UserMapper.isExistUser(providerId);

        if (userCnt == 0) {
            customOAuth2UserMapper.insertUser();
        }

        // TODO 좀 더 생각하고 시간적 여유 있을 때 작업 하자

        return null;
    }
}
