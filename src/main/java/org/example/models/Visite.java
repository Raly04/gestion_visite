package org.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "visites")
@Data
public class Visite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "codepat", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "codemed", nullable = false)
    private Medecin medecin;

    @Column(nullable = false)
    private Date date;

}