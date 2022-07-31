package com.example.securitydemo.controller;

import com.example.securitydemo.config.auth.PrincipalDetails;
import com.example.securitydemo.model.Login;
import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index(){
        // 머스테치 기본폴더 src/main/resources
        // 뷰리졸버 설정 : /templates/, .mustache
        // 자동으로 잡혀서 yml에 mvc.view 설정 안해도됨
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }

    @GetMapping("/test/login2")
    public @ResponseBody String loginTest2(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return principalDetails.getUser().toString();
    }

    @GetMapping("/test/login") // 일반 사용자 로그인
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        System.out.println("principal: " + authentication.getPrincipal());


        UserDetails principalDetails = (UserDetails) authentication.getPrincipal();
        // authentication.getPrincipal 이 오브젝트 형이라 들어오는 정보에 따라서 형 변환에 에러가 발생할 수 있음
        // 예를 들어 loginTest에는 일반 user가 들어가야 userDetails 에 다운캐스팅이 가능하며
        // oauth2로 로그인 사용자는 oAuth2User 로 다운캐스팅이 가능하다.
        System.out.println("principalDetails : " + principalDetails);

        System.out.println("userDetails : " + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    // 정리하면 UserDetails 에는 일반 사용자 정보가 담겨있고
    // Oauth2User 에는 oauth 사용자 정보가 담겨있다.

    @GetMapping("/test/oauth/login") // 구글 로그인
    public @ResponseBody String oauthTest(Authentication authentication, @AuthenticationPrincipal OAuth2User userDetails){ // DI(의존성 주입)
        System.out.println("principal: " + authentication.getPrincipal());


        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oauth : " + oAuth2User.getName());
        System.out.println("attributes : " + oAuth2User.getAttributes());
        System.out.println(userDetails.getName());
        return "세션 정보 확인하기";
    }
}
