package com.project.schoolmanagemensystem.registration;

import com.project.schoolmanagemensystem.appuser.AppUser;
import com.project.schoolmanagemensystem.appuser.AppUserRole;
import com.project.schoolmanagemensystem.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator
                .test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not found");
        }
        return appUserService.signUpUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                request.getDepartment(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.STUDENT
        ));
    }
}
