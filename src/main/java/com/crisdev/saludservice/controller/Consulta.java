package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/consulta")
public class Consulta {

    @Autowired
    TurnoService turnoService;
    @Autowired
    PacienteService pacienteService;


    @GetMapping("/atenderConsulta/{id}")
    public String atenderConsulta(@PathVariable String id, HttpSession session, ModelMap modelMap) {

        Turno turno = turnoService.buscarPorId(id);

        try {
            Paciente paciente = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
            modelMap.addAttribute("turno", turno);
            modelMap.addAttribute("paciente", paciente);
        } catch (MiException e) {
            throw new RuntimeException(e);
        }

        return "consulta_form";
    }

    @PostMapping("/crearConsulta")
    public String crearConsulta(@RequestParam String motivo, @RequestParam double peso, @RequestParam double altura,
                                @RequestParam String alergias, @RequestParam String diagnostico, @RequestParam String indicaciones,
                                String observaciones) {

        return "redirect:/profesional/listarCitas";
    }
}
