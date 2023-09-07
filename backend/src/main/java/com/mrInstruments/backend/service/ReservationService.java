package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.ReservationDto;
import com.mrInstruments.backend.dto.ReviewDto;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.entities.Reservation;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.ProductRepository;
import com.mrInstruments.backend.repository.ReservationRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProductService productService;

    public ReservationService(ReservationRepository reservationRepository, ProductRepository productRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ProductService productService) {
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.productService = productService;
    }

    Logger LOGGER = LogManager.getLogger(ReservationService.class);

    public Optional<ReservationDto> findReservationById(Long id) throws Exception{
        Optional<Reservation> reservationFind = reservationRepository.findById(id);
        if (reservationFind.isPresent()) {
            Reservation reservation = reservationFind.get();
            return Optional.of(reservationToReservationDto(reservation));
        }else{
            throw new ResourceNotFoundException("No existe id asociado a una reservación en el sistema");
        }
    }

    public ReservationDto createReservation(ReservationDto reservationDto) throws Exception{
        Set<Product> products = reservationDto.getProductos()
                .stream()
                .map(productDto -> {
                    try {
                        return productRepository.findById(productDto.getId())
                                .orElseThrow(()->new ResourceNotFoundException("No se encontro " +
                                        "caracteristica asociada al producto"));
                    } catch (Exception e){
                        try {
                            throw new BadRequestException(e.getMessage());
                        } catch (BadRequestException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).collect(Collectors.toSet());

        Reservation newReservation = new Reservation();
        newReservation.setProducts(products);
        newReservation = reservationDtoToReservation(newReservation, reservationDto, bCryptPasswordEncoder);
        newReservation = reservationRepository.save(newReservation);
        LOGGER.log(Level.INFO, "Reserva registrada correctamente");
        return reservationToReservationDto(newReservation);
    }

    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) throws Exception{
        if (!reservationRepository.existsById(id)) {
            try {
                throw new ResourceNotFoundException("Reserva no encontrada");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (id != reservationDto.getId()){
            throw new ResourceNotFoundException("El producto que quiere modificar debe tener igual id del producto a modificar");
        }
        return createReservation(reservationDto);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            try {
                throw new ResourceNotFoundException("Reserva no encontrada");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        reservationRepository.deleteById(id);
    }

    public Set<ReservationDto> listReservations() throws BadRequestException {
        try{
           return reservationRepository.findAll()
                    .stream()
                    .map(reservation -> reservationToReservationDto(reservation))
                    .collect(Collectors.toSet());
        }catch(Exception ex){
            throw new BadRequestException("Error con la consulta a la base de datos");
        }
    }

    public List<ReservationDto> obtenerReservationPorIdProducto(Long id) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("No se encontro reserva asociada a ese usuario"));
        return product.getReservations()
                .stream()
                .map(reservation -> reservationToReservationDto(reservation))
                .collect(Collectors.toList());
    }

    public List<ReservationDto> obtenerReservationPorIdUsuario(Long id) {
        return reservationRepository.findAllByUsuarioId(id)
                .stream()
                .map(reservation -> reservationToReservationDto(reservation))
                .collect(Collectors.toList());
    }
}
