package com.example.securitydemo.config.oauth.provider;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUserInfo extends OAuth2BaseUserInfo{

    private OAuth2User oAuth2User;
    private OAuth2UserRequest userRequest;

    private Map<String, String> userInfo;

    public NaverUserInfo(OAuth2User oAuth2User, OAuth2UserRequest userRequest, Map<String, String> userInfo){
        this.oAuth2User = oAuth2User;
        this.userRequest = userRequest;
        this.userInfo = userInfo;
    }

    @Override
    public String getProviderId() {
        return userInfo.get("id");
    }
    @Override
    public String getProvider() {
        return userRequest.getClientRegistration().getRegistrationId();
    }

    @Override
    public String getEmail() {
        return userInfo.get("email");
    }

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }
}
