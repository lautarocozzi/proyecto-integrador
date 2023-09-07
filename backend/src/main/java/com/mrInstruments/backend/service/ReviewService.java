package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.ReviewDto;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.entities.Review;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.ProductRepository;
import com.mrInstruments.backend.repository.ReviewRepository;
import com.mrInstruments.backend.repository.UserRepository;
import com.mrInstruments.backend.utils.UtilsDtoMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    Logger LOGGER = LogManager.getLogger(ReviewService.class);

    public List<ReviewDto> getAllReviews() throws Exception {
        List<ReviewDto> reviewsListDto = reviewRepository.findAll()
                .stream()
                .map(UtilsDtoMapper::reviewToReviewDto)
                .collect(Collectors.toList());
        LOGGER.log(Level.INFO, "Listando reseñas...");
        return reviewsListDto;
    }

    public Optional<ReviewDto> getReviewById(Long id) throws Exception{
        Optional<Review> reviewGet = reviewRepository.findById(id);
        if (reviewGet.isPresent()) {
            Review review = reviewGet.get();
            return Optional.of(reviewToReviewDto(review));
        } else {
            throw new ResourceNotFoundException("No existe id asociado a una reseña en el sistema");
        }
    }

    public ReviewDto createReview(ReviewDto reviewDto) throws Exception{
        Review review = new Review();
        if (reviewDto.getProducto() == null || reviewDto.getUsuario() == null){
            throw new BadRequestException("La review debe tener usuario y producto asociado");
        }
        review.setProduct(productRepository.findById(reviewDto.getProducto().getId())
                .orElseThrow(()-> new BadRequestException("No existe producto asociado a esa review en la base de datos"))
        );
        review.setUsuario(userRepository.findById(reviewDto.getUsuario().getId())
                .orElseThrow(()-> new BadRequestException("No existe usuario asociado a esa review en la base de datos"))
        );
        review = reviewDtoToReview(review, reviewDto);
        return reviewToReviewDto(reviewRepository.save(review));
    }

    public void deleteReview(Long id) throws BadRequestException{
        try {
            reviewRepository.deleteById(id);
            LOGGER.log(Level.INFO,"Reseña con id= "+id+" fue eliminada");
        }catch (Exception ex){
            throw new BadRequestException("Error - no se pudo eliminar la Reseña");
        }
    }

    public ReviewDto updateReview(Long id, ReviewDto reviewDto) throws Exception{
        if (!reviewRepository.existsById(id)) {
            try {
                throw new ResourceNotFoundException("Reseña no encontrada");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return createReview(reviewDto);
    }

    public List<ReviewDto> obtenerReviewPorIdProducto(Long id) {
        return reviewRepository.findAllByProductId(id)
                .stream()
                .map(review -> reviewToReviewDto(review))
                .collect(Collectors.toList());
    }
}
