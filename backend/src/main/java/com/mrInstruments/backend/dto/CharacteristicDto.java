package com.mrInstruments.backend.dto;



import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CharacteristicDto implements Serializable {

    private Long id;
    private String titulo;
    private String imagen;

    public CharacteristicDto(Long id, String título, String imagen) {
        this.id = id;
        this.titulo = título;
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "CharacteristicDto{" +
                "id=" + id +
                ", título='" + titulo + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
