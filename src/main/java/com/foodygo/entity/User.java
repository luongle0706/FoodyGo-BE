package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user-account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userID;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(255)")
    String fullName;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    String email;

    @Column(name = "password", columnDefinition = "NVARCHAR(60)")
    String password;

    @Column(name = "phone", unique = true, columnDefinition = "NVARCHAR(12)")
    String phone;

    @Column(name = "dob", columnDefinition = "DATE")
    LocalDate dob;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    boolean enabled;

    @Column(name = "non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    boolean nonLocked;

    @Column(name = "access_token", columnDefinition = "TEXT")
    String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    Hub hub;

    @OneToOne(mappedBy = "user")
    Customer customer;

    @OneToOne(mappedBy = "owner")
    Restaurant restaurant;

    @OneToMany(mappedBy = "employee")
    @JsonBackReference
    List<Order> employeeOrders;

    @OneToMany(mappedBy = "user")
    List<FcmToken> fcmTokens;
}
