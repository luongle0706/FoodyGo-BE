package com.foodygo.service;

import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.response.TokenResponse;
import com.foodygo.entity.FcmToken;
import com.foodygo.entity.User;
import com.foodygo.entity.composite.FcmTokenId;
import com.foodygo.exception.AuthenticationException;
import com.foodygo.repository.FcmTokenRepository;
import com.foodygo.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public TokenResponse getUserFromFirebase(String googleIdToken, String fcmToken) {
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(googleIdToken);
            String email = token.getEmail();
            Optional<User> user = userRepository.findByEmailAndDeletedIsFalse(email);
            if(user.isPresent()) {
                return userService.login(user.get().getEmail(), user.get().getEmail() + passwordPostfix);
            } else {
                UserRegisterRequest request = UserRegisterRequest.builder()
                        .email(email)
                        .password(email + passwordPostfix)
                        .build();
                userService.registerUser(request);
                Optional<User> optionalUser = userRepository.findByEmailAndDeletedIsFalse(email);

                if(optionalUser.isPresent()) {
                    User newUser = optionalUser.get();
                    newUser.setFullName(token.getName());
                    newUser = userRepository.save(newUser);
                    FcmToken newFcmToken = FcmToken.builder()
                            .user(newUser)
                            .id(FcmTokenId.builder()
                                    .token(fcmToken)
                                    .userId(newUser.getUserID())
                                    .build())
                            .build();
                    fcmTokenRepository.save(newFcmToken);

                    return userService.login(newUser.getEmail(), newUser.getEmail() + passwordPostfix);
                } else {
                    throw new RuntimeException("");
                }
            }
        } catch (FirebaseAuthException ex) {
            throw new AuthenticationException("Google was unable to verify id token");
        }
    }
}
