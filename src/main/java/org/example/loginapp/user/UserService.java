package org.example.loginapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void 회원가입(String username, String password, String email) {
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        userRepository.save(user);
    }

    public User 로그인(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("아이디가 없습니다");
        } else {
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new RuntimeException("비밀번호가 틀렸습니다");
            }
        }

    }

    public User 카카오로그인(String code) {
        // 1. code로 카카오에서 토큰 받기 (위임 완료) - OAuth 2.0

        // 1-1. RestTemplate header 설정
        RestTemplate rt = new RestTemplate();

        // 1-2. http header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 1-3. http body 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "28fbdf682d4f7a875db89d4fbfa78c7b");
        body.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
        body.add("code", code);

        // 1-4. body + header 객체 만들기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 1-5. API 요청 하기 (토큰 받기)
        ResponseEntity<KakaoResponse.TokenDTO> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                KakaoResponse.TokenDTO.class
        );

        // 1-6. 값 확인
        System.out.println(response.getBody().toString());

        // 2. 토큰으로 사용자 정보 받기 (PK, Email) - 강제 회원가입을 위해 필요
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + response.getBody().getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<>(headers2);

        ResponseEntity<KakaoResponse.KakaoUserDTO> response2 = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request2,
                KakaoResponse.KakaoUserDTO.class
        );

        System.out.println("response2 : " + response2.getBody().toString());

        // 3. 해당 정보로 DB 조회 (있을 수도, 없을 수도)
        String username = "kakao_" + response2.getBody().getId();
        User userPS = userRepository.findByUsername(username);

        // 4. 있으면? - 조회된 유저 정보 리턴
        if (userPS != null) {
            System.out.println("어? 유저가 있네? 강제 로그인 진행");
            return userPS;
        } else {
            System.out.println("어? 유저가 없네? 강제 회원가입 and 강제 로그인");
            // 5. 없으면? - 강제 회원가입 (provider_pk)
            // 유저네임 : (provider_pk)
            // 비밀번호 : UUID
            // 이메일 : email 받은 값
            // 프로바이더 : kakao
            User user = User.builder()
                    .username(username)
                    .password(UUID.randomUUID().toString())
                    .email(response2.getBody().getProperties().getNickname() + "@nate.com")
                    .provider("kakao")
                    .build();
            User returnUser = userRepository.save(user);
            return returnUser;
        }
    }

public User 네이버로그인(String code, String state) {
    // 1. code로 카카오에서 토큰 받기 (위임 완료) - OAuth 2.0

    // 1-1. RestTemplate header 설정
    RestTemplate rt = new RestTemplate();

    // 1-2. http header 설정
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // 1-3. http body 설정
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    System.out.println("1----------------------");
    body.add("grant_type", "authorization_code");
    body.add("client_id", "EmrTw2gHUjV4TuHKRsIk");
    body.add("client_secret", "huuWUneFGu");
    body.add("code", code);
    body.add("state", state);
    System.out.println("2----------------------");
    // 1-4. body + header 객체 만들기
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
    System.out.println("3----------------------");
    // 1-5. API 요청 하기 (토큰 받기)
    ResponseEntity<NaverResponse.TokenDTO> response = rt.exchange(
            "https://nid.naver.com/oauth2.0/token",
            HttpMethod.POST,
            request,
            NaverResponse.TokenDTO.class
    );
    System.out.println("4----------------------");
    // 1-6. 값 확인
    System.out.println(response.getBody().toString());

    // 2. 토큰으로 사용자 정보 받기 (PK, Email) - 강제 회원가입을 위해 필요
    HttpHeaders headers2 = new HttpHeaders();
    headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    headers2.add("Authorization", "Bearer " + response.getBody().getAccessToken());

    HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<>(headers2);

    ResponseEntity<NaverResponse.NaverUserDTO> response2 = rt.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.POST,
            request2,
            NaverResponse.NaverUserDTO.class
    );

    System.out.println("response2 : " + response2.getBody().toString());

    // 3. 해당 정보로 DB 조회 (있을 수도, 없을 수도)
    String username = "naver_" + response2.getBody().getResponse().getId();
    User userPS = userRepository.findByUsername(username);

    // 4. 있으면? - 조회된 유저 정보 리턴
    if (userPS != null) {
        System.out.println("어? 유저가 있네? 강제 로그인 진행");
        return userPS;
    } else {
        System.out.println("어? 유저가 없네? 강제 회원가입 and 강제 로그인");
        // 5. 없으면? - 강제 회원가입 (provider_pk)
        // 유저네임 : (provider_pk)
        // 비밀번호 : UUID
        // 이메일 : email 받은 값
        // 프로바이더 : naver
        User user = User.builder()
                .username(username)
                .password(UUID.randomUUID().toString())
                .email(response2.getBody().getResponse().getEmail())
                .provider("naver")
                .build();
        User returnUser = userRepository.save(user);
        return returnUser;
    }
}
}
