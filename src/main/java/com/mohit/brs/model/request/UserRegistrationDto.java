package com.mohit.brs.model.request;

import com.mohit.brs.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private Role role;
}
