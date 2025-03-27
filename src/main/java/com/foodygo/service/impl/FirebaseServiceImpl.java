package com.foodygo.service.impl;

import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.response.TokenResponse;
import com.foodygo.entity.FcmToken;
import com.foodygo.entity.User;
import com.foodygo.exception.AuthenticationException;
import com.foodygo.repository.FcmTokenRepository;
import com.foodygo.repository.UserRepository;
import com.foodygo.service.spec.FirebaseService;
import com.foodygo.service.spec.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseServiceImpl implements FirebaseService {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Value("${application.postfix-password}")
    private String passwordPostfix;

    @Override
    @Transactional
    public TokenResponse getUserFromFirebase(String googleIdToken, String fcmToken) {
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(googleIdToken);
            String email = token.getEmail();
            Optional<User> user = userRepository.findByEmailAndDeletedIsFalse(email);
            // Delete previous fcmToken
            if (fcmTokenRepository.existsById(fcmToken)) {
                fcmTokenRepository.deleteById(fcmToken);
            }
            if (user.isPresent()) {
                FcmToken newDeviceToken = FcmToken.builder()
                        .token(fcmToken)
                        .user(user.get())
                        .build();
                fcmTokenRepository.save(newDeviceToken);
                return userService.login(user.get().getEmail(), user.get().getEmail() + passwordPostfix);
            } else {
                // Create new user
                UserRegisterRequest request = UserRegisterRequest.builder()
                        .email(email)
                        .password(email + passwordPostfix)
                        .fullName(token.getName())
                        .build();
                userService.registerUser(request);
                Optional<User> newUser = userRepository.findByEmailAndDeletedIsFalse(email);

                if (newUser.isPresent()) {
                    FcmToken newDeviceToken = FcmToken.builder()
                            .token(fcmToken)
                            .user(newUser.get())
                            .build();
                    fcmTokenRepository.save(newDeviceToken);

                    TokenResponse response = userService.login(newUser.get().getEmail(), newUser.get().getEmail() + passwordPostfix);
                    response.setCreated(true);
                    return response;
                } else {
                    throw new AuthenticationException("Google was unable to authorize account access");
                }
            }
        } catch (FirebaseAuthException ex) {
            throw new AuthenticationException("Google was unable to verify id token");
        }
    }
}
