package com.project.schoolmanagemensystem.appuser;

import com.project.schoolmanagemensystem.email.EmailService;import com.project.schoolmanagemensystem.registration.RegistrationRequest;
import com.project.schoolmanagemensystem.registration.RegistrationService;
import com.project.schoolmanagemensystem.registration.token.ConfirmationToken;
import com.project.schoolmanagemensystem.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationToken confirmationToken;
    private final EmailService emailService;
    private final RegistrationService registrationService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
       boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
       LocalDateTime userConfirmed = confirmationToken.getConfirmedAt();

       if(userConfirmed == null){
       }
       if(userExists){
           //TODO check of attributes are the same and
           //TODO if email not confirmed send confirmation email
           throw new IllegalStateException("email already taken");


       }

       String encodedPassword = bCryptPasswordEncoder
               .encode(appUser.getPassword());

       appUser.setPassword(encodedPassword);

       appUserRepository.save(appUser);


        //create a token and save it using the method below
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationtoken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationtoken);

        //TODO: SEND EMAIL
        return token;
    }
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

}
