package com.nextra.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sofa_frame")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SofaFrame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String material;

    @Column(columnDefinition = "jsonb")
    private String customFields;
}
