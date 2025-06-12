package com.lms.petshopfrontend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pet_categories")
public class PetCategory {
    @Id
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;
}
