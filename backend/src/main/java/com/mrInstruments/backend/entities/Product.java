package com.mrInstruments.backend.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenes;
    private double precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorias_id", nullable = false)
    private Category category;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinTable(name = "productos_has_caracteristicas",
            joinColumns = @JoinColumn(name = "productos_id" ),
            inverseJoinColumns = @JoinColumn(name = "caracteristicas_id"))
    private Set<Characteristic> characteristics = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinTable(name = "pedidos_has_productos",
            joinColumns = @JoinColumn(name = "productos_id"),
            inverseJoinColumns = @JoinColumn(name = "pedidos_id"))
    private Set<Reservation> reservations = new HashSet<>();


    /*@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER) //   antes->(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorias_id", nullable = false)
    private Category category;*/
    public Product() {
        this.setCharacteristics(new HashSet<>());
    }

    public Product(String nombre, String descripcion, String imagenes, double precio, Category category) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.precio = precio;
        this.category = category;
        this.setCharacteristics(new HashSet<>());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenes='" + imagenes + '\'' +
                ", precio=" + precio +
                ", category=" + category +
                ", characteristics=" + characteristics +
                ", reservations=" + reservations +
                '}';
    }
}
