package com.test.healthboosting.common.util;

import com.test.healthboosting.loginapi.dto.MemberDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO member;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(MemberDTO member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
    }

    @Override
    public String getName() {
        return member.getOauthProviderId();
    }

    public MemberDTO getMember() {
        return member;
    }
}
