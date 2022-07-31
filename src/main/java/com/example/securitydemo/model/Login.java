package com.example.securitydemo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor// id, password, 이메일, 주민번호, 집주소, 사진, 등 64개
public class Login {
    private String id;
    private String password;
    private String 이메일;
    private String 주민번호;
}
