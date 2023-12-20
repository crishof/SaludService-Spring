package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
