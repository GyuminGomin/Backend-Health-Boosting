package com.test.healthboosting.common.service;

import com.test.healthboosting.common.enums.LoginType;
import com.test.healthboosting.common.enums.Role;
import com.test.healthboosting.common.mapper.CustomOAuth2UserMapper;
import com.test.healthboosting.common.util.CustomOAuth2User;
import com.test.healthboosting.loginapi.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CustomOAuth2UserMapper customOAuth2UserMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = "";
        String email = "";
        String name = "";
        String profileImg = "";
        String gender = "";
        String mobile = "";
        if (provider.equals("google")) {
            providerId = oAuth2User.getName();
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            profileImg = oAuth2User.getAttribute("picture");
        } else if (provider.equals("naver")) {
            Map<String, Object> response = oAuth2User.getAttribute("response");
            providerId = Objects.toString(response.get("id"), "");
            email = Objects.toString(response.get("email"), "");
            name = Objects.toString(response.get("name"), "");
            profileImg = Objects.toString(response.get("profile_image"), "");
            gender = Objects.toString(response.get("gender"), "");
            mobile = Objects.toString(response.get("mobile"), "");
        } else if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            providerId = oAuth2User.getName();
            email = Objects.toString(kakaoAccount.get("email"), "");
            name = Objects.toString(profile.get("nickname"), "");
            profileImg = Objects.toString(profile.get("profile_image_url"), "");
        }

        MemberDTO memberDTO = MemberDTO.builder()
                .loginType(LoginType.OAUTH)
                .oauthProvider(provider)
                .oauthProviderId(providerId)
                .email(email)
                .name(name)
                .profileImageUrl(profileImg)
                .gender(gender)
                .phoneNumber(mobile)
                .role(Role.USER)
                .build();

        customOAuth2UserMapper.insertUser(memberDTO);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO, oAuth2User.getAttributes());

        // 사용자 정보를 기반으로 OAuth2User 객체 생성
        return customOAuth2User;
    }
}
