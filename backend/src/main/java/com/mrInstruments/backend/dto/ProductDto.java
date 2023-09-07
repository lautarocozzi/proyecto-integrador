package com.mrInstruments.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ProductDto implements Serializable {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenes;
    private double precio;
    private CategoryDto categoria;
    private Set<CharacteristicDto> caracteristicas = new HashSet<>();

    public ProductDto(Long id, String nombre, String descripcion, String imagenes, double precio, CategoryDto categoria, Set<CharacteristicDto> caracteristicas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
        this.precio = precio;
        this.categoria = categoria;
        this.caracteristicas = caracteristicas;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenes='" + imagenes + '\'' +
                ", precio=" + precio +
                ", categoria=" + categoria +
                ", caracteristicas=" + caracteristicas +
                '}';
    }
}
