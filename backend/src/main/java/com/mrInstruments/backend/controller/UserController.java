package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.UserDto;
import com.mrInstruments.backend.dto.UserToRegisterDto;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.UserService;

import com.mrInstruments.backend.utils.UtilsDtoMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.Optional;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.userToRegisterDtoToUserDto;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LogManager.getLogger(UserController.class);

    //---------------------POST GUARDAR USUARIO --------------------------------
    @PostMapping
    public ResponseEntity<UserDto> guardarUser(@RequestBody UserToRegisterDto userToRegisterDto) throws Exception {
        UserDto userDTO = userToRegisterDtoToUserDto(new UserDto(), userToRegisterDto);
        return ResponseEntity.ok(userService.guardarUsuario(userDTO));
    }

    //----------------------GET LISTAR USUARIO ------------------------
    @GetMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> listarUser() throws BadRequestException {
        return ResponseEntity.ok(userService.obtenerTodosLosUsuarios());
    }

    //----------------------DELETE USUARIO SE AGREGA E------------------------

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> eliminarUser(@PathVariable Long id) throws Exception {
        userService.eliminarUsuario(id);
        return ResponseEntity.ok("Se elimino correctamente el user con id: "+ id);
    }

    //----------------------GET BUSCAR POR ID ------------------------
    @GetMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<UserDto> buscarUserPorId(@PathVariable Long id) throws Exception {
        Optional<UserDto> usuarioBuscado= userService.buscarUserPorId(id);
        if(usuarioBuscado.isPresent()){
            logger.log(Level.INFO,"User con id= " + id + " fue encontrado");
            return ResponseEntity.ok(usuarioBuscado.get());
        }else {
            //return ResponseEntity.notFound().build();
            throw new ResourceNotFoundException("Error al buscar id: "+id+" no se encontró.");
        }
    }

    //----------------------ACTUALIZAR USUARIO ------------------------
    @PutMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<UserDto> actualizarUsuario(@RequestBody UserToRegisterDto userToRegisterDto) throws Exception {
        UserDto userDto = userToRegisterDtoToUserDto(new UserDto(), userToRegisterDto);
        return ResponseEntity.ok(userService.actualizarUsuario(userDto));
    }
    //----------------------GET BUSCAR POR EMAIL ------------------------
    @PermitAll
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> buscarUsuariosPorEmail(@PathVariable String email)throws ResourceNotFoundException{
        return ResponseEntity.ok(userService.buscarUsuarioPorEmail(email));
    }
    //----------------------BUSCAR POR NOMBRE USUARIO  ------------------------

    @GetMapping("/nombre/{nombre}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> buscarUsuarioPorNombre(@PathVariable String nombre) throws  ResourceNotFoundException{
        return ResponseEntity.ok(userService.buscarUsuarioPorNombre(nombre));
    }
}
