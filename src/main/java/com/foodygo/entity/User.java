package com.foodygo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(30)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(30)")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = true, columnDefinition = "VARCHAR(60)")
    private String password;

    @Column(name = "phone", nullable = true, unique = true, columnDefinition = "VARCHAR(12)")
    private String phone;

    @Column(name = "avatar", nullable = true, columnDefinition = "VARCHAR(255)")
    private String avatar;

    @Column(name = "code_verify", nullable = false, columnDefinition = "VARCHAR(36)")
    private String codeVerify;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean enabled;

    @Column(name = "non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean nonLocked;

    @Column(name = "year_of_birth", nullable = true)
    private Date yearOfBirth;

    @Column(name = "offline_at", nullable = true)
    private Date offlineAt;

    @Column(name = "access_token", nullable = true, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", nullable = true, columnDefinition = "TEXT")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "roleID")
    private Role role;

    @ManyToMany
    private Set<Hub> hubs;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<Image> images;
}
