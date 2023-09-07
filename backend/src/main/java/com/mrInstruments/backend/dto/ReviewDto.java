package com.mrInstruments.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ReviewDto implements Serializable {

    private Long id;
    private int valoracion;
    private String nombreUsuario;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaPublicacion;
    private String comentario;
    private UserDto usuario;
    private ProductDto producto;

    public ReviewDto(Long id, int valoracion, String nombreUsuario, LocalDate fechaPublicacion, String comentario, UserDto usuario, ProductDto producto) {
        this.id = id;
        this.valoracion = valoracion;
        this.nombreUsuario = nombreUsuario;
        this.fechaPublicacion = fechaPublicacion;
        this.comentario = comentario;
        this.usuario = usuario;
        this.producto = producto;
    }
}
