package com.mrInstruments.backend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "caracteristicas")
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String imagen;

    public Characteristic(String título, String imagen) {
        this.titulo = título;
        this.imagen = imagen;
    }

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "productos_has_caracteristicas",
            joinColumns = @JoinColumn(name = "caracteristicas_id"),
            inverseJoinColumns = @JoinColumn(name = "productos_id"))
    private Set<Product> products;

    public Characteristic(Long id, String título, String imagen, Set<Product> products) {
        this.id = id;
        this.titulo = título;
        this.imagen = imagen;
        this.products = products;
    }
}
