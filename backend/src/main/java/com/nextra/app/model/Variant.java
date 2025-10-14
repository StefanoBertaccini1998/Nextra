package com.nextra.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "variant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String color;
    private String fabric;

    @Column(columnDefinition = "jsonb")
    private String optional;

    private BigDecimal priceDelta;

    @Column(columnDefinition = "jsonb")
    private String customFields;
}
