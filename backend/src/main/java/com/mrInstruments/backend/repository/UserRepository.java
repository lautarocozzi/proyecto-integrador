package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    Optional<UserDetails> findByEmail(String email);
    List<User> findByNombre(String nombre);

    Optional<User> findByEmailAndPassword(String email, String password);

}

