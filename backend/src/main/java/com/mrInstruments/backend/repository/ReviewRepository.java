package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    Review save(Review product);
    void deleteById(Long id);
    @Query(value = "SELECT * FROM resenias r WHERE r.productos_id = :id",
            nativeQuery = true)
    List<Review> findAllByProductId(@Param("id") Long id);
}
