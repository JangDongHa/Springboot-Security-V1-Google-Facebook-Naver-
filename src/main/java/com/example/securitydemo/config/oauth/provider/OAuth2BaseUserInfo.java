package com.example.securitydemo.config.oauth.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


public abstract class OAuth2BaseUserInfo implements OAuth2UserInfo{


    public String getPassword(BCryptPasswordEncoder bCryptPasswordEncoder){
        return bCryptPasswordEncoder.encode("getinthere");
    }

    public String getRole(){
        return "ROLE_USER";
    }
}
