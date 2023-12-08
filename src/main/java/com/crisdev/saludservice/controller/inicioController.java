package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.serviceImpl.ProfesionalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class inicioController {

    @Autowired
    ProfesionalServiceImpl profesionalService;

    @GetMapping("/registroProfesional")
    public String registroProfesional() {
        return "profesional_registro";
    }

    @PostMapping("/registrarProfesional")
    public String registrarProfesional(String nombre,
                                       String apellido,
                                       String email,
                                       String password,
                                       String password2,
                                       ModelMap modelMap) {

        try {
            profesionalService.crearProfesional(nombre, apellido, email, password, password2);
            modelMap.addAttribute("exito", "Usuario creado con éxito");
            return "index";
        } catch (MiException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.put("nombre",nombre);
            modelMap.put("apellido",apellido);
            modelMap.put("email", email);
            return "profesional_registro";
        }
    }
}
