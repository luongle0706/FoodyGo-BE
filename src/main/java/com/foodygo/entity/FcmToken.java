package com.foodygo.entity;

import com.foodygo.entity.composite.FcmTokenId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FcmToken {
    @EmbeddedId
    FcmTokenId id;

    @ManyToOne
    @MapsId("userId")
    User user;

    @Builder.Default
    boolean loggedIn = true;

    @Builder.Default
    boolean deleted = false;
}
