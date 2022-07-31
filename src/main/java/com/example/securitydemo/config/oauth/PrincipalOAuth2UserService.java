package com.example.securitydemo.config.oauth;

import com.example.securitydemo.config.auth.PrincipalDetails;
import com.example.securitydemo.config.oauth.provider.FacebookUserInfo;
import com.example.securitydemo.config.oauth.provider.GoogleUserInfo;
import com.example.securitydemo.config.oauth.provider.NaverUserInfo;
import com.example.securitydemo.config.oauth.provider.OAuth2BaseUserInfo;
import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // 여기가 oauth 정보를 받은 이후 로그인 처리를 담당
        // userRequest에는 액세스토큰과 Register 정보가 담겨있음
        System.out.println(userRequest.getClientRegistration()); // 어떤 Oauth로 로그인 되어있는지 (registrationId='google')
        System.out.println(userRequest.getAccessToken().getTokenValue()); // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인하면 -> code 리턴(oauth-client를 통해) -> 이후 코드를 통해 access token 요청

        // userRequest 정보로 회원 프로필을 받아야함 -> 그 때 사용되는 함수 : loadUser (loadUser : 구글로부터 회원 프로필을 받아주는 메서드)
        System.out.println(super.loadUser(userRequest).getAttributes());


        // 회원가입 강제진행
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2BaseUserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User, userRequest);

        }
        else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User, userRequest);
        }else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(oAuth2User, userRequest, oAuth2User.getAttribute("response"));
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = oAuth2UserInfo.getUsername();
        String password = oAuth2UserInfo.getPassword(bCryptPasswordEncoder);
        String role = oAuth2UserInfo.getRole();

        // 회원가입 하기 전에 중복이 있는지 확인
        User userEntity = userRepository.findByUsername(username).orElse(null);

        User user = User.builder().email(email).password(password).provider(provider).providerId(providerId).username(username).role(role).build();
        if (userEntity == null){
            userRepository.save(user);
            System.out.println("oAUth 로그인이 최초입니다.");
            userEntity = user; // UserEntity 를 OAuth2User 로 Return 할 때 매개변수로 넣어주는데, 빈 값이면 nullException 이 발생하므로 회원가입이 진행되자마자 UserEntity에 값을 넣어줌
        }
        else
            System.out.println("oAUth 로그인이 이미 되어있습니다.");


        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
