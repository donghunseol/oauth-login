package org.example.loginapp.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    // http://localhost:8080/oauth/kakao/callback?code=3u9fk
    @GetMapping("/oauth/kakao/callback")
    public String oauthCallback(String code) {
        System.out.println("카카오 콜백 : " + code);
        User sessionUser = userService.카카오로그인(code);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/shop";
    }

// http://localhost:8080/oauth/naver/callback?code=3u9fk
@GetMapping("/oauth/naver/callback")
public String oauthNaverCallback(String code, String state) {
    System.out.println("네이버 콜백 : " + code);
    User sessionUser = userService.네이버로그인(code, state);
    session.setAttribute("sessionUser", sessionUser);
    return "redirect:/shop";
}

    @GetMapping("/join-form")
    public String joinForm() {
        return "join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "login-form";
    }

    @PostMapping("/join")
    public String join(String username, String password, String email) {
        userService.회원가입(username, password, email);
        return "redirect:/login-form";
    }

    @PostMapping("/login")
    public String login(String username, String password) {
        User sessionUser = userService.로그인(username, password);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/shop";
    }
}
