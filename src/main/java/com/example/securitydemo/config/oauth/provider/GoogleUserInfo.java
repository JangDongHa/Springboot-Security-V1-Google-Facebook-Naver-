package com.example.securitydemo.config.oauth.provider;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUserInfo extends OAuth2BaseUserInfo{

    private OAuth2User oAuth2User;
    private OAuth2UserRequest userRequest;

    public GoogleUserInfo(OAuth2User oAuth2User, OAuth2UserRequest userRequest){
        this.oAuth2User = oAuth2User;
        this.userRequest = userRequest;
    }

    @Override
    public String getProviderId() {
        return oAuth2User.getAttribute("sub");
    }

    @Override
    public String getProvider() {
        return userRequest.getClientRegistration().getRegistrationId();
    }

    @Override
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }
}
