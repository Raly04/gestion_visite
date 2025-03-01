package org.example.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false , length = 25)
    private String username;
    @Column(unique = true, nullable = false , length = 25)
    private String password;
}
