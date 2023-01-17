package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.entity.User.UserType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostUserReq {
    @NotBlank(message= "Insert phone number")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong phone number")
    @Size(max=20, message = "maximum 20 characters")
    private String phone;
    @NotBlank(message= "Insert name")
    @Size(max=20, message = "maximum 20 characters")
    private String name;
    @NotBlank(message= "Insert user id")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message= "Wrong ID format")
    @Size(max=20, message = "maximum 20 characters")
    private String userId;
    @NotBlank(message= "Insert password")
    @Size(min=6, max=20, message = "minimum 6 & maximum 20 characters")
    private String password;
    @NotBlank(message = "Insert birthday")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-([012][0-9]|3[01])", message= "Wrong Date Format")
    private String birthday; //'YYYY-MM-DD'

    //@NotBlank(message = "Insert user type")
    protected UserType userType;

    public User toEntity() {
        return User.builder()
                .phone(this.phone)
                .name(this.name)
                .userId(this.userId)
                .password(this.password)
                .birthday(LocalDate.parse(this.birthday))
                .userType(this.userType)
                .build();
    }
}
