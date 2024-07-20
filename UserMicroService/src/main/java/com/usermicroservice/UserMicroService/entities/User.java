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
    @GeneratedValue(generator = "user_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_generator", sequenceName = "user_sequence", allocationSize = 1, initialValue = 1)
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Column
    private String email;

    @Column
    private String about;

    @Column
    private long profileImageId;
}
