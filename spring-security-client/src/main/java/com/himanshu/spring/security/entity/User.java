package com.himanshu.spring.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    @Column(
            length = 60
    )
    private String password;
    private String role;
    private boolean enabled = false;
}
