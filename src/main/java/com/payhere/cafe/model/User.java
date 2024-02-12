package com.payhere.cafe.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity(name = "user")
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;
}
