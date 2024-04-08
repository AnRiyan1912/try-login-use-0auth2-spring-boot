package com.enigma.try0auth2.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "Authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    public String authority;
}
