package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.foodygo.enums.EnumRoleNameType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int roleID;

    @Enumerated(EnumType.STRING)
    EnumRoleNameType roleName;

    String displayName;

    @JsonBackReference
    @OneToMany(mappedBy = "role")
    Set<User> users = new HashSet<>();

    public Role(EnumRoleNameType roleName, Set<User> users) {
        this.roleName = roleName;
        this.users = users;
    }

}
