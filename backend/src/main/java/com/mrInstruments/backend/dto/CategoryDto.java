package com.mrInstruments.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto implements Serializable {

    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;

    public CategoryDto(Long id, String titulo, String descripcion, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }
}
