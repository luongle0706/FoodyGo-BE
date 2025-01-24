package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userID;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    String email;

    @Column(name = "password", columnDefinition = "VARCHAR(60)")
    String password;

    @Column(name = "phone", unique = true, columnDefinition = "VARCHAR(12)")
    String phone;

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

    @OneToOne(mappedBy = "user")
    Customer customer;

    @OneToMany(mappedBy = "employee")
    @JsonBackReference
    List<Order> employeeOrders;
}
