package com.mrInstruments.backend.security;

//import com.mrInstruments.backend.entities.User;
//import com.mrInstruments.backend.enums.UserRol;
//import com.mrInstruments.backend.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
public class DataLoader {//implements ApplicationRunner {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        String passAdmin="password";
//        String passCifradoAdmin= passwordEncoder.encode(passAdmin);
//
//        userService.guardarUsuario(new User("Ibis", "Fortunato", "ibis@gmail.com", passCifradoAdmin, UserRol.ROLE_ADMIN, "ibis@gmail.com"));
//        System.out.println("La contraseña cifrada es: "+passCifradoAdmin);
//
//        //ROL USUARIO
//        String passUser="password";
//        String passCifradoUser= passwordEncoder.encode(passUser);
//        System.out.println("La contraseña cifrada es: "+passCifradoUser);
//        userService.guardarUsuario(new User("Celeste","Pieras","celeste@gmail.com",passCifradoUser, UserRol.ROLE_USER, "celeste@gmail.com"));
//
//        /*//ROL CEO
//        String passCeo="password";
//        String passCifradoCeo= passwordEncoder.encode(passUser);
//        System.out.println("La contraseña cifrada es: "+passCifradoCeo);
//        userService.guardarUsuario(new User("Lauta","Diosquez","lautag@gmail",passCifradoUser, UserRol.ROLE_CEO));*/
//    }
}