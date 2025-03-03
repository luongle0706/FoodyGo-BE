package com.foodygo.service.spec;

import com.foodygo.dto.response.TokenResponse;

public interface FirebaseService {
    TokenResponse getUserFromFirebase(String googleIdToken, String fcmToken);
}
