package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.ProfesionalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

@Controller
@RequestMapping("/")
public class inicioController {

    final ProfesionalService profesionalService;
    final PacienteService pacienteService;

    public inicioController(ProfesionalService profesionalService, PacienteService pacienteService) {
        this.profesionalService = profesionalService;
        this.pacienteService = pacienteService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelMap) {

        if (error != null) {
            modelMap.addAttribute("error", "usuario o contraseña incorrectos");
        }
        return "login";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado == null || logueado.getRol() == null) {
            return "index";
        }
        if (logueado.getRol().toString().equals("PACIENTE")) {
            return "redirect:/paciente/dashboard";
        }
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRol().toString().equals("PROFESIONAL")) {
            return "redirect:/profesional/dashboard";
        }
        return "index";
    }

    @GetMapping("/registroProfesional")
    public String registroProfesional(ModelMap modelMap) {

        Especialidad[] especialidades = Especialidad.values();
        modelMap.addAttribute("especialidades", especialidades);
        return "profesional_registro";
    }

    @PostMapping("/registrarProfesional")
    public String registrarProfesional(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long dni, @RequestParam("fechaNacimiento") String fechaNacimiento, @RequestParam(required = false) MultipartFile fotoPerfil, @RequestParam(required = false) Long matricula, @RequestParam(required = false) MultipartFile diploma, @RequestParam(required = false) Especialidad especialidad, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelMap) {

        try {
            profesionalService.crearProfesional(nombre, apellido, dni, fechaNacimiento, fotoPerfil, matricula, diploma, especialidad, email, password, password2);
            modelMap.addAttribute("exito", "Usuario creado con éxito");
            return "index";
        } catch (MiException | ParseException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.put("nombre", nombre);
            modelMap.put("apellido", apellido);
            modelMap.put("dni", dni);
            modelMap.put("fechaNacimiento", fechaNacimiento);
            modelMap.put("matricula", matricula);

            Especialidad[] especialidades = Especialidad.values();
            modelMap.addAttribute("especialidades", especialidades);
//            modelMap.put("especialidad",especialidad);

            modelMap.put("email", email);
            return "profesional_registro";
        }
    }

    @GetMapping("/registroPaciente")
    public String registroPaciente() {
        return "paciente_registro";
    }

    @PostMapping("/registrarPaciente")
    public String registrarPaciente(@RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long dni, @RequestParam("fechaNacimiento") String fechaNacimiento, MultipartFile fotoPerfil, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelMap) {

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
