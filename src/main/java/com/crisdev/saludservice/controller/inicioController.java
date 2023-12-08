package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.serviceImpl.PacienteServiceImpl;
import com.crisdev.saludservice.serviceImpl.ProfesionalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@Controller
@RequestMapping("/")
public class inicioController {

    @Autowired
    ProfesionalServiceImpl profesionalService;

    @Autowired
    PacienteServiceImpl pacienteService;

    @GetMapping("/registroProfesional")
    public String registroProfesional() {
        return "profesional_registro";
    }

    @PostMapping("/registrarProfesional")
    public String registrarProfesional(@RequestParam String nombre,
                                       @RequestParam String apellido,
                                       @RequestParam(required = false) Long dni,
                                       @RequestParam("fechaNacimiento") String fechaNacimiento,
                                       MultipartFile fotoPerfil,
                                       Long matricula,
                                       MultipartFile diploma,
                                       @RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String password2,
                                       ModelMap modelMap) {

        try {
            profesionalService.crearProfesional(nombre, apellido, dni, fechaNacimiento, fotoPerfil, matricula, diploma, email, password, password2);
            modelMap.addAttribute("exito", "Usuario creado con éxito");
            return "index";
        } catch (MiException | ParseException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.put("nombre", nombre);
            modelMap.put("apellido", apellido);
            modelMap.put("dni", dni);
            modelMap.put("fechaNacimiento", fechaNacimiento);
            modelMap.put("matricula", matricula);
            modelMap.put("email", email);
            return "profesional_registro";
        }
    }

    @GetMapping("/registroPaciente")
    public String registroPaciente() {
        return "paciente_registro";
    }

    @PostMapping("/registrarPaciente")
    public String registrarPaciente(@RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam(required = false) Long dni,
                                    @RequestParam("fechaNacimiento") String fechaNacimiento,
                                    MultipartFile fotoPerfil,
                                    @RequestParam String email,
                                    @RequestParam String password,
                                    @RequestParam String password2,
                                    ModelMap modelMap) {

        try {
            pacienteService.crearPaciente(nombre, apellido, dni, fechaNacimiento, fotoPerfil, email, password, password2);
            modelMap.addAttribute("exito", "Usuario creado con éxito");
            return "index";
        } catch (MiException | ParseException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.put("nombre", nombre);
            modelMap.put("apellido", apellido);
            modelMap.put("dni", dni);
            modelMap.put("fechaNacimiento", fechaNacimiento);
            modelMap.put("email", email);
            return "paciente_registro";
        }
    }
}
