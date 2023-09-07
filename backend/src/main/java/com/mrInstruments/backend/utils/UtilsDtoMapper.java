package com.mrInstruments.backend.utils;

import com.mrInstruments.backend.dto.*;
import com.mrInstruments.backend.entities.*;
import com.mrInstruments.backend.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;

public final class UtilsDtoMapper {

    public UtilsDtoMapper() {
    }

    public static UserDto userToUserDto(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setNombre(user.getNombre());
        userDTO.setApellido(user.getApellido());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRol(user.getUserRol());
        return userDTO;
    }

    public static User userDtoToUser(UserDto userDTO, User user, BCryptPasswordEncoder bCryptPasswordEncoder) throws BadRequestException {
        Long id = userDTO.getId();
        if (id != null && id > 0){
            user.setId(id);
        }
        user.setNombre(userDTO.getNombre());
        user.setApellido(userDTO.getApellido());
        user.setEmail(userDTO.getEmail());
        String password = userDTO.getPassword();
        password = password != null && !("").equals(password) ? bCryptPasswordEncoder.encode(password) : null;
        if (Optional.ofNullable(password)
                .isPresent()
                && !user.getPassword().equals(password)){
                user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        }

        return user;
    }

    public static CategoryDto categoryToCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitulo(category.getTitulo());
        categoryDto.setDescripcion(category.getDescripcion());
        categoryDto.setImagen(category.getImagen());
        return categoryDto;
    }

    public static Category categoryDtoToCategory(Category category, CategoryDto categoryDto){
        Long id = categoryDto.getId();
        if (id != null && id > 0){
            category.setId(id);
        }
        category.setTitulo(categoryDto.getTitulo());
        category.setDescripcion(categoryDto.getDescripcion());
        category.setImagen(categoryDto.getImagen());
        return category;
    }

    public static CharacteristicDto characteristicToCharacteristicDto(Characteristic characteristic){
        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setId(characteristic.getId());
        characteristicDto.setImagen(characteristic.getImagen());
        characteristicDto.setTitulo(characteristic.getTitulo());
        return characteristicDto;
    }

    public static Characteristic characteristicDtoToCharacteristic (CharacteristicDto characteristicDto, Characteristic characteristic){
        Long id = characteristicDto.getId();
        if (id != null && id > 0){
            characteristic.setId(id);
        }
        characteristic.setTitulo(characteristicDto.getTitulo());
        characteristic.setImagen(characteristicDto.getImagen());
        return characteristic;
    }

    public static ProductDto productToProductDto (Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setNombre(product.getNombre());
        productDto.setDescripcion(product.getDescripcion());
        productDto.setImagenes(product.getImagenes());
        productDto.setPrecio(product.getPrecio());
        productDto.setCategoria(categoryToCategoryDto(product.getCategory()));

        productDto.setCaracteristicas(
                product.getCharacteristics()
                        .stream()
                        .map(UtilsDtoMapper::characteristicToCharacteristicDto)
                        .collect(Collectors.toSet())
        );
        return productDto;
    }

    public static Product productDtoToProduct(Product product, ProductDto productDto) throws Exception{
        Long id = productDto.getId();
        if (id != null && id > 0){
            product.setId(id);
        }
        product.setNombre(productDto.getNombre());
        product.setDescripcion(productDto.getDescripcion());
        product.setImagenes(productDto.getImagenes());
        product.setPrecio(productDto.getPrecio());
//        product.setCategory(categoryDtoToCategory(new Category(), productDto.getCategoria()));
//        product.setCharacteristics(characteristics);
//        for (CharacteristicDto characterDto : productDto.getCaracteristicas()) {
//            try {
//                product.getCharacteristics().add(
//                        characteristicDtoToCharacteristic(characterDto, new Characteristic())
//                );System.out.println(characterDto.toString());
//            } catch (BadRequestException e) {
//                throw new RuntimeException(e);
//            }
//        }
        return product;
    }

    public static UserDto userToRegisterDtoToUserDto(UserDto userDto, UserToRegisterDto userToRegisterDto){
        userDto.setId(userToRegisterDto.getId());
        userDto.setNombre(userToRegisterDto.getNombre());
        userDto.setApellido(userToRegisterDto.getApellido());
        userDto.setEmail(userToRegisterDto.getEmail());
        userDto.setPassword(userToRegisterDto.getPassword());
        userDto.setUserRol(userToRegisterDto.getUserRol());
        return userDto;
    }

    public static ReservationDto reservationToReservationDto (Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setFechaReserva(reservation.getFechaIngreso());
        reservationDto.setFechaEntrega(reservation.getFechaSalida());
        reservationDto.setReservaActiva(reservation.isReservaActiva());
        reservationDto.setSeguro(reservation.getSeguro());
        reservationDto.setUsuario(userToUserDto(reservation.getUsuario()));
        reservationDto.setProductos(
                reservation.getProducts()
                .stream()
                .map(product -> productToProductDto(product))
                .collect(Collectors.toSet())
        );
        return reservationDto;

    }

    public static Reservation  reservationDtoToReservation(Reservation reservation, ReservationDto reservationDto, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        Long id = reservationDto.getId();
        if (id != null && id > 0){
            reservation.setId(id);
        }
        reservation.setUsuario(userDtoToUser(reservationDto.getUsuario(), new User(), bCryptPasswordEncoder));
        reservation.setFechaIngreso(reservationDto.getFechaReserva());
        reservation.setFechaSalida(reservationDto.getFechaEntrega());
        reservation.setReservaActiva(reservationDto.isReservaActiva());
        reservation.setSeguro(reservationDto.getSeguro());
        return reservation;
    }

    public static ReviewDto reviewToReviewDto (Review review){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setValoracion(review.getValoracion());
        reviewDto.setNombreUsuario(review.getNombreUsuario());
        reviewDto.setFechaPublicacion(review.getFechaPublicacion());
        reviewDto.setComentario(review.getComentario());
        reviewDto.setUsuario(userToUserDto(review.getUsuario()));
        reviewDto.setProducto(productToProductDto(review.getProduct()));
        return reviewDto;
    }

    public static Review reviewDtoToReview(Review review, ReviewDto reviewDto){
        Long id = reviewDto.getId();
        if (id != null && id > 0){
            review.setId(id);
        }
        review.setValoracion(reviewDto.getValoracion());
        review.setNombreUsuario(reviewDto.getNombreUsuario());
        review.setFechaPublicacion(reviewDto.getFechaPublicacion());
        review.setComentario(reviewDto.getComentario());
        return review;
    }
}
