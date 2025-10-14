package com.nextra.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String material;
    private Double heightCm;

    @Column(columnDefinition = "jsonb")
    private String customFields;
}
