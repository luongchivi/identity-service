package com.luongchivi.identity_service.dto.request.user;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.luongchivi.identity_service.validator.DateOfBirthConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 8, message = "USERNAME_TOO_SHORT")
    String username;

    @Size(min = 8, message = "PASSWORD_TOO_SHORT")
    String password;

    String firstName;
    String lastName;

    @DateOfBirthConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dateOfBirth;
}
