package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.UserDto;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.UserRepository;
import com.mrInstruments.backend.service.email.EmailTemplateService;
import com.mrInstruments.backend.utils.UtilsDtoMapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Logger logger = LogManager.getLogger(UserService.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailTemplateService emailService;

    public UserService(UserRepository userRepository, EmailTemplateService emailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDto> obtenerTodosLosUsuarios() {
        List<UserDto> userListDto;
        userListDto = userRepository.findAll()
                .stream()
                .map(UtilsDtoMapper::userToUserDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO,"Listando usuarios...");
        return userListDto;
    }

    public UserDto actualizarUsuario(UserDto userDTO) throws Exception {
        User usuarioBuscado = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario con ID: " + userDTO.getId()));
        usuarioBuscado = userDtoToUser(userDTO, usuarioBuscado, bCryptPasswordEncoder);
        logger.log(Level.INFO, "Usuario actualizado");
        return userToUserDto(userRepository.save(usuarioBuscado));
    }

    public void eliminarUsuario(Long id) throws Exception {
        User usuarioAEliminar = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID: " + id));
        userRepository.deleteById(usuarioAEliminar.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDetails> usuarioBuscado= userRepository.findByEmail(email);
        if(usuarioBuscado.isPresent()){
            return usuarioBuscado.get();
        }else{
            throw new UsernameNotFoundException("No existe email asociado a un usuario en el sistema");
        }
    }

    public UserDto guardarUsuario(UserDto userDTO) throws Exception {
        Optional<UserDetails> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new BadRequestException("No se pudo crear el usuario: El email ya esta registrado en el sistema");
        } else {
            User user = new User(userDTO.getNombre(), userDTO.getApellido(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getUserRol());
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            logger.log(Level.INFO, "Usuario creado");
            try {
                UserDto userDto1 = userToUserDto(userRepository.save(user));
                emailService.sendEmailFromTemplate(userDto1.getNombre(), userDto1.getEmail());
                return userDto1;
            } catch (Exception ex) {
                throw new BadRequestException("No se pudo registrar el usuario");
            }
        }
    }

    public Optional<UserDto> buscarUserPorId(Long id) throws Exception{

        Optional<User> usuarioBuscado = userRepository.findById(id);
        if(usuarioBuscado.isPresent()){
            User user = usuarioBuscado.get();
            return Optional.ofNullable(userToUserDto(user));
        }else{
            throw new ResourceNotFoundException("No existe id asociado a un usuario en el sistema");
        }
    }

    public List<UserDto> buscarUsuarioPorNombre(String nombre) throws ResourceNotFoundException {
        List<User> listaUsuariosEncontrados = userRepository.findByNombre(nombre);
        if (listaUsuariosEncontrados.isEmpty()) {
            throw new ResourceNotFoundException("No existe usuario asociado a ese nombre en el sistema");
        }
        logger.log(Level.INFO,"Users con nombre= " + nombre + " fueron encontrados");
        List<UserDto> userDtoList = listaUsuariosEncontrados
                .stream()
                .map( user -> userToUserDto(user))
                .collect(Collectors.toList());
        return userDtoList;
    }

    public User buscarUsuarioPorEmailAndPassword(String email, String password) throws UsernameNotFoundException {
        Optional<User> usuarioBuscado = findUserDtoToUser(email, password);
        if(usuarioBuscado.isPresent()){
            return usuarioBuscado.get();
        }else{
            throw new UsernameNotFoundException("No existe email asociado a un usuario en el sistema");
        }
    }

    public UserDto buscarUsuarioPorEmail(String email) throws ResourceNotFoundException {
        User usuarioEncontrado = (User) userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("No existe usuario asociado a ese email en el sistema"));
        logger.log(Level.INFO,"User con email= " + email + " fue encontrado");
        return userToUserDto(usuarioEncontrado);
    }

    public Optional<User> findUserDtoToUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}

