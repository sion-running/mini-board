package com.wanted.august.model.request;

import com.wanted.august.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    @Pattern(
            regexp = "^(?=.*[a-zA-Z\\d])[a-zA-Z\\d]{8,12}$",
            message = "영어와 숫자의 조합으로 최소 8자 최대 12자까지 가능")
    private String userName;

    @Pattern(
            regexp = "^[a-zA-Z가-힣]+$",
            message = "영어 대소문자 혹은 한글 이름을 사용")
    private String nickName;

    @Pattern(
            regexp = "^(?:(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?~\\\\/-])(?=.{7,})|(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?~\\\\/-])(?=.{7,})|(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?~\\\\/-])(?=.{7,})).*$",
            message = "대소문자, 숫자 5개 이상, 특수문자 포함 2개 이상")
    private String password;

    @Email
    private String email;

    @Pattern(
            regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "Invalid phone number format")
    private String phone;

    private UserRole role = UserRole.USER;
}
