package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.service.ConsultaService;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/consulta")
public class ConsultaController {

@Autowired
TurnoService turnoService;
@Autowired
PacienteService pacienteService;
@Autowired
ConsultaService consultaService;


    @GetMapping("/atenderConsulta/{id}")
    public String atenderConsulta(@PathVariable String id, ModelMap modelMap) {

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
    public String crearConsulta(@RequestParam String idTurno, @RequestParam String motivo, String antecedentes, @RequestParam String diagnostico, @RequestParam String indicaciones, String observaciones, HttpSession session, ModelMap modelMap) {


        try {
            Profesional profesional = (Profesional) session.getAttribute("usuariosession");
            consultaService.crearConsulta(idTurno, motivo, antecedentes, diagnostico, indicaciones, observaciones, profesional);
            turnoService.atenderTurno(idTurno);
            modelMap.addAttribute("exito", "Consulta registrada con éxito");
        } catch (MiException e) {
            modelMap.addAttribute("error", e.getMessage());
            return "redirect:/profesional/listarCitas";

        }
        return "redirect:/profesional/listarCitas";
    }

    @PostMapping("/valorar")
    public String valorarConsulta(@RequestParam String idConsulta, @RequestParam int estrellas){


        System.out.println("idConsulta = " + idConsulta);
        System.out.println("estrellas = " + estrellas);

        consultaService.valorarConsulta(idConsulta,estrellas);

        return "redirect:/paciente/listarConsultas";
    }

}

