package com.crisdev.saludservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @GetMapping("/listarCitas")
    public String listarCitas(){

        return "paciente_citas";
    }
}
