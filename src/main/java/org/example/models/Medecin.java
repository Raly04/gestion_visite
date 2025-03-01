package org.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "medecins")
@Data
public class Medecin {
    @Id
    private String codemed;
    @Column(nullable = false)
    private String nom;
    private String prenom;
    @Column(nullable = false)
    private String grade;
}
