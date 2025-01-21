package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.foodygo.enums.EnumRoleName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int roleID;

    @Enumerated(EnumType.STRING)
    EnumRoleName roleName;

    String displayName;

    @JsonBackReference
    @OneToMany(mappedBy = "role")
    Set<User> users = new HashSet<>();

    public Role(EnumRoleName roleName, Set<User> users) {
        this.roleName = roleName;
        this.users = users;
    }

}
