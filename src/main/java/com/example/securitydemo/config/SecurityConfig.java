package com.example.securitydemo.config;

import com.example.securitydemo.config.oauth.PrincipalOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 구글 로그인 과정 (OAuth2 Client)
// 1. 코드받기(인증된 사용자라는 코드) 2. 액세스토큰(권한)
// 3. 사용자 프로필 정보 가져오기 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키는 것도 가능
// 4-2. 추가적으로(구글에서 받은 정보가 아닌) 정보를 추가해야할 게 필요하다면 추가적인 회원가입 창을 뿌려줄 수 있음


@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화 (컨트롤러에 authenticated 말고 한 두개 간단하게 인증과정을 적용하고 싶으면
// @Secured("ROLE_ADMIN") 이런식으로 걸어줄 수 있음
// prePostEnabled : preAuthorize 어노테이션 활성화
// Secured 랑 같지만 preAuthorize 는 컨트롤러의 메서드가 실행되기 직전에 실행 + authenticated 가 아니라 .access 처럼 hasRole 로 설정 
// postAuthorize 도 있는데 이건 메서드가 실행되고 검사하는거라 거의 안씀
public class SecurityConfig {
    @Autowired
    private PrincipalOAuth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated() // 인증 된 사람만
                    .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 특정 권한만
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 특정 권한만
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/loginForm")
                    .loginProcessingUrl("/login") // /login 주소가 호출되면 시큐리티가 낚아채서 로그인 진행
                    .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                    .loginPage("/loginForm")// 구글 로그인이 완료가 되면 엑세스토큰 + 사용자 프로필 정보를 한번에 받음
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService)
                    .and()
                    .defaultSuccessUrl("/")
                .and().build();
    }

}
