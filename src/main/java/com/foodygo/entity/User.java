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

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = true, columnDefinition = "VARCHAR(60)")
    private String password;

    @Column(name = "phone", nullable = true, unique = true, columnDefinition = "VARCHAR(12)")
    private String phone;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean enabled;

    @Column(name = "non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean nonLocked;

    @Column(name = "access_token", nullable = true, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", nullable = true, columnDefinition = "TEXT")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    @OneToMany(mappedBy = "employee")
    private List<Order> employeeOrders;
}
