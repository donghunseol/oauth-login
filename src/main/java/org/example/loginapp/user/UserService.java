package org.example.loginapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        }else {
            if (user.getPassword().equals(password)){
                return user;
            }else {
                throw new RuntimeException("비밀번호가 틀렸습니다");
            }
        }

    }
}