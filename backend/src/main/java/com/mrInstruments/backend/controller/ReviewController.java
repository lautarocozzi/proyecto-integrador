package com.mrInstruments.backend.controller;
import com.mrInstruments.backend.dto.ReviewDto;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.ReviewService;
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
@RequestMapping("/resenias")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    Logger logger = LogManager.getLogger(ReviewController.class);

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) throws Exception{
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) throws Exception {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if(reviewDto.isPresent()){
            logger.log(Level.INFO,"Reseña con id= " + id + " fue encontrado");
            return ResponseEntity.ok(reviewDto.get());
        }else {
            throw new ResourceNotFoundException("Error al buscar id: " + id + " reseña no encontrada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id,  @RequestBody ReviewDto reviewDto) throws Exception{
        //Reservation updatedReservation = reservationService.updateReservation(id, reservationDto);
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) throws Exception{
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Reseña eliminada");
    }

    @GetMapping
    public ResponseEntity<?> getAllReviews() throws Exception{
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PermitAll
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> buscarReservasPorIdUsuario(@PathVariable Long id) throws Exception{
        List<ReviewDto> reviewDtos = reviewService.obtenerReviewPorIdProducto(id);
        return ResponseEntity.ok(reviewDtos);
    }

}
