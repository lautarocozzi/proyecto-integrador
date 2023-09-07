package com.mrInstruments.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrInstruments.backend.enums.UserRol;
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
public class UserDto implements Serializable {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    @JsonIgnore
    private String password;
    private UserRol userRol;
//    Lo comento porque para crear reservas solo se debe hacer mediante el endpoint de reservas
//    Pero si se quieren las reservas de un usuario ahi si necesitariamos enviar este set de reservas pero solo
//    se enviaria el set de reservas  y se deberia crear un endpoint en reservasController para llamarlo
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    @JsonIgnore
    private Set<ReservationDto> reservas = new HashSet<>();


    public UserDto(Long id, String nombre, String apellido, String email, String password, UserRol userRol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.userRol = userRol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRol getUserRol() {
        return userRol;
    }

    public void setUserRol(UserRol userRol) {
        this.userRol = userRol;
    }

}
