package com.mrInstruments.backend.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categorias")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;

    public Category(String título, String descripcion, String imagen) {
        this.titulo = título;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

}
