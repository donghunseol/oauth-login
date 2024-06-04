package org.example.loginapp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

public class KakaoResponse {

    /**
     * KAKAO 토큰 요청에 대한 응답 JSON
     * 아래의 JSON 을 파싱
     *  {
     *      "access_token": "ilry8NsJgkkGdowCnw9i0tOsyQ9_9usdAAAAAQopyV4AAAGPzS71n5CBbdpZdq0Z",
     *      "token_type": "bearer",
     *      "refresh_token": "OtKTPfv2ETV6JH5BJiB5cVqLiHDH0wUgAAAAAgopyV4AAAGPzS71nJCBbdpZdq0Z",
     *      "expires_in": 21599,
     *      "scope": "profile_nickname",
     *      "refresh_token_expires_in": 5183999
     *  }
     */
    @Data
    public static class TokenDTO {
        @JsonProperty("access_token") // 이걸 걸면 JSON 데이터가 위치에 들어간다.
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("expires_in")
        private Integer expiresIn;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("refresh_token_expires_in")
        private Integer refreshTokenExpiresIn;
    }

    /**
     * KAKAO 사용자 정보 조회 JSON 데이터
     *  아래의 JSON 을 파싱
     *  {
     *      "id": 3506309930,
     *      "connected_at": "2024-05-31T05:45:58Z",
     *      "properties": {
     *         "nickname": "설동훈"
     *      },
     *  }
     */
    @Data
    public static class KakaoUserDTO{
        @JsonProperty("id")
        private Long id;
        @JsonProperty("connected_at")
        private Timestamp connectedAt;
        @JsonProperty("properties")
        private Properties properties;

        @Data
        static class Properties{
            @JsonProperty("nickname")
            private String nickname;
        }
    }
}
