package com.usermicroservice.UserMicroService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_table")
public class User {
    @Id
//    @GeneratedValue(generator = "user_generator", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "user_generator", sequenceName = "user_sequence", allocationSize = 1, initialValue = 1)
    private String id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String about;

    @Transient
    private Image profilePicture;
}
