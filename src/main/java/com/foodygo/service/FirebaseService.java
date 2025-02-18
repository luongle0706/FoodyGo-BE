package com.foodygo.service;

import com.foodygo.dto.response.TokenResponse;

public interface FirebaseService {
    TokenResponse getUserFromFirebase(String googleIdToken);
}
