package com.mrInstruments.backend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "resenias")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int valoracion;
    private String nombreUsuario;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate fechaPublicacion;
    private String comentario;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "usuarios_id", nullable = false)
    private User usuario;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne
    @JoinColumn(name = "productos_id", nullable = false)
    private Product product;


    public Review(int valoracion, String nombreUsuario, LocalDate fechaPublicacion, String comentario, User usuario, Product product) {
        this.valoracion = valoracion;
        this.nombreUsuario = nombreUsuario;
        this.fechaPublicacion = fechaPublicacion;
        this.comentario = comentario;
        this.usuario = usuario;
        this.product = product;
    }
}
