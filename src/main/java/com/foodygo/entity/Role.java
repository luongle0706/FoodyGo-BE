package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.foodygo.enums.EnumRoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;

    @Enumerated(EnumType.STRING)
    private EnumRoleName roleName;

    private String displayName;

    @JsonBackReference
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

    public Role(EnumRoleName roleName, Set<User> users) {
        this.roleName = roleName;
        this.users = users;
    }

}
