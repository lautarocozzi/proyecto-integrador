package com.mrInstruments.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ReservationDto implements Serializable {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaReserva;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEntrega;
    private boolean reservaActiva;
    private double seguro;
    private UserDto usuario;
    private Set<ProductDto> productos = new HashSet<>();

    public ReservationDto(Long id, LocalDate fechaReserva, LocalDate fechaEntrega, boolean reservaActiva, double seguro, UserDto usuario, Set<ProductDto> productos) {
        this.id = id;
        this.fechaReserva = fechaReserva;
        this.fechaEntrega = fechaEntrega;
        this.reservaActiva = reservaActiva;
        this.seguro = seguro;
        this.usuario = usuario;
        this.productos = productos;
    }
}
