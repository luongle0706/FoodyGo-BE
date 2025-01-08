package com.foodygo.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "user")
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

    @Column(name = "address", nullable = true, columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "avata", nullable = true, columnDefinition = "VARCHAR(255)")
    private String avata;

    @Column(name = "code_verify", nullable = false, columnDefinition = "VARCHAR(36)")
    private String codeVerify;
    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean enabled;

    @Column(name = "non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean nonLocked;

    @Column(name= "year_of_birth", nullable = true)
    private Date yearOfBirth;

    @Column(name = "offline_at", nullable = true)
    private Date offlineAt;

    @ManyToOne
    @JoinColumn(name = "roleID")
    private Role role;

}
