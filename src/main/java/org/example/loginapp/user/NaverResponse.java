package org.example.loginapp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

public class NaverResponse {
    /**
     * {
     * "access_token": "AAAAOsc6ENfrZt4o5tlAHZ449NuhaWA4E72Hov9skyjnt8UTkXQADiyypgldn9bb6OmW2X5wCjHd1gzCW3NZd2UFkXM",
     * "refresh_token": "aZ5XSArp7Rh7IRmR3Rsjrp9bu5VWF2PfRcauVUT3qmbg7Yyipd0vjisRFp6U2FQ8KPHmj0WpisTPipACAvZZhSbhvQXvBkXJ9QL7YHViiFVU3ufFnZH3Bhwp5UmzPisTFL5uxs",
     * "token_type": "bearer",
     * "expires_in": "3600"
     * }
     */
    @Data
    public static class TokenDTO {
        @JsonProperty("access_token") // 이걸 걸면 JSON 데이터가 위치에 들어간다.
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private String expiresIn;
    }

    /**
     * {
     * "resultcode": "00",
     * "message": "success",
     * "response": {
     * "id": "TKY9LTVzsUVKILKol-g4ijI2hS_MVLXvv7PPZQqkNhM",
     * "email": "ehdgns5647@naver.com",
     * "name": "설동훈"
     * }
     * }
     */
    @Data
    public static class NaverUserDTO {
        @JsonProperty("resultcode")
        private String resultcode;
        @JsonProperty("message")
        private String message;
        @JsonProperty("response")
        private Response response;

        @Data
        static class Response {
            @JsonProperty("id")
            private String id;
            @JsonProperty("email")
            private String email;
            @JsonProperty("name")
            private String name;
        }
    }
}
