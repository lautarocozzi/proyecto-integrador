package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.ReservationDto;
import com.mrInstruments.backend.dto.ReviewDto;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.ReservationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    Logger logger = LogManager.getLogger(ReservationController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) throws Exception {
        Optional<ReservationDto> reservation = reservationService.findReservationById(id);
        if(reservation.isPresent()){
            logger.log(Level.INFO,"Reserva con id= " + id + " fue encontrado");
            return ResponseEntity.ok(reservation.get());
        }else {
            throw new ResourceNotFoundException("Error al buscar id: " + id + " reserva no encontrada.");
        }
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) throws Exception {
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,  @RequestBody ReservationDto reservationDto) throws Exception{
        //Reservation updatedReservation = reservationService.updateReservation(id, reservationDto);
        return ResponseEntity.ok(reservationService.updateReservation(id, reservationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) throws Exception{
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reserva eliminada");
    }

    @GetMapping
    public ResponseEntity<?> listReservations() throws Exception{
        return ResponseEntity.ok(reservationService.listReservations());
    }

    @PermitAll
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> buscarReservasPorIdProducto(@PathVariable Long id) throws Exception{
        List<ReservationDto> reservas = reservationService.obtenerReservationPorIdProducto(id);
        return ResponseEntity.ok(reservas);
    }
    @PermitAll
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> buscarReservasPorIdUsuario(@PathVariable Long id) throws Exception{
        List<ReservationDto> reservas = reservationService.obtenerReservationPorIdUsuario(id);
        return ResponseEntity.ok(reservas);
    }
}