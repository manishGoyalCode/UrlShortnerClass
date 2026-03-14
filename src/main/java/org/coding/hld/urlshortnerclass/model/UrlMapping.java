package org.coding.hld.urlshortnerclass.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "url_mapping")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shortCode;

    @Column(columnDefinition = "TEXT")
    private String originalUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters setters
}