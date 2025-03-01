package org.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "patients")
@Data
public class Patient {
    @Id
    private String codepat ;
    @Column(nullable = false)
    private String nom;
    private String prenom;
    @Column(nullable = false)
    private String sexe;
    private String adresse;
}
